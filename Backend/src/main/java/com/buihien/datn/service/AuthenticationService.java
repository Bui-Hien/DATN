package com.buihien.datn.service;

import com.buihien.datn.dto.ChangePasswordDto;
import com.buihien.datn.dto.SignInDto;
import com.buihien.datn.dto.TokenResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    TokenResponseDto accessToken(SignInDto signInRequest);

    TokenResponseDto refreshToken(HttpServletRequest request);

    void removeToken(HttpServletRequest request);

    void forgotPassword(String email);

    void resetPassword(ChangePasswordDto request);

    void changePassword(ChangePasswordDto request);

}
