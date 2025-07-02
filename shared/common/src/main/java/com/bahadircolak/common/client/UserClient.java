package com.bahadircolak.common.client;

import com.bahadircolak.common.constants.CommonConstants;
import com.bahadircolak.common.constants.ErrorMessages;
import com.bahadircolak.common.exception.ServiceCommunicationException;
import com.bahadircolak.common.exception.UserNotFoundException;
import com.bahadircolak.common.exception.WalletServiceException;
import com.bahadircolak.common.validation.CommonValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserClient {

    private final RestTemplate restTemplate;
    private final CommonValidator validator;

    @Value("${services.user-service.url:" + CommonConstants.USER_SERVICE_DEFAULT_URL + "}")
    private String userServiceUrl;

    @Value("${services.wallet-service.url:" + CommonConstants.WALLET_SERVICE_DEFAULT_URL + "}")
    private String walletServiceUrl;

    public Long getUserIdByUsername(String username) {
        validator.validateUsername(username);
        
        String url = buildUserIdUrl(username);
        RequestEntity<Void> request = createGetRequest(url);
        
        try {
            ResponseEntity<Long> response = restTemplate.exchange(request, Long.class);
            return response.getBody();
        } catch (RestClientException e) {
            throw new UserNotFoundException(
                String.format(ErrorMessages.USER_ID_NOT_FOUND, username), e
            );
        }
    }

    public Map<String, Object> getUserById(Long userId) {
        validator.validateUserId(userId);
        
        String url = buildUserByIdUrl(userId);
        RequestEntity<Void> request = createGetRequest(url);
        
        try {
            ResponseEntity<Map> response = restTemplate.exchange(request, Map.class);
            return response.getBody();
        } catch (RestClientException e) {
            throw new UserNotFoundException(
                String.format(ErrorMessages.USER_NOT_FOUND, userId.toString()), e
            );
        }
    }

    public boolean userExists(Long userId) {
        try {
            getUserById(userId);
            return true;
        } catch (UserNotFoundException e) {
            return false;
        }
    }

    public BigDecimal getUserWalletBalance(Long userId) {
        validator.validateUserId(userId);
        
        String url = buildWalletBalanceUrl(userId);
        RequestEntity<Void> request = createGetRequest(url);
        
        try {
            ResponseEntity<BigDecimal> response = restTemplate.exchange(request, BigDecimal.class);
            return response.getBody();
        } catch (RestClientException e) {
            throw new WalletServiceException(
                String.format(ErrorMessages.BALANCE_FETCH_ERROR, userId), e
            );
        }
    }

    public void updateUserWalletBalance(Long userId, BigDecimal amount) {
        validator.validateUserId(userId);
        validator.validateAmount(amount);
        
        String url = buildUpdateBalanceUrl(userId, amount);
        RequestEntity<Void> request = createPostRequest(url);
        
        try {
            restTemplate.exchange(request, Void.class);
        } catch (RestClientException e) {
            throw new WalletServiceException(
                String.format(ErrorMessages.BALANCE_UPDATE_ERROR, userId), e
            );
        }
    }

    private String buildUserIdUrl(String username) {
        return userServiceUrl + CommonConstants.API_USERS_PATH + 
               String.format(CommonConstants.USERNAME_ID_PATH, username);
    }

    private String buildUserByIdUrl(Long userId) {
        return userServiceUrl + CommonConstants.API_USERS_PATH + 
               String.format(CommonConstants.USER_BY_ID_PATH, userId);
    }

    private String buildWalletBalanceUrl(Long userId) {
        return walletServiceUrl + CommonConstants.API_WALLET_PATH + 
               String.format(CommonConstants.BALANCE_PATH, userId);
    }

    private String buildUpdateBalanceUrl(Long userId, BigDecimal amount) {
        return walletServiceUrl + CommonConstants.API_WALLET_PATH + 
               String.format(CommonConstants.UPDATE_BALANCE_PATH, userId, amount);
    }

    private RequestEntity<Void> createGetRequest(String url) {
        return createRequest(url, HttpMethod.GET);
    }

    private RequestEntity<Void> createPostRequest(String url) {
        return createRequest(url, HttpMethod.POST);
    }

    private RequestEntity<Void> createRequest(String url, HttpMethod method) {
        try {
            HttpHeaders headers = createAuthHeaders();
            return new RequestEntity<>(headers, method, URI.create(url));
        } catch (Exception e) {
            throw new ServiceCommunicationException(ErrorMessages.SERVICE_COMMUNICATION_ERROR, e);
        }
    }

    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String authHeader = getCurrentAuthToken();
        
        if (authHeader != null) {
            headers.set(CommonConstants.AUTHORIZATION_HEADER, 
                       CommonConstants.BEARER_TOKEN_PREFIX + authHeader);
        }
        
        return headers;
    }

    private String getCurrentAuthToken() {
        try {
            return (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        } catch (Exception e) {
            return null;
        }
    }
} 