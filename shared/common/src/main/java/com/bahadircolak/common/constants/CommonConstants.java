package com.bahadircolak.common.constants;

public final class CommonConstants {
    
    public static final String BEARER_TOKEN_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    
    public static final String USER_SERVICE_DEFAULT_URL = "http://localhost:8081";
    public static final String WALLET_SERVICE_DEFAULT_URL = "http://localhost:8083";
    
    public static final String API_USERS_PATH = "/api/users";
    public static final String API_WALLET_PATH = "/api/wallet";
    public static final String USERNAME_ID_PATH = "/username/%s/id";
    public static final String USER_BY_ID_PATH = "/%d";
    public static final String BALANCE_PATH = "/%d/balance";
    public static final String UPDATE_BALANCE_PATH = "/%d/update-balance?amount=%s";
    
    private CommonConstants() {
    }
} 