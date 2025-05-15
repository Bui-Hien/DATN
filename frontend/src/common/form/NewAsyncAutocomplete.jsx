import React, {useCallback, useEffect, useState} from "react";
import TextField from "@mui/material/TextField";
import Autocomplete from "@mui/material/Autocomplete";

export default function NewAsyncAutocomplete() {
    const [value, setValue] = useState(null); // giá trị được chọn
    const [inputValue, setInputValue] = useState(""); // giá trị nhập text
    const [options, setOptions] = useState([]); // dữ liệu dropdown

    const fetchData = useCallback((searchText) => {
        if (!searchText) {
            setOptions([]);
            return;
        }

        const url = `mywebsite/searchApi?query=${encodeURIComponent(searchText)}`;
        fetch(url)
            .then((res) => {
                if (!res.ok) throw new Error("Network response was not ok");
                return res.json();
            })
            .then((data) => {
                setOptions(data);
            })
            .catch((err) => {
                console.error("Fetch error:", err);
                setOptions([]);
            });
    }, []);

    // Mỗi lần inputValue thay đổi thì fetch dữ liệu
    useEffect(() => {
        fetchData(inputValue);
    }, [inputValue, fetchData]);

    return (
        <Autocomplete
            value={value}
            onChange={(event, newValue) => {
                setValue(newValue);
                console.log("Selected:", newValue);
            }}
            inputValue={inputValue}
            onInputChange={(event, newInputValue) => {
                setInputValue(newInputValue);
            }}
            options={options}
            getOptionLabel={(option) =>
                typeof option === "string" ? option : `${option.value} - ${option.label}`
            }
            renderInput={(params) => (
                <TextField {...params} label="Search" variant="outlined" fullWidth />
            )}
            renderOption={(props, option) => (
                <li {...props} key={option.value}>
                    {option.label}
                </li>
            )}
            filterOptions={(x) => x} // tắt lọc mặc định của MUI
            freeSolo={false} // chỉ chọn từ danh sách
        />
    );
}
