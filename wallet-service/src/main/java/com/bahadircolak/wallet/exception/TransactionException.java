package com.bahadircolak.wallet.exception;

public class TransactionException extends WalletException {
    
    public TransactionException(String message) {
        super(message, "TRANSACTION_ERROR");
    }
    
    public TransactionException(String message, Throwable cause) {
        super(message, "TRANSACTION_ERROR", cause);
    }
} 