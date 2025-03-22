package com.buihien.datn.service.impl;

import com.buihien.datn.domain.Role;
import com.buihien.datn.domain.User;
import com.buihien.datn.domain.UserRole;
import com.buihien.datn.dto.RoleDto;
import com.buihien.datn.dto.UserDto;
import com.buihien.datn.dto.UserRoleDto;
import com.buihien.datn.repository.RoleRepository;
import com.buihien.datn.repository.UserRoleRepository;
import com.buihien.datn.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class UserRoleServiceImp implements UserRoleService {
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void handleSetRoleListInUser(UserDto dto, User entity) {
        if (entity.getRoles() == null) {
            entity.setRoles(new HashSet<>());
        }
        entity.getRoles().clear();
        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            for (UserRoleDto item : dto.getRoles()) {
                Role role = null;
                if (item.getRole().getId() != null) {
                    role = roleRepository.findById(item.getRole().getId()).orElse(null);
                }
                if (role == null) continue;
                UserRole userRole = null;
                if (entity.getId() != null) {
                    userRole = userRoleRepository.getUserRoleByUserIdAndRoleId(entity.getId(), role.getId());
                }
                if (userRole == null) {
                    userRole = new UserRole();
                }
                userRole.setRole(role);
                userRole.setUser(entity);
                entity.getRoles().add(userRole);
            }
        }
    }
}
