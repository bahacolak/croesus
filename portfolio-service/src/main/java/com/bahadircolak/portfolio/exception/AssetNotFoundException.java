package com.bahadircolak.portfolio.exception;

import com.bahadircolak.portfolio.constants.ErrorMessages;

public class AssetNotFoundException extends PortfolioException {
    
    public AssetNotFoundException(String assetSymbol) {
        super(String.format(ErrorMessages.ASSET_NOT_FOUND, assetSymbol), "ASSET_NOT_FOUND");
    }
} 