package com.example.auth.dto.roleAccess;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteRoleAccessRequestDto {
    private String roleCode;
    private String operationCode;
}
