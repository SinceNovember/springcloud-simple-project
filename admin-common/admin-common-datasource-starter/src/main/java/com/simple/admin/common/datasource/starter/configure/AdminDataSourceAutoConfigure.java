package com.simple.admin.common.datasource.starter.configure;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据源相关的通用配置
 */
@Configuration
public class AdminDataSourceAutoConfigure {

    /**
     * 注册分页插件
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        List<ISqlParser> sqlParserList = new ArrayList<>();
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        sqlParserList.add(new BlockAttackSqlParser());
        paginationInterceptor.setSqlParserList(sqlParserList);
        return paginationInterceptor;
    }
}
