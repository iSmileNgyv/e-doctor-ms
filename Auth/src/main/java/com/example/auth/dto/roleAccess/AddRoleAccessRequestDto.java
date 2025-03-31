package com.example.auth.dto.roleAccess;

import lombok.Data;

@Data
public class AddRoleAccessRequestDto {
    private String roleCode;
    private String operationCode;
}
