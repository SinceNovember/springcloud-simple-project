package com.simple.admin.auth.controller;

import com.simple.admin.auth.handler.ReturnCode;
import com.simple.admin.core.entity.Response;
import com.simple.admin.core.entity.ResponseData;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/token")
public class AuthController {

    @Resource
    private TokenStore tokenStore;

    @Resource
    private  RedisTemplate<Object, String> redisTemplate;

    private static final String TOKEN_BLACKLIST_PREFIX = "token_";

    /**
     * 用户退出登录
     * @param authHeader 从请求头获取token
     */
    @DeleteMapping("/logout")
    public ResponseData logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authHeader){

        //获取token，去除前缀
        String token = authHeader.replace(OAuth2AccessToken.BEARER_TYPE,"").trim();

        // 解析Token
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);

        //token 已过期
        if(oAuth2AccessToken.isExpired()){
            return Response.fail(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        if(StringUtils.isBlank(oAuth2AccessToken.getValue())){
            //访问令牌不合法
            return Response.fail(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(oAuth2AccessToken);

        String userName = oAuth2Authentication.getName();

        //获取token唯一标识
        String jti = (String) oAuth2AccessToken.getAdditionalInformation().get("jti");

        //获取过期时间
        Date expiration = oAuth2AccessToken.getExpiration();
        long exp = expiration.getTime() / 1000;

        long currentTimeSeconds = System.currentTimeMillis() / 1000;

        //设置token过期时间
        redisTemplate.opsForValue().set(TOKEN_BLACKLIST_PREFIX + jti, userName, (exp - currentTimeSeconds), TimeUnit.SECONDS);

        return Response.ok("退出成功");
    }
}
