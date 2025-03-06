package com.example.auth.service.impl;

import com.example.auth.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

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

        RegisterResponseDto.Builder responseBuilder = RegisterResponseDto.newBuilder();
        AtomicInteger completedCalls = new AtomicInteger(0);

        StreamObserver<RegisterResponseDto> responseObserver = createResponseObserver(future, responseBuilder, completedCalls);

        sendOtpRequest(userId, userAgent, responseObserver);

        sendNotificationRequest(userId, email, responseObserver);

        return future;
    }

    private StreamObserver<RegisterResponseDto> createResponseObserver(
            CompletableFuture<RegisterResponseDto> future,
            RegisterResponseDto.Builder responseBuilder,
            AtomicInteger completedCalls) {
        return new StreamObserver<>() {
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
                if (completedCalls.incrementAndGet() == 2) { // Wait for both calls to complete
                    responseBuilder.setSuccess(true);
                    responseBuilder.setMessage("All processes completed");
                    future.complete(responseBuilder.build());
                }
            }
        };
    }

    private void sendOtpRequest(Long userId, String userAgent, StreamObserver<RegisterResponseDto> responseObserver) {
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

    private void sendNotificationRequest(Long userId, String email, StreamObserver<RegisterResponseDto> responseObserver) {
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
    }
}
