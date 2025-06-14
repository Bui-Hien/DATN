package com.buihien.datn.service.impl;

import com.buihien.datn.DatnConstants;
import com.buihien.datn.domain.*;
import com.buihien.datn.dto.RoleDto;
import com.buihien.datn.dto.UserDto;
import com.buihien.datn.dto.UserRoleDto;
import com.buihien.datn.repository.*;
import com.buihien.datn.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SetupDataServiceImpl implements SetupDataService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserService userService;

    @Override
    public void setupRoles() {
        this.createRolesIfNotExists();
        this.createUserAdmin();
    }

    private void createRolesIfNotExists() {
        Map<String, String> roles = new HashMap<>();
        roles.put(DatnConstants.ROLE_ADMIN, "Quản trị viên toàn quyền");
        roles.put(DatnConstants.ROLE_USER, "Người dùng thông thường");
        roles.put(DatnConstants.ROLE_MANAGER, "Quản lý chung");
        roles.put(DatnConstants.ROLE_HR, "Nhân sự");

        for (Map.Entry<String, String> entry : roles.entrySet()) {
            String roleName = entry.getKey();
            String description = entry.getValue();

            if (roleRepository.findByName(roleName).isEmpty()) {
                Role role = new Role();
                role.setName(roleName);
                role.setDescription(description);
                roleRepository.save(role);
            }
        }
    }

    private void createUserAdmin() {
        User user = userService.findUserByRole(DatnConstants.ROLE_ADMIN);
        if (user != null) return;
        Role role = roleRepository.findByName(DatnConstants.ROLE_ADMIN).orElse(null);
        UserDto userDto = new UserDto();
        userDto.setUsername("admin");
        userDto.setPassword("123456");
        userDto.setConfirmPassword("123456");
        userDto.setEmail("admin@gmail.com");
        RoleDto roleDto = new RoleDto(role);
        if (userDto.getRoles() == null) {
            userDto.setRoles(new ArrayList<>());
        }
        userDto.getRoles().add(roleDto);
        userService.saveOrUpdate(userDto);
    }
}
