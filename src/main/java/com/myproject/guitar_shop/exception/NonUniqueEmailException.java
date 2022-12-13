package com.myproject.guitar_shop.exception;

public class NonUniqueEmailException extends RuntimeException {
    public NonUniqueEmailException() {
    }

    public NonUniqueEmailException(String errorMessage) {
        super(errorMessage);
    }
}
