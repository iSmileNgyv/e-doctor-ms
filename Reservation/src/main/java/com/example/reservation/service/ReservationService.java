package com.example.reservation.service;

import com.example.reservation.dto.MakeReservationRequestDto;

public interface ReservationService {
    void makeReservation(MakeReservationRequestDto request);
}
