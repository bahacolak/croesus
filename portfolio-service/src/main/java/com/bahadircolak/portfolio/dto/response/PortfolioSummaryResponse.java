package com.bahadircolak.portfolio.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioSummaryResponse {
    private BigDecimal totalValue;
    private BigDecimal totalProfitLoss;
} 