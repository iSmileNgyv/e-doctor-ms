package com.example.edoctor.service;


import com.example.edoctor.dto.auth.AuthResponseDTO;
import com.example.edoctor.exception.auth.UnAuthrorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthClient authClient;
    public AuthResponseDTO auth(String token) {
        try {
            var authResponse =  authClient.authenticate(token);
            AuthResponseDTO response = new AuthResponseDTO();
            response.setUsername(authResponse.getUsername());
            response.setExpiredAt(authResponse.getExpiredAt());
            return response;
        } catch (Exception e) {
            throw new UnAuthrorizedException();
        }

    }
}
