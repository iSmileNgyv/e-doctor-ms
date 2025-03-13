package com.example.auth.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "operation")
public class OperationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
}
