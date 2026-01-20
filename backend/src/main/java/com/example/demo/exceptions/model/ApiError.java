package com.example.demo.exceptions.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ApiError(
        Instant timestamp,
        int status,
        String message,
        String path,
        String errorCode,
        List<ErrorDetails> errors
) {

    public record ErrorDetails(String field, String message) {}

    public ApiError(int status, String message, String path, String errorCode, List<ErrorDetails> errors) {
        this(Instant.now(), status, message, path, errorCode, errors);
    }

    public ApiError(int status, String message, String path, String errorCode) {
        this(status, message, path, errorCode, null);
    }
}