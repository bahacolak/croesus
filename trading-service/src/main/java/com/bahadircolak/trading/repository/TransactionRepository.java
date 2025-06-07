package com.bahadircolak.trading.repository;

import com.bahadircolak.trading.model.Transaction;
import com.bahadircolak.trading.model.Transaction.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    List<Transaction> findByUserId(Long userId);
    
    List<Transaction> findByUserIdAndType(Long userId, TransactionType type);
    
    List<Transaction> findByUserIdAndAssetId(Long userId, Long assetId);
    
    List<Transaction> findByUserIdOrderByTransactionDateDesc(Long userId);
    
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.userId = :userId AND t.type = :type")
    BigDecimal getTotalAmountByUserIdAndType(@Param("userId") Long userId, @Param("type") TransactionType type);
    
    List<Transaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
} 