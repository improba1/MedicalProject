package com.example.demo.service.jwt;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;
import io.jsonwebtoken.Claims;

public interface JwtService {

    String extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    // Генерація токенів завжди з email (UserDetails.getUsername() повертає email)
    String generateToken(UserDetails userDetails);
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);

    boolean isTokenValid(String token, UserDetails userDetails);
}