package com.example.auth.service.impl;

import com.example.auth.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class RegisterGrpcClientImpl {

    @GrpcClient("otpService")
    private RegisterMultiServiceGrpc.RegisterMultiServiceStub otpServiceStub;

    @GrpcClient("notificationService")
    private RegisterMultiServiceGrpc.RegisterMultiServiceStub notificationServiceStub;

    public CompletableFuture<RegisterResponseDto> registerMulti(Long userId, String email, String userAgent) {
        CompletableFuture<RegisterResponseDto> future = new CompletableFuture<>();
        System.out.println("grpc " + userId + " " + email + " " + userAgent);
        // OTP isteği için StreamObserver
        StreamObserver<RegisterResponseDto> otpResponseObserver = new StreamObserver<>() {
            @Override
            public void onNext(RegisterResponseDto response) {
                // OTP yanıtını işle
                System.out.println("OTP Response: " + response);
            }

            @Override
            public void onError(Throwable t) {
                future.completeExceptionally(t);
            }

            @Override
            public void onCompleted() {
                // OTP isteği tamamlandı
                System.out.println("OTP Request Completed");
            }
        };

        // Notification isteği için StreamObserver
        StreamObserver<RegisterResponseDto> notificationResponseObserver = new StreamObserver<>() {
            @Override
            public void onNext(RegisterResponseDto response) {
                // Notification yanıtını işle
                System.out.println("Notification Response: " + response);
                future.complete(response); // İşlemi tamamla
            }

            @Override
            public void onError(Throwable t) {
                future.completeExceptionally(t);
            }

            @Override
            public void onCompleted() {
                // Notification isteği tamamlandı
                System.out.println("Notification Request Completed");
            }
        };

        // OTP isteğini gönder
        StreamObserver<RegisterMultiRequestDto> otpRequestObserver = otpServiceStub.registerMulti(otpResponseObserver);
        otpRequestObserver.onNext(
                RegisterMultiRequestDto.newBuilder()
                        .setOtpRequest(
                                RegisterOtpRequestDto.newBuilder()
                                        .setUserId(userId)
                                        .setDeliveryMethod(DeliveryMethod.EMAIL)
                                        .setUserAgent(userAgent)
                                        .build()
                        )
                        .build()
        );
        otpRequestObserver.onCompleted();

        // Notification isteğini gönder
        StreamObserver<RegisterMultiRequestDto> notificationRequestObserver = notificationServiceStub.registerMulti(notificationResponseObserver);
        notificationRequestObserver.onNext(
                RegisterMultiRequestDto.newBuilder()
                        .setNotificationRequest(
                                RegisterNotificationRequestDto.newBuilder()
                                        .setUserId(userId)
                                        .setDeliveryMethod(DeliveryMethod.EMAIL)
                                        .setRecipient(email)
                                        .build()
                        )
                        .build()
        );
        notificationRequestObserver.onCompleted();

        return future;
    }
}