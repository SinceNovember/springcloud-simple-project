package com.simple.admin.order.controller;

import com.simple.admin.core.entity.Response;
import com.simple.admin.core.entity.ResponseData;
import com.simple.admin.order.api.IOrderService;
import com.simple.admin.order.api.entity.Order;
import com.simple.admin.order.feign.AccountFeign;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/v1/order")
public class OrderController {

    @Resource
    private IOrderService orderService;

    @Resource
    private AccountFeign accountFeign;

    @GetMapping
    public ResponseData listOrder() {
        return Response.ok(orderService.list());
    }

    @GetMapping("/account")
    public ResponseData listAccount() {
        return Response.ok(accountFeign.listUser());
    }

    @PostMapping
    public ResponseData addOrder(@RequestBody Order order) {
        orderService.addOrder(order);
        return Response.ok();
    }
}
