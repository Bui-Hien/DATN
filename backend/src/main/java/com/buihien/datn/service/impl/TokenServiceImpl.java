package com.buihien.datn.service.impl;

import com.buihien.datn.domain.Token;
import com.buihien.datn.exception.InvalidDataException;
import com.buihien.datn.exception.ResourceNotFoundException;
import com.buihien.datn.repository.TokenRepository;
import com.buihien.datn.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public void saveOrUpdateAccessRefresh(Token token) {
        if (token == null || token.getRefreshToken() == null || token.getUser() == null || token.getUser().getId() == null) {
            throw new InvalidDataException("Invalid user or token");
        }
        Token entity = tokenRepository.findTokenWithUserIdRefreshToken(token.getUser().getId(), token.getRefreshToken()).orElse(null);
        if (entity == null) {
            entity = new Token();
        }
        if (token.getRefreshToken() != null) {
            entity.setRefreshToken(token.getRefreshToken());
        }
        entity.setUser(token.getUser());
        entity.setAccessToken(token.getAccessToken());
        entity.setResetToken(token.getResetToken());
        tokenRepository.save(entity);
    }

    @Override
    public void saveRestSetToken(Token token) {
        tokenRepository.save(token);
    }

    @Override
    public void deleteTokenRefresh(String refreshToken) {
        if (refreshToken == null) throw new InvalidDataException("Invalid username");
        tokenRepository.deleteTokenRefresh(refreshToken);
    }

    @Override
    public void deleteToken(String username) {
        if (username == null) throw new InvalidDataException("Invalid username");
        tokenRepository.deleteAllTokenOfUser(username);

    }

    @Override
    public Token getTokenByUsername(String username) {
        if (username == null) throw new ResourceNotFoundException("Token not found");
        return tokenRepository.findTokenByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Token not found"));
    }
}
