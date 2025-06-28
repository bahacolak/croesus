package com.bahadircolak.trading.exception;

import java.math.BigDecimal;

public class InsufficientBalanceException extends TradingException {
    
    public InsufficientBalanceException(BigDecimal required, BigDecimal current) {
        super(String.format("Insufficient balance. Required: %s, Current: %s", required, current), 
              "INSUFFICIENT_BALANCE");
    }
    
    public InsufficientBalanceException(String message) {
        super(message, "INSUFFICIENT_BALANCE");
    }
} 