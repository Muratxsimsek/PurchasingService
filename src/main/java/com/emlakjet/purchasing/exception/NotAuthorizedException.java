package com.emlakjet.purchasing.exception;

public class NotAuthorizedException extends Exception{
    public NotAuthorizedException() {
        super("User not authorized");
    }
}
