package com.myproject.guitar_shop.exception;

public class EmptyCartException extends RuntimeException {

    public EmptyCartException(String errorMessage) {
        super(errorMessage);
    }

}
