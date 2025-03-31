package com.example.auth.repository;

import com.example.auth.entity.RoleAccessEntity;
import com.example.auth.entity.RoleEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface RoleAccessRepository extends JpaRepository<RoleAccessEntity, Long> {
    @Query("SELECT COUNT(ra) > 0 FROM RoleAccessEntity ra WHERE ra.operation.code = :operationCode AND ra.role.code IN :roleCodes AND ra.isActive = true")
    boolean existsByOperationCodeAndRoleCodes(String operationCode, Collection<String> roleCodes);
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM RoleAccessEntity ra WHERE ra.role.code = :roleCode AND ra.operation.code = :operationCode")
    void deleteRoleAccessEntitiesByRoleCodeAndOperationCode(@Param("roleCode") String roleCode, @Param("operationCode") String operationCode);
    @Query("SELECT ra FROM RoleAccessEntity ra WHERE ra.role = :role AND ra.isActive = true")
    List<RoleAccessEntity> findByRoleCode(RoleEntity role);
}
