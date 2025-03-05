package com.example.otp.dto.register;

import com.example.otp.DeliveryMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {
    private long userId;
    private DeliveryMethod deliveryMethod;
}
