package com.bahadircolak.croesus.controller;

import com.bahadircolak.croesus.dto.request.WalletTransactionRequest;
import com.bahadircolak.croesus.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/balance")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getBalance() {
        return walletService.getBalance();
    }

    @PostMapping("/deposit")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deposit(@Valid @RequestBody WalletTransactionRequest request) {
        return walletService.deposit(request);
    }

    @PostMapping("/withdraw")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> withdraw(@Valid @RequestBody WalletTransactionRequest request) {
        return walletService.withdraw(request);
    }
} 