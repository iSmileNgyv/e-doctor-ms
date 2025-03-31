package com.example.auth.controller;

import com.example.auth.dto.operation.getAll.GetAllOperationResponseDto;
import com.example.auth.service.OperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/operation")
@RequiredArgsConstructor
public class OperationController {
    private final OperationService operationService;
    @GetMapping
    public List<GetAllOperationResponseDto> getAll() {
        return operationService.getAll();
    }
}
