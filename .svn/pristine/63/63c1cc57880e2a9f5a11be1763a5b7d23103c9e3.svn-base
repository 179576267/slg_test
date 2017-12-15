package com.douqu.game.core.config.goods;

import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.entity.ext.BagInfo;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.util.LoadUtils;

import java.util.Arrays;

/**
 * @author: Gavin.
 * Description: 配置文件中的装备强化
 * Date: 2017/11/1 0001 下午 7:39
 * Huan Yu Copyright (c) 2017 All Rights Reserved.
 */
public class EquipIntensifyConfig extends GameObject {

    /**强化所需的资源
     * goodsType里面的type是品质
     * id是资源ID
     * */
    public GoodsData [] needAssets;
    /**返还
     * goodsType里面的type是品质
     * id是资源ID
     * */
    public GoodsData [] restitution;

    @Override
    public void check() {
        if(needAssets == null)
        {
            System.out.println("EquipIntensify check needAssets is null -> id:" + id + " name:" + name);
        }

        if(restitution == null)
        {
            System.out.println("EquipIntensify check restitution is null -> id:" + id + " name:" + name);
        }
    }

    public boolean checkEnough(BagInfo bagInfo, int type)
    {
        for(GoodsData goodsData : needAssets) {
            if(goodsData.type == type)
            {
                if(bagInfo.getAsset(goodsData.id) < goodsData.value)
                    return false;
                else
                    break;
            }
        }
        return true;
    }


    @Override
    public void setVariable(String key, String value) {
        if("needAssets".equals(key)) {
            needAssets = LoadUtils.loadGoodDataToArray(key, value);
            for(GoodsData data : needAssets)
            {
                data.checkAsset(this.getClass(), key);
            }
        }else if("restitution".equals(key)) {
            restitution = LoadUtils.loadGoodDataToArray(key, value);
            for(GoodsData data : restitution)
            {
                data.checkAsset(this.getClass(), key);
            }
        }else{
            super.setVariable(key, value);
        }
    }

    @Override
    public String toString() {
        return "EquipIntensify{" +
                "needAssets=" + Arrays.toString(needAssets) +
                ", restitution=" + Arrays.toString(restitution) +
                '}';
    }
}
