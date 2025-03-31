package com.example.auth.util;

import com.example.auth.exception.auth.UnauthorizedException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Service
public class TokenManager {
    private static final int VALIDITY  = 7 * 24 * 60 * 60 * 1000;
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("your_secret_key_32_characters_long".getBytes());
    public String generateToken(String username, List<String> roles, boolean superAdmin) {
        return Jwts.builder()
                .subject(username)
                .issuer("http://localhost:8085")
                .claim("role", roles)
                .claim("super_admin", superAdmin)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + VALIDITY))
                .signWith(SECRET_KEY)
                .compact();
    }

    public String getUsernameToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch(ExpiredJwtException e) {
            throw new UnauthorizedException("Token expired");
        } catch(JwtException e) {
            throw new UnauthorizedException("Token invalid");
        }
    }

    public boolean isSuperAdmin(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("super_admin", Boolean.class);
        } catch(ExpiredJwtException e) {
            throw new UnauthorizedException("Token expired");
        } catch(JwtException e) {
            throw new UnauthorizedException("Invalid token");
        }
    }

    public long getExpiredAt(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .getTime();
        } catch(ExpiredJwtException e) {
            throw new UnauthorizedException("Token expired");
        } catch(JwtException e) {
            throw new UnauthorizedException("Invalid token");
        }
    }

    public boolean isExpired(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration()
                    .before(new Date(System.currentTimeMillis()));
        } catch(ExpiredJwtException e) {
            return true;
        } catch(JwtException e) {
            throw new UnauthorizedException("Invalid token");
        }
    }
}
