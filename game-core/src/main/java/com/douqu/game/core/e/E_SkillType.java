package com.douqu.game.core.e;

/**
 * Created by bean on 2017/8/1.
 */
public enum E_SkillType implements BaseEnum {

    NONE(0,"无"),
    HP_ADD(1,"加血类型"),
    HP_SUB(2,"减血类型"),
    ATT_ADD(3,"属性增加类型"),
    ATT_SUB(4,"属性减少类型")

    ;

    int code;

    String msg;

    E_SkillType(int code, String msg)
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
