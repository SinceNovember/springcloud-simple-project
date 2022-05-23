package com.simple.admin.security.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("admin.security")
public class SecurityProperties {

    /**
     * 是否只能通过网关获取资源
     * 默认为True
     */
    private Boolean onlyFetchByGateway = Boolean.TRUE;

    public Boolean getOnlyFetchByGateway() {
        return onlyFetchByGateway;
    }

    public void setOnlyFetchByGateway(Boolean onlyFetchByGateway) {
        this.onlyFetchByGateway = onlyFetchByGateway;
    }
}
