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
 * 添加一个给请求头token的全局过滤器，防止客户绕过网关向服务发起请求
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
            // 是否在黑名单
            if (isBlack(headerToken)) {
                throw new HttpServerErrorException(HttpStatus.FORBIDDEN, "该令牌已过期，请重新获取令牌");
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
     * 通过redis判断token是否为黑名单
     *
     * @param headerToken 请求头
     * @return boolean
     */
    private boolean isBlack(String headerToken) {
        //todo  移除所有oauth2相关代码，暂时使用 OAuth2AccessToken.BEARER_TYPE 代替
        String token = headerToken.replace(OAuth2AccessToken.BEARER_TYPE, StringUtil.EMPTY).trim();

        //解析token
        JWSObject jwsObject = null;
        try {
            jwsObject = JWSObject.parse(token);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String payload = jwsObject.getPayload().toString();
        JSONObject jsonObject = JSON.parseObject(payload);

        // JWT唯一标识
        String jti = jsonObject.getString("jti");
        return redisTemplate.hasKey(TOKEN_BLACKLIST_PREFIX + jti);
    }
}
