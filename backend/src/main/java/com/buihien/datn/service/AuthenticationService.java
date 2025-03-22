package com.buihien.datn.service;

import com.buihien.datn.dto.auth.ChangePasswordDto;
import com.buihien.datn.dto.auth.SignInDto;
import com.buihien.datn.dto.auth.TokenResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    TokenResponseDto accessToken(SignInDto signInRequest);

    TokenResponseDto refreshToken(HttpServletRequest request);

    void removeToken(HttpServletRequest request);

    void forgotPassword(String email);

    void resetPassword(ChangePasswordDto request);

    void changePassword(ChangePasswordDto request);

}
