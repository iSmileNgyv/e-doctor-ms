package com.example.auth.dto.role.getAll;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllRoleResponseDto {
    private String code;
    private String name;
    private boolean defaultRole;
    private List<OperationResponseDto> operations;
}
