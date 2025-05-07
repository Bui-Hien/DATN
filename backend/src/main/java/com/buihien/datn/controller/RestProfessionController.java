package com.buihien.datn.controller;

import com.buihien.datn.dto.ProfessionDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/profession")
@RestController
public class RestProfessionController extends GenericApi<ProfessionDto, SearchDto> {

    public RestProfessionController(GenericService<ProfessionDto, SearchDto> genericService) {
        super(ProfessionDto.class, genericService);
    }
}
