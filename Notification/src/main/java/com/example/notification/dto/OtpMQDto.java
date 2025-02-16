package com.example.notification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpMQDto {
    @JsonProperty("userId")
    private int userId;
    @JsonProperty("code")
    private String code;
}
