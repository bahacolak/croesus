package com.bahadircolak.user.exception;

public class UserAlreadyExistsException extends UserException {
    
    public UserAlreadyExistsException(String message) {
        super(message, "USER_ALREADY_EXISTS");
    }
    
    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, "USER_ALREADY_EXISTS", cause);
    }
} 