package com.chinalife.sell.repository;

import com.chinalife.sell.dataobject.OrderMaster;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderMasterRepositoryTest {

    @Autowired
    private OrderMasterRepository repository;

    private  final  String OPENID="110110";

    @Test
    public void saveTest()
    {
        OrderMaster orderMaster=new OrderMaster();
        orderMaster.setOrderId("123456");
        orderMaster.setBuyerName("师兄");
        orderMaster.setBuyerPhone("123456789123");
        orderMaster.setBuyerAddress("幕课网");
        orderMaster.setBuyerOpenid(OPENID);
        orderMaster.setOrderAmount(new BigDecimal(2.5));
        OrderMaster result=repository.save(orderMaster);
        assertNotNull(result);

    }

    @Test
    void findByBuyerOpenid() {

        Pageable pageable=PageRequest.of(1,3);
        Page<OrderMaster>result=repository.findByBuyerOpenid(OPENID,pageable);
        assertNotEquals(0,result.getTotalElements());


    }
}