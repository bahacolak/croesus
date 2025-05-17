package com.bahadircolak.croesus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "assets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asset {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String symbol;
    
    @Column(nullable = false)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetType type;
    
    @Column(name = "current_price", nullable = false)
    private BigDecimal currentPrice;
    
    @Column(name = "price_change_24h")
    private BigDecimal priceChange24h;
    
    @Column(name = "price_change_percent_24h")
    private BigDecimal priceChangePercent24h;
    
    public enum AssetType {
        CRYPTO,
        CURRENCY,
        PRECIOUS_METAL,
        STOCK
    }
} 