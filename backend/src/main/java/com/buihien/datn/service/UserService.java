package com.buihien.datn.service;

import com.buihien.datn.domain.User;
import com.buihien.datn.dto.UserDto;
import com.buihien.datn.dto.search.SearchDto;
import com.buihien.datn.generic.GenericService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends GenericService<UserDto, SearchDto> {
    void saveUser(User user);

    User getByUsername(String username);

    UserDetailsService userDetailsService();

    List<String> getAllRolesByUserId(long userId);

    List<String> getAllRolesByUserUserName(String username);

    User findUserByRole(String role);
}
