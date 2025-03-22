package com.buihien.datn.service;

import com.buihien.datn.domain.User;
import com.buihien.datn.dto.UserDto;

public interface UserRoleService {
    void handleSetRoleListInUser(UserDto dto, User entity);
}
