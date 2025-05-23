import React, {useEffect, useCallback, useMemo, useRef, useState} from "react";
import {FastField, getIn, useField, useFormikContext} from "formik";
import Editor, {useMonaco} from "@monaco-editor/react";
import {Parser} from "expr-eval";
import {
    FormHelperText,
    FormLabel,
    Box,
    Chip,
    Typography
} from "@mui/material";
import {
    CheckCircle as CheckIcon,
    Error as ErrorIcon
} from "@mui/icons-material";
import CommonPopupV2 from "../CommonPopupV2";

const FormulaEditor = (props) => {
    return (
        <FastField {...props} name={props.name} shouldUpdate={shouldComponentUpdate}>
            {({field, meta, form}) => {
                return <MyFormulaEditor {...props} field={field} meta={meta} formik={form}/>;
            }}
        </FastField>
    );
};

const shouldComponentUpdate = (nextProps, currentProps) => {
    return (
        nextProps.name !== currentProps.name ||
        nextProps.value !== currentProps.value ||
        nextProps.open !== currentProps.open ||
        nextProps.label !== currentProps.label ||
        nextProps.required !== currentProps.required ||
        nextProps.disabled !== currentProps.disabled ||
        nextProps.readOnly !== currentProps.readOnly ||
        nextProps.variables !== currentProps.variables ||
        nextProps.formik.isSubmitting !== currentProps.formik.isSubmitting ||
        Object.keys(nextProps).length !== Object.keys(currentProps).length ||
        getIn(nextProps.formik.values, currentProps.name) !== getIn(currentProps.formik.values, currentProps.name) ||
        getIn(nextProps.formik.errors, currentProps.name) !== getIn(currentProps.formik.errors, currentProps.name) ||
        getIn(nextProps.formik.touched, currentProps.name) !== getIn(currentProps.formik.touched, currentProps.name) ||
        getIn(nextProps.formik.initialValues, currentProps.name) !== getIn(currentProps.formik.initialValues, currentProps.name)
    );
};

