package com.example.edoctor.filter;

import com.example.edoctor.exception.auth.UnauthorizedException;
import com.example.edoctor.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@RequiredArgsConstructor
public class AuthFilter implements GlobalFilter {
    private final AuthService authService;
    private final RouteValidator routeValidator;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestPath = exchange.getRequest().getURI().getPath();

        // if there is whitelist no need auth
        if (routeValidator.isPublicRoute(requestPath)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Invalid token");
        String token = authHeader.substring(7);
        return Mono.fromCallable(() -> authService.auth(token))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(response -> {
                    exchange.getRequest().mutate()
                            .header("X-User-Id", response.getUsername())
                            .header("Authorization", "Bearer " + token)
                            .build();
                    return chain.filter(exchange);

                })
                .onErrorResume(e -> Mono.error(new UnauthorizedException("Invalid token")));
    }
}
