package com.example.otp.dto;

import com.example.otp.util.enums.OperationType;
import lombok.Data;

import java.util.UUID;

@Data
public class RequestDto {
    private int userId;
    private OperationType operationType;
    private UUID requestId;
}
