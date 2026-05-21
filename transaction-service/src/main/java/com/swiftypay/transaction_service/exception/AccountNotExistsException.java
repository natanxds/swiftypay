package com.swiftypay.transaction_service.exception;

public class AccountNotExistsException extends RuntimeException {
    public AccountNotExistsException(String message) {
        super(message);
    }
}
