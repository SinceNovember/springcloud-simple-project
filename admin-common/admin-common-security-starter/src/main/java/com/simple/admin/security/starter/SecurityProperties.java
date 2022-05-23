package com.simple.admin.security.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("admin.security")
public class SecurityProperties {

    /**
     * �Ƿ�ֻ��ͨ�����ػ�ȡ��Դ
     * Ĭ��ΪTrue
     */
    private Boolean onlyFetchByGateway = Boolean.TRUE;

    public Boolean getOnlyFetchByGateway() {
        return onlyFetchByGateway;
    }

    public void setOnlyFetchByGateway(Boolean onlyFetchByGateway) {
        this.onlyFetchByGateway = onlyFetchByGateway;
    }
}
