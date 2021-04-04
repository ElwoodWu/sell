package com.chinalife.sell.service.impl;

import com.chinalife.sell.dto.OrderDTO;
import com.chinalife.sell.enums.ResultEnum;
import com.chinalife.sell.exception.SellException;
import com.chinalife.sell.service.BuyerService;
import com.chinalife.sell.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BuyerServiceImpl implements BuyerService {
    @Autowired
    private OrderService orderService;

    @Override
    public OrderDTO findOrderOne(String openid, String orderId) {
    return  checkOrderOwner(openid,orderId);
    }

    @Override
    public OrderDTO cancelOrder(String openid, String orderId) {
        OrderDTO orderDTO=checkOrderOwner(openid,orderId);
        if(orderDTO==null)
        {
            log.error("查不到该订单");
            throw new SellException(ResultEnum.ORDER_NOT_EXIT);
        }
        return orderService.cancel(orderDTO);
    }

    private  OrderDTO checkOrderOwner(String openid, String orderId)
    {
        OrderDTO orderDTO=orderService.findOne(orderId);
        if(orderDTO==null)
        {
            return null;
        }
        if(!orderDTO.getBuyerOpenid().equalsIgnoreCase(openid))
        {
            log.error("订单openid 不一致");
            throw new SellException(ResultEnum.ORDER_OWNER_ERROR);
        }
        return orderDTO;
    }
}
