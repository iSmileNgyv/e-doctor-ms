package com.example.auth.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Collection;
import java.util.Date;

@Entity
@Data
@Table(name = "auth_user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    @Column(name = "login_otp", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean loginOtp;
    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isActive;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<RoleEntity> roles;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}
