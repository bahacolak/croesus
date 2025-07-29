package com.bahadircolak.wallet.service;

import com.bahadircolak.wallet.constants.ErrorMessages;
import com.bahadircolak.wallet.constants.SuccessMessages;
import com.bahadircolak.wallet.constants.WalletConstants;
import com.bahadircolak.wallet.dto.request.TransferRequest;
import com.bahadircolak.wallet.dto.request.WalletTransactionRequest;
import com.bahadircolak.wallet.dto.response.MessageResponse;
import com.bahadircolak.wallet.dto.response.WalletResponse;
import com.bahadircolak.wallet.exception.InsufficientBalanceException;
import com.bahadircolak.wallet.exception.UserNotFoundException;
import com.bahadircolak.wallet.exception.WalletNotFoundException;
import com.bahadircolak.wallet.model.Wallet;
import com.bahadircolak.wallet.model.WalletTransaction;
import com.bahadircolak.wallet.model.WalletTransaction.TransactionType;
import com.bahadircolak.wallet.repository.WalletRepository;
import com.bahadircolak.wallet.validation.WalletValidator;
import com.bahadircolak.common.client.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionService transactionService;
    private final UserClient userClient;
    private final WalletValidator validator;

    @Override
    @Transactional
    public WalletResponse createWallet(Long userId) {
        validator.validateUserId(userId);
        checkWalletNotExists(userId);
        checkUserExists(userId);

        Wallet wallet = buildNewWallet(userId);
        Wallet savedWallet = walletRepository.save(wallet);
        
        return convertToWalletResponse(savedWallet);
    }

    @Override
    public WalletResponse getWalletByUserId(Long userId) {
        validator.validateUserId(userId);
        Wallet wallet = findActiveWalletByUserId(userId);
        return convertToWalletResponse(wallet);
    }

    @Override
    public BigDecimal getBalance(Long userId) {
        validator.validateUserId(userId);
        return walletRepository.findBalanceByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException(
                    String.format(ErrorMessages.WALLET_NOT_FOUND_FOR_USER, userId)
                ));
    }

    @Override
    public WalletResponse getCurrentUserWallet() {
        Long userId = getCurrentUserId();
        Wallet wallet = walletRepository.findByUserIdAndIsActiveTrue(userId)
                .orElseGet(() -> createWalletForUser(userId));
        return convertToWalletResponse(wallet);
    }

    @Override
    @Transactional
    public MessageResponse deposit(WalletTransactionRequest request) {
        validator.validateTransactionRequest(request);
        
        Long userId = getCurrentUserId();
        Wallet wallet = getOrCreateWallet(userId);
        
        BigDecimal oldBalance = wallet.getBalance();
        BigDecimal newBalance = oldBalance.add(request.getAmount());
        
        updateWalletBalance(wallet, newBalance);
        createDepositTransaction(userId, wallet.getId(), request, oldBalance, newBalance);
        
        return new MessageResponse(
            String.format(SuccessMessages.DEPOSIT_SUCCESSFUL, newBalance), 
            true
        );
    }

    @Override
    @Transactional
    public MessageResponse withdraw(WalletTransactionRequest request) {
        validator.validateTransactionRequest(request);
        
        Long userId = getCurrentUserId();
        Wallet wallet = findActiveWalletByUserId(userId);
        
        BigDecimal oldBalance = wallet.getBalance();
        validateSufficientBalance(oldBalance, request.getAmount());
        
        BigDecimal newBalance = oldBalance.subtract(request.getAmount());
        updateWalletBalance(wallet, newBalance);
        createWithdrawalTransaction(userId, wallet.getId(), request, oldBalance, newBalance);
        
        return new MessageResponse(
            String.format(SuccessMessages.WITHDRAWAL_SUCCESSFUL, newBalance), 
            true
        );
    }

    @Override
    @Transactional
    public MessageResponse transfer(TransferRequest request) {
        validator.validateTransferRequest(request);
        
        Long fromUserId = getCurrentUserId();
        validator.validateSelfTransfer(fromUserId, request.getTargetUserId());
        checkUserExists(request.getTargetUserId());
        
        Wallet fromWallet = findActiveWalletByUserId(fromUserId);
        Wallet toWallet = getOrCreateWallet(request.getTargetUserId());
        
        validateSufficientBalance(fromWallet.getBalance(), request.getAmount());
        
        String referenceId = UUID.randomUUID().toString();
        processTransfer(fromWallet, toWallet, request, referenceId);
        
        return new MessageResponse(
            String.format(SuccessMessages.TRANSFER_SUCCESSFUL, fromWallet.getBalance()), 
            true
        );
    }

    @Override
    @Transactional
    public void updateBalance(Long userId, BigDecimal amount) {
        validator.validateUserId(userId);
        
        Wallet wallet = getOrCreateWallet(userId);
        BigDecimal newBalance = wallet.getBalance().add(amount);
        
        if (newBalance.compareTo(WalletConstants.MIN_BALANCE) < 0) {
            throw new InsufficientBalanceException(ErrorMessages.BALANCE_CANNOT_BE_NEGATIVE);
        }
        
        updateWalletBalance(wallet, newBalance);
    }

    private void checkWalletNotExists(Long userId) {
        if (walletRepository.existsByUserId(userId)) {
            throw new WalletNotFoundException(
                String.format(ErrorMessages.WALLET_ALREADY_EXISTS, userId)
            );
        }
    }

    private void checkUserExists(Long userId) {
        if (!userClient.userExists(userId)) {
            throw new UserNotFoundException(
                String.format(ErrorMessages.USER_NOT_FOUND, userId)
            );
        }
    }

    private Wallet buildNewWallet(Long userId) {
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setBalance(WalletConstants.ZERO_BALANCE);
        wallet.setCurrencyCode(WalletConstants.DEFAULT_CURRENCY_CODE);
        wallet.setIsActive(true);
        return wallet;
    }

    private Wallet findActiveWalletByUserId(Long userId) {
        return walletRepository.findByUserIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new WalletNotFoundException(
                    String.format(ErrorMessages.WALLET_NOT_FOUND_FOR_USER, userId)
                ));
    }

    private Long getCurrentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userClient.getUserIdByUsername(username);
    }

    private Wallet getOrCreateWallet(Long userId) {
        return walletRepository.findByUserIdAndIsActiveTrue(userId)
                .orElseGet(() -> createWalletForUser(userId));
    }

    private Wallet createWalletForUser(Long userId) {
        Wallet wallet = buildNewWallet(userId);
        return walletRepository.save(wallet);
    }

    private void updateWalletBalance(Wallet wallet, BigDecimal newBalance) {
        wallet.setBalance(newBalance);
        walletRepository.save(wallet);
    }

    private void validateSufficientBalance(BigDecimal currentBalance, BigDecimal amount) {
        if (currentBalance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                String.format(ErrorMessages.INSUFFICIENT_BALANCE, currentBalance, amount)
            );
        }
    }

    private void createDepositTransaction(Long userId, Long walletId, WalletTransactionRequest request, 
                                        BigDecimal oldBalance, BigDecimal newBalance) {
        WalletTransaction transaction = buildTransaction(
            userId, walletId, TransactionType.DEPOSIT, request.getAmount(),
            oldBalance, newBalance, 
            getDescriptionOrDefault(request.getDescription(), WalletConstants.DEFAULT_DEPOSIT_DESCRIPTION),
            getReferenceIdOrGenerate(request.getReferenceId())
        );
        transactionService.saveTransaction(transaction);
    }

    private void createWithdrawalTransaction(Long userId, Long walletId, WalletTransactionRequest request, 
                                           BigDecimal oldBalance, BigDecimal newBalance) {
        WalletTransaction transaction = buildTransaction(
            userId, walletId, TransactionType.WITHDRAWAL, request.getAmount(),
            oldBalance, newBalance,
            getDescriptionOrDefault(request.getDescription(), WalletConstants.DEFAULT_WITHDRAWAL_DESCRIPTION),
            getReferenceIdOrGenerate(request.getReferenceId())
        );
        transactionService.saveTransaction(transaction);
    }

    private void processTransfer(Wallet fromWallet, Wallet toWallet, TransferRequest request, String referenceId) {
        BigDecimal fromOldBalance = fromWallet.getBalance();
        BigDecimal fromNewBalance = fromOldBalance.subtract(request.getAmount());
        
        BigDecimal toOldBalance = toWallet.getBalance();
        BigDecimal toNewBalance = toOldBalance.add(request.getAmount());
        
        updateWalletBalance(fromWallet, fromNewBalance);
        updateWalletBalance(toWallet, toNewBalance);
        
        createTransferTransactions(fromWallet, toWallet, request, 
                                 fromOldBalance, fromNewBalance, toOldBalance, toNewBalance, referenceId);
    }

    private void createTransferTransactions(Wallet fromWallet, Wallet toWallet, TransferRequest request,
                                          BigDecimal fromOldBalance, BigDecimal fromNewBalance,
                                          BigDecimal toOldBalance, BigDecimal toNewBalance, String referenceId) {
        
        WalletTransaction outTransaction = buildTransaction(
            fromWallet.getUserId(), fromWallet.getId(), TransactionType.TRANSFER_OUT, request.getAmount(),
            fromOldBalance, fromNewBalance,
            getDescriptionOrDefault(request.getDescription(), 
                String.format(WalletConstants.DEFAULT_TRANSFER_OUT_DESCRIPTION, toWallet.getUserId())),
            referenceId
        );
        
        WalletTransaction inTransaction = buildTransaction(
            toWallet.getUserId(), toWallet.getId(), TransactionType.TRANSFER_IN, request.getAmount(),
            toOldBalance, toNewBalance,
            getDescriptionOrDefault(request.getDescription(), 
                String.format(WalletConstants.DEFAULT_TRANSFER_IN_DESCRIPTION, fromWallet.getUserId())),
            referenceId
        );
        
        transactionService.saveTransaction(outTransaction);
        transactionService.saveTransaction(inTransaction);
    }

    private WalletTransaction buildTransaction(Long userId, Long walletId, TransactionType type, BigDecimal amount,
                                             BigDecimal balanceBefore, BigDecimal balanceAfter, 
                                             String description, String referenceId) {
        WalletTransaction transaction = new WalletTransaction();
        transaction.setUserId(userId);
        transaction.setWalletId(walletId);
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setBalanceBefore(balanceBefore);
        transaction.setBalanceAfter(balanceAfter);
        transaction.setDescription(description);
        transaction.setReferenceId(referenceId);
        return transaction;
    }

    private String getDescriptionOrDefault(String description, String defaultDescription) {
        return description != null ? description : defaultDescription;
    }

    private String getReferenceIdOrGenerate(String referenceId) {
        return referenceId != null ? referenceId : UUID.randomUUID().toString();
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