package com.bahadircolak.portfolio.repository;

import com.bahadircolak.portfolio.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    
    List<Portfolio> findByUserId(Long userId);
    
    @Query("SELECT p FROM Portfolio p WHERE p.userId = :userId AND p.asset.id = :assetId")
    Optional<Portfolio> findByUserIdAndAssetId(@Param("userId") Long userId, @Param("assetId") Long assetId);
    
    @Query("SELECT p FROM Portfolio p WHERE p.userId = :userId AND p.asset.symbol = :symbol")
    Optional<Portfolio> findByUserIdAndAssetSymbol(@Param("userId") Long userId, @Param("symbol") String symbol);
    
    @Query("SELECT SUM(p.currentValue) FROM Portfolio p WHERE p.userId = :userId")
    BigDecimal getTotalValueByUserId(@Param("userId") Long userId);
    
    @Query("SELECT SUM(p.profitLoss) FROM Portfolio p WHERE p.userId = :userId")
    BigDecimal getTotalProfitLossByUserId(@Param("userId") Long userId);
} 