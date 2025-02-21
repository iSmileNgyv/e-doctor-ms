package com.example.auth.controller;

import com.example.auth.dto.LoginRequestDto;
import com.example.auth.dto.LoginRequestResponseDto;
import com.example.auth.util.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final TokenManager tokenManager;

    @PostMapping("login")
    public LoginRequestResponseDto login(@RequestBody LoginRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        return new LoginRequestResponseDto(tokenManager.generateToken(request.getUsername()));
    }
}
