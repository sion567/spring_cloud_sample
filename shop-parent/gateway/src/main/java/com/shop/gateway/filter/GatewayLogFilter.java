package com.shop.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
public class GatewayLogFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String rawPath = exchange.getRequest().getURI().getPath();
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            URI routeUri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
            Long statusCode = exchange.getResponse().getStatusCode() != null ?
                    (long) exchange.getResponse().getStatusCode().value() : 0L;
            String targetUri = (routeUri != null) ? routeUri.toString() : "not found(404)";
            log.info(" --Gateway -> req url: [{}],  target: [{}], status code: [{}]",
                    rawPath, targetUri, statusCode);
        }));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}