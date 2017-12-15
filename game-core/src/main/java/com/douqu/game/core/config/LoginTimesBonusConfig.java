package com.douqu.game.core.config;

import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.util.LoadUtils;

/**
 * @author wangzhenfei
 *         2017-12-14 15:13
 */
public class LoginTimesBonusConfig extends GameObject{
    /**奖励**/
    public GoodsData[] reward;

    @Override
    public void check()
    {
        super.check();
        if(reward == null)
        {
            System.out.println("LoginTimesBonusConfig check reward is null -> id:" + id + " name:" + name);
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
                data.check(LoginTimesBonusConfig.class, key);
            }
        }

        else
            super.setVariable(key, value);
    }

}
