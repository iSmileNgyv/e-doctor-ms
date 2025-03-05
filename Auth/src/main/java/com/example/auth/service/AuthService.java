package com.example.auth.service;

import com.example.auth.dto.login.LoginRequestDto;
import com.example.auth.dto.login.LoginResponseDto;
import com.example.auth.dto.otp.VerifyOtpRequestDto;
import com.example.auth.dto.register.RegisterRequestDto;

public interface AuthService {
    void register(RegisterRequestDto request, String userAgent);
    LoginResponseDto login(LoginRequestDto request, String requestId);
    LoginResponseDto verifyLoginOtp(VerifyOtpRequestDto request);
    void verifyRegisterOtp(VerifyOtpRequestDto request);
}
