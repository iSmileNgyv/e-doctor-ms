package com.example.otp.dto;

import com.example.otp.util.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {
    private long userId;
    private OperationType operationType;
}
