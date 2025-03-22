package com.buihien.datn.domain;

import jakarta.persistence.*;

@Table(name = "tbl_token")
@Entity
public class Token extends AuditableEntity {
    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "reset_token")
    private String resetToken;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Token() {
    }
    public Token(User user) {
        this.user = user;
    }
    public Token(User user, String accessToken) {
        this.user = user;
        this.accessToken = accessToken;
    }

    public Token(User user, String accessToken, String refreshToken) {
        this.user = user;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public Token(User user, String accessToken, String refreshToken, String resetToken) {
        this.user = user;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.resetToken = resetToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
