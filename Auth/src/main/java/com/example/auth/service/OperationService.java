package com.example.auth.service;

import com.example.auth.dto.operation.getAll.GetAllOperationResponseDto;
import com.example.auth.dto.operation.getByEndpoint.GetOperationByEndpointResponseDto;

import java.util.HashMap;
import java.util.List;

public interface OperationService {
    GetOperationByEndpointResponseDto getOperationCode(String endpoint);
    List<GetAllOperationResponseDto> getAll();
}
