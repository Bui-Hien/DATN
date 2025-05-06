package com.buihien.datn.dto.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Method;

public class EnumValueValidator implements ConstraintValidator<ValidEnumValue, Integer> {
    private Class<? extends Enum<?>> enumClass;
    private String methodName;

    @Override
    public void initialize(ValidEnumValue constraintAnnotation) {
        enumClass = constraintAnnotation.enumClass();
        methodName = constraintAnnotation.method();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) return true;

        try {
            Method method = enumClass.getMethod(methodName);
            for (Object constant : enumClass.getEnumConstants()) {
                Object methodValue = method.invoke(constant);
                if (value.equals(methodValue)) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false; // method không tồn tại hoặc gọi lỗi
        }

        return false;
    }
}
