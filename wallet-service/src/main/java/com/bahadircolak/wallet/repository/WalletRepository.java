package com.bahadircolak.wallet.repository;

import com.bahadircolak.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    
    Optional<Wallet> findByUserId(Long userId);
    
    Optional<Wallet> findByUserIdAndIsActiveTrue(Long userId);
    
    @Query("SELECT w.balance FROM Wallet w WHERE w.userId = :userId AND w.isActive = true")
    Optional<BigDecimal> findBalanceByUserId(@Param("userId") Long userId);
    
    boolean existsByUserId(Long userId);
}