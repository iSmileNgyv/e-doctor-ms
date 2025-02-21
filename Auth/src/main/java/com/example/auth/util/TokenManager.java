package com.example.auth.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class TokenManager {
    private static final String SECRET_KEY = "secretKey12345";
    private static final int VALIDY  = 5 * 60 * 1000;
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuer("http://localhost:8085")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + VALIDY))
                .signWith(Jwts.SIG.HS256.key().build())
                .compact();
    }
    public boolean tokenValidate(String token) {
        return getUsernameToken(token) != null && isExpired(token);
    }

    public String getUsernameToken(String token) {
        return Jwts.parser()
                .verifyWith(Jwts.SIG.HS256.key().build())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith(Jwts.SIG.HS256.key().build())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date(System.currentTimeMillis()));
    }
}
