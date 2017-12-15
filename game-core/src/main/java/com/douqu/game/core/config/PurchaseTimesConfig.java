package com.douqu.game.core.config;

import com.douqu.game.core.config.common.CommonData;
import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.util.LoadUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangzhenfei
 *         2017-12-04 14:38
 */
public class PurchaseTimesConfig extends GameObject{
    /** 祭祀次数购买价格 **/
    public CommonData[] altarBuyPrice;
    public Map<Integer, Integer> altarBuyPriceMap = new HashMap<>();

    /** 购买竞技次数价格 **/
    public GoodsData[] buyArenaMax;

    /** 每日签到补签价格 **/
    public GoodsData[] reissueDailySign;

    @Override
    public void check()
    {
        super.check();
        if(altarBuyPrice == null)
        {
            System.out.println("PurchaseTimesConfig check buyExpMax is null -> id:" + id + " name:" + name);
        }

        if(buyArenaMax == null)
        {
            System.out.println("PurchaseTimesConfig check buyArenaMax is null -> id:" + id + " name:" + name);
        }

        if(reissueDailySign == null)
        {
            System.out.println("PurchaseTimesConfig check reissueDailySign is null -> id:" + id + " name:" + name);
        }
    }

    @Override
    public void setVariable(String key, String value)
    {
        if("altarBuyPrice".equals(key))
        {
            altarBuyPrice = LoadUtils.loadDataToArray(key, value);
            for(CommonData data : altarBuyPrice)
            {
               if(DataFactory.getInstance().getGameObject(DataFactory.ALTAR_KEY, data.id) == null){
                   System.out.println("PurchaseTimesConfig check altarBuyPrice is null -> id:" + id + " name:" + name);
               }
                altarBuyPriceMap.put(data.id, data.value);
            }
        }



        else if("buyArenaMax".equals(key))
        {
            buyArenaMax = LoadUtils.loadGoodDataToArray(key, value);
            for(GoodsData data : buyArenaMax)
            {
                data.check(PurchaseTimesConfig.class, key);
            }
        }

        else if("reissueDailySign".equals(key))
        {
            reissueDailySign = LoadUtils.loadGoodDataToArray(key, value);
            for(GoodsData data : reissueDailySign)
            {
                data.check(PurchaseTimesConfig.class, key);
            }
        }


        else
            super.setVariable(key, value);
    }

    @Override
    public String toString() {
        return "PurchaseTimesConfig{" +
                "altarBuyPrice=" + Arrays.toString(altarBuyPrice) +
                ", altarBuyPriceMap=" + altarBuyPriceMap +
                ", buyArenaMax=" + Arrays.toString(buyArenaMax) +
                ", reissueDailySign=" + Arrays.toString(reissueDailySign) +
                "} " + super.toString();
    }
}
