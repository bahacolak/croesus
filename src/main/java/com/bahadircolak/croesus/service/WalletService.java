package com.bahadircolak.croesus.service;

import com.bahadircolak.croesus.dto.request.WalletTransactionRequest;
import com.bahadircolak.croesus.dto.response.MessageResponse;
import com.bahadircolak.croesus.model.Transaction;
import com.bahadircolak.croesus.model.Transaction.TransactionType;
import com.bahadircolak.croesus.model.User;
import com.bahadircolak.croesus.repository.TransactionRepository;
import com.bahadircolak.croesus.repository.UserRepository;
import com.bahadircolak.croesus.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public ResponseEntity<?> deposit(WalletTransactionRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setWalletBalance(user.getWalletBalance().add(request.getAmount()));
        userRepository.save(user);
        
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setAmount(request.getAmount());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setDescription(request.getDescription() != null ? 
                request.getDescription() : "Deposit");
        transactionRepository.save(transaction);
        
        return ResponseEntity.ok(new MessageResponse("Deposit successful. New balance: " 
                + user.getWalletBalance()));
    }
    
    @Transactional
    public ResponseEntity<?> withdraw(WalletTransactionRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getWalletBalance().compareTo(request.getAmount()) < 0) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Insufficient balance. Current balance: " + user.getWalletBalance()));
        }
        
        user.setWalletBalance(user.getWalletBalance().subtract(request.getAmount()));
        userRepository.save(user);

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setAmount(request.getAmount());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setDescription(request.getDescription() != null ? 
                request.getDescription() : "Withdrawal");
        transactionRepository.save(transaction);
        
        return ResponseEntity.ok(new MessageResponse("Withdrawal successful. New balance: " 
                + user.getWalletBalance()));
    }
    
    public ResponseEntity<?> getBalance() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return ResponseEntity.ok(new MessageResponse("Current balance: " + user.getWalletBalance()));
    }
} 