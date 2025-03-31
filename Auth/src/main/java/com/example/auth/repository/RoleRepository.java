package com.example.auth.repository;

import com.example.auth.dto.role.getAll.GetAllRoleResponseDto;
import com.example.auth.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    RoleEntity findByDefaultRole(boolean defaultRole);
    @Query(value = """
    SELECT r.code AS roleCode, r.name AS roleName, r.default_role AS defaultRole,
           o.code AS operationCode, o.name AS operationName
    FROM role r
             LEFT JOIN role_access ro ON r.code = ro.role_code
             LEFT JOIN operation o ON ro.operation_code = o.code
""", nativeQuery = true)
    List<Object[]> getAllRolesWithOperationsNative();

    Optional<RoleEntity> findByCode(String code);
}
