package com.example.inventoryservice.exception;

public class ProductInactiveException
        extends RuntimeException {

    public ProductInactiveException(String message) {
        super(message);
    }
}