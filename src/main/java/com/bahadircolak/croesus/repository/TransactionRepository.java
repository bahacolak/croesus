package com.bahadircolak.croesus.repository;

import com.bahadircolak.croesus.model.Asset;
import com.bahadircolak.croesus.model.Transaction;
import com.bahadircolak.croesus.model.Transaction.TransactionType;
import com.bahadircolak.croesus.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    List<Transaction> findByUser(User user);
    
    Page<Transaction> findByUser(User user, Pageable pageable);
    
    List<Transaction> findByUserAndType(User user, TransactionType type);
    
    List<Transaction> findByUserAndAsset(User user, Asset asset);
    
    List<Transaction> findByUserAndTransactionDateBetween(User user, LocalDateTime startDate, LocalDateTime endDate);
} 