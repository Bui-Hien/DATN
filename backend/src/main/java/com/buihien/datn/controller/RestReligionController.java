package com.buihien.datn.controller;

import com.buihien.datn.dto.ReligionDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/religion")
@RestController
public class RestReligionController extends GenericApi<ReligionDto, SearchDto> {

    public RestReligionController(GenericService<ReligionDto, SearchDto> genericService) {
        super(ReligionDto.class, genericService);
    }
}
