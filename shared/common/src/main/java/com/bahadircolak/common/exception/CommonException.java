package com.bahadircolak.common.exception;

public class CommonException extends RuntimeException {
    
    private final String errorCode;
    
    public CommonException(String message) {
        super(message);
        this.errorCode = "COMMON_ERROR";
    }
    
    public CommonException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public CommonException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "COMMON_ERROR";
    }
    
    public CommonException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
} 