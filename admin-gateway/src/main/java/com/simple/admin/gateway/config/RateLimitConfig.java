package com.simple.admin.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimitConfig {

    /**
     * 自定义限流标志的key，多个维度可以从这里入手
     * exchange对象中获取服务ID、请求信息，用户信息等
     */
    @Bean
    public KeyResolver ipKeyResolver() {

        return ex -> Mono.just(ex.getRequest().getRemoteAddress().getHostName());
    }


}
