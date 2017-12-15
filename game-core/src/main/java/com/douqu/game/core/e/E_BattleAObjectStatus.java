package com.douqu.game.core.e;


/**
 * Created by bean on 2017/8/1.
 */
public enum E_BattleAObjectStatus implements BaseEnum {

    FREE(0,"空闲"),
    BATTLING(1,"战斗中"),
    GOING(2,"前进中"),
    CHASING(3,"追击中")
    ;

    int code;

    String msg;

    E_BattleAObjectStatus(int code, String msg)
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
