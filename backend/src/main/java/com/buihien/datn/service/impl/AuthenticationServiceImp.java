package com.buihien.datn.service.impl;

import com.buihien.datn.domain.User;
import com.buihien.datn.dto.auth.ChangePasswordDto;
import com.buihien.datn.dto.auth.SignInDto;
import com.buihien.datn.dto.auth.TokenResponseDto;
import com.buihien.datn.exception.ConflictDataException;
import com.buihien.datn.exception.InvalidDataException;
import com.buihien.datn.exception.ResourceNotFoundException;
import com.buihien.datn.service.AuthenticationService;
import com.buihien.datn.service.JwtService;
import com.buihien.datn.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.buihien.datn.DatnConstants.TokenType.REFRESH_TOKEN;
import static com.buihien.datn.DatnConstants.TokenType.RESET_TOKEN;
import static org.springframework.http.HttpHeaders.REFERER;

@Service
public class AuthenticationServiceImp implements AuthenticationService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    @Value("${maxFailedLoginAttempts}")
    private int maxFailedLoginAttempts;

    @Override
    public TokenResponseDto accessToken(SignInDto signInRequest) {
        User user = userService.getByUsername(signInRequest.getUsername());
        if (user == null) {
            throw new ResourceNotFoundException("Không tìm thấy người dùng");
        }

        if (!user.isEnabled()) {
            throw new ConflictDataException("User not active");
        }

        // Xác thực người dùng
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
        } catch (BadCredentialsException ex) {
            long lastLoginFailures = (user.getLastLoginFailures() != null) ? user.getLastLoginFailures() + 1 : 1;
            long totalLoginFailures = (user.getTotalLoginFailures() != null) ? user.getTotalLoginFailures() + 1 : 1;

            user.setLastLoginFailures(lastLoginFailures);
            user.setTotalLoginFailures(totalLoginFailures);
            if (lastLoginFailures >= maxFailedLoginAttempts) {
                user.setVoided(true);
            }
            userService.saveUser(user);
            throw new InvalidDataException("Tên người dùng hoặc mật khẩu không chính xác");
        }

        // Lấy danh sách quyền của user
        List<String> roles = userService.getAllRolesByUserId(user.getId());
        List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).toList();

        // Nếu xác thực thành công, tiếp tục tạo token
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new TokenResponseDto(accessToken, refreshToken);

    }

    @Override
    public TokenResponseDto refreshToken(HttpServletRequest request) {
        try {
            final String refreshToken = request.getHeader("X-Refresh-Token");
            if (StringUtils.isBlank(refreshToken)) {
                throw new InvalidDataException("Token must be not blank");
            }

            final String userName = jwtService.extractUsername(refreshToken, REFRESH_TOKEN.getValue());
            var user = userService.getByUsername(userName);

            if (!jwtService.isValid(refreshToken, REFRESH_TOKEN.getValue(), user)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Refresh token is invalid or expired");
            }

            String accessToken = jwtService.generateToken(user);
            return new TokenResponseDto(accessToken, refreshToken);
        } catch (ExpiredJwtException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Refresh token expired");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid refresh token");
        }
    }


    //    Logout
    @Override
    public void removeToken(HttpServletRequest request) {
        final String refreshToken = request.getHeader(REFERER);
        if (StringUtils.isBlank(refreshToken)) {
            throw new InvalidDataException("Token must be not blank");
        }
        jwtService.extractUsername(refreshToken, REFRESH_TOKEN.getValue());
    }

    @Override
    @Transactional
    public void forgotPassword(String username) {

        User user = userService.getByUsername(username);
        String resetToken = jwtService.generateResetToken(user);
    }

    @Override
    @Transactional
    public void resetPassword(ChangePasswordDto request) {
        if (request == null || request.getSecretKey() == null || request.getPassword() == null || request.getConfirmPassword() == null) {
            throw new InvalidDataException("Invalid request data");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidDataException("New passwords do not match");
        }
        String userName = jwtService.extractUsername(request.getSecretKey(), RESET_TOKEN.getValue());
        var user = userService.getByUsername(userName);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setLastLoginFailures(null);
        user.setVoided(false);
        userService.saveUser(user);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordDto request) {
        if (request == null || request.getOldPassword() == null || request.getPassword() == null || request.getConfirmPassword() == null) {
            throw new InvalidDataException("Vui lòng nhập đầy đủ dữ liệu");
        }
        User user = userService.getCurrentUserEntity();
        if (user == null) {
            throw new ResourceNotFoundException("Người dùng không tồn tại");
        }
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new InvalidDataException("Mật khẩu hiện tại không chính xác");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidDataException("Mật khẩu mới và mật khẩu xác nhận không khớp");
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setLastLoginFailures(null);
        user.setVoided(false);
        userService.saveUser(user);
    }

}
