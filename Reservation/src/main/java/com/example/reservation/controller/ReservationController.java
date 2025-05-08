package com.example.reservation.controller;

import com.example.reservation.dto.MakeReservationRequestDto;
import com.example.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/reservation")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public void makeReservation(@RequestBody MakeReservationRequestDto request) {
        reservationService.makeReservation(request);
    }
}
