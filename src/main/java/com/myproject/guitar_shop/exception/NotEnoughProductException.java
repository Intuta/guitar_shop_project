package com.myproject.guitar_shop.exception;

public class NotEnoughProductException extends RuntimeException {
    public NotEnoughProductException() {
    }

    public NotEnoughProductException(String errorMessage) {
        super(errorMessage);
    }

}
