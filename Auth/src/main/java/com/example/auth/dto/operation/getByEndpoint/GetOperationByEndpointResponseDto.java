package com.example.auth.dto.operation.getByEndpoint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetOperationByEndpointResponseDto {
    private String operationCode;
    private boolean isGlobal;
}
