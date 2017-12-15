package com.douqu.game.core.config.challenge;

import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.util.LoadUtils;

import java.util.Arrays;

/**
 * @author wangzhenfei
 *         2017-10-11 14:07
 *         竞技场积分奖励配置
 */
public class OfficialIntegralConfig extends GameObject {

    /** 积分 **/
    public int integral;
    /** 奖励组 */
    public GoodsData[] reward;

    @Override
    public void check()
    {

        if(reward == null)
        {
            System.out.println("OfficialIntegral check reward is null -> id:" + id + " name:" + name);
        }
    }



    @Override
    public void copyTo(GameObject obj) {
        super.copyTo(obj);
        OfficialIntegralConfig reward = (OfficialIntegralConfig) obj;
        reward.integral = integral;

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
                    System.out.println("OfficialIntegral setVariable Error -> type : " + data.type + " id:" + data.id + " num:" + data.value);
                }
            }
        }

        else
            super.setVariable(key, value);
    }

    @Override
    public String toString() {
        return "OfficialIntegral{" +
                "integral=" + integral +
                ", reward=" + Arrays.toString(reward) +
                "} " + super.toString();
    }
}
