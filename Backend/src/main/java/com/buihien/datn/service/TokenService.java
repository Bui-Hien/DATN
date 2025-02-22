package com.buihien.datn.service;

import com.buihien.datn.domain.Token;

public interface TokenService {
    void saveOrUpdateAccessRefresh(Token token);

    void saveRestSetToken(Token token);

    void deleteTokenRefresh(String refreshToken);

    void deleteToken(String username);

    Token getTokenByUsername(String name);
}
