package com.buihien.datn.controller;

import com.buihien.datn.dto.BankDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/bank")
@RestController
public class RestBankController extends GenericApi<BankDto, SearchDto> {

    public RestBankController(GenericService<BankDto, SearchDto> genericService) {
        super(BankDto.class, genericService);
    }
}
