package com.example.lankamarket.features.user_management.exceptions;

public class InvalidUserOperationException extends RuntimeException {
    public InvalidUserOperationException(String message) {
        super(message);
    }
    
    public InvalidUserOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
