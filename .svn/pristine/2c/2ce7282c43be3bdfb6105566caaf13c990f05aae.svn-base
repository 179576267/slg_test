package com.douqu.game.core.config.goods;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.config.card.CardConfig;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.util.LoadUtils;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-10-17 10:37
 */
public class PropConfig extends Goods {

    /**
     * 道具类型
     * 1为消耗品、2为任务道具、4为卡片碎片、5士兵经验道具、6饰品经验道具
     */

    public int type;

    public int showType;

    /***
     * 当前已存放数量
     */
    public int count;

    public int effectValue;

    /**
     * 分解返回的英魂数量（当为装备的时候才会有）
     */
    public GoodsData[] soulData;

    /**
     * 碎片对应的卡牌ID
     */
    private CardConfig cardConfig;

    @Override
    public void copyTo(GameObject gameObject)
    {
        super.copyTo(gameObject);

        PropConfig prop = (PropConfig) gameObject;
        prop.type = type;
        prop.count = this.count;
        prop.effectValue = this.effectValue;
        prop.soulData = this.soulData;
        prop.cardConfig = this.cardConfig;
    }


    @Override
    public void check() {
        if (type == ConstantFactory.PROP_TYPE_BAG) {
            if (DataFactory.getInstance().getGameObject(DataFactory.DROP_GROUP_KEY, effectValue) == null) {
                System.out.println("PropConfig check effectValue is null -> id:" + id + " name:" + name);
            }
        }
    }

    @Override
    public void setVariable(String key, String value)
    {
        if("effectValue".equals(key))
        {
//            if(!StringUtils.isEmpty(value) && value.split(ConstantFactory.DIVISION).length > 0){
            soulData = new GoodsData[0];
            if(type == ConstantFactory.PROP_TYPE_EQUIP_SYN_EXP){
                soulData  = LoadUtils.loadGoodDataToArray(key, value);
                for(GoodsData data : soulData)
                {
                   data.check(PropConfig.class, key);
                }
            }else if(type == ConstantFactory.PROP_TYPE_BAG){
                effectValue = Integer.parseInt(value);
            }else {
                effectValue = Integer.parseInt(value);
            }

        }

        else
            super.setVariable(key, value);
    }






    public CardConfig getCardConfig() {
        return cardConfig;
    }

    public void setCardConfig(CardConfig cardConfig) {
        this.cardConfig = cardConfig;
    }

    @Override
    public String toString() {
        return "Prop{" +
                "type=" + type +
                ", count=" + count +
                "} " + super.toString();
    }
}
