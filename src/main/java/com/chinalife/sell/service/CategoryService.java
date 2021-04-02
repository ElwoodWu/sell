package com.chinalife.sell.service;

import com.chinalife.sell.dataobject.ProductCategory;

import java.util.List;

public interface CategoryService {

    ProductCategory findOne(Integer categoryId);
    List<ProductCategory>findAll();
    List<ProductCategory>findCategoryTypeIn(List<Integer>categoryTypeList);
    ProductCategory save(ProductCategory productCategory);
}
