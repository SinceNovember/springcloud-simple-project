package com.simple.admin.gateway.noauth.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 添加一个给请求头token的全局过滤器，防止客户绕过网关向服务发起请求
 */
@Component
@Order(0)
public class GateWayRequestFilter implements GlobalFilter {

    public static final String GATEWAY_TOKEN_HEADER = "gateway_token_header";

    public static final String GATEWAY_TOKEN_VALUE = "gateway_token_value";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        byte[] token = Base64Utils.encode((GATEWAY_TOKEN_VALUE).getBytes());
        String[] headerValues = {new String(token)};
        ServerHttpRequest build = exchange.getRequest()
                .mutate()
                .header(GATEWAY_TOKEN_HEADER, headerValues)
                .build();

        ServerWebExchange newExchange = exchange.mutate().request(build).build();
        return chain.filter(newExchange);
    }
}
