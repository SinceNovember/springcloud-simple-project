package com.simple.admin.product.feign;

import com.simple.admin.core.entity.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "admin-account")
public interface AccountFeign {

    @GetMapping("/v1/user")
    ResponseData listUser();

    @GetMapping("/v1/user/{userId}")
    ResponseData getUserById(@PathVariable(value = "userId") Integer userId);
}
