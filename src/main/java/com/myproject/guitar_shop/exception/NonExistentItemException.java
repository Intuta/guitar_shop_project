package com.myproject.guitar_shop.exception;

public class NonExistentItemException extends RuntimeException{

    public NonExistentItemException(String errorMessage) {
        super(errorMessage);
    }

}
