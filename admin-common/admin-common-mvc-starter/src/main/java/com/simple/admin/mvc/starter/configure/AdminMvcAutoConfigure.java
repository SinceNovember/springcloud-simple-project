package com.simple.admin.mvc.starter.configure;

import com.simple.admin.core.entity.ResponseData;
import com.simple.admin.core.entity.Response;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Configuration
public class AdminMvcAutoConfigure {

    @Bean
    public ResultResponseBodyAdvice resultResponseBodyAdvice() {
        return new ResultResponseBodyAdvice();
    }

    /**
     * 包装返回结果
     */
    @ControllerAdvice
    public static class ResultResponseBodyAdvice implements ResponseBodyAdvice<Object> {
        @Override
        public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
            return true;
        }

        @Override
        public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
            if (body instanceof ResponseData) {
                return body;
            }
            return Response.ok(body);
        }
    }
}
