package com.douqu.game.core.config.challenge;


import com.douqu.game.core.config.BattleInitConfig;
import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.util.LoadUtils;

/**
 * @author wangzhenfei
 *         2017-10-19 14:50
 */
public class OfficialRankConfig extends BattleInitConfig {

    public String avatar;
    /**
     * 所属阵营
     */
    public int camp;
    /**
     * 最大容纳人数
     */
    public int maxPerson;
    /** 每日奖励组 */
    public GoodsData[] dailyReward;

    /** 首次奖励组 */
    public GoodsData[] firstReward;

    /** 通过奖励积分 */
    public GoodsData[] passReward;
    /**
     * 特权id
     */
    public int privilegeId;

    @Override
    public void check() {
        super.check();
    }

    @Override
    public void setVariable(String key, String value) {
        if("dailyReward".equals(key))
        {
            dailyReward = LoadUtils.loadGoodDataToArray(key, value);
            for(GoodsData data : dailyReward)
            {
                data.check(this.getClass(), key);
            }

        } else if ("firstReward".equals(key)) {
            firstReward = LoadUtils.loadGoodDataToArray(key, value);
            for(GoodsData data : firstReward)
            {
                data.check(this.getClass(), key);
            }
        }

        else if ("passReward".equals(key)) {
            passReward = LoadUtils.loadGoodDataToArray(key, value);
            for(GoodsData data : passReward)
            {
                data.check(this.getClass(), key);
            }
        }

        else
            super.setVariable(key, value);
    }

    @Override
    public String toString() {
        return "OfficialRackObject{" +
                "camp=" + camp +
                "} " + super.toString();
    }
}
