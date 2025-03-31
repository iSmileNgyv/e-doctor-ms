package com.example.auth.controller;

import com.example.auth.dto.login.LoginRequestDto;
import com.example.auth.dto.login.LoginResponseDto;
import com.example.auth.dto.otp.VerifyOtpRequestDto;
import com.example.auth.dto.register.RegisterRequestDto;
import com.example.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("login")
    public LoginResponseDto login(@RequestBody LoginRequestDto request, @RequestHeader("User-Agent") String userAgent) {
        return authService.login(request, userAgent);
    }

    @PostMapping("register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequestDto request, @RequestHeader("User-Agent") String userAgent) {
        authService.register(request, userAgent);
        return ResponseEntity.ok().build();
    }

    @PostMapping("verify-login-otp")
    public LoginResponseDto verifyLoginOtp(@RequestBody VerifyOtpRequestDto request, @RequestHeader("User-Agent") String userAgent) {
        return authService.verifyLoginOtp(request, userAgent);
    }

    @PostMapping("verify-register-otp")
    public ResponseEntity<Void> verifyRegisterOtp(@RequestBody VerifyOtpRequestDto request, @RequestHeader("User-Agent") String userAgent) {
        authService.verifyRegisterOtp(request, userAgent);
        return ResponseEntity.ok().build();
    }
}
