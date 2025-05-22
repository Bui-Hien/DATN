package com.buihien.datn.controller;

import com.buihien.datn.dto.PersonBankAccountDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/person-bank-account")
@RestController
public class RestPersonBankAccountController extends GenericApi<PersonBankAccountDto, SearchDto> {

    public RestPersonBankAccountController(GenericService<PersonBankAccountDto, SearchDto> genericService) {
        super(PersonBankAccountDto.class, genericService);
    }
}
