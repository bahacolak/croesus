package com.bahadircolak.user.exception;

public class JwtException extends UserException {
    
    public JwtException(String message) {
        super(message, "JWT_ERROR");
    }
    
    public JwtException(String message, Throwable cause) {
        super(message, "JWT_ERROR", cause);
    }
} 