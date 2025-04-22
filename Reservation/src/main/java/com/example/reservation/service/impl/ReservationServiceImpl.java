package com.example.reservation.service.impl;

import com.example.reservation.UserInfo;
import com.example.reservation.dto.MakeReservationRequestDto;
import com.example.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final UserServiceGrpcClientService userServiceGrpcClientService;
    @Override
    public void makeReservation(MakeReservationRequestDto request) {
        List<UserInfo> usersInfo = userServiceGrpcClientService.getUsersInfo(List.of(request.getUserId(), request.getCustomerId()));
    }
}