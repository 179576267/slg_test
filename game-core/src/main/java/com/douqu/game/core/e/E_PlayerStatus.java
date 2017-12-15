package com.douqu.game.core.e;

/**
 * Created by bean on 2017/8/17.
 */
public enum  E_PlayerStatus implements BaseEnum {

    FREE(1,"空闲"),
    MATCHING(2,"匹配中"),
    MATCH_WAIT_BATTLE(3,"匹配成功等待创建战斗"),
    BATTLE_WAIT_START(4,"战斗创建成功等待开始"),
    BATTLING(5,"战斗中"),
    BATTLE_OFFLINE(6,"战斗中离线")
    ;

    int code;

    String msg;

    E_PlayerStatus(int code, String msg)
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
