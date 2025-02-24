package com.example.auth.exception.user;

import com.example.auth.exception.BaseException;
import org.springframework.http.HttpStatus;

public class RoleNotFoundException extends BaseException {
    public RoleNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND.value());
    }

    public RoleNotFoundException() {
        super("Role not found", HttpStatus.NOT_FOUND.value());
    }
}
