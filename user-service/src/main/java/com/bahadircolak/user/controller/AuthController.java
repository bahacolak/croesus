package com.bahadircolak.user.controller;

import com.bahadircolak.user.constants.UserConstants;
import com.bahadircolak.user.dto.request.LoginRequest;
import com.bahadircolak.user.dto.request.SignupRequest;
import com.bahadircolak.user.dto.response.JwtResponse;
import com.bahadircolak.user.dto.response.MessageResponse;
import com.bahadircolak.user.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = UserConstants.CROSS_ORIGIN_PATTERN, maxAge = UserConstants.CROSS_ORIGIN_MAX_AGE)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final IAuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse response = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        MessageResponse response = authService.registerUser(signupRequest);
        return ResponseEntity.ok(response);
    }
} 