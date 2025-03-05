package com.example.auth.repository;

import com.example.auth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT u FROM UserEntity u LEFT JOIN FETCH u.roles WHERE u.username = :username")
    Optional<UserEntity> findByUsernameWithRoles(@Param("username") String username);

    Optional<UserEntity> findByUsername(String username);

    @Query("UPDATE UserEntity u SET u.isActive = :isActive WHERE u.id = :id")
    @Modifying
    int updateIsActiveById(@Param("id") long id, @Param("isActive") boolean isActive);
}
