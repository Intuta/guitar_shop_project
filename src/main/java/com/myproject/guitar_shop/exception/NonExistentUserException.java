package com.myproject.guitar_shop.exception;

public class NonExistentUserException extends RuntimeException {

    public NonExistentUserException(String errorMessage) {
        super(errorMessage);
    }

}
