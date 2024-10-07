package com.emlakjet.purchasing.exception;

public class ProductNotFoundException extends Exception{
    public ProductNotFoundException() {
        super("Product not found");
    }
}
