package com.chinalife.sell.service.impl;

import com.chinalife.sell.converter.OrderMaster2OrderDTOConverter;
import com.chinalife.sell.dataobject.OrderDetail;
import com.chinalife.sell.dataobject.OrderMaster;
import com.chinalife.sell.dataobject.ProductInfo;
import com.chinalife.sell.dto.CartDTO;
import com.chinalife.sell.dto.OrderDTO;
import com.chinalife.sell.enums.OrderStatusEnum;
import com.chinalife.sell.enums.PayStatusEnum;
import com.chinalife.sell.enums.ResultEnum;
import com.chinalife.sell.exception.SellException;
import com.chinalife.sell.repository.OrderDetailRepository;
import com.chinalife.sell.repository.OrderMasterRepository;
import com.chinalife.sell.service.OrderService;
import com.chinalife.sell.service.ProductService;
import com.chinalife.sell.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {

        String orderId=KeyUtil.getUniqueKey();

        BigDecimal orderAmount=new BigDecimal(BigInteger.ZERO);

        for(OrderDetail orderDetail:orderDTO.getOrderDetailList())
        {
        ProductInfo productInfo= productService.findOne(orderDetail.getProductId());
        if(productInfo==null)
        {
throw  new SellException(ResultEnum.PRODUCT_NOT_EXIST);

        }

        orderAmount=productInfo.getProductPrice().multiply(new BigDecimal(orderDetail.getProductQuantity()))
                .add(orderAmount);

        orderDetail.setDetailId(KeyUtil.getUniqueKey());
        orderDetail.setOrderId(orderId);
            BeanUtils.copyProperties(productInfo,orderDetail);
            orderDetailRepository.save(orderDetail);




        }

        OrderMaster orderMaster=new OrderMaster();
        BeanUtils.copyProperties(orderDTO,orderMaster);
        orderMaster.setOrderId(orderId);
        orderMaster.setOrderAmount(orderAmount);
orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
BeanUtils.copyProperties(orderMaster,orderDTO);
        orderMasterRepository.save(orderMaster);


        List<CartDTO>cartDTOList= orderDTO.getOrderDetailList().stream().map(e->new CartDTO(e.getProductId(),e.getProductQuantity()))
                .collect(Collectors.toList());

        productService.decreaseStock(cartDTOList);

        return orderDTO;




    }

    @Override
    public OrderDTO findOne(String orderId) {

        OrderMaster orderMaster=new OrderMaster();
        orderMaster.setOrderId(orderId);
        Example<OrderMaster> example=Example.of(orderMaster);
        orderMaster=orderMasterRepository.findOne(example).get();
        if(orderMaster==null)
        {
throw new SellException(ResultEnum.ORDER_NOT_EXIT);
        }
List<OrderDetail>orderDetailList=orderDetailRepository.findByOrderId(orderId);
        if(CollectionUtils.isEmpty(orderDetailList))
        {
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIT);
        }
        OrderDTO orderDTO=new OrderDTO();
        BeanUtils.copyProperties(orderMaster,orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);

        return orderDTO;
    }

    @Override
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster>orderMasterPage=orderMasterRepository.findByBuyerOpenid(buyerOpenid, pageable);
        List<OrderDTO>orderDTOList= OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());
       return new PageImpl<OrderDTO>(orderDTOList,pageable,orderMasterPage.getTotalElements());



    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {

        OrderMaster orderMaster=new OrderMaster();

        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode()))

        {
log.error("【取消订单】订单状态不正确, orderId={}, orderStatus={}",orderDTO.getOrderId(),orderDTO.getOrderStatus());
throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO,orderMaster);
        OrderMaster updateResult=orderMasterRepository.save(orderMaster);

        if(updateResult==null)
        {
            log.error("【取消订单】更新失败, orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);

        }

        if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList()))
        {
            log.error("【取消订单】订单中无商品详情, orderDTO={}",orderDTO);
            throw  new SellException(ResultEnum.ORDER_DETAIL_EMPTY);

        }
        List<CartDTO>cartDTOList=orderDTO.getOrderDetailList().stream()
                .map(e->new CartDTO(e.getProductId(),e.getProductQuantity()))
                .collect(Collectors.toList());

        productService.increaseStock(cartDTOList);



        if(orderDTO.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode()))
        {
            //TODO
        }

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {

        if(!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode()))
        {
            log.error("【完结订单】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("【完结订单】更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【订单支付完成】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        if(!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode()))
        {
            log.error("【订单支付完成】订单支付状态不正确, orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_PAY_STATUS_ERROR);
        }
        orderDTO.setPayStatus(PayStatusEnum.SUCCESS.getCode());
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        OrderMaster updateResult = orderMasterRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("【订单支付完成】更新失败, orderMaster={}", orderMaster);
            throw new SellException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        return orderDTO;

    }
}
