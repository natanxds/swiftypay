package com.swiftypay.transaction_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountNotExistsException.class)
    public ResponseEntity<String> handleAccountNotExistsException(AccountNotExistsException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
