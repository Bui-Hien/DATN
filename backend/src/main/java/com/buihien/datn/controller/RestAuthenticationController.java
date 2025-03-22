package com.buihien.datn.controller;


import com.buihien.datn.dto.auth.ChangePasswordDto;
import com.buihien.datn.dto.auth.SignInDto;
import com.buihien.datn.dto.auth.TokenResponseDto;
import com.buihien.datn.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/auth")
public class RestAuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/access-token")
    public ResponseEntity<TokenResponseDto> accessToken(@RequestBody SignInDto request) {
        return new ResponseEntity<>(authenticationService.accessToken(request), HttpStatus.OK); // 200 - vì đây là xác thực thành công
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponseDto> refreshToken(HttpServletRequest request) {
        return new ResponseEntity<>(authenticationService.refreshToken(request), HttpStatus.OK); // 200 - làm mới token thành công
    }

    @PostMapping("/remove-token")
    public ResponseEntity<?> removeToken(HttpServletRequest request) {
        authenticationService.removeToken(request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 - xóa thành công, không cần trả về content
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody String email) {
        authenticationService.forgotPassword(email);
        return new ResponseEntity<>(HttpStatus.OK); // 200 - gửi email reset password thành công
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ChangePasswordDto request) {
        authenticationService.resetPassword(request);
        return new ResponseEntity<>(HttpStatus.OK); // 200 - reset password thành công
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordDto request) {
        authenticationService.changePassword(request);
        return new ResponseEntity<>(HttpStatus.OK); // 200 - đổi mật khẩu thành công
    }
}
