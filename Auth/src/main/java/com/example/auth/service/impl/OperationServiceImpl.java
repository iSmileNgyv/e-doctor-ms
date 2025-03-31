package com.example.auth.service.impl;

import com.example.auth.dto.operation.getAll.GetAllOperationResponseDto;
import com.example.auth.entity.OperationEntity;
import com.example.auth.repository.OperationRepository;
import com.example.auth.service.OperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {
    private final OperationRepository operationRepository;
    @Override
    public String getOperationCode(String endpoint) {
        var operation = operationRepository.findByEndpoint(endpoint);
        return operation.map(OperationEntity::getCode).orElse(null);
    }

    @Override
    public List<GetAllOperationResponseDto> getAll() {
        var operations = operationRepository.findAll();

        return operations.stream().map(
                operation -> new GetAllOperationResponseDto(
                        operation.getCode(),
                        operation.getName()
                )
        ).toList();
    }
}
