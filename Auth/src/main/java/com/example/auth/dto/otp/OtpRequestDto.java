package com.example.auth.dto.otp;

import com.example.auth.util.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpRequestDto {
    private OperationType operationType;
    private long userId;
}
