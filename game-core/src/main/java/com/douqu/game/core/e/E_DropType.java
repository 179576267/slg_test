package com.douqu.game.core.e;


/**
 * Created by bean on 2017/8/1.
 */
public enum E_DropType implements BaseEnum {

    RANDOM(1,"随机库(可能会掉落多种物品)"),
    ONLY_ONE(2,"权重库(只会掉落一种物品)")


    ;

    int code;

    String msg;

    E_DropType(int code, String msg)
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
