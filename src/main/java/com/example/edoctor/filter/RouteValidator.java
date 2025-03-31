package com.example.edoctor.filter;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouteValidator {
    private static final List<String> PUBLIC_ROUTES = List.of(
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/api/v1/auth/verify-login-otp",
            "/api/v1/auth/verify-register-otp"
    );

    public boolean isPublicRoute(String requestPath) {
        return PUBLIC_ROUTES.stream().anyMatch(requestPath::equalsIgnoreCase);
    }
}
