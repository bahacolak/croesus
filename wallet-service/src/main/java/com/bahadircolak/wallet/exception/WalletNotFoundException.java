package com.bahadircolak.wallet.exception;

public class WalletNotFoundException extends WalletException {
    
    public WalletNotFoundException(String message) {
        super(message, "WALLET_NOT_FOUND");
    }
    
    public WalletNotFoundException(String message, Throwable cause) {
        super(message, "WALLET_NOT_FOUND", cause);
    }
} 