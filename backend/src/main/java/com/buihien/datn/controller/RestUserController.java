package com.buihien.datn.controller;

import com.buihien.datn.dto.UserDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class RestUserController extends GenericApi<UserDto, SearchDto> {
    public RestUserController(GenericService<UserDto, SearchDto> genericService) {
        super(UserDto.class, genericService);
    }
}
