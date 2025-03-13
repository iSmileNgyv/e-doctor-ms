package com.example.notification.service.impl;

import com.example.notification.*;
import com.example.notification.entity.UserEntity;
import com.example.notification.repository.UserRepository;
import com.example.notification.util.enums.DeliveryMethod;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import java.util.ArrayList;
import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class NotificationGrpcServiceImpl extends RegisterMultiServiceGrpc.RegisterMultiServiceImplBase {
    private final UserRepository userRepository;

    @Override
    public StreamObserver<RegisterMultiRequestDto> registerMulti(StreamObserver<RegisterResponseDto> responseObserver) {
        List<RequestResult> results = new ArrayList<>();

        return new StreamObserver<RegisterMultiRequestDto>() {
            @Override
            public void onNext(RegisterMultiRequestDto request) {
                if (request.hasNotificationRequest()) {
                    RegisterNotificationRequestDto notificationRequest = request.getNotificationRequest();
                    try {
                        var user = userRepository.findByUserIdAndDeliveryMethod(
                                notificationRequest.getUserId(),
                                DeliveryMethod.valueOf(notificationRequest.getDeliveryMethod().name())
                        );
                        System.out.println("Notification user saved " + System.currentTimeMillis());

                        if (user.isPresent()) {
                            results.add(
                                    RequestResult.newBuilder()
                                            .setUserId(notificationRequest.getUserId())
                                            .setSuccess(false)
                                            .setMessage("User already exists")
                                            .build()
                            );
                            return;
                        }

                        var userEntity = new UserEntity();
                        userEntity.setUserId(notificationRequest.getUserId());
                        userEntity.setDeliveryMethod(DeliveryMethod.valueOf(notificationRequest.getDeliveryMethod().name()));
                        userEntity.setRecipient(notificationRequest.getRecipient());
                        userRepository.save(userEntity);
                        results.add(
                                RequestResult.newBuilder()
                                        .setUserId(notificationRequest.getUserId())
                                        .setSuccess(true)
                                        .setMessage("Notification registration successful")
                                        .build()
                        );
                    } catch (Exception e) {
                        results.add(
                                RequestResult.newBuilder()
                                        .setUserId(notificationRequest.getUserId())
                                        .setSuccess(false)
                                        .setMessage("Error: " + e.getMessage())
                                        .build()
                        );
                    }
                } else {
                    System.out.println("Ignoring non-notification request");
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
                        .setMessage("Notification registrations processed")
                        .addAllResults(results)
                        .build();

                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
    }
}