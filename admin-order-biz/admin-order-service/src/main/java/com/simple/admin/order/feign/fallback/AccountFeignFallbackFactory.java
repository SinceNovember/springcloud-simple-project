package com.simple.admin.order.feign.fallback;

import com.simple.admin.account.api.entity.User;
import com.simple.admin.core.entity.Response;
import com.simple.admin.core.entity.ResponseData;
import com.simple.admin.order.feign.AccountFeign;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class AccountFeignFallbackFactory implements FallbackFactory<AccountFeign> {

    @Override
    public AccountFeign create(Throwable throwable) {
        AccountFeignFallback fallback = new AccountFeignFallback();
        fallback.setCause(throwable);
        return fallback;
    }


    public class AccountFeignFallback implements AccountFeign {

        private Throwable cause;

        @Override
        public ResponseData listUser() {
            return Response.fail("接口熔断");
        }

        @Override
        public ResponseData addUser(User user) {
            return Response.fail("接口熔断");
        }

        public Throwable getCause() {
            return cause;
        }

        public void setCause(Throwable cause) {
            this.cause = cause;
        }
    }
}
