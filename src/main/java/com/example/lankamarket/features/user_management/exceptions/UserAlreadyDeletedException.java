package com.example.lankamarket.features.user_management.exceptions;

public class UserAlreadyDeletedException extends RuntimeException {
    public UserAlreadyDeletedException(String message) {
        super(message);
    }
    
    public UserAlreadyDeletedException(String message, Throwable cause) {
        super(message, cause);
    }
}
