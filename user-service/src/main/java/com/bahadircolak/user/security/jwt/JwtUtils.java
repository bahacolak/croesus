package com.bahadircolak.user.security.jwt;

import com.bahadircolak.user.constants.ErrorMessages;
import com.bahadircolak.user.exception.JwtException;
import com.bahadircolak.user.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs * 1000L))
                .signWith(getSigningKey())
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException e) {
            throw new JwtException(ErrorMessages.JWT_TOKEN_INVALID, e);
        } catch (ExpiredJwtException e) {
            throw new JwtException(ErrorMessages.JWT_TOKEN_EXPIRED, e);
        } catch (UnsupportedJwtException e) {
            throw new JwtException(ErrorMessages.JWT_TOKEN_UNSUPPORTED, e);
        } catch (IllegalArgumentException e) {
            throw new JwtException(ErrorMessages.JWT_CLAIMS_EMPTY, e);
        }
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
} 