package com.bahadircolak.trading.validation;

import com.bahadircolak.trading.constants.ErrorMessages;
import com.bahadircolak.trading.constants.TradingConstants;
import com.bahadircolak.trading.dto.request.TradeRequest;
import com.bahadircolak.trading.exception.TradingException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Component
public class TradeValidator {
    
    public void validateTradeRequest(TradeRequest request) {
        validateSymbol(request.getSymbol());
        validateQuantity(request.getQuantity());
    }
    
    private void validateSymbol(String symbol) {
        if (!StringUtils.hasText(symbol)) {
            throw new TradingException(ErrorMessages.INVALID_SYMBOL, "INVALID_SYMBOL");
        }
        
        if (symbol.trim().length() < 2) {
            throw new TradingException("Symbol must be at least 2 characters", "INVALID_SYMBOL_LENGTH");
        }
    }
    
    private void validateQuantity(BigDecimal quantity) {
        if (quantity == null) {
            throw new TradingException(ErrorMessages.INVALID_QUANTITY, "INVALID_QUANTITY");
        }
        
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TradingException(ErrorMessages.INVALID_QUANTITY, "INVALID_QUANTITY");
        }
        
        if (quantity.compareTo(TradingConstants.MINIMUM_TRADE_AMOUNT) < 0) {
            throw new TradingException(
                String.format("Quantity must be at least %s", TradingConstants.MINIMUM_TRADE_AMOUNT), 
                "QUANTITY_TOO_SMALL"
            );
        }
        
        if (quantity.compareTo(TradingConstants.MAXIMUM_TRADE_AMOUNT) > 0) {
            throw new TradingException(
                String.format("Quantity cannot exceed %s", TradingConstants.MAXIMUM_TRADE_AMOUNT), 
                "QUANTITY_TOO_LARGE"
            );
        }
    }
} 