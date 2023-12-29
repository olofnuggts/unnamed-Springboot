package com.yk.unnamed.exceptions;

public class AuthenticationException extends RuntimeException {

    // Constructors
    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    // You can add additional constructors or methods if needed
}