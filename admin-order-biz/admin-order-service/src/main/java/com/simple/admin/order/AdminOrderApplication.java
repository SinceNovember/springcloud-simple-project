package com.simple.admin.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableFeignClients(basePackages = "com.simple.admin.order.feign")
public class AdminOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminOrderApplication.class, args);
    }
}
