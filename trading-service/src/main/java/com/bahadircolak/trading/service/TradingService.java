package com.bahadircolak.trading.service;

import com.bahadircolak.trading.dto.request.TradeRequest;
import com.bahadircolak.trading.dto.response.MessageResponse;
import com.bahadircolak.trading.dto.response.TradeResponse;
import com.bahadircolak.trading.model.Transaction;
import com.bahadircolak.trading.model.Transaction.TransactionType;
import com.bahadircolak.trading.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradingService {

    private final UserService userService;
    private final MarketService marketService;
    private final PortfolioService portfolioService;
    private final TransactionRepository transactionRepository;

    @Transactional
    public ResponseEntity<?> buyAsset(TradeRequest request) {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long userId = userService.getUserIdByUsername(userDetails.getUsername());

            // Market Service'den asset bilgilerini al
            Map<String, Object> asset = marketService.getAssetBySymbol(request.getSymbol());
            Long assetId = Long.valueOf(asset.get("id").toString());
            BigDecimal currentPrice = new BigDecimal(asset.get("currentPrice").toString());

            BigDecimal totalCost = currentPrice.multiply(request.getQuantity());

            // User Service'den wallet bakiyesini kontrol et
            BigDecimal walletBalance = userService.getUserWalletBalance(userId);
            if (walletBalance.compareTo(totalCost) < 0) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Insufficient balance. Required: " + totalCost + ", Current: " + walletBalance));
            }

            // Wallet bakiyesini güncelle (negatif amount ile)
            userService.updateUserWalletBalance(userId, totalCost.negate());

            // Portfolio Service'den mevcut pozisyonu kontrol et
            Map<String, Object> existingPortfolio = portfolioService.getPortfolioByUserAndAsset(userId, assetId);

            // Transaction kaydı oluştur
            Transaction transaction = new Transaction();
            transaction.setUserId(userId);
            transaction.setAssetId(assetId);
            transaction.setType(TransactionType.BUY);
            transaction.setAmount(totalCost);
            transaction.setQuantity(request.getQuantity());
            transaction.setUnitPrice(currentPrice);
            transaction.setTransactionDate(LocalDateTime.now());
            transaction.setDescription(request.getDescription() != null ?
                    request.getDescription() : asset.get("symbol") + " purchase");
            
            Transaction savedTransaction = transactionRepository.save(transaction);

            return ResponseEntity.ok(new TradeResponse(
                    request.getQuantity() + " " + asset.get("symbol") + " successfully purchased.",
                    asset.get("symbol").toString(),
                    request.getQuantity(),
                    currentPrice,
                    totalCost,
                    "BUY",
                    savedTransaction.getTransactionDate()
            ));

        } catch (Exception e) {
            log.error("Error during buy operation: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error during buy operation: " + e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> sellAsset(TradeRequest request) {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long userId = userService.getUserIdByUsername(userDetails.getUsername());

            // Market Service'den asset bilgilerini al
            Map<String, Object> asset = marketService.getAssetBySymbol(request.getSymbol());
            Long assetId = Long.valueOf(asset.get("id").toString());
            BigDecimal currentPrice = new BigDecimal(asset.get("currentPrice").toString());

            // Portfolio Service'den mevcut pozisyonu kontrol et
            Map<String, Object> portfolio = portfolioService.getPortfolioByUserAndAsset(userId, assetId);
            if (portfolio == null) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Asset " + asset.get("symbol") + " not found in portfolio"));
            }

            BigDecimal currentQuantity = new BigDecimal(portfolio.get("quantity").toString());
            if (currentQuantity.compareTo(request.getQuantity()) < 0) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Insufficient asset. Current: " + currentQuantity + 
                                ", Requested to sell: " + request.getQuantity()));
            }

            BigDecimal saleAmount = currentPrice.multiply(request.getQuantity());

            // Wallet bakiyesini güncelle (pozitif amount ile)
            userService.updateUserWalletBalance(userId, saleAmount);

            // Transaction kaydı oluştur
            Transaction transaction = new Transaction();
            transaction.setUserId(userId);
            transaction.setAssetId(assetId);
            transaction.setType(TransactionType.SELL);
            transaction.setAmount(saleAmount);
            transaction.setQuantity(request.getQuantity());
            transaction.setUnitPrice(currentPrice);
            transaction.setTransactionDate(LocalDateTime.now());
            transaction.setDescription(request.getDescription() != null ?
                    request.getDescription() : asset.get("symbol") + " sale");
            
            Transaction savedTransaction = transactionRepository.save(transaction);

            return ResponseEntity.ok(new TradeResponse(
                    request.getQuantity() + " " + asset.get("symbol") + " successfully sold.",
                    asset.get("symbol").toString(),
                    request.getQuantity(),
                    currentPrice,
                    saleAmount,
                    "SELL",
                    savedTransaction.getTransactionDate()
            ));

        } catch (Exception e) {
            log.error("Error during sell operation: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error during sell operation: " + e.getMessage()));
        }
    }
} 