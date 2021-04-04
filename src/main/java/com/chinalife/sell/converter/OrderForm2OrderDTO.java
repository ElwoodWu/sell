package com.chinalife.sell.converter;

import com.chinalife.sell.dataobject.OrderDetail;
import com.chinalife.sell.dto.OrderDTO;
import com.chinalife.sell.enums.ResultEnum;
import com.chinalife.sell.exception.SellException;
import com.chinalife.sell.form.OrderForm;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class OrderForm2OrderDTO {

    public  static OrderDTO convert(OrderForm orderForm)
    {
        Gson gson=new Gson();
        OrderDTO orderDTO=new OrderDTO();

        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerPhone(orderForm.getPhone());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());

        List<OrderDetail>orderDetailList=new ArrayList<>();
       try {
          orderDetailList= gson.fromJson(orderForm.getItems(),new TypeToken<List<OrderDetail>>(){}.getType());
       }catch (Exception e)
       {
           log.error("对象转换出错,string={}",orderForm.getItems());
           throw new SellException(ResultEnum.PARAM_ERROR);
       }
        orderDTO.setOrderDetailList(orderDetailList);

       return orderDTO;

    }
}
