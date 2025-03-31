package com.example.auth.service.impl;

import com.example.auth.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class OtpGrpcClientServiceImpl {
    @GrpcClient("otpService")
    private OtpServiceGrpc.OtpServiceBlockingStub otpServiceBlockingStub;

    public CreateOtpResponseDto createOtp(CreateOtpRequestDto createOtpRequestDto) {
        return otpServiceBlockingStub.createOtp(createOtpRequestDto);
    }

    public VerifyOtpResponseDto verifyOtp(VerifyOtpRequestDto verifyOtpRequestDto) {
        return otpServiceBlockingStub.verifyOtp(verifyOtpRequestDto);
    }
}
