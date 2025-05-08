package com.example.reservation.service.impl;

import com.example.reservation.UserInfo;
import com.example.reservation.dto.MakeReservationRequestDto;
import com.example.reservation.entity.ReservationEntity;
import com.example.reservation.repository.ReservationRepository;
import com.example.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final UserServiceGrpcClientService userServiceGrpcClientService;
    private final ReservationRepository reservationRepository;
    @Override
    public void makeReservation(MakeReservationRequestDto request) {
        Map<Long, UserInfo> usersInfo = userServiceGrpcClientService.getUsersInfo(List.of(request.getUserId(), request.getCustomerId()));
        System.out.println(request);
        System.err.println(usersInfo);
        var requestUserInfo = usersInfo.get(request.getUserId());
        var customerUserInfo = usersInfo.get(request.getCustomerId());
        reservationRepository.save(ReservationEntity.builder()
                .customerId(customerUserInfo.getId())
                .customerFullName(customerUserInfo.getFullName())
                .customerUsername(customerUserInfo.getUsername())
                .customerPhoneNumber(customerUserInfo.getPhoneNumber())
                .userId(requestUserInfo.getId())
                .userUsername(requestUserInfo.getUsername())
                .userFullName(requestUserInfo.getFullName())
                .userPhoneNumber(requestUserInfo.getPhoneNumber())
                .fromDate(request.getFromDate())
                .toDate(request.getToDate())
                .build());
    }
}