package com.chinalife.sell.service.impl;

import com.chinalife.sell.dataobject.ProductInfo;
import com.chinalife.sell.enums.ProductStatusEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceImplTest {

    @Autowired
    private  ProductServiceImpl productService;

    @Test
    void findUpAll() {

        List<ProductInfo>productInfoList=productService.findUpAll();
        assertNotEquals(0,productInfoList.size());
    }

    @Test
    void findAll() {
        Pageable pageable=PageRequest.of(0,2);
        Page<ProductInfo>productInfoPage=productService.findAll(pageable);
        System.out.println(productInfoPage.getTotalElements());

    }

    @Test
    void findOne() {

        ProductInfo productInfo=productService.findOne("123456");
        assertEquals("123456",productInfo.getProductId());
    }

    @Test
    void save() {
        ProductInfo productInfo=new ProductInfo();
        productInfo.setProductId("123457");
        productInfo.setProductName("皮皮虾");
        productInfo.setProductPrice(new BigDecimal(3.2));
        productInfo.setProductStock(100);
        productInfo.setProductDescription("很好喝的粥");
        productInfo.setProductIcon("http://xxxxx.jpg");
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());
        productInfo.setCategoryType(2);

        productInfo=productService.save(productInfo);
        assertNotNull(productInfo);


    }
}