package com.chinalife.sell.service.impl;

import com.chinalife.sell.dataobject.ProductInfo;
import com.chinalife.sell.enums.ProductStatusEnum;
import com.chinalife.sell.repository.ProductInfoRepository;
import com.chinalife.sell.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl  implements ProductService {


    @Override
    public List<ProductInfo> findUpAll() {
        return repository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Autowired
    private ProductInfoRepository repository;
    @Override
    public ProductInfo findOne(String productId) {
        ProductInfo productInfo=new ProductInfo();
        productInfo.setProductId(productId);
        Example<ProductInfo>example=Example.of(productInfo);
        return repository.findOne(example).get();
    }


    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return repository.save(productInfo);
    }
}
