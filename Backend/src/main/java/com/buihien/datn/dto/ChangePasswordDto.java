package com.buihien.datn.dto;

import jakarta.validation.constraints.NotBlank;

public class ChangePasswordDto {

    private String secretKey;

    private String username;

    private String oldPassword;

    @NotBlank(message = "password must be not blank")
    private String password;

    @NotBlank(message = "confirmPassword must be not blank")
    private String confirmPassword;

    public ChangePasswordDto() {
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
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
}
