package com.example.reservation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@Table(name = "reservation")
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long userId;
    private String userFullName;
    private String userUsername;
    private String userPhoneNumber;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private long customerId;
    private String customerFullName;
    private String customerUsername;
    private String customerPhoneNumber;
}