const MyFormulaEditor = ({
                             name,
                             label,
                             variables = [],
                             required = false,
                             disabled = false,
                             readOnly = false,
                             height = "150px",
                             showVariables = true,
                             debounceTime = 300,
                             fontSize = 14,
                             open = false,
                             onClosePopup,
                             ...otherProps
                         }) => {
    const [field, meta, helpers] = useField(name);
    const {setFieldValue, setFieldTouched} = useFormikContext();
    const monaco = useMonaco();
    const validationTimeoutRef = useRef(null);

    const [validationStatus, setValidationStatus] = useState(null); // 'valid', 'invalid', null
    const [usedVariables, setUsedVariables] = useState([]);
    const [isEditorReady, setIsEditorReady] = useState(false);

    // Validation function - hỗ trợ toán tử cơ bản và ngoặc
    const validateFormula = useCallback((value) => {
        if (!value?.trim()) {
            setValidationStatus(null);
            setUsedVariables([]);
            helpers.setError(undefined);
            return;
        }

        // Kiểm tra cân bằng ngoặc trước khi parse
        const bracketsCheck = checkBracketsBalance(value);
        if (!bracketsCheck.isValid) {
            helpers.setError(bracketsCheck.error);
            setValidationStatus('invalid');
            setUsedVariables([]);
            return;
        }

        try {
            // Tạo parser với toán tử cơ bản + ngoặc
            const parser = new Parser({
                operators: {
                    add: true,        // +
                    subtract: true,   // -
                    multiply: true,   // *
                    divide: true,     // /
                    remainder: false, // %
                    power: false,     // ^
                    factorial: false,
                    concatenate: false,
                    conditional: false,
                    logical: false,
                    comparison: false,
                    'in': false,
                    assignment: false
                }
            });

            const expr = parser.parse(value);
            const detectedVars = expr.variables();

            // Kiểm tra biến không hợp lệ
            const invalidVars = detectedVars.filter(v => !variables.includes(v));

            if (invalidVars.length > 0) {
                const errorMsg = `Biến không hợp lệ: ${invalidVars.join(', ')}`;
                helpers.setError(errorMsg);
                setValidationStatus('invalid');
                setUsedVariables(detectedVars);
                return;
            }

            // Thử tính toán với giá trị mẫu
            const testValues = {};
            detectedVars.forEach(v => {
                testValues[v] = 2; // Dùng 2 thay vì 1 để tránh chia cho 0
            });

            try {
                const result = expr.evaluate(testValues);

                // Kiểm tra kết quả có hợp lệ
                if (!isFinite(result)) {
                    helpers.setError("Công thức tạo ra kết quả không hợp lệ (vô cực hoặc NaN)");
                    setValidationStatus('invalid');
                    setUsedVariables(detectedVars);
                    return;
                }

                helpers.setError(undefined);
                setValidationStatus('valid');
                setUsedVariables(detectedVars);
            } catch (evalError) {
                const errorMsg = "Công thức có lỗi tính toán (có thể chia cho 0)";
                helpers.setError(errorMsg);
                setValidationStatus('invalid');
                setUsedVariables(detectedVars);
            }

        } catch (parseError) {
            const errorMsg = "Cú pháp công thức không hợp lệ";
            helpers.setError(errorMsg);
            setValidationStatus('invalid');
            setUsedVariables([]);
        }
    }, [variables, helpers]);

    // Hàm kiểm tra cân bằng ngoặc
    const checkBracketsBalance = useCallback((formula) => {
        const brackets = {
            '(': ')',
            '[': ']',
            '{': '}'
        };

        const openBrackets = Object.keys(brackets);
        const closeBrackets = Object.values(brackets);
        const stack = [];

        for (let i = 0; i < formula.length; i++) {
            const char = formula[i];

            if (openBrackets.includes(char)) {
                stack.push({bracket: char, position: i});
            } else if (closeBrackets.includes(char)) {
                if (stack.length === 0) {
                    return {
                        isValid: false,
                        error: `Ngoặc đóng '${char}' thừa tại vị trí ${i + 1}`
                    };
                }

                const lastOpen = stack.pop();
                const expectedClose = brackets[lastOpen.bracket];

                if (char !== expectedClose) {
                    return {
                        isValid: false,
                        error: `Ngoặc không khớp: '${lastOpen.bracket}' tại vị trí ${lastOpen.position + 1} cần đóng bằng '${expectedClose}' nhưng gặp '${char}' tại vị trí ${i + 1}`
                    };
                }
            }
        }

        if (stack.length > 0) {
            const openBracket = stack[stack.length - 1];
            return {
                isValid: false,
                error: `Ngoặc mở '${openBracket.bracket}' tại vị trí ${openBracket.position + 1} chưa được đóng`
            };
        }

        return {isValid: true};
    }, []);

    // Debounced change handler
    const handleChange = useCallback((value = "") => {
        setFieldTouched(name, true);
        setFieldValue(name, value);

        if (validationTimeoutRef.current) {
            clearTimeout(validationTimeoutRef.current);
        }

        validationTimeoutRef.current = setTimeout(() => {
            validateFormula(value);
        }, debounceTime);
    }, [name, setFieldTouched, setFieldValue, validateFormula, debounceTime]);

    // Monaco Editor setup - chỉ gợi ý biến và toán tử cơ bản
    useEffect(() => {
        if (!monaco || !variables.length || !isEditorReady) return;

        const completionProvider = monaco.languages.registerCompletionItemProvider("plaintext", {
            provideCompletionItems: (model, position) => {
                const word = model.getWordUntilPosition(position);
                const range = {
                    startLineNumber: position.lineNumber,
                    endLineNumber: position.lineNumber,
                    startColumn: word.startColumn,
                    endColumn: word.endColumn
                };

                const suggestions = variables.map((v) => ({
                    label: v,
                    kind: monaco.languages.CompletionItemKind.Variable,
                    insertText: v,
                    detail: "Biến có sẵn",
                    range: range
                }));

                return {suggestions};
            }
        });

        return () => completionProvider.dispose();
    }, [monaco, variables, isEditorReady]);

    // Cleanup timeout
    useEffect(() => {
        return () => {
            if (validationTimeoutRef.current) {
                clearTimeout(validationTimeoutRef.current);
            }
        };
    }, []);

    // Editor options
    const editorOptions = useMemo(() => ({
        readOnly: disabled || readOnly,
        fontSize: fontSize,
        minimap: {enabled: false},
        wordWrap: "on",
        lineNumbers: "off",
        padding: {top: 10, bottom: 10},
        scrollBeyondLastLine: false,
        automaticLayout: true,
        suggest: {
            showKeywords: false,
            showSnippets: false,
            showClasses: false,
            showFunctions: false,
            showVariables: true
        },
        quickSuggestions: {
            other: true,
            comments: false,
            strings: false
        }
    }), [disabled, readOnly, fontSize]);

    // Get validation icon
    const getValidationIcon = () => {
        if (validationStatus === 'valid') {
            return <CheckIcon color="success" fontSize="small"/>;
        } else if (validationStatus === 'invalid') {
            return <ErrorIcon color="error" fontSize="small"/>;
        }
        return null;
    };

    return (
        <CommonPopupV2
            size="sm"
            scroll={"paper"}
            open={open}
            noDialogContent
            title={<Box sx={{display: 'flex', alignItems: 'center', mb: 0.5}}>
                {label && (
                    <FormLabel sx={{mr: 1}}>
                        {label} {required && <span style={{color: "red"}}>*</span>}
                    </FormLabel>
                )}
                {getValidationIcon()}
            </Box>}
            onClosePopup={() => onClosePopup(false)}
            noIcon={true}
        >
            <Box sx={{mb: 2}} {...otherProps}>
                {/* Available Variables */}
                {showVariables && variables.length > 0 && (
                    <Box sx={{mb: 1, px: 2}}>
                        <Typography variant="body2" color="text.secondary" sx={{mb: 0.5}}>
                            Biến có sẵn (Toán tử: +, -, *, /, ngoặc: ( ) [ ] {}):
                        </Typography>
                        <Box sx={{display: 'flex', flexWrap: 'wrap', gap: 0.5}}>
                            {variables.map((variable) => (
                                <Chip
                                    key={variable}
                                    label={variable}
                                    size="small"
                                    variant={usedVariables.includes(variable) ? "filled" : "outlined"}
                                    color={usedVariables.includes(variable) ? "primary" : "default"}
                                    onClick={() => {
                                        const currentValue = field.value || "";
                                        const newValue = currentValue + (currentValue && !currentValue.endsWith('(') && !currentValue.endsWith('[') && !currentValue.endsWith('{') ? " + " : "") + variable;
                                        handleChange(newValue);
                                    }}
                                    sx={{cursor: 'pointer'}}
                                />
                            ))}

                            {/* Thêm nút ngoặc */}
                            {['( )', '[ ]', '{ }'].map((bracket) => (
                                <Chip
                                    key={bracket}
                                    label={bracket}
                                    size="small"
                                    variant="outlined"
                                    color="secondary"
                                    onClick={() => {
                                        const currentValue = field.value || "";
                                        const openBracket = bracket.charAt(0);
                                        const closeBracket = bracket.charAt(2);
                                        // Thêm ngoặc mở, con trỏ sẽ ở giữa để người dùng nhập
                                        const newValue = currentValue + openBracket + closeBracket;
                                        handleChange(newValue);
                                    }}
                                    sx={{cursor: 'pointer'}}
                                />
                            ))}

                            {/* Thêm nút toán tử */}
                            {[' + ', ' - ', ' * ', ' / '].map((operator) => (
                                <Chip
                                    key={operator.trim()}
                                    label={operator.trim()}
                                    size="small"
                                    variant="outlined"
                                    color="default"
                                    onClick={() => {
                                        const currentValue = field.value || "";
                                        const newValue = currentValue + operator;
                                        handleChange(newValue);
                                    }}
                                    sx={{cursor: 'pointer'}}
                                />
                            ))}
                        </Box>
                    </Box>
                )}

                {/* Editor Container */}
                <Box
                    sx={{
                        border: "1px solid",
                        borderColor: meta.touched && meta.error ? "error.main" : "#ccc",
                        borderRadius: 1,
                        overflow: "hidden"
                    }}
                >
                    <Editor
                        height={height}
                        defaultLanguage="plaintext"
                        value={field.value || ""}
                        onChange={handleChange}
                        options={editorOptions}
                        onMount={() => setIsEditorReady(true)}
                        loading={
                            <Box sx={{
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                                height: height,
                                color: 'text.secondary'
                            }}>
                                Đang tải editor...
                            </Box>
                        }
                    />
                </Box>

                {/* Error Message */}
                {meta.touched && meta.error && (
                    <FormHelperText error sx={{mt: 0.5}}>
                        {meta.error}
                    </FormHelperText>
                )}

                {/* Success Message */}
                {validationStatus === 'valid' && usedVariables.length > 0 && (
                    <FormHelperText sx={{mt: 0.5, color: 'success.main'}}>
                        Công thức hợp lệ. Sử dụng biến: {usedVariables.join(', ')}
                    </FormHelperText>
                )}
            </Box>
        </CommonPopupV2>
    );
};

export default React.memo(FormulaEditor);