package com.bahadircolak.wallet.repository;

import com.bahadircolak.wallet.model.WalletTransaction;
import com.bahadircolak.wallet.model.WalletTransaction.TransactionType;
import com.bahadircolak.wallet.model.WalletTransaction.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {
    
    List<WalletTransaction> findByUserId(Long userId);
    
    List<WalletTransaction> findByUserIdOrderByTransactionDateDesc(Long userId);
    
    List<WalletTransaction> findByWalletId(Long walletId);
    
    List<WalletTransaction> findByUserIdAndType(Long userId, TransactionType type);
    
    List<WalletTransaction> findByUserIdAndStatus(Long userId, TransactionStatus status);
    
    List<WalletTransaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<WalletTransaction> findByUserIdAndTransactionDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT SUM(wt.amount) FROM WalletTransaction wt WHERE wt.userId = :userId AND wt.type = :type AND wt.status = :status")
    BigDecimal getTotalAmountByUserIdAndTypeAndStatus(@Param("userId") Long userId, 
                                                     @Param("type") TransactionType type, 
                                                     @Param("status") TransactionStatus status);
    
    @Query("SELECT COUNT(wt) FROM WalletTransaction wt WHERE wt.userId = :userId AND wt.type = :type")
    Long countByUserIdAndType(@Param("userId") Long userId, @Param("type") TransactionType type);
} 