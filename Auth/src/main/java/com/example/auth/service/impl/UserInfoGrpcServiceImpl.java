package com.example.auth.service.impl;

import com.example.auth.GetUsersInfoRequest;
import com.example.auth.GetUsersInfoResponse;
import com.example.auth.ReservationServiceGrpc;
import com.example.auth.repository.UserRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
public class UserInfoGrpcServiceImpl extends ReservationServiceGrpc.ReservationServiceImplBase {
    private final UserRepository userRepository;
    @Override
    public void getUsersInfo(GetUsersInfoRequest request, StreamObserver<GetUsersInfoResponse> responseObserver) {
        try {
            var users = userRepository.getUsersByIdIn(request.getUserIdList());
            var userMap = users.stream()
                    .collect(Collectors.toMap(
                            user -> user.getId(), // Map key: user ID
                            user -> com.example.auth.UserInfo.newBuilder()
                                    .setId(user.getId())
                                    .setFullName(String.format("%s %s", user.getName(), user.getSurname()))
                                    .setUsername(user.getUsername())
                                    .setPhoneNumber(user.getPhoneNumber())
                                    .build()
                    ));
            var response = GetUsersInfoResponse.newBuilder()
                    .putAllUsers(userMap)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }
}
