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
        RegisterResponseDto.Builder responseBuilder = RegisterResponseDto.newBuilder();
        sendNotificationRequest(userId, email).thenRun(() -> sendOtpRequest(userId, userAgent, future, responseBuilder)).exceptionally(ex -> {
            future.completeExceptionally(ex);
            return null;
        });

        return future;
    }

    private CompletableFuture<Void> sendNotificationRequest(Long userId, String email) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        StreamObserver<RegisterResponseDto> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(RegisterResponseDto response) {
                System.out.println("Notification response received.");
            }

            @Override
            public void onError(Throwable t) {
                future.completeExceptionally(t);
            }

            @Override
            public void onCompleted() {
                future.complete(null); // Notification işlemi tamamlandı
            }
        };

        StreamObserver<RegisterMultiRequestDto> notificationRequestObserver = notificationServiceStub.registerMulti(responseObserver);
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

    private void sendOtpRequest(Long userId, String userAgent, CompletableFuture<RegisterResponseDto> future, RegisterResponseDto.Builder responseBuilder) {
        StreamObserver<RegisterResponseDto> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(RegisterResponseDto response) {
                synchronized (responseBuilder) {
                    responseBuilder.addAllResults(response.getResultsList());
                }
            }

            @Override
            public void onError(Throwable t) {
                future.completeExceptionally(t);
            }

            @Override
            public void onCompleted() {
                responseBuilder.setSuccess(true);
                responseBuilder.setMessage("All processes completed");
                future.complete(responseBuilder.build());
            }
        };

        StreamObserver<RegisterMultiRequestDto> otpRequestObserver = otpServiceStub.registerMulti(responseObserver);
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
    }
}
