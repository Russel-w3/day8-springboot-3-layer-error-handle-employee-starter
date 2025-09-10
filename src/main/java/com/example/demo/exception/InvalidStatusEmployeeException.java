package com.example.demo.exception;

public class InvalidStatusEmployeeException extends RuntimeException {
    public InvalidStatusEmployeeException(String message) {
        super(message);
    }
}
