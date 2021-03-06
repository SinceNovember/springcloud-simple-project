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
 * ????/??????????????
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    // ??????????
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    /**
     * access_token ?????? ->access_token??????
     * ??????????????????????????????????????????????????access_token??????????????redis
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
     * TokenEnhancer??????????????????JWT??????????????OAuth??????????????????????????
     */
    @Bean
    public JwtAccessTokenConverter jwtTokenEnhancer() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // ????????????
        converter.setSigningKey("javadaily");
        return converter;
    }


    /**
     * ????????????clientDetails????????
     * ??InMemoryClientDetailsService ?? JdbcClientDetailsService ????????????
     */
    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * ??????????????????
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
        //????token????????????12????????????????6????   21600
        tokenServices.setAccessTokenValiditySeconds(60 * 60 * 6);
        //????refresh_token??????????????30??????????????7??
        tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 7);
        return tokenServices;
    }

    /**
     * ??????????Endpoints????
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //????????????refresh_token??????????????userDetailService

        //jwt??????
        endpoints.authenticationManager(this.authenticationManager)
                .userDetailsService(userDetailsService)
                //????????????tokenService????????????????????tokenService????????????tokenService????????????????
                .tokenServices(tokenServices());
//                .tokenStore(tokenStore())
//                .accessTokenConverter(jwtTokenEnhancer());
        // ????????????????
        endpoints.exceptionTranslator(new CustomWebResponseExceptionTranslator());


        /** access_token??????
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
     * ??????????????????????????
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

        CustomClientCredentialsTokenEndpointFilter endpointFilter = new CustomClientCredentialsTokenEndpointFilter(security);
        endpointFilter.afterPropertiesSet();
        endpointFilter.setAuthenticationEntryPoint(authenticationEntryPoint());
        security.addTokenEndpointAuthenticationFilter(endpointFilter);

        security
                .authenticationEntryPoint(authenticationEntryPoint())
//                .allowFormAuthenticationForClients() //??????????????????????????
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    /**
     * client??????????????????jdbc????
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetails());
    }
}
