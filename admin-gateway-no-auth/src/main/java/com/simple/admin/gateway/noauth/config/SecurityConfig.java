package com.simple.admin.gateway.noauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {

    /**
     * 给SpringCloud添加认证、授权过滤器
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    SecurityWebFilterChain webFluxSecurityFilterChain(ServerHttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable();
        return http.build();
    }
}
