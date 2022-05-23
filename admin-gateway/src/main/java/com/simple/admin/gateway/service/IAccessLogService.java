package com.simple.admin.gateway.service;

import com.simple.admin.gateway.entity.GatewayLog;
import reactor.core.publisher.Mono;

public interface IAccessLogService {

    /**
     * ����AccessLog
     * @param gatewayLog ������Ӧ��־
     * @return ��Ӧ��־
     */
    Mono<GatewayLog> saveAccessLog(GatewayLog gatewayLog);

}
