package com.simple.admin.gateway.service;

import com.simple.admin.gateway.entity.GatewayLog;
import reactor.core.publisher.Mono;

public interface IAccessLogService {

    /**
     * 保存AccessLog
     * @param gatewayLog 请求响应日志
     * @return 响应日志
     */
    Mono<GatewayLog> saveAccessLog(GatewayLog gatewayLog);

}
