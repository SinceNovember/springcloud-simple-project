package com.simple.admin.product;

import com.baomidou.mybatisplus.extension.service.IService;
import com.simple.admin.product.entity.Product;

public interface IProductService extends IService<Product> {

    boolean addProduct(Product product);
}
