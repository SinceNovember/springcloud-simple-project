package com.simple.admin.security.starter.interceptor;

import com.alibaba.fastjson.JSON;
import com.simple.admin.core.entity.Response;
import com.simple.admin.core.entity.ResponseData;
import com.simple.admin.security.starter.SecurityProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class ServerProtectInterceptor implements HandlerInterceptor {

    public static final String GATEWAY_TOKEN_HEADER = "gateway_token_header";

    public static final String GATEWAY_TOKEN_VALUE = "gateway_token_value";

    private SecurityProperties properties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!properties.getOnlyFetchByGateway()) {
            return true;
        }

        String token = request.getHeader(GATEWAY_TOKEN_HEADER);

        String gatewayToken = new String(Base64Utils.encode(GATEWAY_TOKEN_VALUE.getBytes()));

        if (Objects.equals(gatewayToken, token)) {
            return true;
        } else {
            ResponseData responseData = Response.fail(HttpStatus.FORBIDDEN, "please use gateway");
            response.getWriter().write(JSON.toJSONString(responseData));
            return false;
        }
    }

    public void setProperties(SecurityProperties properties) {
        this.properties = properties;
    }
}
