package com.douqu.game.battle.entity.aobject;

/**
 * BUFF的属性值
 * @Author: Bean
 * @Description:
 * @Date: 2017-11-28 11:28
 */
public class BattleAtt {

    /**
     * 基础值
     */
    public int base;

    /**
     * 加成值
     */
    public int plus;


    public BattleAtt(int base)
    {
        this.base = base;
    }

    public void addPlus(int value)
    {
        if(value == 0)
            return;

        plus += value;
    }

    public int getAtt()
    {
        return base + plus;
    }


    @Override
    public String toString() {
        return "{" +
                "base=" + base +
                ", plus=" + plus +
                '}';
    }
}
