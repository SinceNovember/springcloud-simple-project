package com.simple.admin.gateway.filter;

import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.shaded.json.JSONUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.text.ParseException;

/**
 * ���һ��������ͷtoken��ȫ�ֹ���������ֹ�ͻ��ƹ����������������
 */
@Component
@Order(0)
public class GatewayRequestFilter implements GlobalFilter {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    public static final String GATEWAY_TOKEN_HEADER = "gateway_token_header";

    public static final String GATEWAY_TOKEN_VALUE = "gateway_token_value";

    private static final String TOKEN_BLACKLIST_PREFIX = "token_";


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String headerToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtil.isNotEmpty(headerToken)) {
            // �Ƿ��ں�����
            if (isBlack(headerToken)) {
                throw new HttpServerErrorException(HttpStatus.FORBIDDEN, "�������ѹ��ڣ������»�ȡ����");
            }
        }


        byte[] token = Base64Utils.encode((GATEWAY_TOKEN_VALUE).getBytes());
        String[] headerValues = {new String(token)};
        ServerHttpRequest build = exchange.getRequest()
                .mutate()
                .header(GATEWAY_TOKEN_HEADER, headerValues)
                .build();

        ServerWebExchange newExchange = exchange.mutate().request(build).build();
        return chain.filter(newExchange);
    }

    /**
     * ͨ��redis�ж�token�Ƿ�Ϊ������
     *
     * @param headerToken ����ͷ
     * @return boolean
     */
    private boolean isBlack(String headerToken) {
        //todo  �Ƴ�����oauth2��ش��룬��ʱʹ�� OAuth2AccessToken.BEARER_TYPE ����
        String token = headerToken.replace(OAuth2AccessToken.BEARER_TYPE, StringUtil.EMPTY).trim();

        //����token
        JWSObject jwsObject = null;
        try {
            jwsObject = JWSObject.parse(token);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String payload = jwsObject.getPayload().toString();
        JSONObject jsonObject = JSON.parseObject(payload);

        // JWTΨһ��ʶ
        String jti = jsonObject.getString("jti");
        return redisTemplate.hasKey(TOKEN_BLACKLIST_PREFIX + jti);
    }
}
