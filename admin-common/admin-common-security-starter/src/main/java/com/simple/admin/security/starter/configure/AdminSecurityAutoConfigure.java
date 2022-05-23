package com.simple.admin.security.starter.configure;

import com.simple.admin.security.starter.SecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@EnableConfigurationProperties(SecurityProperties.class)
@Import(AdminSecurityInterceptorConfigure.class)
public class AdminSecurityAutoConfigure {
}
