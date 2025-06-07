package com.bahadircolak.wallet.service;

import com.bahadircolak.wallet.dto.request.TransferRequest;
import com.bahadircolak.wallet.dto.request.WalletTransactionRequest;
import com.bahadircolak.wallet.dto.response.MessageResponse;
import com.bahadircolak.wallet.dto.response.WalletResponse;
import com.bahadircolak.wallet.model.Wallet;
import com.bahadircolak.wallet.model.WalletTransaction;
import com.bahadircolak.wallet.model.WalletTransaction.TransactionType;
import com.bahadircolak.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionService transactionService;
    private final UserService userService;

    @Transactional
    public WalletResponse createWallet(Long userId) {
        if (walletRepository.existsByUserId(userId)) {
            throw new RuntimeException("Wallet already exists for user: " + userId);
        }

        // User'ın var olduğunu kontrol et
        if (!userService.userExists(userId)) {
            throw new RuntimeException("User not found: " + userId);
        }

        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setCurrencyCode("USD");
        wallet.setIsActive(true);

        Wallet savedWallet = walletRepository.save(wallet);
        log.info("Created wallet for user: {}", userId);

        return convertToWalletResponse(savedWallet);
    }

    public WalletResponse getWalletByUserId(Long userId) {
        Wallet wallet = walletRepository.findByUserIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for user: " + userId));
        return convertToWalletResponse(wallet);
    }

    public BigDecimal getBalance(Long userId) {
        return walletRepository.findBalanceByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for user: " + userId));
    }

    public WalletResponse getCurrentUserWallet() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userService.getUserIdByUsername(userDetails.getUsername());
        
        Wallet wallet = walletRepository.findByUserIdAndIsActiveTrue(userId)
                .orElseGet(() -> createWalletForUser(userId));
        
        return convertToWalletResponse(wallet);
    }

    @Transactional
    public MessageResponse deposit(WalletTransactionRequest request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userService.getUserIdByUsername(userDetails.getUsername());

        Wallet wallet = walletRepository.findByUserIdAndIsActiveTrue(userId)
                .orElseGet(() -> createWalletForUser(userId));

        BigDecimal oldBalance = wallet.getBalance();
        BigDecimal newBalance = oldBalance.add(request.getAmount());
        
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);

        // Transaction kaydı oluştur
        WalletTransaction transaction = new WalletTransaction();
        transaction.setUserId(userId);
        transaction.setWalletId(wallet.getId());
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setAmount(request.getAmount());
        transaction.setBalanceBefore(oldBalance);
        transaction.setBalanceAfter(newBalance);
        transaction.setDescription(request.getDescription() != null ? request.getDescription() : "Deposit");
        transaction.setReferenceId(request.getReferenceId() != null ? request.getReferenceId() : UUID.randomUUID().toString());
        
        transactionService.saveTransaction(transaction);

        log.info("Deposit successful for user: {}, amount: {}", userId, request.getAmount());
        return new MessageResponse("Deposit successful. New balance: " + newBalance);
    }

    @Transactional
    public MessageResponse withdraw(WalletTransactionRequest request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userService.getUserIdByUsername(userDetails.getUsername());

        Wallet wallet = walletRepository.findByUserIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found for user: " + userId));

        BigDecimal oldBalance = wallet.getBalance();
        
        if (oldBalance.compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance. Current balance: " + oldBalance + ", Requested: " + request.getAmount());
        }

        BigDecimal newBalance = oldBalance.subtract(request.getAmount());
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);

        // Transaction kaydı oluştur
        WalletTransaction transaction = new WalletTransaction();
        transaction.setUserId(userId);
        transaction.setWalletId(wallet.getId());
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setAmount(request.getAmount());
        transaction.setBalanceBefore(oldBalance);
        transaction.setBalanceAfter(newBalance);
        transaction.setDescription(request.getDescription() != null ? request.getDescription() : "Withdrawal");
        transaction.setReferenceId(request.getReferenceId() != null ? request.getReferenceId() : UUID.randomUUID().toString());
        
        transactionService.saveTransaction(transaction);

        log.info("Withdrawal successful for user: {}, amount: {}", userId, request.getAmount());
        return new MessageResponse("Withdrawal successful. New balance: " + newBalance);
    }

    @Transactional
    public MessageResponse transfer(TransferRequest request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long fromUserId = userService.getUserIdByUsername(userDetails.getUsername());

        // Hedef kullanıcının var olduğunu kontrol et
        if (!userService.userExists(request.getTargetUserId())) {
            throw new RuntimeException("Target user not found: " + request.getTargetUserId());
        }

        // Kendi kendine transfer kontrolü
        if (fromUserId.equals(request.getTargetUserId())) {
            throw new RuntimeException("Cannot transfer to yourself");
        }

        // Gönderen cüzdan
        Wallet fromWallet = walletRepository.findByUserIdAndIsActiveTrue(fromUserId)
                .orElseThrow(() -> new RuntimeException("Source wallet not found"));

        // Alıcı cüzdan (yoksa oluştur)
        Wallet toWallet = walletRepository.findByUserIdAndIsActiveTrue(request.getTargetUserId())
                .orElseGet(() -> createWalletForUser(request.getTargetUserId()));

        // Bakiye kontrolü
        if (fromWallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance. Current balance: " + fromWallet.getBalance());
        }

        String referenceId = UUID.randomUUID().toString();

        // Gönderen bakiyesini azalt
        BigDecimal fromOldBalance = fromWallet.getBalance();
        BigDecimal fromNewBalance = fromOldBalance.subtract(request.getAmount());
        fromWallet.setBalance(fromNewBalance);
        walletRepository.save(fromWallet);

        // Alıcı bakiyesini artır
        BigDecimal toOldBalance = toWallet.getBalance();
        BigDecimal toNewBalance = toOldBalance.add(request.getAmount());
        toWallet.setBalance(toNewBalance);
        walletRepository.save(toWallet);

        // Gönderen için transaction
        WalletTransaction outTransaction = new WalletTransaction();
        outTransaction.setUserId(fromUserId);
        outTransaction.setWalletId(fromWallet.getId());
        outTransaction.setType(TransactionType.TRANSFER_OUT);
        outTransaction.setAmount(request.getAmount());
        outTransaction.setBalanceBefore(fromOldBalance);
        outTransaction.setBalanceAfter(fromNewBalance);
        outTransaction.setDescription(request.getDescription() != null ? 
                request.getDescription() : "Transfer to user " + request.getTargetUserId());
        outTransaction.setReferenceId(referenceId);
        
        // Alıcı için transaction
        WalletTransaction inTransaction = new WalletTransaction();
        inTransaction.setUserId(request.getTargetUserId());
        inTransaction.setWalletId(toWallet.getId());
        inTransaction.setType(TransactionType.TRANSFER_IN);
        inTransaction.setAmount(request.getAmount());
        inTransaction.setBalanceBefore(toOldBalance);
        inTransaction.setBalanceAfter(toNewBalance);
        inTransaction.setDescription(request.getDescription() != null ? 
                request.getDescription() : "Transfer from user " + fromUserId);
        inTransaction.setReferenceId(referenceId);

        transactionService.saveTransaction(outTransaction);
        transactionService.saveTransaction(inTransaction);

        log.info("Transfer successful from user: {} to user: {}, amount: {}", fromUserId, request.getTargetUserId(), request.getAmount());
        return new MessageResponse("Transfer successful. New balance: " + fromNewBalance);
    }

    @Transactional
    public void updateBalance(Long userId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByUserIdAndIsActiveTrue(userId)
                .orElseGet(() -> createWalletForUser(userId));

        BigDecimal oldBalance = wallet.getBalance();
        BigDecimal newBalance = oldBalance.add(amount);
        
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Insufficient balance for operation");
        }

        wallet.setBalance(newBalance);
        walletRepository.save(wallet);

        log.info("Balance updated for user: {}, amount: {}, new balance: {}", userId, amount, newBalance);
    }

    private Wallet createWalletForUser(Long userId) {
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setCurrencyCode("USD");
        wallet.setIsActive(true);
        
        Wallet savedWallet = walletRepository.save(wallet);
        log.info("Auto-created wallet for user: {}", userId);
        
        return savedWallet;
    }

    private WalletResponse convertToWalletResponse(Wallet wallet) {
        WalletResponse response = new WalletResponse();
        response.setId(wallet.getId());
        response.setUserId(wallet.getUserId());
        response.setBalance(wallet.getBalance());
        response.setCurrencyCode(wallet.getCurrencyCode());
        response.setIsActive(wallet.getIsActive());
        response.setCreatedAt(wallet.getCreatedAt());
        response.setUpdatedAt(wallet.getUpdatedAt());
        return response;
    }
} 