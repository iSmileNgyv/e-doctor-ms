package com.example.otp.controller;

import com.example.otp.dto.RequestDto;
import com.example.otp.dto.verify.VerifyRequestDto;
import com.example.otp.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/otp")
@RequiredArgsConstructor
public class OtpController {
    private final OtpService otpService;

    @PostMapping
    public ResponseEntity<Void> createOtp(@RequestBody RequestDto request, @RequestHeader("User-Agent") String userAgent) {
        otpService.createOtpCode(request, userAgent);
        return ResponseEntity.ok().build();
    }

    @PostMapping("verify")
    public ResponseEntity<Void> verifyOtp(@RequestBody VerifyRequestDto request, @RequestHeader("User-Agent") String userAgent) {
        otpService.verifyOtpCode(request, userAgent);
        return ResponseEntity.ok().build();
    }

}
