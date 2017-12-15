package com.douqu.game.core.config.goods;

import com.douqu.game.core.config.common.CommonLvUp;
import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.util.LoadUtils;

/**
 * @author: Gavin.
 * Description:
 * Date: 2017/11/1 0001 下午 6:55
 * Huan Yu Copyright (c) 2017 All Rights Reserved.
 */
public class AccessoryIntensifyConfig extends CommonLvUp {

    /**返还的道具，资源，装备*/
    public GoodsData[] restitution;

    @Override
    public void check() {
        if(restitution == null)
        {
            System.out.println("AccessoryIntensify check restitution is null -> id:" + id + " name:" + name);
        }

    }


    @Override
    public void setVariable(String key, String value) {


        if("restitution".equals(key))
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
        return "AccessoryIntensify{" +
                "restitution=" + restitution +
                '}';
    }
}
