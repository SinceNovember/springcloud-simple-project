package com.simple.admin.auth.config;

import com.alibaba.fastjson.JSON;
import com.simple.admin.auth.handler.CustomClientCredentialsTokenEndpointFilter;
import com.simple.admin.auth.handler.CustomWebResponseExceptionTranslator;
import com.simple.admin.auth.handler.ReturnCode;
import com.simple.admin.core.entity.Response;
import com.simple.admin.core.entity.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.util.WebUtils;

import javax.sql.DataSource;

/**
 * 授权/认证服务器配置
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    // 认证管理器
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    /**
     * access_token 存储器 ->access_token的方式
     * 这里存储在数据库，大家可以结合自己的业务场景考虑将access_token存入数据库还是redis
     */
//    @Bean
//    public TokenStore tokenStore() {
//        return new JdbcTokenStore(dataSource);
//    }
    @Bean
    public TokenStore tokenStore() {
        //return new JdbcTokenStore(dataSource);
        return new JwtTokenStore(jwtTokenEnhancer());
    }

    /**
     * JwtAccessTokenConverter
     * TokenEnhancer的子类，帮助程序在JWT编码的令牌值和OAuth身份验证信息之间进行转换。
     */
    @Bean
    public JwtAccessTokenConverter jwtTokenEnhancer() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 设置对称签名
        converter.setSigningKey("javadaily");
        return converter;
    }


    /**
     * 从数据库读取clientDetails相关配置
     * 有InMemoryClientDetailsService 和 JdbcClientDetailsService 两种方式选择
     */
    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * 注入密码加密实现器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Primary
    @Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenEnhancer(jwtTokenEnhancer());
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        //设置token有效期，默认12小时，此处修改为6小时   21600
        tokenServices.setAccessTokenValiditySeconds(60 * 60 * 6);
        //设置refresh_token的有效期，默认30天，此处修改为7天
        tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 7);
        return tokenServices;
    }

    /**
     * 认证服务器Endpoints配置
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //如果需要使用refresh_token模式则需要注入userDetailService

        //jwt的方式
        endpoints.authenticationManager(this.authenticationManager)
                .userDetailsService(userDetailsService)
                //注入自定义的tokenService，如果不使用自定义的tokenService那么就需要将tokenService里的配置移到这里
                .tokenServices(tokenServices());
//                .tokenStore(tokenStore())
//                .accessTokenConverter(jwtTokenEnhancer());
        // 自定义异常转换类
        endpoints.exceptionTranslator(new CustomWebResponseExceptionTranslator());


        /** access_token的方式
         endpoints.userDetailsService(userDetailsService)
         .authenticationManager(this.authenticationManager)
         .tokenStore(tokenStore());
         **/
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, e) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            ResponseData responseData = Response.fail(ReturnCode.CLIENT_AUTHENTICATION_FAILED.getMsg());
            response.getWriter().write(JSON.toJSONString(responseData));
//            WebUtils.writeJson(response,resultData);
        };
    }

    /**
     * 认证服务器相关接口权限管理
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

        CustomClientCredentialsTokenEndpointFilter endpointFilter = new CustomClientCredentialsTokenEndpointFilter(security);
        endpointFilter.afterPropertiesSet();
        endpointFilter.setAuthenticationEntryPoint(authenticationEntryPoint());
        security.addTokenEndpointAuthenticationFilter(endpointFilter);

        security
                .authenticationEntryPoint(authenticationEntryPoint())
//                .allowFormAuthenticationForClients() //如果使用表单认证则需要加上
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    /**
     * client存储方式，此处使用jdbc存储
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetails());
    }
}
