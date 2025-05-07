package com.buihien.datn.controller;

import com.buihien.datn.dto.EthnicsDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/ethnics")
@RestController
public class RestEthnicsController extends GenericApi<EthnicsDto, SearchDto> {

    public RestEthnicsController(GenericService<EthnicsDto, SearchDto> genericService) {
        super(EthnicsDto.class, genericService);
    }
}
