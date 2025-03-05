package com.example.otp.service;

import com.example.otp.dto.RequestDto;
import com.example.otp.dto.register.RegisterRequestDto;
import com.example.otp.dto.verify.VerifyRequestDto;

public interface OtpService {
    void createOtpCode(RequestDto request, String userAgent);
    void verifyOtpCode(VerifyRequestDto request, String userAgent);
    void register(RegisterRequestDto request);
}
