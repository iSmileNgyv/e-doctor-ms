package com.example.auth.service;

import com.example.auth.dto.operation.getAll.GetAllOperationResponseDto;

import java.util.List;

public interface OperationService {
    String getOperationCode(String endpoint);
    List<GetAllOperationResponseDto> getAll();
}
