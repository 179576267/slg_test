package com.douqu.game.core.config.challenge;

import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.util.LoadUtils;

import java.util.Arrays;

/**
 * @author wangzhenfei
 *         2017-10-11 14:07
 *         竞技场奖励配置
 */
public class ArenaDailyRewardConfig extends GameObject {

    /** 最小排名 **/
    public int minRank;
    /** 最大排名 **/
    public int maxRank;
    /** 奖励组 */
    public GoodsData[] dailyReward;

    @Override
    public void check()
    {
        if(dailyReward == null)
        {
            System.out.println("ArenaReward check props is null -> id:" + id + " name:" + name);
        }

    }



    @Override
    public void copyTo(GameObject obj) {
        super.copyTo(obj);
        ArenaDailyRewardConfig rewardData = (ArenaDailyRewardConfig) obj;
        rewardData.minRank = minRank;
        rewardData.maxRank = maxRank;
        rewardData.dailyReward  = dailyReward;
    }

    @Override
    public void setVariable(String key, String value)
    {
        if("dailyReward".equals(key))
        {
            dailyReward = LoadUtils.loadGoodDataToArray(key, value);
            for(GoodsData data : dailyReward)
            {
               data.check(this.getClass(), key);
            }
        }

        else
            super.setVariable(key, value);
    }

    @Override
    public String toString() {
        return "ArenaReward{" +
                "minRank=" + minRank +
                ", maxRank=" + maxRank +
                ", dailyReward=" + Arrays.toString(dailyReward) +
                '}';
    }
}
