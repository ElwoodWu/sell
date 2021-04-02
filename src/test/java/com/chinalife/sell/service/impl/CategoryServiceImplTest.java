package com.chinalife.sell.service.impl;

import com.chinalife.sell.dataobject.ProductCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryServiceImplTest {

    @Autowired
    private  CategoryServiceImpl categoryService;
    @Test
    void findOne() {

        ProductCategory productCategory=categoryService.findOne(1);
        assertEquals(1,productCategory.getCategoryId());
    }

    @Test
    void findAll() {

        List<ProductCategory>list=categoryService.findAll();
        assertNotEquals(0,list.size());
    }

    @Test
    void findCategoryTypeIn() {
        List<ProductCategory>productCategoryList=categoryService.findCategoryTypeIn(Arrays.asList(1,2,3));
        assertNotEquals(0,productCategoryList.size());



    }

    @Test
    void save() {
        ProductCategory productCategory=new ProductCategory("男生专享",10);
        productCategory=categoryService.save(productCategory);
        assertNotNull(productCategory);
    }
}