package com.chinalife.sell.controller;

import com.chinalife.sell.VO.ProductInfoVO;
import com.chinalife.sell.VO.ProductVO;
import com.chinalife.sell.VO.ResultVO;
import com.chinalife.sell.dataobject.ProductCategory;
import com.chinalife.sell.dataobject.ProductInfo;
import com.chinalife.sell.service.CategoryService;
import com.chinalife.sell.service.ProductService;

import com.chinalife.sell.utils.ResultVOUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/buyer/product")
public class BuyerProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;





    @GetMapping("/list")

    public ResultVO list()
    {
        List<ProductInfo>productInfoList=productService.findUpAll();

        List<Integer>categoryTypeList=productInfoList.stream()
                .map(e->e.getCategoryType())
                .collect(Collectors.toList());


        List<ProductCategory>productCategoryList=categoryService.findCategoryTypeIn(categoryTypeList);

        List<ProductVO> productVOList = new ArrayList<>();
        for (ProductCategory productCategory: productCategoryList) {
            ProductVO productVO = new ProductVO();
            productVO.setCategoryType(productCategory.getCategoryType());
            productVO.setCategoryName(productCategory.getCategoryName());

            List<ProductInfoVO> productInfoVOList = new ArrayList<>();
            for (ProductInfo productInfo: productInfoList) {
                if (productInfo.getCategoryType().equals(productCategory.getCategoryType())) {
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    BeanUtils.copyProperties(productInfo, productInfoVO);
                    productInfoVOList.add(productInfoVO);
                }
            }
            productVO.setProductInfoVOList(productInfoVOList);
            productVOList.add(productVO);
        }




        return ResultVOUtil.success(productVOList);
    }

}
