package com.buihien.datn.service.impl;

import com.buihien.datn.domain.Staff;
import com.buihien.datn.domain.User;
import com.buihien.datn.dto.StaffDto;
import com.buihien.datn.dto.UserDto;
import com.buihien.datn.repository.StaffRepository;
import com.buihien.datn.service.ExtractCurrentUserService;
import com.buihien.datn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExtractCurrentUserServiceImpl implements ExtractCurrentUserService {
    @Autowired
    private UserService userService;
    @Autowired
    private StaffRepository staffRepository;

    @Override
    public User extractCurrentUser() {
        return this.getCurrentUser();
    }

    @Override
    public UserDto extractCurrentUserDto() {
        User user = this.getCurrentUser();
        return new UserDto(user);
    }

    @Override
    public Staff extractCurrentStaff() {
        User currentUser = this.getCurrentUser(); // Lấy user hiện tại
        if (currentUser != null && currentUser.getId() != null) {
            // Giả sử bạn có một phương thức để tìm staff dựa trên userId
            return staffRepository.findByUserId(currentUser.getId());
        }
        return null;
    }

    @Override
    public StaffDto extractCurrentStaffDto() {
        User currentUser = this.getCurrentUser(); // Lấy user hiện tại
        if (currentUser != null && currentUser.getId() != null) {
            // Giả sử bạn có một phương thức để tìm staff dựa trên userId
            Staff staff = staffRepository.findByUserId(currentUser.getId());
            return new StaffDto(staff, false);
        }
        return null;
    }

    @Override
    public boolean hasAnyRole(List<String> roles) {
        User user = this.getCurrentUser();
        List<String> currentRoles = userService.getAllRolesByUserUserName(user.getUsername());
        return currentRoles.stream().anyMatch(roles::contains);
    }

    @Override
    public boolean hasAllRoles(List<String> roles) {
        User user = this.getCurrentUser();
        List<String> currentRoles = userService.getAllRolesByUserUserName(user.getUsername());
        return roles.stream().allMatch(currentRoles::contains);

    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        throw new SecurityException("You are not logged in");
    }
}
