package com.douqu.game.core.config.card;

import com.douqu.game.core.config.common.CommonLvUp;
import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.util.LoadUtils;

/**
 * Created by bean on 2017/7/21.
 */
public class CardLvConfig extends CommonLvUp {

    /**返还的道具，资源，装备*/
    public GoodsData[] restitution;

    @Override
    public void check() {
        if(restitution == null)
        {
            System.out.println("CardLv check restitution is null -> id:" + id + " name:" + name);
        }
    }

    @Override
    public void setVariable(String key, String value)
    {
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


}
