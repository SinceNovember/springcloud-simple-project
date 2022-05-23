package com.simple.admin.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableFeignClients(basePackages = "com.simple.admin.product.feign")
public class AdminProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminProductApplication.class, args);
    }
}
