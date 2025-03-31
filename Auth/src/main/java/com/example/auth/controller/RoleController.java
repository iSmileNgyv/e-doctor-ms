package com.example.auth.controller;

import com.example.auth.dto.role.create.CreateRoleRequestDto;
import com.example.auth.dto.role.getAll.GetAllRoleResponseDto;
import com.example.auth.dto.role.update.UpdateRoleRequestDto;
import com.example.auth.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping("create")
    public ResponseEntity<Void> create(@RequestBody CreateRoleRequestDto request) {
        roleService.createRole(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("update")
    public ResponseEntity<Void> update(@RequestBody UpdateRoleRequestDto request) {
        roleService.updateRole(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<GetAllRoleResponseDto> getAll() {
        return roleService.getAllRoles();
    }
}
