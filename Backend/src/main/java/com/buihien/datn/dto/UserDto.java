package com.buihien.datn.dto;

import com.buihien.datn.domain.User;
import com.buihien.datn.domain.UserRole;

import java.util.ArrayList;
import java.util.List;

public class UserDto extends AuditableEntityDto {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
    private List<UserRoleDto> roles;
    private PersonDto person;

    public UserDto() {
    }

    public UserDto(User entity) {
        super(entity);
        if (entity != null) {
            this.username = entity.getUsername();
            this.email = entity.getEmail();
            if (entity.getPerson() != null) {
                this.person = new PersonDto(entity.getPerson());
            }
            if (entity.getRoles() != null && !entity.getRoles().isEmpty()) {
                this.roles = new ArrayList<>();
                for (UserRole role : entity.getRoles()) {
                    this.roles.add(new UserRoleDto(role));
                }
            }
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<UserRoleDto> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRoleDto> roles) {
        this.roles = roles;
    }

    public PersonDto getPerson() {
        return person;
    }

    public void setPerson(PersonDto person) {
        this.person = person;
    }
}
