package com.example.otp.controller;

import com.example.otp.dto.RequestDto;
import com.example.otp.dto.ResponseDto;
import com.example.otp.dto.verify.VerifyRequestDto;
import com.example.otp.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/otp")
@RequiredArgsConstructor
public class OtpController {
    private final OtpService otpService;

    @PostMapping
    public ResponseDto createOtp(@RequestBody RequestDto request) {
        return otpService.createOtpCode(request);
    }

    @PostMapping("verify")
    public ResponseDto verifyOtp(@RequestBody VerifyRequestDto request) {
        return otpService.verifyOtpCode(request);
    }

}
