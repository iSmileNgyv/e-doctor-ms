package com.example.otp.service.impl;

import com.example.otp.RegisterMultiServiceGrpc;
import com.example.otp.RegisterMultiRequestDto;
import com.example.otp.RegisterOtpRequestDto;
import com.example.otp.RegisterResponseDto;
import com.example.otp.RequestResult;
import com.example.otp.dto.register.RegisterRequestDto;
import com.example.otp.service.OtpService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.ArrayList;
import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class RegisterGrpcServerImpl extends RegisterMultiServiceGrpc.RegisterMultiServiceImplBase {
    private final OtpService otpService;

    @Override
    public StreamObserver<RegisterMultiRequestDto> registerMulti(StreamObserver<RegisterResponseDto> responseObserver) {
        List<RequestResult> results = new ArrayList<>();

        return new StreamObserver<RegisterMultiRequestDto>() {
            @Override
            public void onNext(RegisterMultiRequestDto request) {
                if (request.hasOtpRequest()) {
                    RegisterOtpRequestDto otpRequest = request.getOtpRequest();
                    try {
                        otpService.register(new RegisterRequestDto(
                                otpRequest.getUserId(),
                                otpRequest.getDeliveryMethod()
                        ));
                        System.out.println("OTP saved successfully");
                        results.add(
                                RequestResult.newBuilder()
                                        .setUserId(otpRequest.getUserId())
                                        .setSuccess(true)
                                        .setMessage("OTP kaydı başarılı")
                                        .build()
                        );
                    } catch (Exception e) {
                        results.add(
                                RequestResult.newBuilder()
                                        .setUserId(otpRequest.getUserId())
                                        .setSuccess(false)
                                        .setMessage("OTP kayıt hatası: " + e.getMessage())
                                        .build()
                        );
                    }
                } else {
                    System.out.println("Ignoring non-OTP request");
                }
            }

            @Override
            public void onError(Throwable t) {
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                RegisterResponseDto response = RegisterResponseDto.newBuilder()
                        .setSuccess(results.stream().allMatch(RequestResult::getSuccess))
                        .setMessage("OTP kayıtları işlendi")
                        .addAllResults(results)
                        .build();

                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
    }
}