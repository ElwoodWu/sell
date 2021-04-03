package com.chinalife.sell.exception;

import com.chinalife.sell.enums.ResultEnum;

public class SellException extends  RuntimeException{
    private  Integer code;

    public SellException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code=resultEnum.getCode();
    }
}
