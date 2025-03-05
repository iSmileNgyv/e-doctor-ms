package com.example.auth.dto.register;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {
    @NotNull(message = "Username is required")
    @Size(min = 3, message = "Username must be at least 3 characters")
    private String username;
    @NotNull(message = "Password is required")
    @Size(min = 5, message = "Password must be at least 5 characters")
    private String password;
    @NotNull(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
}
