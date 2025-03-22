package com.buihien.datn.service;

import com.buihien.datn.domain.User;
import com.buihien.datn.dto.UserDto;
import com.buihien.datn.dto.search.SearchDto;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService {
    UserDto saveOrUpdate(UserDto dto);

    void saveUser(User user);

    void deleteById(long id);

    User getByUsername(String username);

    UserDetailsService userDetailsService();

    List<String> getAllRolesByUserId(long userId);

    List<String> getAllRolesByUserUserName(String username);

    UserDto getById(long id);

    User findUserByRole(String role);

    Page<UserDto> pagingUser(SearchDto dto);
}
