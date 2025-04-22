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
    private String name;
    private String surname;
    private String username;
    private String password;
    private String phoneNumber;
    @Column(name = "login_otp", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean loginOtp = false;
    @Column(name = "is_active", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isActive = false;
    @Column(name = "super_admin", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean superAdmin = false;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<RoleEntity> roles;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}
