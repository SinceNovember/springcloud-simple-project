package com.simple.admin.order.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simple.admin.account.api.entity.User;
import com.simple.admin.order.api.IOrderService;
import com.simple.admin.order.api.entity.Order;
import com.simple.admin.order.feign.AccountFeign;
import com.simple.admin.order.feign.ProductFeign;
import com.simple.admin.order.mapper.OrderMapper;
import com.simple.admin.product.entity.Product;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    public static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Resource
    private AccountFeign accountFeign;

    @Resource
    private ProductFeign productFeign;

    @Override
    @GlobalTransactional
    public boolean addOrder(Order order) {
        log.info("准备添加订单");

        log.info("添加用户");
        User user = new User();
        user.setId(4);
        user.setNickname("ddddd");
        user.setUsername("aaa");
        user.setPassword("55");
        user.setCreateTime(new Date());
        accountFeign.addUser(user);
        log.info("添加商品");
        Product product = new Product();
        product.setId(5);
        product.setProductName("11");
        product.setPrice(200);
        product.setTotal(1000);
        productFeign.addProduct(product);

        log.info("添加订单");
        return save(order);
//        throw new RuntimeException("有个错误了啊");
    }
}
