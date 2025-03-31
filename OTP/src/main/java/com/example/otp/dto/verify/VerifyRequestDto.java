package com.example.otp.dto.verify;

import com.example.otp.util.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyRequestDto {
    private long userId;
    private String otpCode;
    private OperationType operationType;
    private String userAgent;
}