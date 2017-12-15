package com.douqu.game.core.config;

import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.util.LoadUtils;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-11-21 18:30
 */
public class StableData extends GameObject {

    public GoodsData[] goods;

    public int times;


    @Override
    public void check()
    {
        if(goods == null)
        {
            System.out.println("StableData check goods is null -> id:" + id + " name:" + name);
        }
    }

    @Override
    public void setVariable(String key, String value)
    {
        if("goods".equals(key))
        {
            if(!"0".equals(key)){
                goods = LoadUtils.loadGoodDataToArray(key, value);
                for(GoodsData data : goods)
                {
                    data.check(StableData.class, key);
                }
            }

        }

        else
            super.setVariable(key, value);
    }

    @Override
    public String toString() {
        return "StableData{" +
                "goods=" + goods +
                ", times=" + times +
                "} " + super.toString();
    }
}
