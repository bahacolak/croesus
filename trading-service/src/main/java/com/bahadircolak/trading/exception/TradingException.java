package com.bahadircolak.trading.exception;

public class TradingException extends RuntimeException {
    private final String errorCode;

    public TradingException(String message) {
        super(message);
        this.errorCode = "TRADING_ERROR";
    }

    public TradingException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public TradingException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "TRADING_ERROR";
    }

    public TradingException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
} 