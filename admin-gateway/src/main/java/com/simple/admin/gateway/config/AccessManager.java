package com.simple.admin.gateway.config;

import com.alibaba.nacos.common.utils.ConcurrentHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Set;

/**
 * �����Ȩ������
 */
@Component
public class AccessManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    public static final Logger log = LoggerFactory.getLogger(AccessManager.class);

    private Set<String> permitAll = new ConcurrentHashSet<>();

    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public static final String ROLE_PREFIX = "ROLE_";


    public AccessManager() {
        permitAll.add("/");
        permitAll.add("/error");
        permitAll.add("/favicon.ico");
        permitAll.add("/**/v2/api-docs/**");
        permitAll.add("/**/swagger-resources/**");
        permitAll.add("/webjars/**");
        permitAll.add("/doc.html");
        permitAll.add("/swagger-ui.html");
        permitAll.add("/**/oauth/**");
        permitAll.add("/**/current/get");
        permitAll.add("/auth/token/logout");
    }

    /**
     * ʵ��Ȩ����֤�ж�
     */
    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authenticationMono, AuthorizationContext authorizationContext) {
        ServerWebExchange exchange = authorizationContext.getExchange();
        //������Դ
        String requestPath = exchange.getRequest().getURI().getPath();
        // �Ƿ�ֱ�ӷ���
        if (permitAll(requestPath)) {
            return Mono.just(new AuthorizationDecision(true));
        }

        return authenticationMono.map(auth -> {
            return new AuthorizationDecision (checkAuthorities(exchange, auth, requestPath));
        }).defaultIfEmpty(new AuthorizationDecision(false));

    }

    /**
     * У���Ƿ����ھ�̬��Դ
     *
     * @param requestPath ����·��
     * @return
     */
    private boolean permitAll(String requestPath) {
        return permitAll.stream()
                .filter(r -> antPathMatcher.match(r, requestPath)).findFirst().isPresent();
    }


    /**
     * Ȩ��У��
     * @param auth �û�Ȩ��
     * @param requestPath ����·��
     * @return
     */
    private boolean checkAuthorities(ServerWebExchange exchange, Authentication auth, String requestPath) {
        if (auth instanceof OAuth2Authentication) {
            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

            return authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .filter(item -> !item.startsWith(ROLE_PREFIX))
                    .anyMatch(permission -> antPathMatcher.match(permission, requestPath));
        }
        return false;

//        if (auth instanceof OAuth2Authentication) {
//            OAuth2Authentication authentication = (OAuth2Authentication) auth;
//            String clientId = authentication.getOAuth2Request().getClientId();
//            log.info("clientId is {}", clientId);
//        }
//
//        Object principal = auth.getPrincipal();
//        log.info("�û���Ϣ:{}", principal.toString());
//        return true;
    }
}