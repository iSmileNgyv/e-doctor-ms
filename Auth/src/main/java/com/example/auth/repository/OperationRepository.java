package com.example.auth.repository;

import com.example.auth.entity.OperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OperationRepository extends JpaRepository<OperationEntity, String> {
    Optional<OperationEntity> findByEndpoint(String endpoint);
}
