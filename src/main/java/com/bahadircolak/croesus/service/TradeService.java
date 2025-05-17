package com.bahadircolak.croesus.service;

import com.bahadircolak.croesus.dto.request.TradeRequest;
import com.bahadircolak.croesus.dto.response.MessageResponse;
import com.bahadircolak.croesus.model.Asset;
import com.bahadircolak.croesus.model.Portfolio;
import com.bahadircolak.croesus.model.Transaction;
import com.bahadircolak.croesus.model.Transaction.TransactionType;
import com.bahadircolak.croesus.model.User;
import com.bahadircolak.croesus.repository.PortfolioRepository;
import com.bahadircolak.croesus.repository.TransactionRepository;
import com.bahadircolak.croesus.repository.UserRepository;
import com.bahadircolak.croesus.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradeService {

    private final AssetService assetService;
    private final UserRepository userRepository;
    private final PortfolioRepository portfolioRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public ResponseEntity<?> buyAsset(TradeRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Asset asset = assetService.getAssetBySymbol(request.getSymbol())
                .orElseThrow(() -> new RuntimeException("Asset not found: " + request.getSymbol()));

        BigDecimal totalCost = asset.getCurrentPrice().multiply(request.getQuantity());

        if (user.getWalletBalance().compareTo(totalCost) < 0) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Insufficient balance. Required: " + totalCost + ", Current: " + user.getWalletBalance()));
        }

        user.setWalletBalance(user.getWalletBalance().subtract(totalCost));
        userRepository.save(user);

        Portfolio portfolio = portfolioRepository.findByUserAndAsset(user, asset)
                .orElse(new Portfolio());

        if (portfolio.getId() == null) {
            portfolio.setUser(user);
            portfolio.setAsset(asset);
            portfolio.setQuantity(request.getQuantity());
            portfolio.setAverageBuyPrice(asset.getCurrentPrice());
            portfolio.setTotalInvestment(totalCost);
        } else {
            BigDecimal newTotalQuantity = portfolio.getQuantity().add(request.getQuantity());
            BigDecimal newTotalInvestment = portfolio.getTotalInvestment().add(totalCost);

            BigDecimal newAverageBuyPrice = newTotalInvestment.divide(newTotalQuantity, 8, RoundingMode.HALF_UP);
            
            portfolio.setQuantity(newTotalQuantity);
            portfolio.setAverageBuyPrice(newAverageBuyPrice);
            portfolio.setTotalInvestment(newTotalInvestment);
        }

        BigDecimal currentValue = asset.getCurrentPrice().multiply(portfolio.getQuantity());
        portfolio.setCurrentValue(currentValue);
        
        BigDecimal profitLoss = currentValue.subtract(portfolio.getTotalInvestment());
        portfolio.setProfitLoss(profitLoss);
        
        if (portfolio.getTotalInvestment().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal profitLossPercentage = profitLoss.multiply(new BigDecimal("100"))
                    .divide(portfolio.getTotalInvestment(), 2, RoundingMode.HALF_UP);
            portfolio.setProfitLossPercentage(profitLossPercentage);
        }
        
        portfolioRepository.save(portfolio);

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAsset(asset);
        transaction.setType(TransactionType.BUY);
        transaction.setAmount(totalCost);
        transaction.setQuantity(request.getQuantity());
        transaction.setUnitPrice(asset.getCurrentPrice());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setDescription(request.getDescription() != null ?
                request.getDescription() : asset.getSymbol() + " purchase");
        transactionRepository.save(transaction);

        return ResponseEntity.ok(new MessageResponse(
                request.getQuantity() + " " + asset.getSymbol() + " successfully purchased. Total cost: " + totalCost));
    }

    @Transactional
    public ResponseEntity<?> sellAsset(TradeRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Asset asset = assetService.getAssetBySymbol(request.getSymbol())
                .orElseThrow(() -> new RuntimeException("Asset not found: " + request.getSymbol()));

        Portfolio portfolio = portfolioRepository.findByUserAndAsset(user, asset)
                .orElseThrow(() -> new RuntimeException("Asset " + asset.getSymbol() + " not found in portfolio"));

        if (portfolio.getQuantity().compareTo(request.getQuantity()) < 0) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Insufficient asset. Current: " + portfolio.getQuantity() + ", Requested to sell: " + request.getQuantity()));
        }

        BigDecimal saleAmount = asset.getCurrentPrice().multiply(request.getQuantity());

        user.setWalletBalance(user.getWalletBalance().add(saleAmount));
        userRepository.save(user);

        BigDecimal newQuantity = portfolio.getQuantity().subtract(request.getQuantity());
        
        if (newQuantity.compareTo(BigDecimal.ZERO) <= 0) {
            portfolioRepository.delete(portfolio);
        } else {
            BigDecimal saleRatio = request.getQuantity().divide(portfolio.getQuantity(), 8, RoundingMode.HALF_UP);

            BigDecimal investmentAmountSold = portfolio.getTotalInvestment().multiply(saleRatio);

            BigDecimal newTotalInvestment = portfolio.getTotalInvestment().subtract(investmentAmountSold);
            
            portfolio.setQuantity(newQuantity);
            portfolio.setTotalInvestment(newTotalInvestment);

            BigDecimal currentValue = asset.getCurrentPrice().multiply(newQuantity);
            portfolio.setCurrentValue(currentValue);
            
            BigDecimal profitLoss = currentValue.subtract(newTotalInvestment);
            portfolio.setProfitLoss(profitLoss);
            
            if (newTotalInvestment.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal profitLossPercentage = profitLoss.multiply(new BigDecimal("100"))
                        .divide(newTotalInvestment, 2, RoundingMode.HALF_UP);
                portfolio.setProfitLossPercentage(profitLossPercentage);
            }
            
            portfolioRepository.save(portfolio);
        }

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAsset(asset);
        transaction.setType(TransactionType.SELL);
        transaction.setAmount(saleAmount);
        transaction.setQuantity(request.getQuantity());
        transaction.setUnitPrice(asset.getCurrentPrice());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setDescription(request.getDescription() != null ?
                request.getDescription() : asset.getSymbol() + " sale");
        transactionRepository.save(transaction);

        return ResponseEntity.ok(new MessageResponse(
                request.getQuantity() + " " + asset.getSymbol() + " successfully sold. Total amount: " + saleAmount));
    }
} 