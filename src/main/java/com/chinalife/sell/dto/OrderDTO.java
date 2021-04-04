package com.chinalife.sell.dto;

import com.chinalife.sell.dataobject.OrderDetail;
import com.chinalife.sell.enums.OrderStatusEnum;
import com.chinalife.sell.enums.PayStatusEnum;
import com.chinalife.sell.serializer.Date2LongSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {
    private String orderId;
    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;
    private String buyerOpenid;
    private BigDecimal orderAmount;
    private Integer orderStatus= OrderStatusEnum.NEW.getCode();
    private Integer payStatus= PayStatusEnum.WAIT.getCode();
    @JsonSerialize(using= Date2LongSerializer.class)
    private Date createTime;
    @JsonSerialize(using = Date2LongSerializer.class)
    private Date updateTime;
    private List<OrderDetail>orderDetailList;
}
