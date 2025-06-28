package com.bahadircolak.trading.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetInfo {
    private Long id;
    private String symbol;
    private String name;
    private BigDecimal currentPrice;
    
    public String getDisplayName() {
        return name != null ? name : symbol;
    }
} 