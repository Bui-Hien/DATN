package com.buihien.datn.controller;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.dto.UserDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericApi;
import com.buihien.datn.generic.GenericService;
import com.buihien.datn.generic.ResponseData;
import com.buihien.datn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user")
public class RestUserController extends GenericApi<UserDto, SearchDto> {
    @Autowired
    private UserService userService;

    public RestUserController(GenericService<UserDto, SearchDto> genericService) {
        super(UserDto.class, genericService);
    }

    @Secured({DatnConstants.ROLE_MANAGER, DatnConstants.ROLE_ADMIN})
    @GetMapping("/get-current-user")
    public ResponseData<UserDto> getCurrentUser() {
        UserDto result = userService.getCurrentUser();
        return new ResponseData<>(HttpStatus.OK.value(), "Lấy người dùng hiện tại", result);
    }
}
