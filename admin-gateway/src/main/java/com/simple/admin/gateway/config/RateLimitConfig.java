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
     * �Զ���������־��key�����ά�ȿ��Դ���������
     * exchange�����л�ȡ����ID��������Ϣ���û���Ϣ��
     */
    @Bean
    public KeyResolver ipKeyResolver() {

        return ex -> Mono.just(ex.getRequest().getRemoteAddress().getHostName());
    }


}
