package com.buihien.datn.controller;

import com.buihien.datn.dto.PositionDto;
import com.buihien.datn.dto.search.PositionSearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/position")
@RestController
public class RestPositionController extends GenericApi<PositionDto, PositionSearchDto> {

    public RestPositionController(GenericService<PositionDto, PositionSearchDto> genericService) {
        super(PositionDto.class, genericService);
    }
}
