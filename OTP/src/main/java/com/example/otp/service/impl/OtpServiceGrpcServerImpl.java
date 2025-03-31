package com.example.otp.service.impl;

import com.example.otp.*;
import com.example.otp.dto.RequestDto;
import com.example.otp.dto.verify.VerifyRequestDto;
import com.example.otp.service.OtpService;
import com.example.otp.util.enums.OperationType;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class OtpServiceGrpcServerImpl extends OtpServiceGrpc.OtpServiceImplBase {
    private final OtpService otpService;
    @Override
    public void createOtp(CreateOtpRequestDto request, StreamObserver<CreateOtpResponseDto> responseObserver) {
        try {
            otpService.createOtpCode(
                    new RequestDto(request.getUserId(), OperationType.valueOf(request.getOperationType().name())),
                    request.getUserAgent()
            );

            CreateOtpResponseDto response = CreateOtpResponseDto.newBuilder()
                    .setSuccess(true)
                    .setMessage("Otp created")
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void verifyOtp(VerifyOtpRequestDto request, StreamObserver<VerifyOtpResponseDto> responseObserver) {
        var verifyRequest = VerifyRequestDto.builder()
                .userId(request.getUserId())
                .otpCode(request.getOtpCode())
                .operationType(OperationType.valueOf(request.getOperationType().name()))
                .userAgent(request.getUserAgent())
                .build();
        try {
            otpService.verifyOtpCode(verifyRequest, request.getUserAgent());
        } catch (RuntimeException e) {
            responseObserver.onNext(VerifyOtpResponseDto.newBuilder().setSuccess(false).build());
            responseObserver.onError(e);
        }
        responseObserver.onNext(VerifyOtpResponseDto.newBuilder().setSuccess(true).build());
        responseObserver.onCompleted();
    }
}
