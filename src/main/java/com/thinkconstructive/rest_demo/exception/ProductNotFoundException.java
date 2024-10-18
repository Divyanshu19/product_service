package com.thinkconstructive.rest_demo.exception;

public class ProductNotFoundException extends  RuntimeException {
    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductNotFoundException(String message) {
        super(message);
    }
}
