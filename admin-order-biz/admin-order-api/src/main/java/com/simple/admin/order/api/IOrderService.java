package com.simple.admin.order.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.simple.admin.order.api.entity.Order;

public interface IOrderService extends IService<Order> {

    boolean addOrder(Order order);
}
