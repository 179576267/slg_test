package com.douqu.game.core.e;


/**
 * Created by wangzhenfei on 2017/8/1.
 */
public enum E_ExchangeRewardType implements BaseEnum {

    OFFICIAL_RANK(1,"官阶战"),
    ARENA(2,"竞技场")

    ;

    int code;

    String msg;

    E_ExchangeRewardType(int code, String msg)
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
