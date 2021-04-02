package com.chinalife.sell.repository;

import com.chinalife.sell.dataobject.ProductCategory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;


import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ProductCategoryRepositoryTest {

    @Autowired
    private ProductCategoryRepository repository;

    @Test

    public void findOneTest()
    {
      ProductCategory productCategory=new ProductCategory("",0);
      productCategory.setCategoryId(1);
        Example<ProductCategory>example=Example.of(productCategory);

        Optional<ProductCategory>productOptional=repository.findOne(example);

        if(productOptional.isPresent())
        {

            productCategory=productOptional.get();
            System.out.println(productCategory.getCategoryName());
        }
          else
        {
            System.out.println("exit");
        }
    }

    @Test
    @Transactional
    public void saveTest()
    {
        ProductCategory productCategory=new ProductCategory("女生最爱",3);
        productCategory=repository.save(productCategory);
        System.out.println(productCategory.getCategoryId());
    }


    @Test
    public void findByCategoryTypeIn()
    {
        List<Integer>list= Arrays.asList(2,3,4);
        List<ProductCategory>productCategoryList=repository.findByCategoryTypeIn(list);

        System.out.println(productCategoryList.size());


    }

}