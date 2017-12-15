package com.douqu.game.core.config;

import com.douqu.game.core.entity.GameObject;

/**
 * @author wangzhenfei
 *         2017-12-04 14:32
 *         充值配置
 */
public class RechargeConfig extends GameObject{

    public String icon;

    /**1,月卡  2,钻石**/
    public int type;

    public int  rmb;

    /**立即获得**/
    public int  money;

    /**额外获得**/
    public int  extra;

    /**月卡天数**/
    public int  days;

    @Override
    public String toString() {
        return "RechargeConfig{" +
                "icon='" + icon + '\'' +
                ", type=" + type +
                ", rmb=" + rmb +
                ", money=" + money +
                ", extra=" + extra +
                ", days=" + days +
                "} " + super.toString();
    }
}
