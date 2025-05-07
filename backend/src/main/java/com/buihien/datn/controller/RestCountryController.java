package com.buihien.datn.controller;

import com.buihien.datn.dto.CountryDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/country")
@RestController
public class RestCountryController extends GenericApi<CountryDto, SearchDto> {

    public RestCountryController(GenericService<CountryDto, SearchDto> genericService) {
        super(CountryDto.class, genericService);
    }
}
