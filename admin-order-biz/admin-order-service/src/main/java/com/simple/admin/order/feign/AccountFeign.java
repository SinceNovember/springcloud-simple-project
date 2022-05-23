package com.simple.admin.order.feign;

import com.simple.admin.account.api.entity.User;
import com.simple.admin.core.entity.ResponseData;
import com.simple.admin.order.feign.fallback.AccountFeignFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "admin-account", fallbackFactory = AccountFeignFallbackFactory.class)
public interface AccountFeign {

    @GetMapping("/v1/user")
    ResponseData listUser();

    @PostMapping("/v1/user")
    ResponseData addUser(@RequestBody User user);

}
