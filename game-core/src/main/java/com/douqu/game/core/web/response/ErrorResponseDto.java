package com.douqu.game.core.web.response;

import com.douqu.game.core.e.ReturnMessage;

/**
 * @author wangzhenfei
 *         2017-07-17 16:46
 */
public class ErrorResponseDto extends BaseResponseDto{
    public ErrorResponseDto(ReturnMessage message){
        super(message.getCode(), message.getMsg());
    }
}
