package com.example.demo.repository;

import com.example.demo.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {

    @Query("select t from Token t where t.user.id = :userId and t.expired = false and t.revoked = false")
    List<Token> findAllValidTokenByUserId(UUID userId);

    Optional<Token> findByToken(String token);

    @Modifying
    @Transactional
    @Query("update Token t set t.expired = true, t.revoked = true where t.user.email = :email")
    void invalidateTokensForUser(String email);
}