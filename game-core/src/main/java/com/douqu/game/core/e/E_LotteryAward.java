package com.douqu.game.core.e;

/**
 * @author: Gavin.
 * Description:
 * Date: 2017/10/10 0010 下午 12:23
 * Huan Yu Copyright (c) 2017 All Rights Reserved.
 */
public enum E_LotteryAward implements BaseEnum {

    ASSET(1,"资源"),
    GOODS(2,"道具"),
    CARD(3,"卡片")
    ;



    int code;

    String msg;

    E_LotteryAward(int code, String msg)
    {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
