package com.bahadircolak.wallet.exception;

public class InsufficientBalanceException extends WalletException {
    
    public InsufficientBalanceException(String message) {
        super(message, "INSUFFICIENT_BALANCE");
    }
    
    public InsufficientBalanceException(String message, Throwable cause) {
        super(message, "INSUFFICIENT_BALANCE", cause);
    }
} 