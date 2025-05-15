import React, {useState} from "react";
import {FormControl} from "@mui/material";
import SearchIcon from "@mui/icons-material/Search";
import {useTranslation} from "react-i18next";
import "./SearchBox.scss";

export default function CommonSearchInput(props) {
    const { t } = useTranslation();
    const [keyword, setKeyword] = useState("");

    const handleKeyDownEnterSearch = (event) => {
        if (event.key === "Enter") {
            props.search({ keyword });
        }
    };

    const handleSearchClick = () => {
        props.search({ keyword });
    };

    return (
        <FormControl fullWidth>
            <div className="search-box" style={{ position: "relative" }}>
                <input
                    type="text"
                    value={keyword}
                    onChange={(e) => setKeyword(e.target.value)}
                    onKeyDown={handleKeyDownEnterSearch}
                    placeholder={t("general.enterSearch")}
                    style={{ width: "100%", paddingRight: "32px" }} // paddingRight for icon space
                />
                <button
                    type="button"
                    className="btn btn-search"
                    onClick={handleSearchClick}
                    style={{
                        position: "absolute",
                        top: 4,
                        right: 3,
                        border: "none",
                        background: "transparent",
                        cursor: "pointer",
                        padding: 0,
                    }}
                    aria-label={t("general.search")}
                >
                    <SearchIcon />
                </button>
            </div>
        </FormControl>
    );
}
