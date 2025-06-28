package com.bahadircolak.trading.service;

import com.bahadircolak.trading.constants.ErrorMessages;
import com.bahadircolak.trading.constants.TradingConstants;
import com.bahadircolak.trading.dto.AssetInfo;
import com.bahadircolak.trading.dto.request.TradeRequest;
import com.bahadircolak.trading.dto.response.MessageResponse;
import com.bahadircolak.trading.dto.response.TradeResponse;
import com.bahadircolak.trading.exception.AssetNotFoundException;
import com.bahadircolak.trading.exception.InsufficientAssetException;
import com.bahadircolak.trading.exception.InsufficientBalanceException;
import com.bahadircolak.trading.exception.TradingException;
import com.bahadircolak.trading.model.Transaction;
import com.bahadircolak.trading.model.Transaction.TransactionType;
import com.bahadircolak.trading.repository.TransactionRepository;
import com.bahadircolak.common.client.UserClient;
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

    private final UserClient userClient;
    private final MarketService marketService;
    private final PortfolioService portfolioService;
    private final TransactionRepository transactionRepository;

    @Transactional
    public ResponseEntity<TradeResponse> buyAsset(TradeRequest request) {
        Long userId = getCurrentUserId();
        AssetInfo asset = validateAndGetAsset(request.getSymbol());
        BigDecimal totalCost = calculateTotalCost(asset.getCurrentPrice(), request.getQuantity());
        
        validateSufficientBalance(userId, totalCost);
        
        return executeBuyTransaction(userId, asset, request, totalCost);
    }

    @Transactional
    public ResponseEntity<TradeResponse> sellAsset(TradeRequest request) {
        Long userId = getCurrentUserId();
        AssetInfo asset = validateAndGetAsset(request.getSymbol());
        validateSufficientAssetInPortfolio(userId, asset, request.getQuantity());
        BigDecimal saleAmount = calculateTotalCost(asset.getCurrentPrice(), request.getQuantity());
        return executeSellTransaction(userId, asset, request, saleAmount);
    }

    private Long getCurrentUserId() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userClient.getUserIdByUsername(userDetails.getUsername());
    }
    
    private AssetInfo validateAndGetAsset(String symbol) {
        Map<String, Object> assetData = marketService.getAssetBySymbol(symbol);
        
        if (assetData == null) {
            throw new AssetNotFoundException(symbol);
        }
        
        if (assetData.get("id") == null || assetData.get("currentPrice") == null || assetData.get("symbol") == null) {
            throw new TradingException(String.format(ErrorMessages.ASSET_DATA_INCOMPLETE, symbol), "INCOMPLETE_ASSET_DATA");
        }
        
        return new AssetInfo(
                Long.valueOf(assetData.get("id").toString()),
                assetData.get("symbol").toString(),
                assetData.get("name") != null ? assetData.get("name").toString() : null,
                new BigDecimal(assetData.get("currentPrice").toString())
        );
    }
    
    private BigDecimal calculateTotalCost(BigDecimal unitPrice, BigDecimal quantity) {
        return unitPrice.multiply(quantity);
    }
    
    private void validateSufficientBalance(Long userId, BigDecimal totalCost) {
        BigDecimal walletBalance = userClient.getUserWalletBalance(userId);
        
        if (walletBalance.compareTo(totalCost) < 0) {
            throw new InsufficientBalanceException(totalCost, walletBalance);
        }
    }
    
    private void validateSufficientAssetInPortfolio(Long userId, AssetInfo asset, BigDecimal requestedQuantity) {
        Map<String, Object> portfolio = portfolioService.getPortfolioByUserAndAsset(userId, asset.getId());
        
        if (portfolio == null) {
            log.debug("Portfolio not found with market ID: {}, trying to find by symbol: {}", asset.getId(), asset.getSymbol());
            portfolio = portfolioService.getPortfolioByUserAndSymbol(userId, asset.getSymbol());
        }
        
        if (portfolio == null || portfolio.get("quantity") == null) {
            throw new TradingException(String.format(ErrorMessages.ASSET_NOT_IN_PORTFOLIO, asset.getSymbol()), "ASSET_NOT_IN_PORTFOLIO");
        }
        
        BigDecimal currentQuantity = new BigDecimal(portfolio.get("quantity").toString());
        if (currentQuantity.compareTo(requestedQuantity) < 0) {
            throw new InsufficientAssetException(asset.getSymbol(), currentQuantity, requestedQuantity);
        }
    }
    
    private ResponseEntity<TradeResponse> executeBuyTransaction(Long userId, AssetInfo asset, TradeRequest request, BigDecimal totalCost) {
        userClient.updateUserWalletBalance(userId, totalCost.negate());
        
        Long portfolioAssetId = getOrCreatePortfolioAssetId(userId, asset);
        portfolioService.updatePortfolio(userId, portfolioAssetId, request.getQuantity(), asset.getCurrentPrice(), 
                TradingConstants.BUY_OPERATION, asset.getSymbol(), asset.getDisplayName());
        
        Transaction transaction = createTransaction(userId, asset, request, TransactionType.BUY, totalCost, TradingConstants.BUY_OPERATION);
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        return ResponseEntity.ok(createTradeResponse(
                request.getQuantity() + " " + asset.getSymbol() + " successfully purchased.",
                asset.getSymbol(),
                request.getQuantity(),
                asset.getCurrentPrice(),
                totalCost,
                TradingConstants.BUY_OPERATION,
                savedTransaction.getTransactionDate()
        ));
    }
    
    private ResponseEntity<TradeResponse> executeSellTransaction(Long userId, AssetInfo asset, TradeRequest request, BigDecimal saleAmount) {
        userClient.updateUserWalletBalance(userId, saleAmount);
        
        Long portfolioAssetId = getOrCreatePortfolioAssetId(userId, asset);
        portfolioService.updatePortfolio(userId, portfolioAssetId, request.getQuantity(), asset.getCurrentPrice(), 
                TradingConstants.SELL_OPERATION, asset.getSymbol(), asset.getDisplayName());
        
        Transaction transaction = createTransaction(userId, asset, request, TransactionType.SELL, saleAmount, TradingConstants.SELL_OPERATION);
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        return ResponseEntity.ok(createTradeResponse(
                request.getQuantity() + " " + asset.getSymbol() + " successfully sold.",
                asset.getSymbol(),
                request.getQuantity(),
                asset.getCurrentPrice(),
                saleAmount,
                TradingConstants.SELL_OPERATION,
                savedTransaction.getTransactionDate()
        ));
    }
    
    private Long getOrCreatePortfolioAssetId(Long userId, AssetInfo asset) {
        Map<String, Object> portfolio = portfolioService.getPortfolioByUserAndSymbol(userId, asset.getSymbol());
        if (portfolio != null && portfolio.get("asset") != null) {
            Map<String, Object> portfolioAsset = (Map<String, Object>) portfolio.get("asset");
            return Long.valueOf(portfolioAsset.get("id").toString());
        }
        
        return asset.getId();
    }
    
    private Transaction createTransaction(Long userId, AssetInfo asset, TradeRequest request, TransactionType type, BigDecimal amount, String operation) {
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setAssetId(asset.getId());
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setQuantity(request.getQuantity());
        transaction.setUnitPrice(asset.getCurrentPrice());
        transaction.setTransactionDate(LocalDateTime.now());
        
        String description = request.getDescription();
        if (description == null) {
            description = String.format(
                    TradingConstants.BUY_OPERATION.equals(operation) ? 
                            TradingConstants.DEFAULT_BUY_DESCRIPTION : TradingConstants.DEFAULT_SELL_DESCRIPTION,
                    asset.getSymbol()
            );
        }
        transaction.setDescription(description);
        
        return transaction;
    }
    
    private TradeResponse createTradeResponse(String message, String symbol, BigDecimal quantity, 
                                              BigDecimal unitPrice, BigDecimal totalAmount, String operation, LocalDateTime transactionDate) {
        return new TradeResponse(message, symbol, quantity, unitPrice, totalAmount, operation, transactionDate);
    }
} 