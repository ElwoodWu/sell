package com.chinalife.sell.service;

import com.chinalife.sell.dataobject.OrderMaster;
import com.chinalife.sell.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

   OrderDTO create(OrderDTO orderDTO);

   OrderDTO findOne(String orderId);

   Page<OrderDTO> findList(String buyerOpenid, Pageable pageable);

   OrderDTO cancel(OrderDTO orderDTO);

   OrderDTO  finish(OrderDTO orderDTO);

   OrderDTO paid(OrderDTO orderDTO);

}
