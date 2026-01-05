package com.example.demo.exceptions;

public class IllegalStateFoundException extends RuntimeException {
    public IllegalStateFoundException(String message) {
        super(message);
    }
}