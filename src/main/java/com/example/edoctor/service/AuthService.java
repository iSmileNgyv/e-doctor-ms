package com.example.edoctor.service;

import com.example.edoctor.dto.auth.AuthResponseDTO;
import com.example.edoctor.exception.auth.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthClient authClient;
    private final StringRedisTemplate stringRedisTemplate;
    public AuthResponseDTO auth(String token) {
        try {
            if(isCached(token)) {
                return new AuthResponseDTO();
            }
            var authResponse =  authClient.authenticate(token);
            AuthResponseDTO response = new AuthResponseDTO();
            response.setUsername(authResponse.getUsername());
            response.setExpiredAt(authResponse.getExpiredAt());
            saveCache(token, authResponse.getExpiredAt());
            return response;
        } catch (Exception e) {
            throw new UnauthorizedException("Unauthorized");
        }
    }

    private void saveCache(String token, long expiredAt) {
        int differenceMinutes = Duration.between(Instant.now(), Instant.ofEpochSecond(expiredAt)).toMinutesPart();
        this.stringRedisTemplate.opsForValue().set(token, "true", Duration.ofMinutes(differenceMinutes));
    }

    private boolean isCached(String token) {
        return this.stringRedisTemplate.hasKey(token);
    }
}
