package com.bahadircolak.portfolio.exception;

import com.bahadircolak.portfolio.constants.ErrorMessages;

import java.math.BigDecimal;

public class InsufficientQuantityException extends PortfolioException {
    
    public InsufficientQuantityException(BigDecimal available, BigDecimal requested) {
        super(String.format(ErrorMessages.INSUFFICIENT_QUANTITY, available, requested), "INSUFFICIENT_QUANTITY");
    }
} 