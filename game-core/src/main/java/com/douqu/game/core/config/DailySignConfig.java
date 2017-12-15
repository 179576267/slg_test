package com.douqu.game.core.config;

import com.douqu.game.core.config.common.CommonData;
import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.util.LoadUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangzhenfei
 *         2017-12-04 14:38
 */
public class DailySignConfig extends GameObject{
    /** 签到奖励**/
    public GoodsData[] reward;

    /**双倍的最低vip等级**/
    public int doubleVipLevel;

    @Override
    public void check()
    {
        super.check();
        if(reward == null)
        {
            System.out.println("DailySignConfig check reward is null -> id:" + id + " name:" + name);
        }
    }

    @Override
    public void setVariable(String key, String value)
    {
         if("reward".equals(key))
        {
            reward = LoadUtils.loadGoodDataToArray(key, value);
            for(GoodsData data : reward)
            {
                data.check(DailySignConfig.class, key);
            }
        }

        else
            super.setVariable(key, value);
    }

}
