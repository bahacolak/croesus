package com.bahadircolak.common.exception;

public class WalletServiceException extends CommonException {
    
    public WalletServiceException(String message) {
        super(message, "WALLET_SERVICE_ERROR");
    }
    
    public WalletServiceException(String message, Throwable cause) {
        super(message, "WALLET_SERVICE_ERROR", cause);
    }
} 