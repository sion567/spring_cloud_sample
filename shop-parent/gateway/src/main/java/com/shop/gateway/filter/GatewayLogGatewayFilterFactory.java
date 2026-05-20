package com.shop.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
public class GatewayLogGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            String rawPath = exchange.getRequest().getURI().getPath();
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                URI routeUri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
                Long statusCode = exchange.getResponse().getStatusCode() != null ?
                        (long) exchange.getResponse().getStatusCode().value() : 0L;
                if (routeUri != null) {
                    log.info("----路由监控 -> 原始请求: [{}], 最终实际转发目的地: [{}], 响应状态码: [{}]", rawPath, routeUri, statusCode);
                }
            }));
        };
    }
}
