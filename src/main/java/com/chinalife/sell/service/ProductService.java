package com.chinalife.sell.service;

import com.chinalife.sell.dataobject.ProductInfo;
import com.chinalife.sell.dto.CartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    ProductInfo findOne(String productId);



    List<ProductInfo>findUpAll();

    Page<ProductInfo> findAll(Pageable pageable);
    ProductInfo save(ProductInfo productInfo);

    void increaseStock(List<CartDTO>cartDTOList);

  void decreaseStock(List<CartDTO>cartDTOList);

}
