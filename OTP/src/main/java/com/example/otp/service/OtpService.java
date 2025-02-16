package com.example.otp.service;

import com.example.otp.dto.RequestDto;
import com.example.otp.dto.ResponseDto;
import com.example.otp.dto.verify.VerifyRequestDto;

public interface OtpService {
    ResponseDto createOtpCode(RequestDto request);
    ResponseDto verifyOtpCode(VerifyRequestDto request);
}
