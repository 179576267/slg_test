package com.douqu.game.core.config.goods;

import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.entity.ext.BagInfo;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.util.LoadUtils;

import java.util.Arrays;

/**
 * @author: Gavin.
 * Description:
 * Date: 2017/11/1 0001 下午 7:28
 * Huan Yu Copyright (c) 2017 All Rights Reserved.
 */
public class AccessoryUpConfig extends GameObject {


    /**资源*/
    public GoodsData[] needProps;

    /**返还的道具，资源，装备*/
    public GoodsData[] restitution;

    @Override
    public void check() {
        if(needProps == null)
        {
            System.out.println("AccessoryUp check needProps is null -> id:" + id + " name:" + name);
        }

        if(restitution == null)
        {
            System.out.println("AccessoryUp check restitution is null -> id:" + id + " name:" + name);
        }
    }

    /**
     * 检查背包资源是否足够
     *
     * */
    public boolean checkEnoughProp(BagInfo bagInfo, int quality) {
        for (int i =0 ; i  < needProps.length; i++){
            //品质
            if(quality == needProps[i].type){
                return bagInfo.getPropCount(needProps[i].id) >= needProps[i].value;
            }
        }
        return false;
    }



    @Override
    public void setVariable(String key, String value) {
        if("needProps".equals(key)){
            needProps = LoadUtils.loadGoodDataToArray(key, value);
            for(GoodsData data : needProps)
            {
                data.checkProp(this.getClass(), key);
            }
        }
        else if("restitution".equals(key))
        {
            restitution = LoadUtils.loadGoodDataToArray(key, value);
            for(GoodsData data : restitution)
            {
                data.check(this.getClass(), key);
            }
        }else{
            super.setVariable(key, value);
        }
    }

    @Override
    public String toString() {
        return "AccessoryUp{" +
                ", needProps=" + needProps +
                ", restitution=" + Arrays.toString(restitution) +
                '}';
    }
}
