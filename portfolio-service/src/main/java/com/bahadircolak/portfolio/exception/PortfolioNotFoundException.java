package com.bahadircolak.portfolio.exception;

import com.bahadircolak.portfolio.constants.ErrorMessages;

public class PortfolioNotFoundException extends PortfolioException {
    
    public PortfolioNotFoundException(Long userId, String asset) {
        super(String.format(ErrorMessages.PORTFOLIO_NOT_FOUND, userId, asset), "PORTFOLIO_NOT_FOUND");
    }
} 