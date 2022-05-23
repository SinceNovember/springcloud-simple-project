package com.simple.admin.product.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simple.admin.product.IProductService;
import com.simple.admin.product.entity.Product;
import com.simple.admin.product.mapper.ProductMapper;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Override
    public boolean addProduct(Product product) {
        return save(product);
    }

}
