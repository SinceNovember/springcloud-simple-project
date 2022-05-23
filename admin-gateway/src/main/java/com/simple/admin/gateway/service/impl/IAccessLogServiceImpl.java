package com.simple.admin.gateway.service.impl;

import com.simple.admin.gateway.dao.AccessLogRepository;
import com.simple.admin.gateway.entity.GatewayLog;
import com.simple.admin.gateway.service.IAccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class IAccessLogServiceImpl implements IAccessLogService {

    @Autowired
    private AccessLogRepository accessLogRepository;

    @Override
    public Mono<GatewayLog> saveAccessLog(GatewayLog gatewayLog) {
        return accessLogRepository.insert(gatewayLog);
    }
}
