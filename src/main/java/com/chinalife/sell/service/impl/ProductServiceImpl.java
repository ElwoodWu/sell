package com.chinalife.sell.service.impl;

import com.chinalife.sell.dataobject.ProductInfo;
import com.chinalife.sell.dto.CartDTO;
import com.chinalife.sell.enums.ProductStatusEnum;
import com.chinalife.sell.enums.ResultEnum;
import com.chinalife.sell.exception.SellException;
import com.chinalife.sell.repository.ProductInfoRepository;
import com.chinalife.sell.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl  implements ProductService {

    @Override
    @Transactional
    public void increaseStock(List<CartDTO> cartDTOList) {

        for(CartDTO cartDTO:cartDTOList)
        {
            ProductInfo productInfo=new ProductInfo();
            productInfo.setProductId(cartDTO.getProductId());
            Example<ProductInfo> example=Example.of(productInfo);
            productInfo=repository.findOne(example).get();
            if(productInfo==null)
            {
                throw  new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            Integer result=productInfo.getProductStock()+cartDTO.getProductQuantity();
            productInfo.setProductStock(result);

            repository.save(productInfo);


        }

    }

    @Override
    @Transactional
    public void decreaseStock(List<CartDTO> cartDTOList) {

        for(CartDTO cartDTO:cartDTOList)
        {
            ProductInfo productInfo=new ProductInfo();
            productInfo.setProductId(cartDTO.getProductId());
              Example<ProductInfo> example=Example.of(productInfo);
            productInfo=repository.findOne(example).get();

            if(productInfo==null)
            {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }

            Integer result=productInfo.getProductStock()-cartDTO.getProductQuantity();
            if(result<0)
            {
throw  new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            }
            productInfo.setProductStock(result);
            repository.save(productInfo);

        }


    }

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
