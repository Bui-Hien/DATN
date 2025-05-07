package com.buihien.datn.repository;

import com.buihien.datn.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {
    @Query("SELECT entity FROM Token entity WHERE entity.user.id = :id AND entity.refreshToken = :refreshToken")
    Optional<Token> findTokenWithUserIdRefreshToken(@Param("id") UUID id, @Param("refreshToken") String refreshToken);

    @Transactional
    @Modifying
    @Query("DELETE FROM Token entity WHERE entity.user.email = :keyword OR entity.user.username = :keyword")
    void deleteAllTokenOfUser(@Param("keyword") String keyword);

    @Transactional
    @Modifying
    @Query("DELETE FROM Token entity WHERE entity.refreshToken = :refreshToken")
    void deleteTokenRefresh(@Param("refreshToken") String refreshToken);

    @Query("SELECT entity FROM Token entity WHERE entity.user.username = :keyword OR entity.user.email = :keyword")
    Optional<Token> findTokenByUsername(@Param("keyword") String keyword);
}
