package com.bahadircolak.wallet.controller;

import com.bahadircolak.wallet.dto.request.TransferRequest;
import com.bahadircolak.wallet.dto.request.WalletTransactionRequest;
import com.bahadircolak.wallet.dto.response.MessageResponse;
import com.bahadircolak.wallet.dto.response.WalletResponse;
import com.bahadircolak.wallet.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
@Slf4j
public class WalletController {

    private final WalletService walletService;

    @GetMapping
    public ResponseEntity<WalletResponse> getCurrentUserWallet() {
        try {
            WalletResponse wallet = walletService.getCurrentUserWallet();
            return ResponseEntity.ok(wallet);
        } catch (Exception e) {
            log.error("Error fetching current user wallet", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getCurrentUserBalance() {
        try {
            WalletResponse wallet = walletService.getCurrentUserWallet();
            return ResponseEntity.ok(wallet.getBalance());
        } catch (Exception e) {
            log.error("Error fetching current user balance", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<WalletResponse> getWalletByUserId(@PathVariable Long userId) {
        try {
            WalletResponse wallet = walletService.getWalletByUserId(userId);
            return ResponseEntity.ok(wallet);
        } catch (RuntimeException e) {
            log.error("Error fetching wallet for user: {}", userId, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Unexpected error fetching wallet for user: {}", userId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{userId}/balance")
    public ResponseEntity<BigDecimal> getUserBalance(@PathVariable Long userId) {
        try {
            BigDecimal balance = walletService.getBalance(userId);
            return ResponseEntity.ok(balance);
        } catch (RuntimeException e) {
            log.error("Error fetching balance for user: {}", userId, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Unexpected error fetching balance for user: {}", userId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<MessageResponse> deposit(@Valid @RequestBody WalletTransactionRequest request) {
        try {
            MessageResponse response = walletService.deposit(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing deposit", e);
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Deposit failed: " + e.getMessage(), false));
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<MessageResponse> withdraw(@Valid @RequestBody WalletTransactionRequest request) {
        try {
            MessageResponse response = walletService.withdraw(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error processing withdrawal", e);
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Withdrawal failed: " + e.getMessage(), false));
        } catch (Exception e) {
            log.error("Unexpected error processing withdrawal", e);
            return ResponseEntity.internalServerError()
                    .body(new MessageResponse("Withdrawal failed: " + e.getMessage(), false));
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<MessageResponse> transfer(@Valid @RequestBody TransferRequest request) {
        try {
            MessageResponse response = walletService.transfer(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error processing transfer", e);
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Transfer failed: " + e.getMessage(), false));
        } catch (Exception e) {
            log.error("Unexpected error processing transfer", e);
            return ResponseEntity.internalServerError()
                    .body(new MessageResponse("Transfer failed: " + e.getMessage(), false));
        }
    }

    @PostMapping("/{userId}/create")
    public ResponseEntity<WalletResponse> createWallet(@PathVariable Long userId) {
        try {
            WalletResponse wallet = walletService.createWallet(userId);
            return ResponseEntity.ok(wallet);
        } catch (RuntimeException e) {
            log.error("Error creating wallet for user: {}", userId, e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unexpected error creating wallet for user: {}", userId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{userId}/update-balance")
    public ResponseEntity<MessageResponse> updateBalance(@PathVariable Long userId, @RequestParam BigDecimal amount) {
        try {
            walletService.updateBalance(userId, amount);
            return ResponseEntity.ok(new MessageResponse("Balance updated successfully"));
        } catch (RuntimeException e) {
            log.error("Error updating balance for user: {}", userId, e);
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Balance update failed: " + e.getMessage(), false));
        } catch (Exception e) {
            log.error("Unexpected error updating balance for user: {}", userId, e);
            return ResponseEntity.internalServerError()
                    .body(new MessageResponse("Balance update failed: " + e.getMessage(), false));
        }
    }
} 