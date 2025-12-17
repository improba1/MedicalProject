package com.example.demo.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.demo.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TokenRepository extends JpaRepository<Token, UUID> {

    @Query("select t from Token t where t.user.id = :id and (t.expired = false or t.revoked = false)")
    List<Token> findAllValidTokenByUser(UUID id);

    Optional<Token> findByToken(String token);
}