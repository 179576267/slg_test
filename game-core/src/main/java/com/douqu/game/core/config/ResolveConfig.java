package com.douqu.game.core.config;

import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.util.LoadUtils;

/**
 * @Author: wangzhenfei
 * @Description:
 * @Date: 2017-11-21 18:30
 */
public class ResolveConfig extends GameObject {

    public GoodsData[] goods;

    public int type;

    public int quality;


    @Override
    public void check()
    {
        if(goods == null)
        {
            System.out.println("ResolveData check goods is null -> id:" + id + " name:" + name);
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
                    data.check(ResolveConfig.class, key);
                }
            }

        }

        else
            super.setVariable(key, value);
    }


}
