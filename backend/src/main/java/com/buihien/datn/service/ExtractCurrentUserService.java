package com.buihien.datn.service;

import com.buihien.datn.domain.User;
import com.buihien.datn.dto.UserDto;

import java.util.List;

public interface ExtractCurrentUserService {
    User extractCurrentUser();

    UserDto extractCurrentUserDto();

    boolean hasAnyRole(List<String> roles);

    boolean hasAllRoles(List<String> roles);
}
