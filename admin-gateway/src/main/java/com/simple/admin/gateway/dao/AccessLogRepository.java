package com.simple.admin.gateway.dao;

import com.simple.admin.gateway.entity.GatewayLog;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessLogRepository extends ReactiveMongoRepository<GatewayLog, String> {
}
