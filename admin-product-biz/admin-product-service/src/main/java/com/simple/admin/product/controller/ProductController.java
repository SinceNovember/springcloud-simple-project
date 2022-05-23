package com.simple.admin.product.controller;

import com.simple.admin.core.entity.ResponseData;
import com.simple.admin.core.entity.Response;
import com.simple.admin.product.IProductService;
import com.simple.admin.product.entity.Product;
import com.simple.admin.product.feign.AccountFeign;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/v1/product")
public class ProductController {

    @Resource
    private AccountFeign accountFeign;

    @Resource
    private IProductService productService;

    @GetMapping
    public ResponseData listProduct() {
        System.out.println("µ÷ÓÃÔ¶³ÌaccountFeign");
        return Response.ok(accountFeign.listUser());
    }

    @GetMapping("/{userId}")
    public ResponseData getProduct(@PathVariable("userId") Integer userId) {
        return Response.ok(accountFeign.getUserById(userId));
    }

    @PostMapping
    public ResponseData addProduct(@RequestBody Product product) {
        productService.addProduct(product);
        return Response.ok();
    }
}
