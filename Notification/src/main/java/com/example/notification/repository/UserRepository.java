package com.example.notification.repository;

import com.example.notification.entity.UserEntity;
import com.example.notification.util.enums.DeliveryMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUserIdAndDeliveryMethod(int userId, DeliveryMethod deliveryMethod);
}
