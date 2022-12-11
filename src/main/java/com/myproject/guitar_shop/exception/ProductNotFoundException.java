package com.myproject.guitar_shop.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String errorMessage) {
        super(errorMessage);
    }

}
