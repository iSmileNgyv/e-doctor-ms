package com.example.otp.dto.verify;

import com.example.otp.util.enums.OperationType;
import lombok.Data;

@Data
public class VerifyRequestDto {
    private int userId;
    private String otpCode;
    private OperationType operationType;
}