package com.example.reservation.entity;

import com.example.reservation.util.enums.DeliveryMethod;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

@Entity
@Data
@Table(name = "reservation_user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long userId;
    private int reservationTime = 30; // in minutes (default 30)
    private LocalTime workStartTime;
    private LocalTime workEndTime;
    private LocalTime lunchStartTime;
    private LocalTime lunchEndTime;
    private DeliveryMethod deliveryMethod = DeliveryMethod.EMAIL;
    private boolean internalNotification = true;

    @PrePersist
    public void setDefaultHours() {
        if (workStartTime == null)
            workStartTime = LocalTime.of(9, 0);
        if (workEndTime == null)
            workEndTime = LocalTime.of(18, 0);
        if (lunchStartTime == null)
            lunchStartTime = LocalTime.of(13, 0);
        if (lunchEndTime == null)
            lunchEndTime = LocalTime.of(14, 0);
    }
}
