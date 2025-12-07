package com.example.demo.handler;

import com.example.demo.exceptions.model.ApiError;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import jakarta.persistence.EntityNotFoundException;

import java.security.SignatureException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 400 - Validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest req) {
        var errors = ex.getBindingResult().getFieldErrors().stream().map(fe ->
                new ApiError.ErrorDetails(fe.getField(), fe.getDefaultMessage())
        ).toList();
        return ApiError.buildResponse(
                "The submitted data is invalid. Please check the marked fields",
                HttpStatus.BAD_REQUEST,
                req,
                errors
        );
    }

    // 401 - JWT expired
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiError> handleExpiredJwt(ExpiredJwtException ex, HttpServletRequest req) {
        return ApiError.buildResponse("Token has expired", HttpStatus.UNAUTHORIZED, req);
    }

    // 401 - JWT invalid (пошкоджений, підроблений, unsupported)
    @ExceptionHandler({MalformedJwtException.class, UnsupportedJwtException.class, SignatureException.class})
    public ResponseEntity<ApiError> handleInvalidJwt(RuntimeException ex, HttpServletRequest req) {
        return ApiError.buildResponse("Invalid JWT token", HttpStatus.UNAUTHORIZED, req);
    }

    // 401 - Bad credentials
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCred(BadCredentialsException ex, HttpServletRequest req) {
        return ApiError.buildResponse("Invalid username or password", HttpStatus.UNAUTHORIZED, req);
    }

    // 403 - Forbidden (access denied)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(HttpServletRequest req) {
        return ApiError.buildResponse("You do not have permission to access this resource", HttpStatus.FORBIDDEN, req);
    }

    // 404 - Resource not found (endpoint not found)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(NoHandlerFoundException ex, HttpServletRequest req) {
        return ApiError.buildResponse("Resource not found", HttpStatus.NOT_FOUND, req);
    }

    // 404 - Entity not found in DB
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest req) {
        return ApiError.buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, req);
    }

    // 500 - Internal server error (fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {
        log.error(ex.getMessage(), ex);
        return ApiError.buildResponse("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR, req);
    }
}
