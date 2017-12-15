package com.douqu.game.core.e;


/**
 * Created by bean on 2017/8/1.
 */
public enum E_RechargeType implements BaseEnum {

    MOUTH_CARD(1,"月卡"),
    DIAMOND(2,"钻石")

    ;

    int code;

    String msg;

    E_RechargeType(int code, String msg)
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
