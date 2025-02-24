package com.example.auth.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class TokenManager {
    private static final int VALIDITY  = 5 * 60 * 1000;
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("your_secret_key_32_characters_long".getBytes());
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuer("http://localhost:8085")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + VALIDITY))
                .signWith(SECRET_KEY)
                .compact();
    }
    public boolean tokenValidate(String token) {
        return getUsernameToken(token) != null && isExpired(token);
    }

    public String getUsernameToken(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date(System.currentTimeMillis()));
    }
}
