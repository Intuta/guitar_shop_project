package com.myproject.guitar_shop.exception;

public class IncorrectPasswordException extends RuntimeException {

    public IncorrectPasswordException(String errorMessage) {
        super(errorMessage);
    }

}
