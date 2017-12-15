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
 *         2017-12-04 11:46
 *         vip特权配置
 */
public class VipConfig extends GameObject {

    /**Vip经验**/
    public int needRecharge;

    /**祭祀免费次数**/
    public CommonData[] altarFreeTimes;
    public Map<Integer, Integer>  altarFreeTimesMap = new HashMap<>();

    /**祭祀收费次数**/
    public CommonData[] altarPurchaseMax;
    public Map<Integer, Integer>  altarPurchaseMaxMap = new HashMap<>();

    /**购买竞技次数**/
    public int buyArenaMax;
    /**卡组额外卡牌数量**/
    public int additionalCard;
    /**Vip礼包ID**/
    public int gift;
    /**显示原价**/
    public int originalPrice;
    /**实际价格**/
    public int discountPrice;

    @Override
    public void check()
    {
        super.check();
        if(altarFreeTimes == null)
        {
            System.out.println("VipConfig check altarFreeTimes is null -> id:" + id + " name:" + name);
        }

        if(altarPurchaseMax == null)
        {
            System.out.println("VipConfig check altarPurchaseMax is null -> id:" + id + " name:" + name);
        }

        if(DataFactory.getInstance().getGameObject(DataFactory.DROP_GROUP_KEY, gift) == null){
            System.out.println("VipConfig check gift drop group is null -> gift:" + gift + " name:" + name);
        }
    }


    @Override
    public void setVariable(String key, String value)
    {
        if("altarFreeTimes".equals(key))
        {
            altarFreeTimes = LoadUtils.loadDataToArray(key, value);
            for(CommonData data : altarFreeTimes)
            {
                if(DataFactory.getInstance().getGameObject(DataFactory.ALTAR_KEY, data.id) == null){
                    System.out.println("VipConfig check altarFreeTimes is null -> id:" + id + " name:" + name);
                }
                altarFreeTimesMap.put(data.id, data.value);
            }
        }

        else  if("altarPurchaseMax".equals(key))
        {
            altarPurchaseMax = LoadUtils.loadDataToArray(key, value);
            for(CommonData data : altarPurchaseMax)
            {
                if(DataFactory.getInstance().getGameObject(DataFactory.ALTAR_KEY, data.id) == null){
                    System.out.println("VipConfig check altarPurchaseMax is null -> id:" + id + " name:" + name);
                }
                altarPurchaseMaxMap.put(data.id, data.value);

            }
        }
        else
            super.setVariable(key, value);
    }


    @Override
    public String toString() {
        return "VipConfig{" +
                ", needRecharge=" + needRecharge +
                ", altarFreeTimes=" + Arrays.toString(altarFreeTimes) +
                ", altarPurchaseMax=" + Arrays.toString(altarPurchaseMax) +
                ", buyArenaMax=" + buyArenaMax +
                ", additionalCard=" + additionalCard +
                ", gift=" + gift +
                ", originalPrice=" + originalPrice +
                ", discountPrice=" + discountPrice +
                "} " + super.toString();
    }
}
