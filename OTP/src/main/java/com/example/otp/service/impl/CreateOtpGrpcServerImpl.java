package com.example.otp.service.impl;

import com.example.otp.CreateOtpCodeServiceGrpc;
import com.example.otp.CreateOtpRequestDto;
import com.example.otp.CreateOtpResponseDto;
import com.example.otp.dto.RequestDto;
import com.example.otp.service.OtpService;
import com.example.otp.util.enums.OperationType;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class CreateOtpGrpcServerImpl extends CreateOtpCodeServiceGrpc.CreateOtpCodeServiceImplBase {
    private final OtpService otpService;
    @Override
    public void createOtp(CreateOtpRequestDto request, StreamObserver<CreateOtpResponseDto> responseObserver) {
        try {
            System.out.println("OTP oluşturuluyor, userId: " + request.getUserId());
            otpService.createOtpCode(
                    new RequestDto(request.getUserId(), OperationType.valueOf(request.getOperationType().name())),
                    request.getUserAgent()
            );

            CreateOtpResponseDto response = CreateOtpResponseDto.newBuilder()
                    .setSuccess(true)
                    .setMessage("Otp created")
                    .build();
            System.out.println("OTP başarıyla oluşturuldu, userId: " + request.getUserId());
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            System.err.println("OTP oluşturulamadı: " + e.getMessage());
            responseObserver.onError(e);
        }
    }
}
