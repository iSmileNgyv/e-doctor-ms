package com.example.auth.service.impl;

import com.example.auth.dto.role.create.CreateRoleRequestDto;
import com.example.auth.dto.role.getAll.GetAllRoleResponseDto;
import com.example.auth.dto.role.getAll.OperationResponseDto;
import com.example.auth.dto.role.update.UpdateRoleRequestDto;
import com.example.auth.entity.RoleEntity;
import com.example.auth.exception.role.CannotCreateRoleException;
import com.example.auth.exception.role.CannotUpdateRoleException;
import com.example.auth.repository.RoleRepository;
import com.example.auth.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Override
    public void createRole(CreateRoleRequestDto request) {
        try {
            var role = new RoleEntity();
            role.setCode(request.getCode());
            role.setName(request.getName());
            roleRepository.save(role);
        } catch (Exception _) {
            throw new CannotCreateRoleException();
        }
    }

    @Override
    @Transactional
    public void updateRole(UpdateRoleRequestDto request) {
        try {
            var role = roleRepository.findByCode(request.getCode()).orElseThrow(() -> new CannotUpdateRoleException("Role not found"));
            if(request.isDefaultRole()) {
                var defaultRole = roleRepository.findByDefaultRole(true);
                defaultRole.setDefaultRole(false);
                roleRepository.save(defaultRole);

                role.setDefaultRole(true);
            }
            role.setName(request.getName());
            roleRepository.save(role);
        } catch (Exception ex) {
            //System.out.println("Cannot update role " + Arrays.toString(ex.getStackTrace()));
            throw new CannotUpdateRoleException();
        }
    }

    @Override
    public List<GetAllRoleResponseDto> getAllRoles() {
        List<Object[]> results = roleRepository.getAllRolesWithOperationsNative();
        Map<String, GetAllRoleResponseDto> roleMap = new HashMap<>();

        for (Object[] row : results) {
            String roleCode = (String) row[0];
            String roleName = (String) row[1];
            boolean defaultRole = (Boolean) row[2];
            String operationCode = (String) row[3];
            String operationName = (String) row[4];

            roleMap.putIfAbsent(roleCode, new GetAllRoleResponseDto(roleCode, roleName, defaultRole, new ArrayList<>()));
            if (operationCode != null) {
                roleMap.get(roleCode).getOperations().add(new OperationResponseDto(operationCode, operationName));
            }
        }

        return new ArrayList<>(roleMap.values());
    }
}
