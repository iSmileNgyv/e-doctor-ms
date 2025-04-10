package com.example.auth.service.impl;

import com.example.auth.entity.OperationEntity;
import com.example.auth.entity.RoleAccessEntity;
import com.example.auth.entity.RoleEntity;
import com.example.auth.repository.RoleAccessRepository;
import com.example.auth.repository.RoleRepository;
import com.example.auth.service.CustomWebSocketHandler;
import com.example.auth.service.RoleAccessService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleAccessServiceImpl implements RoleAccessService {
    private final RoleAccessRepository roleAccessRepository;
    private final RoleRepository roleRepository;
    private final CustomWebSocketHandler customWebSocketHandler;

    @Override
    public boolean hasAccess(String operationCode, List<String> roleCodes) {
        return roleAccessRepository.existsByOperationCodeAndRoleCodes(operationCode, roleCodes);
    }

    @Transactional
    @Override
    public void deleteRoleAccess(String roleCode, String operationCode) {
        try {
            roleAccessRepository.deleteRoleAccessEntitiesByRoleCodeAndOperationCode(roleCode, operationCode);
            customWebSocketHandler.broadcastMessage(roleCode);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("Failed to delete role access", e);
        }
    }

    @Override
    public void addRoleAccess(String roleCode, String operationCode) {
        RoleAccessEntity entity = new RoleAccessEntity();
        RoleEntity roleEntity = new RoleEntity();
        OperationEntity operationEntity = new OperationEntity();
        operationEntity.setCode(operationCode);
        roleEntity.setCode(roleCode);
        entity.setActive(true);
        entity.setRole(roleEntity);
        entity.setOperation(operationEntity);
        try {
            roleAccessRepository.save(entity);
            customWebSocketHandler.broadcastMessage(roleCode);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new RuntimeException("Failed to add role access", e);
        }
    }

    @Override
    public List<String> getOperationCodesByRoleCode(String roleCode) {
        var role = roleRepository.findByCode(roleCode).orElseThrow(() -> new RuntimeException("Role code not found"));
        var operations = roleAccessRepository.findByRoleCode(role);
        return operations.stream().map(roleAccessEntity -> roleAccessEntity.getOperation().getCode()).toList();
    }
}
