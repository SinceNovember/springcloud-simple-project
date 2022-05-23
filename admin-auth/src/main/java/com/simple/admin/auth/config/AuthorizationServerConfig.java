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
 * ��Ȩ/��֤����������
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    // ��֤������
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    /**
     * access_token �洢�� ->access_token�ķ�ʽ
     * ����洢�����ݿ⣬��ҿ��Խ���Լ���ҵ�񳡾����ǽ�access_token�������ݿ⻹��redis
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
     * TokenEnhancer�����࣬����������JWT���������ֵ��OAuth�����֤��Ϣ֮�����ת����
     */
    @Bean
    public JwtAccessTokenConverter jwtTokenEnhancer() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // ���öԳ�ǩ��
        converter.setSigningKey("javadaily");
        return converter;
    }


    /**
     * �����ݿ��ȡclientDetails�������
     * ��InMemoryClientDetailsService �� JdbcClientDetailsService ���ַ�ʽѡ��
     */
    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }

    /**
     * ע���������ʵ����
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
        //����token��Ч�ڣ�Ĭ��12Сʱ���˴��޸�Ϊ6Сʱ   21600
        tokenServices.setAccessTokenValiditySeconds(60 * 60 * 6);
        //����refresh_token����Ч�ڣ�Ĭ��30�죬�˴��޸�Ϊ7��
        tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 7);
        return tokenServices;
    }

    /**
     * ��֤������Endpoints����
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //�����Ҫʹ��refresh_tokenģʽ����Ҫע��userDetailService

        //jwt�ķ�ʽ
        endpoints.authenticationManager(this.authenticationManager)
                .userDetailsService(userDetailsService)
                //ע���Զ����tokenService�������ʹ���Զ����tokenService��ô����Ҫ��tokenService��������Ƶ�����
                .tokenServices(tokenServices());
//                .tokenStore(tokenStore())
//                .accessTokenConverter(jwtTokenEnhancer());
        // �Զ����쳣ת����
        endpoints.exceptionTranslator(new CustomWebResponseExceptionTranslator());


        /** access_token�ķ�ʽ
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
     * ��֤��������ؽӿ�Ȩ�޹���
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

        CustomClientCredentialsTokenEndpointFilter endpointFilter = new CustomClientCredentialsTokenEndpointFilter(security);
        endpointFilter.afterPropertiesSet();
        endpointFilter.setAuthenticationEntryPoint(authenticationEntryPoint());
        security.addTokenEndpointAuthenticationFilter(endpointFilter);

        security
                .authenticationEntryPoint(authenticationEntryPoint())
//                .allowFormAuthenticationForClients() //���ʹ�ñ���֤����Ҫ����
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    /**
     * client�洢��ʽ���˴�ʹ��jdbc�洢
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetails());
    }
}
