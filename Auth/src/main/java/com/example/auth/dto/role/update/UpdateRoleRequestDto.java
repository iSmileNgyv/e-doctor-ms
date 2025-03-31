package com.example.auth.dto.role.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoleRequestDto {
    private String code;
    private String name;
    private boolean defaultRole = false;
}
