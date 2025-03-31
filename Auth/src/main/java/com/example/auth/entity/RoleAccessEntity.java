package com.example.auth.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "role_access")
public class RoleAccessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "role_code", nullable = false)
    private RoleEntity role;

    @ManyToOne
    @JoinColumn(name = "operation_code", nullable = false)
    private OperationEntity operation;

    private boolean isActive = true;
}
