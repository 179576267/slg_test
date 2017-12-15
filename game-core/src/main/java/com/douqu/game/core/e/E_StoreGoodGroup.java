package com.douqu.game.core.e;


/**
 * Created by bean on 2017/8/1.
 */
public enum E_StoreGoodGroup implements BaseEnum {

    GROUP_DEFAULT(0,"默认不区分类型"),
    GOBLIN_STORE_GROUP_CARD(1,"哥布林商店的卡片和碎片"),
    GOBLIN_STORE_GROUP_ASSETS(2,"哥布林商店资源"),
    ARENA_STORE_GROUP_PROP(3,"竞技场的道具")

    ;

    int code;

    String msg;

    E_StoreGoodGroup(int code, String msg)
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
