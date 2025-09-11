package com.example.demo.controller.advice;

import com.example.demo.exception.InvalidAgeAndSalaryEmployeeException;
import com.example.demo.exception.InvalidAgeEmployeeException;
import com.example.demo.exception.InvalidStatusEmployeeException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ResponseStatusException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseException ResponseStatusExceptionHandler(Exception exception) {
        return new ResponseException(exception.getMessage());
    }

    @ExceptionHandler(value = InvalidAgeEmployeeException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseException InvalidAgeEmployeeExceptionHandler(Exception exception) {
        return new ResponseException(exception.getMessage());
    }

    @ExceptionHandler(value = InvalidAgeAndSalaryEmployeeException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseException InvalidAgeAndSalaryEmployeeExceptionHandler(Exception exception) {
        return new ResponseException(exception.getMessage());
    }

    @ExceptionHandler(value = InvalidStatusEmployeeException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseException InvalidStatusEmployeeExceptionHandler(Exception exception) {
        return new ResponseException(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseException handlerArgumentNotValid(MethodArgumentNotValidException exception){
        String errorMessage = exception.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(" | "));

        return new ResponseException(errorMessage);
    }
}
