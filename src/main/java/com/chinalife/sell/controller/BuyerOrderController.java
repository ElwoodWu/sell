package com.chinalife.sell.controller;

import com.chinalife.sell.VO.ResultVO;
import com.chinalife.sell.converter.OrderForm2OrderDTO;
import com.chinalife.sell.dto.OrderDTO;
import com.chinalife.sell.enums.ResultEnum;
import com.chinalife.sell.exception.SellException;
import com.chinalife.sell.form.OrderForm;
import com.chinalife.sell.service.BuyerService;
import com.chinalife.sell.service.OrderService;
import com.chinalife.sell.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BuyerService buyerService;

    @RequestMapping("/create")
public ResultVO<Map<String,String>>create(@Valid OrderForm orderForm, BindingResult bindingResult)

{


    if(bindingResult.hasErrors())
    {
        log.error("参数不正确,orderForm={}",orderForm);
        throw new SellException(ResultEnum.PARAM_ERROR.getCode(),bindingResult.getFieldError().getDefaultMessage());
    }

    OrderDTO orderDTO= OrderForm2OrderDTO.convert(orderForm);

    if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList()))
    {
        log.error("创建订单购物车不能为空");
        throw  new SellException(ResultEnum.CART_EMPTY);
    }
   OrderDTO createResult= orderService.create(orderDTO);
    Map<String,String>map=new HashMap<>();
    map.put("orderId",createResult.getOrderId());
    return ResultVOUtil.success(map);



}

@GetMapping("/list")

    public ResultVO<List<OrderDTO>>list(@RequestParam("openid")String openid,@RequestParam(value="page",defaultValue = "0")Integer page,@RequestParam
        (value="size",defaultValue = "10")Integer size)
{
    if(openid==null||openid.equals(""))
    {
        log.error("openid为空");
        throw new SellException(ResultEnum.PARAM_ERROR);
    }

    Pageable pageable= PageRequest.of(page,size);

    Page<OrderDTO>orderDTOPage=orderService.findList(openid,pageable);


    return ResultVOUtil.success(orderDTOPage.getContent());
}

@GetMapping("/detail")
    public ResultVO<OrderDTO>detail(@RequestParam("openid")String openid,@RequestParam("orderId")String orderId)
{


   OrderDTO orderDTO= buyerService.findOrderOne(openid,orderId);
   return ResultVOUtil.success(orderDTO);
}

@PostMapping("/cancel")
    public ResultVO<OrderDTO>cancel(@RequestParam("openid")String openid,@RequestParam("orderId")String orderId)
{
buyerService.cancelOrder(openid,orderId);
    return ResultVOUtil.success();
}


}
