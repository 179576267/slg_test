package com.douqu.game.core.web.response;


import com.douqu.game.core.e.ReturnMessage;

/**
 * @author: Bean
 * Description:
 * Date: 2017/2/6 Time: 10:21
 * Huan Yu Copyright (c) 2016 All Rights Reserved.
 */
public class BaseResponseDto<T>
{

    private String code;

    private String message;

    private T data;

    public BaseResponseDto(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResponseDto(ReturnMessage returnMessage)
    {
        this.code = returnMessage.getCode();
        this.message = returnMessage.getMsg();
    }

    public BaseResponseDto(ReturnMessage returnMessage, T data)
    {
        this.code = returnMessage.getCode();
        this.message = returnMessage.getMsg();
        this.data = data;
    }


    public BaseResponseDto() {

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseResponseDto{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
