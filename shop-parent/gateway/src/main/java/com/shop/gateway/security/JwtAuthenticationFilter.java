package com.shop.gateway.security;

import com.shop.common.jwt.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
public class JwtAuthenticationFilter implements WebFilter {

    private final String jwtSecret;

    public JwtAuthenticationFilter(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        String method = exchange.getRequest().getMethod().name();

        if (isPublicPath(path, method)) {
            return chain.filter(exchange);
        }

        String token = extractToken(exchange.getRequest());
        if (token == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            if (!JwtUtils.validateToken(jwtSecret, token)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            Claims claims = JwtUtils.parseToken(jwtSecret, token);
            String username = claims.getSubject();
            List<String> roles = JwtUtils.getRoles(jwtSecret, token);

            List<SimpleGrantedAuthority> authorities = roles != null
                    ? roles.stream().map(SimpleGrantedAuthority::new).toList()
                    : List.of();

            Authentication auth = new UsernamePasswordAuthenticationToken(username, null, authorities);

            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));

        } catch (Exception e) {
            log.warn("JWT validation failed: {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private boolean isPublicPath(String path, String method) {
        if ("OPTIONS".equals(method)) {
            return true;
        }
        if (path.startsWith("/api/user/register") || path.startsWith("/api/user/login") || path.startsWith("/api/user/refresh")) {
            return true;
        }
        if (path.startsWith("/actuator")) {
            return true;
        }
        return false;
    }

    private String extractToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
