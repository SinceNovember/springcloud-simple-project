package com.simple.admin.account;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@MapperScan("com.simple.admin.account.mapper")
public class AdminAccountApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminAccountApplication.class, args);
    }
}
