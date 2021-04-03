package com.chinalife.sell.service.impl;

import com.chinalife.sell.dataobject.OrderDetail;
import com.chinalife.sell.dto.OrderDTO;
import com.chinalife.sell.enums.OrderStatusEnum;
import com.chinalife.sell.enums.PayStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class OrderServiceImplTest {
    @Autowired

    private OrderServiceImpl orderService;

    private  final  String BUYER_OPENID="110110";
    private  final  String  ORDER_ID="1617440820575921447";

    @Test
    void create() {

        OrderDTO orderDTO=new OrderDTO();
        orderDTO.setBuyerName("廖师兄");
        orderDTO.setBuyerAddress("幕课网");
        orderDTO.setBuyerPhone("123456789012");
        orderDTO.setBuyerOpenid(BUYER_OPENID);

        List<OrderDetail>list=new ArrayList<>();
        OrderDetail o1 = new OrderDetail();
        o1.setProductId("123456");
        o1.setProductQuantity(1);

        OrderDetail o2 = new OrderDetail();
        o2.setProductId("123457");
        o2.setProductQuantity(2);

        list.add(o1);
        list.add(o2);

        orderDTO.setOrderDetailList(list);
        OrderDTO result=orderService.create(orderDTO);

        log.info("【创建订单】result={}",result);

        assertNotNull(result);

    }

    @Test
    void findOne() {

        OrderDTO orderDTO=orderService.findOne(ORDER_ID);
        log.info("【创建订单】result={}",orderDTO);
        assertEquals(ORDER_ID,orderDTO.getOrderId());
    }

    @Test
    void findList() {
        Pageable pageable=PageRequest.of(0,2);
        Page<OrderDTO>orderDTOPage=orderService.findList(BUYER_OPENID,pageable);
        assertNotEquals(0,orderDTOPage.getTotalElements());


    }

    @Test
    void cancel() {

        OrderDTO orderDTO=orderService.findOne(ORDER_ID);
        OrderDTO result=orderService.cancel(orderDTO);
        assertEquals(OrderStatusEnum.CANCEL.getCode(),result.getOrderStatus());


    }

    @Test
    void finish() {

        OrderDTO orderDTO=orderService.findOne(ORDER_ID);
        OrderDTO result=orderService.finish(orderDTO);
        assertEquals(OrderStatusEnum.FINISHED.getCode(),result.getOrderStatus());
    }

    @Test
    void paid()
    {
        OrderDTO orderDTO=orderService.findOne(ORDER_ID);
        OrderDTO result=orderService.paid(orderDTO);
        assertEquals(PayStatusEnum.SUCCESS.getCode(),result.getPayStatus());
    }
}