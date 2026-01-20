package com.example.demo.exceptions.model;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiErrorResponseBuilder {

    public ResponseEntity<ApiError> build(
            String message,
            String errorCode,
            HttpStatus status,
            HttpServletRequest request,
            List<ApiError.ErrorDetails> errors
    ) {
        var body = new ApiError(
                status.value(),
                message,
                request.getRequestURI(),
                errorCode,
                errors
        );
        return ResponseEntity.status(status).body(body);
    }

    public ResponseEntity<ApiError> build(
            String message,
            String errorCode,
            HttpStatus status,
            HttpServletRequest request
    ) {
        return build(message, errorCode, status, request, null);
    }
}