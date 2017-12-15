package com.douqu.game.core.e;

import com.douqu.game.core.config.AttributeConfig;

import java.io.Serializable;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-11-08 10:23
 */
public enum E_Attribute implements BaseEnum, Serializable {

    ATK(),
    DEF(),
    HP(),
    CRI(),
    REC(),
    ADD(),
    EXD(),
    HIT(),
    EVA(),
    POWER(),
    WISDOM(),
    AGILITY(),
    MOVE_SPEED(),
    ATTACK_SPEED()

    ;

    int code;

    String msg;

    String remark;

    private static final long serialVersionUID = 1L;

    E_Attribute()
    {
    }

//    public void init(int code, String msg)
//    {
//        this.code = code;
//        this.msg = msg;
//    }

    public void init(AttributeConfig attributeConfig)
    {
        this.code = attributeConfig.id;
        this.msg = attributeConfig.name;
        this.remark = attributeConfig.remark;
    }

    public static E_Attribute forNumber(int id)
    {
        for(E_Attribute attribute : values())
        {
            if(attribute.code == id)
                return attribute;
        }
        return null;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "E_Attribute{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", remark='" + remark + '\'' +
                "} " ;
    }
}
