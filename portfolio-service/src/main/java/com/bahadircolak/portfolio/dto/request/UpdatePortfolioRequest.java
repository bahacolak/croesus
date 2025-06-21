package com.bahadircolak.portfolio.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePortfolioRequest {
    private BigDecimal quantity;
    private BigDecimal price;
    private String action;
    private String assetSymbol;
    private String assetName;
} 