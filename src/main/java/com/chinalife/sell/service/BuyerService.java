package com.chinalife.sell.service;

import com.chinalife.sell.dto.OrderDTO;

public interface BuyerService {

    OrderDTO findOrderOne(String openid,String orderId);

    OrderDTO cancelOrder(String openid,String orderId);
}
