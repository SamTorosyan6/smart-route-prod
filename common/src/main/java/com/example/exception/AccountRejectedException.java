package com.example.exception;

public class AccountRejectedException extends RuntimeException {
    public AccountRejectedException(String message) {
        super(message);
    }
}
