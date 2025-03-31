package com.example.auth.controller;

import com.example.auth.dto.roleAccess.DeleteRoleAccessRequestDto;
import com.example.auth.service.RoleAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/role-access")
@RequiredArgsConstructor
public class RoleAccessController {
    private final RoleAccessService roleAccessService;
    @DeleteMapping("delete")
    public ResponseEntity<Void> delete(@RequestBody DeleteRoleAccessRequestDto request) {
        roleAccessService.deleteRoleAccess(request.getRoleCode(), request.getOperationCode());
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody DeleteRoleAccessRequestDto request) {
        roleAccessService.addRoleAccess(request.getRoleCode(), request.getOperationCode());
        return ResponseEntity.ok().build();
    }

    @GetMapping("{roleCode}")
    public List<String> getOperationCodes(@PathVariable String roleCode) {
        return roleAccessService.getOperationCodesByRoleCode(roleCode);
    }
}
