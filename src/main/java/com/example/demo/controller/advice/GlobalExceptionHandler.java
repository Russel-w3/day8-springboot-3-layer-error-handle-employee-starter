package com.example.demo.controller.advice;

import com.example.demo.exception.InvalidAgeEmployeeException;
import com.example.demo.exception.InvalidStatusEmployeeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ResponseStatusException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseException ResponseStatusExceptionHandler(Exception exception) {
        return new ResponseException(exception.getMessage());
    }

    @ExceptionHandler(value = InvalidAgeEmployeeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseException InvalidAgeEmployeeExceptionHandler(Exception exception) {
        return new ResponseException(exception.getMessage());
    }
}
