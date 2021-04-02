package com.chinalife.sell.service.impl;

import com.chinalife.sell.dataobject.ProductCategory;
import com.chinalife.sell.repository.ProductCategoryRepository;
import com.chinalife.sell.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService
{

    @Autowired
    private ProductCategoryRepository repository;
    @Override
    public ProductCategory findOne(Integer categoryId) {
        ProductCategory productCategory=new ProductCategory();
        productCategory.setCategoryId(categoryId);
        Example<ProductCategory>example=Example.of(productCategory);

        return repository.findOne(example).get();
    }

    @Override
    public List<ProductCategory> findAll() {
        return repository.findAll();
    }

    @Override
    public List<ProductCategory> findCategoryTypeIn(List<Integer> categoryTypeList) {
        return repository.findByCategoryTypeIn(categoryTypeList);
    }

    @Override
    public ProductCategory save(ProductCategory productCategory) {
        return repository.save(productCategory);
    }
}



