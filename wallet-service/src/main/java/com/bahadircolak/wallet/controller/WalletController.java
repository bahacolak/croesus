package com.bahadircolak.wallet.controller;

import com.bahadircolak.wallet.constants.SuccessMessages;
import com.bahadircolak.wallet.constants.WalletConstants;
import com.bahadircolak.wallet.dto.request.TransferRequest;
import com.bahadircolak.wallet.dto.request.WalletTransactionRequest;
import com.bahadircolak.wallet.dto.response.MessageResponse;
import com.bahadircolak.wallet.dto.response.WalletResponse;
import com.bahadircolak.wallet.service.IWalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@CrossOrigin(origins = WalletConstants.CROSS_ORIGIN_PATTERN, maxAge = WalletConstants.CROSS_ORIGIN_MAX_AGE)
@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final IWalletService walletService;

    @GetMapping
    public ResponseEntity<WalletResponse> getCurrentUserWallet() {
        WalletResponse wallet = walletService.getCurrentUserWallet();
        return ResponseEntity.ok(wallet);
    }

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getCurrentUserBalance() {
        WalletResponse wallet = walletService.getCurrentUserWallet();
        return ResponseEntity.ok(wallet.getBalance());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<WalletResponse> getWalletByUserId(@PathVariable Long userId) {
        WalletResponse wallet = walletService.getWalletByUserId(userId);
        return ResponseEntity.ok(wallet);
    }

    @GetMapping("/{userId}/balance")
    public ResponseEntity<BigDecimal> getUserBalance(@PathVariable Long userId) {
        BigDecimal balance = walletService.getBalance(userId);
        return ResponseEntity.ok(balance);
    }

    @PostMapping("/deposit")
    public ResponseEntity<MessageResponse> deposit(@Valid @RequestBody WalletTransactionRequest request) {
        MessageResponse response = walletService.deposit(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<MessageResponse> withdraw(@Valid @RequestBody WalletTransactionRequest request) {
        MessageResponse response = walletService.withdraw(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transfer")
    public ResponseEntity<MessageResponse> transfer(@Valid @RequestBody TransferRequest request) {
        MessageResponse response = walletService.transfer(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{userId}/create")
    public ResponseEntity<WalletResponse> createWallet(@PathVariable Long userId) {
        WalletResponse wallet = walletService.createWallet(userId);
        return ResponseEntity.ok(wallet);
    }

    @PostMapping("/{userId}/update-balance")
    public ResponseEntity<MessageResponse> updateBalance(@PathVariable Long userId, @RequestParam BigDecimal amount) {
        walletService.updateBalance(userId, amount);
        MessageResponse response = new MessageResponse(SuccessMessages.BALANCE_UPDATED_SUCCESSFULLY, true);
        return ResponseEntity.ok(response);
    }
} 