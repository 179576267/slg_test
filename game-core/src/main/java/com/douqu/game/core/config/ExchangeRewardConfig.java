package com.douqu.game.core.config;

import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.config.common.CommonData;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.util.LoadUtils;

import java.util.Arrays;

/**
 * @author wangzhenfei
 *         2017-10-11 14:07
 *         奖励配置
 */
public class ExchangeRewardConfig extends GameObject {
    /**交互类型**/
    public int type;

    /** 消耗资源 */
    public CommonData[] needAssets;

    /** 最小排名 **/
    public int minRank;
    /** 奖励组 */
    public GoodsData[] reward;

    @Override
    public void check()
    {
       super.check();
        if(needAssets == null)
        {
            System.out.println("ArenaReward check needAssets is null -> id:" + id + " name:" + name);
        }

        if(reward == null)
        {
            System.out.println("ArenaReward check props is null -> id:" + id + " name:" + name);
        }
    }



    @Override
    public void copyTo(GameObject obj) {
        super.copyTo(obj);
        ExchangeRewardConfig rewardData = (ExchangeRewardConfig) obj;
        rewardData.needAssets = needAssets;
        rewardData.minRank = minRank;
        rewardData.reward  = reward;
    }

    @Override
    public void setVariable(String key, String value)
    {
        if("reward".equals(key))
        {
            reward = LoadUtils.loadGoodDataToArray(key, value);
            for(GoodsData data : reward)
            {
                int type = 0;
                if(data.type == SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_PROPS_VALUE){
                    type = DataFactory.PROP_KEY;
                }else if(data.type == SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_ASSETS_VALUE){
                    type = DataFactory.ASSET_KEY;
                }else if(data.type == SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_CARDS_VALUE){
                    type = DataFactory.CARD_KEY;
                }

                if(DataFactory.getInstance().getGameObject(type, data.id) == null)
                {
                    System.out.println("ExchangeReward setVariable Error -> type : " + data.type + " id:" + data.id + " num:" + data.value);
                }
            }
        }

        else if("needAssets".equals(key))
       {
           if("0".equals(value))
           {
               needAssets = new CommonData[0];
           }
           else
           {
               needAssets = LoadUtils.loadDataToArray(key, value);
               for(CommonData needAsset : needAssets)
               {
                   if(DataFactory.getInstance().getGameObject(DataFactory.ASSET_KEY, needAsset.id) == null)
                   {
                       System.out.println("ExchangeReward setVariable Error -> needAsset : " + value + " id:" + id);
                   }
                   if(needAsset.value <= 0)
                   {
                       System.out.println("ExchangeReward setVariable Error -> needAsset count error : " + value + " id:" + id + "  needAsset.id:" + needAsset.id);
                   }
               }
           }
       }

        else
            super.setVariable(key, value);
    }

    @Override
    public String toString() {
        return "ArenaReward{" +
                "minRank=" + minRank +
                ", needAssets=" + Arrays.toString(needAssets) +
                '}';
    }
}
