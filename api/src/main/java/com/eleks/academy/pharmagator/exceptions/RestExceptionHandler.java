package com.eleks.academy.pharmagator.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<ApiError> handleException(ResponseStatusException ex, WebRequest request) {
        return new ResponseEntity<>(new ApiError(ex.getReason()), ex.getStatus());
    }

    @ExceptionHandler(InvalidIdentifierException.class)
    protected ResponseEntity<ApiError> handleException(InvalidIdentifierException ex, WebRequest request) {
        return new ResponseEntity<>(new ApiError(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

}
