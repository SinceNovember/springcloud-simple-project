package com.simple.admin.security.starter.configure;

import com.simple.admin.security.starter.SecurityProperties;
import com.simple.admin.security.starter.interceptor.ServerProtectInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class AdminSecurityInterceptorConfigure implements WebMvcConfigurer {

    private SecurityProperties properties;

    @Autowired
    public void setProperties(SecurityProperties properties) {
        this.properties = properties;
    }

    @Bean
    public HandlerInterceptor serverProtectInterceptor() {
        ServerProtectInterceptor interceptor = new ServerProtectInterceptor();
        interceptor.setProperties(properties);
        return interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(serverProtectInterceptor());
    }


}
