package com.douqu.game.core.config.challenge;

import com.douqu.game.core.config.BattleInitConfig;
import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.util.LoadUtils;

import java.util.Arrays;

/**
 * @author wangzhenfei
 *         2017-11-28 16:03
 */
public class HeroTempleConfig extends BattleInitConfig{
    /**地图**/
    public String map;
    /**首次通过奖励**/
    public GoodsData[] firstPassReward;
    /**解下一锁关卡**/
    public int unlockNext;
    /**扫荡奖励**/
    public int sweepReward;

    public String avatar;

    public String modelName;

    public String model;

    @Override
    public void check()
    {
        super.check();

        if(firstPassReward == null)
        {
            System.out.println("HeroTempleConfig check firstPassReward is null -> id:" + id + " name:" + name);
        }
    }


    @Override
    public void setVariable(String key, String value) {
        if("firstPassReward".equals(key))
        {
            firstPassReward = LoadUtils.loadGoodDataToArray(key, value);
            for(GoodsData data : firstPassReward)
            {
                data.check(this.getClass(), key);
            }

        } else if("sweepReward".equals(key))
        {
            this.sweepReward = Integer.parseInt(value);
            if(sweepReward != 0 && DataFactory.getInstance().getGameObject(DataFactory.DROP_GROUP_KEY, sweepReward) == null)
            {
                System.out.println("HeroTempleConfig setVariable Error -> boxReward : " + value + " id:" + id);
            }
        }
        else
            super.setVariable(key, value);
    }


    @Override
    public String toString() {
        return "HeroTempleConfig{" +
                "map='" + map + '\'' +
                ", firstPassReward=" + Arrays.toString(firstPassReward) +
                ", unlockNext=" + unlockNext +
                ", sweepReward=" + sweepReward +
                "} " + super.toString();
    }
}
