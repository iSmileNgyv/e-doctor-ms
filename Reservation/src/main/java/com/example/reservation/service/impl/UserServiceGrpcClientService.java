package com.example.reservation.service.impl;

import com.example.reservation.GetUsersInfoRequest;
import com.example.reservation.GetUsersInfoResponse;
import com.example.reservation.ReservationServiceGrpc;
import com.example.reservation.UserInfo;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceGrpcClientService{
    @GrpcClient("authService")
    private ReservationServiceGrpc.ReservationServiceBlockingStub reservationServiceBlockingStub;

    public Map<Long, UserInfo> getUsersInfo(List<Long> userId) {
        var request = GetUsersInfoRequest.newBuilder()
                .addAllUserId(userId)
                .build();

        GetUsersInfoResponse response = reservationServiceBlockingStub.getUsersInfo(request);
        return response.getUsersMap();
    }
}
