package com.shop.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
public class GatewayLogFilter2 implements GatewayFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("------ [SysOut] 收到请求，路径是: " + exchange.getRequest().getURI().getPath());
        String rawPath = exchange.getRequest().getURI().getPath();
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            URI routeUri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
            Long statusCode = exchange.getResponse().getStatusCode() != null ?
                    (long) exchange.getResponse().getStatusCode().value() : 0L;
            String targetUri = (routeUri != null) ? routeUri.toString() : "未匹配到(404)";
            log.info(" 网关监控 -> 原始请求: [{}], 最终实际转发目的地: [{}], 响应状态码: [{}]",
                    rawPath, targetUri, statusCode);
        }));
    }
}
