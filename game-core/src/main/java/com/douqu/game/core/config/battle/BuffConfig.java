package com.douqu.game.core.config.battle;

import com.douqu.game.core.e.E_Attribute;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.util.LoadUtils;

/**
 * Created by bean on 2017/9/1.
 */
public class BuffConfig extends GameObject {

    public String icon;

    /**
     * 目标类型
     * 1.跟随技能
     * 2.自己
     */
    public int targetType;

    /**
     * 类型
     * 1.持续加血
     * 2.持续伤害
     * 3.增加属性
     * 4.减少属性
     * 101.眩晕
     * 102.冰冻
     * 103.冲锋
     * 105.对某个职业增加伤害
     * 106.对某个职业免除伤害
     */
    public int effectType;

    /**
     * 作用范围
     * 1.单体
     * 2.圆;半径;作用方(敌,我,全体)
     */
    public int[] effectRange;

    /**
     * 持续时间(0表示永久)
     */
    public int effectTime;

    /**
     */
    public int effectValue;

    /**
     * 系数
     */
    public float coefficient;

    public String buffScript;

    public int effectCount;

    /**
     * 覆盖类型
     * 1.覆盖
     * 2.叠加
     */
    public int overlayType;

    /**
     * 叠加层数(叠加时间)
     */
    public int maxOverlayer;

    /**
     * 优先级
     */
    public int priority;

    /**
     * 参考方
     */
    public int referTarget;

    /**
     * 参考属性
     */
    public int referValue;

    @Override
    public void check()
    {
        if(effectRange == null || effectRange.length == 0)
        {
            System.out.println("Load Buff effectRange Error1 -> id:" + id + " name:" + name);
        }

        if(effectRange[0] == ConstantFactory.BUFF_RANGE_TYPE_CIRCLE && effectRange.length != 3)
        {
            System.out.println("Load Buff effectRange Error3 -> id:" + id + " name:" + name);
        }

        if(effectType == ConstantFactory.BUFF_TYPE_ADD_ATT || effectType == ConstantFactory.BUFF_TYPE_SUB_ATT)
        {
            if(E_Attribute.forNumber(referValue) == null)
            {
                System.out.println("Load Buff referValue Error -> id:" + id + " name:" + name);
            }
        }
        if(effectType == ConstantFactory.BUFF_TYPE_ADD_DAMAGE_PROFESSION || effectType == ConstantFactory.BUFF_TYPE_SUB_DAMAGE_PROFESSION)
        {
            if(DataFactory.getInstance().getGameObject(DataFactory.PROFESSION_KEY, referValue) == null)
            {
                if(referValue != ConstantFactory.PROFESSION_ALL)
                {
                    System.out.println("Load Buff referValue Error -> id:" + id + " name:" + name);
                }
            }
        }
    }

    public boolean isFollowSkill()
    {
        return targetType == ConstantFactory.BUFF_TARGET_TYPE_SKILL;
    }

    public boolean isToSelf()
    {
        return targetType == ConstantFactory.BUFF_TARGET_TYPE_SELF;
    }





    @Override
    public void setVariable(String key, String value)
    {
        if("effectRange".equals(key))
        {
            this.effectRange = LoadUtils.loadIntArray(key, value);
        }
        else
            super.setVariable(key, value);
    }

//    @Override
//    public String toString() {
//        return "Buff{" +
//                "effectType=" + effectType +
//                ", targetType=" + targetType +
//                "} " + super.toString();
//    }
}
