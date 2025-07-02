package com.bahadircolak.user.service;

import com.bahadircolak.user.dto.request.LoginRequest;
import com.bahadircolak.user.dto.request.SignupRequest;
import com.bahadircolak.user.dto.response.JwtResponse;
import com.bahadircolak.user.dto.response.MessageResponse;

public interface IAuthService {
    
    JwtResponse authenticateUser(LoginRequest loginRequest);
    
    MessageResponse registerUser(SignupRequest signupRequest);
} 