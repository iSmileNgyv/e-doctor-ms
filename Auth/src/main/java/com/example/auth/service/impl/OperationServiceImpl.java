package com.example.auth.service.impl;

import com.example.auth.dto.operation.getAll.GetAllOperationResponseDto;
import com.example.auth.dto.operation.getByEndpoint.GetOperationByEndpointResponseDto;
import com.example.auth.repository.OperationRepository;
import com.example.auth.service.OperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationServiceImpl implements OperationService {
    private final OperationRepository operationRepository;
    @Override
    public GetOperationByEndpointResponseDto getOperationCode(String endpoint) {
        var operation = operationRepository.findByEndpoint(endpoint);
        if(operation.isEmpty())
            return new GetOperationByEndpointResponseDto(null, false);
        var getOperation = operation.get();
        return new GetOperationByEndpointResponseDto(
                getOperation.getCode(),
                getOperation.isGlobal()
        );
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
