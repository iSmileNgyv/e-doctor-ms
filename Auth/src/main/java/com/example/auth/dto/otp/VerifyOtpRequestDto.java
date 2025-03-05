package com.example.auth.dto.otp;

import com.example.auth.util.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyOtpRequestDto {
    private long userId;
    private String otpCode;
    private OperationType operationType;
}
