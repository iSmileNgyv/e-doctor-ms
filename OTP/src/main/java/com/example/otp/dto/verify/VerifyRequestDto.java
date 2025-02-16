package com.example.otp.dto.verify;

import com.example.otp.util.enums.OperationType;
import lombok.Data;

import java.util.UUID;

@Data
public class VerifyRequestDto {
    private int userId;
    private String otpCode;
    private OperationType operationType;
    private UUID requestId;
}