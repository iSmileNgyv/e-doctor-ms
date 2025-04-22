package com.example.auth.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllUserResponseDto {
    private long id;
    private String name;
    private String surname;
    private String username;
    private boolean isActive;
    private String phoneNumber;
    private List<RoleDto> roles;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoleDto {
        private String code;
        private String name;
    }
}