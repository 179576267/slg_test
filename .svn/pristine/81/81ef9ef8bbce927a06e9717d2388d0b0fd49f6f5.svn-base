package com.douqu.game.core.config.battle;

import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.util.LoadUtils;

/**
 * 星级检测
 * Created by bean on 2017/9/12.
 */
public class StarCheckConfig extends GameObject {

    /**
     * 获得的星星
     */
    public int star;

    /**
     * 类型
     * 1。战斗时间区间
     * 2。主将剩余血量%区间
     * 3。指定种族通关
     * 4。击杀敌军数量
     */
    public int type;

    public int[] values;

    @Override
    public void check()
    {
        if(values == null)
        {
            System.out.println("Load StarCheck values is null! id:" + id);
        }
        if(type == ConstantFactory.STAR_TYPE_BATTLE_TIME)
        {
            if(values.length != 2)
            {
                System.out.println("Load StarCheck values is error! id:" + id + " values:" + values);
            }
        }
        else if(type == ConstantFactory.STAR_TYPE_HP_RATE)
        {
            if(values.length != 2)
            {
                System.out.println("Load StarCheck values is error! id:" + id + " values:" + values);
            }
        }
        else if(type == ConstantFactory.STAR_TYPE_KILL_COUNT)
        {
            if(values.length != 1 && values[0] <= 0)
            {
                System.out.println("Load StarCheck values is error! id:" + id + " values:" + values);
            }
        }
        else if(type == ConstantFactory.STAR_TYPE_CAMP_ID)
        {
            if(values.length != 1 && values[0] <= 0)
            {
                System.out.println("Load StarCheck values is error! id:" + id + " values:" + values);
            }
        }
    }


    @Override
    public void setVariable(String key, String value)
    {
        if ("values".equals(key))
        {
            this.values = LoadUtils.loadIntArray(value);
            if(type == ConstantFactory.STAR_TYPE_BATTLE_TIME || type == ConstantFactory.STAR_TYPE_HP_RATE)
            {
                if(this.values.length != 2)
                {
                    System.out.println("StarCheck setVariable Error -> id:" + id + " value:" + value);
                }
            }
        }
        else
            super.setVariable(key, value);
    }


}
