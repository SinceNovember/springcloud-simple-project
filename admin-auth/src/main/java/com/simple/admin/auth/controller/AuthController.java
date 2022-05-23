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
     * �û��˳���¼
     * @param authHeader ������ͷ��ȡtoken
     */
    @DeleteMapping("/logout")
    public ResponseData logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String authHeader){

        //��ȡtoken��ȥ��ǰ׺
        String token = authHeader.replace(OAuth2AccessToken.BEARER_TYPE,"").trim();

        // ����Token
        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);

        //token �ѹ���
        if(oAuth2AccessToken.isExpired()){
            return Response.fail(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        if(StringUtils.isBlank(oAuth2AccessToken.getValue())){
            //�������Ʋ��Ϸ�
            return Response.fail(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }

        OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(oAuth2AccessToken);

        String userName = oAuth2Authentication.getName();

        //��ȡtokenΨһ��ʶ
        String jti = (String) oAuth2AccessToken.getAdditionalInformation().get("jti");

        //��ȡ����ʱ��
        Date expiration = oAuth2AccessToken.getExpiration();
        long exp = expiration.getTime() / 1000;

        long currentTimeSeconds = System.currentTimeMillis() / 1000;

        //����token����ʱ��
        redisTemplate.opsForValue().set(TOKEN_BLACKLIST_PREFIX + jti, userName, (exp - currentTimeSeconds), TimeUnit.SECONDS);

        return Response.ok("�˳��ɹ�");
    }
}
