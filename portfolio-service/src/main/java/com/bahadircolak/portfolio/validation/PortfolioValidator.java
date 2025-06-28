package com.bahadircolak.portfolio.validation;

import com.bahadircolak.portfolio.constants.ErrorMessages;
import com.bahadircolak.portfolio.constants.PortfolioConstants;
import com.bahadircolak.portfolio.dto.request.UpdatePortfolioRequest;
import com.bahadircolak.portfolio.exception.PortfolioException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Component
public class PortfolioValidator {
    
    public void validateUpdateRequest(UpdatePortfolioRequest request) {
        validateAction(request.getAction());
        validateQuantity(request.getQuantity());
        validatePrice(request.getPrice());
        validateSymbol(request.getAssetSymbol());
    }
    
    private void validateAction(String action) {
        if (!StringUtils.hasText(action)) {
            throw new PortfolioException(String.format(ErrorMessages.INVALID_ACTION, "null"), "INVALID_ACTION");
        }
        
        if (!PortfolioConstants.BUY_ACTION.equals(action) && !PortfolioConstants.SELL_ACTION.equals(action)) {
            throw new PortfolioException(String.format(ErrorMessages.INVALID_ACTION, action), "INVALID_ACTION");
        }
    }
    
    private void validateQuantity(BigDecimal quantity) {
        if (quantity == null || quantity.compareTo(PortfolioConstants.ZERO) <= 0) {
            throw new PortfolioException(ErrorMessages.INVALID_QUANTITY, "INVALID_QUANTITY");
        }
    }
    
    private void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(PortfolioConstants.ZERO) <= 0) {
            throw new PortfolioException(ErrorMessages.INVALID_PRICE, "INVALID_PRICE");
        }
    }
    
    private void validateSymbol(String symbol) {
        if (!StringUtils.hasText(symbol)) {
            throw new PortfolioException(ErrorMessages.INVALID_SYMBOL, "INVALID_SYMBOL");
        }
    }
} 