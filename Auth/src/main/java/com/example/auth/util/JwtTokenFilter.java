package com.example.auth.util;

import com.example.auth.exception.user.UserNotFound;
import com.example.auth.service.OperationService;
import com.example.auth.service.RoleAccessService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private final TokenManager tokenManager;
    private final UserDetailsService userDetailsService;
    private final OperationService operationService;
    private final RoleAccessService roleAccessService;

    public JwtTokenFilter(
            TokenManager tokenManager,
            UserDetailsService userDetailsService,
            OperationService operationService,
            RoleAccessService roleAccessService
    ) {
        this.tokenManager = tokenManager;
        this.userDetailsService = userDetailsService;
        this.operationService = operationService;
        this.roleAccessService = roleAccessService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        String username = null, token = null;
        if(authHeader != null && authHeader.contains("Bearer")) {
            token = authHeader.substring(7);
            try {
                username = tokenManager.getUsernameToken(token);
            }catch(Exception e) {
                throw new UserNotFound("User not found");
            }
        }
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if(!tokenManager.isExpired(token)) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                System.out.println(this.convertToDynamicPattern(request.getRequestURI()) + " request budur");
                if(!tokenManager.isSuperAdmin(token)) {
                    String requestEndpoint = this.convertToDynamicPattern(request.getRequestURI());
                    System.out.println("dynamic pattern " + requestEndpoint);
                    String operationCode = operationService.getOperationCode(requestEndpoint);
                    if (operationCode == null) {
                        System.err.println(requestEndpoint + " operation code not found");
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    }
                    // old user details
                    List<String> roleCodes = userDetails.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .toList();
                    if (!roleAccessService.hasAccess(operationCode, roleCodes)) {
                        System.err.println(roleCodes + " " + operationCode + " not found");
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        return;
                    }
                }
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String convertToDynamicPattern(String requestEndpoint) {
        String[] segments = requestEndpoint.split("/");
        StringBuilder dynamicPattern = new StringBuilder();

        for (String segment : segments) {
            if (segment.isEmpty()) continue;

            if (segment.matches("^[A-Z_]+$")) {
                dynamicPattern.append("/{CODE}");
            } else if (segment.matches("^[0-9]+$")) {
                dynamicPattern.append("/{ID}");
            } else {
                dynamicPattern.append("/").append(segment);
            }
        }

        return dynamicPattern.toString();
    }
}
