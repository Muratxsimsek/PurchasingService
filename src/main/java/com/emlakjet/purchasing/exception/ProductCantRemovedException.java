package com.emlakjet.purchasing.exception;

public class ProductCantRemovedException extends Exception{
    public ProductCantRemovedException() {
        super("Product can not be removed");
    }
}
