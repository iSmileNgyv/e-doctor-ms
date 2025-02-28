package com.example.auth.controller;

import com.example.auth.dto.login.LoginRequestDto;
import com.example.auth.dto.login.LoginResponseDto;
import com.example.auth.dto.register.RegisterRequestDto;
import com.example.auth.dto.register.RegisterResponseDto;
import com.example.auth.service.AuthService;
import com.example.auth.util.TokenManager;
import lombok.RequiredArgsConstructor;
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
    private final AuthService authService;

    @PostMapping("login")
    public LoginResponseDto login(@RequestBody LoginRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        return new LoginResponseDto(tokenManager.generateToken(request.getUsername()));
    }

    @PostMapping("register")
    public RegisterResponseDto register(@RequestBody RegisterRequestDto request) {
        return authService.register(request);
    }

//    @PostMapping
//    public AuthResponseDto auth(@RequestBody AuthRequestDto request) {
//        //return authService.validateToken(request.getToken());
//    }
}
