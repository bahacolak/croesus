package com.bahadircolak.market.validation;

import com.bahadircolak.market.constants.ErrorMessages;
import com.bahadircolak.market.constants.MarketConstants;
import com.bahadircolak.market.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class MarketValidator {

    public void validateSymbol(String symbol) {
        if (symbol == null || symbol.trim().isEmpty()) {
            throw new ValidationException(ErrorMessages.SYMBOL_REQUIRED);
        }
        
        String trimmedSymbol = symbol.trim();
        
        if (trimmedSymbol.length() < MarketConstants.MIN_SYMBOL_LENGTH || 
            trimmedSymbol.length() > MarketConstants.MAX_SYMBOL_LENGTH) {
            throw new ValidationException(String.format(
                ErrorMessages.SYMBOL_INVALID_LENGTH, 
                MarketConstants.MIN_SYMBOL_LENGTH, 
                MarketConstants.MAX_SYMBOL_LENGTH
            ));
        }
        
        if (!trimmedSymbol.matches(MarketConstants.SYMBOL_UPPER_CASE_REGEX)) {
            throw new ValidationException(ErrorMessages.SYMBOL_INVALID_FORMAT);
        }
    }

    public void validateCoinId(String coinId) {
        if (coinId == null || coinId.trim().isEmpty()) {
            throw new ValidationException(ErrorMessages.COIN_ID_REQUIRED);
        }
        
        if (coinId.trim().length() < 2) {
            throw new ValidationException(ErrorMessages.COIN_ID_INVALID);
        }
    }

    public void validateSearchQuery(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new ValidationException(ErrorMessages.SEARCH_QUERY_REQUIRED);
        }
        
        String trimmedQuery = query.trim();
        
        if (trimmedQuery.length() < MarketConstants.MIN_SEARCH_QUERY_LENGTH) {
            throw new ValidationException(String.format(
                ErrorMessages.SEARCH_QUERY_TOO_SHORT, 
                MarketConstants.MIN_SEARCH_QUERY_LENGTH
            ));
        }
        
        if (trimmedQuery.length() > MarketConstants.MAX_SEARCH_QUERY_LENGTH) {
            throw new ValidationException(String.format(
                ErrorMessages.SEARCH_QUERY_TOO_LONG, 
                MarketConstants.MAX_SEARCH_QUERY_LENGTH
            ));
        }
    }
} 