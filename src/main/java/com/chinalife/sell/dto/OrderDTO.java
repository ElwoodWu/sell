package com.chinalife.sell.dto;

import com.chinalife.sell.dataobject.OrderDetail;
import com.chinalife.sell.enums.OrderStatusEnum;
import com.chinalife.sell.enums.PayStatusEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderDTO {
    private String orderId;
    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;
    private String buyerOpenid;
    private BigDecimal orderAmount;
    private Integer orderStatus= OrderStatusEnum.NEW.getCode();
    private Integer payStatus= PayStatusEnum.WAIT.getCode();
    private Date createTime;
    private Date updateTime;
    private List<OrderDetail>orderDetailList;
}
