package com.myproject.guitar_shop.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException() {
    }

    public ProductNotFoundException(String errorMessage) {
        super(errorMessage);
    }

}
