package com.buihien.datn.dto;

import com.buihien.datn.domain.Country;

public class CountryDto extends BaseObjectDto {
    public CountryDto() {
    }

    public CountryDto(Country entity) {
        super(entity);
    }
}
