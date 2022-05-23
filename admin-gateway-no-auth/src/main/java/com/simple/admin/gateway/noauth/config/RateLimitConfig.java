package com.simple.admin.gateway.noauth.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
