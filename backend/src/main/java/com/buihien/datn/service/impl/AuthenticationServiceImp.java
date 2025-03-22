package com.buihien.datn.service.impl;

import com.buihien.datn.domain.Token;
import com.buihien.datn.domain.User;
import com.buihien.datn.dto.ChangePasswordDto;
import com.buihien.datn.dto.SignInDto;
import com.buihien.datn.dto.TokenResponseDto;
import com.buihien.datn.exception.ConflictDataException;
import com.buihien.datn.exception.InvalidDataException;
import com.buihien.datn.exception.ResourceNotFoundException;
import com.buihien.datn.service.*;
import io.micrometer.common.util.StringUtils;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.buihien.datn.DatnConstants.TokenType.*;
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
    @Autowired
    private TokenService tokenService;
    @Autowired
    private MailService mailService;

    @Value("${maxFailedLoginAttempts}")
    private int maxFailedLoginAttempts;

    @Override
    public TokenResponseDto accessToken(SignInDto signInRequest) {
        User user = userService.getByUsername(signInRequest.getUsername());
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
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
            throw new InvalidDataException("Invalid username or password");
        }

        // Lấy danh sách quyền của user
        List<String> roles = userService.getAllRolesByUserId(user.getId());
        List<SimpleGrantedAuthority> authorities = roles.stream().map(SimpleGrantedAuthority::new).toList();

        // Nếu xác thực thành công, tiếp tục tạo token
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        Token token = new Token(user, accessToken, refreshToken);
        tokenService.saveOrUpdateAccessRefresh(token);

        return new TokenResponseDto(accessToken, refreshToken);

    }

    @Override
    public TokenResponseDto refreshToken(HttpServletRequest request) {

        final String refreshToken = request.getHeader(REFERER); //REFERER giống như Bear
        if (StringUtils.isBlank(refreshToken)) {
            throw new InvalidDataException("Token must be not blank");
        }
        final String userName = jwtService.extractUsername(refreshToken, REFRESH_TOKEN.getValue());
        var user = userService.getByUsername(userName);
        if (!jwtService.isValid(refreshToken, REFRESH_TOKEN.getValue(), user)) {
            throw new InvalidDataException("Not allow access with this token");
        }

        String accessToken = jwtService.generateToken(user);

        Token token = new Token(user, accessToken);
        tokenService.saveOrUpdateAccessRefresh(token);

        return new TokenResponseDto(accessToken, refreshToken);
    }

    //    Logout
    @Override
    public void removeToken(HttpServletRequest request) {
        final String refreshToken = request.getHeader(REFERER);
        if (StringUtils.isBlank(refreshToken)) {
            throw new InvalidDataException("Token must be not blank");
        }
        jwtService.extractUsername(refreshToken, REFRESH_TOKEN.getValue());
        tokenService.deleteTokenRefresh(refreshToken);
    }

    @Override
    @Transactional
    public void forgotPassword(String username) {

        User user = userService.getByUsername(username);
        String resetToken = jwtService.generateResetToken(user);
        //Khi quên password thi xóa hết token đi để người dùng đăng nhập lại
        tokenService.deleteToken(user.getUsername());
        Token token = new Token(user);
        token.setResetToken(resetToken);
        tokenService.saveRestSetToken(token);
        try {
            mailService.sendConfirmLink(user.getEmail(), user.getUsername(), resetToken);
            tokenService.deleteToken(resetToken);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new InvalidDataException("Error while sending reset link");
        }
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

        Token token = tokenService.getTokenByUsername(user.getUsername());
        if (!request.getSecretKey().equals(token.getResetToken())) {
            throw new InvalidDataException("Reset token does not match");
        }
        token.setResetToken(null);
        tokenService.saveRestSetToken(token);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setLastLoginFailures(null);
        user.setVoided(false);
        userService.saveUser(user);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordDto request) {
        if (request == null || request.getUsername() == null || request.getOldPassword() == null || request.getPassword() == null || request.getConfirmPassword() == null) {
            throw new InvalidDataException("Invalid request data");
        }
        User user = userService.getByUsername(request.getUsername());
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new InvalidDataException("Old password is incorrect");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidDataException("New passwords do not match");
        }
        tokenService.deleteToken(user.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setLastLoginFailures(null);
        user.setVoided(false);
        userService.saveUser(user);
    }

}
