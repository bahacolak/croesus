package com.bahadircolak.wallet.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final RestTemplate restTemplate;

    @Value("${services.user-service.url:http://user-service:8081}")
    private String userServiceUrl;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            String url = userServiceUrl + "/api/users/internal/username/" + username + "/id";
            
            RequestEntity<Void> request = new RequestEntity<>(HttpMethod.GET, URI.create(url));
            ResponseEntity<Long> response = restTemplate.exchange(request, Long.class);
            
            Long userId = response.getBody();
            if (userId == null) {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
            
            return UserDetailsImpl.build(
                    userId,
                    username,
                    username + "@example.com",
                    List.of("ROLE_USER")
            );
        } catch (Exception e) {
            log.error("User not found with username: {}", username, e);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
} 