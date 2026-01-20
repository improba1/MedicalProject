package com.example.demo.handler;

import com.example.demo.exceptions.model.ApiError;
import com.example.demo.exceptions.model.ApiErrorResponseBuilder;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.security.SignatureException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ApiErrorResponseBuilder errorBuilder;

    // 400 - Validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {

        var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> new ApiError.ErrorDetails(e.getField(), e.getDefaultMessage()))
                .toList();

        return errorBuilder.build(
                "Validation failed",
                "VALIDATION_ERROR",
                HttpStatus.BAD_REQUEST,
                req,
                errors
        );
    }

    // 401 - JWT expired
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiError> handleExpiredJwt(ExpiredJwtException ex, HttpServletRequest req) {
        return errorBuilder.build(
                "Token has expired",
                "JWT_EXPIRED",
                HttpStatus.UNAUTHORIZED,
                req
        );
    }

    // 401 - Invalid JWT
    @ExceptionHandler({MalformedJwtException.class, UnsupportedJwtException.class, SignatureException.class})
    public ResponseEntity<ApiError> handleInvalidJwt(RuntimeException ex, HttpServletRequest req) {
        return errorBuilder.build(
                "Invalid JWT token",
                "JWT_INVALID",
                HttpStatus.UNAUTHORIZED,
                req
        );
    }

    // 401 - Bad credentials
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex, HttpServletRequest req) {
        return errorBuilder.build(
                "Invalid username or password",
                "BAD_CREDENTIALS",
                HttpStatus.UNAUTHORIZED,
                req
        );
    }

    // 403 - Forbidden
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        return errorBuilder.build(
                "Access denied",
                "ACCESS_DENIED",
                HttpStatus.FORBIDDEN,
                req
        );
    }

    // 404 - Endpoint not found
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiError> handleNoHandler(NoHandlerFoundException ex, HttpServletRequest req) {
        return errorBuilder.build(
                "Resource not found",
                "RESOURCE_NOT_FOUND",
                HttpStatus.NOT_FOUND,
                req
        );
    }

    // 404 - Entity not found
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest req) {
        return errorBuilder.build(
                ex.getMessage(),
                "ENTITY_NOT_FOUND",
                HttpStatus.NOT_FOUND,
                req
        );
    }

    // 400 - Illegal state / argument
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ApiError> handleBadRequest(RuntimeException ex, HttpServletRequest req) {
        return errorBuilder.build(
                ex.getMessage(),
                "BAD_REQUEST",
                HttpStatus.BAD_REQUEST,
                req
        );
    }

    // 500 - Fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {
        log.error("Unhandled exception", ex);
        return errorBuilder.build(
                "Internal server error",
                "INTERNAL_ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR,
                req
        );
    }
}