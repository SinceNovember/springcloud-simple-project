package com.simple.admin.order.feign;

import com.simple.admin.core.entity.ResponseData;
import com.simple.admin.product.entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("admin-product")
public interface ProductFeign {

    @PostMapping("/v1/product")
    ResponseData addProduct(@RequestBody Product product);

}
