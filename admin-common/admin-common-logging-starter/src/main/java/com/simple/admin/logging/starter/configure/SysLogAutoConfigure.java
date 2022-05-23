package com.simple.admin.logging.starter.configure;

import com.simple.admin.logging.starter.aspect.SysLogAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SysLogAutoConfigure {

    @Bean
    public SysLogAspect controllerLogAspect(){
        return new SysLogAspect();
    }

}
