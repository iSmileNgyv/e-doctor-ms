package com.example.reservation.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Entity
@Data
@Table(name = "reservation")
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long userId;
    private String userFullName;
    private String userUsername;
    private String userPhoneNumber;
    private ZonedDateTime fromDate;
    private ZonedDateTime toDate;
    private long customerId;
    private String customerFullName;
    private String customerUsername;
    private String customerPhoneNumber;
}
