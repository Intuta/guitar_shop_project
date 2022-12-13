package com.myproject.guitar_shop.exception;

public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException() {
    }

    public IncorrectPasswordException(String errorMessage) {
        super(errorMessage);
    }

}
