package com.example.auth.service;

import com.example.auth.dto.role.create.CreateRoleRequestDto;
import com.example.auth.dto.role.getAll.GetAllRoleResponseDto;
import com.example.auth.dto.role.update.UpdateRoleRequestDto;

import java.util.List;

public interface RoleService {
    void createRole(CreateRoleRequestDto request);
    void updateRole(UpdateRoleRequestDto request);
    List<GetAllRoleResponseDto> getAllRoles();
}
