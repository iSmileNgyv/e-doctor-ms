package com.example.auth.dto.operation.getAll;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllOperationResponseDto {
    private String code;
    private String name;
}
