package com.douqu.game.core.config.store;

import com.douqu.game.core.config.common.CommonData;
import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.protobuf.SGCommonProto;

/**
 * @author wangzhenfei
 *         2017-10-28 17:15
 */
public class Store extends GameObject{

    public int storeType;
    /**
     *  商品
     */
    public GoodsData goods;
    /**
     * 价格
     */
    public CommonData price;
    /**
     * 刷新概率
     */
    public int rate;



    @Override
    public void check()
    {
        if(goods == null)
        {
            System.out.println("Store check goods is null -> id:" + id + " name:" + name);
        }

        if(price == null)
        {
            System.out.println("Store check price is null -> id:" + id + " name:" + name);
        }

    }


    @Override
    public void setVariable(String key, String value)
    {
        if("goods".equals(key))
        {
            goods =  new GoodsData(value);
            int type = 0;
            if(goods.type == SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_PROPS_VALUE){
                type = DataFactory.PROP_KEY;
            }else if(goods.type == SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_ASSETS_VALUE){
                type = DataFactory.ASSET_KEY;
            }else if(goods.type == SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_CARDS_VALUE){
                type = DataFactory.CARD_KEY;
            }

            if(DataFactory.getInstance().getGameObject(type, goods.id) == null)
            {
                System.out.println("GoblinStore setVariable goods Error -> type : " + goods.type + " id:" + goods.id + " num:" + goods.value);
            }
        }else if("price".equals(key))
        {
            price =  new CommonData(value);
            if(DataFactory.getInstance().getGameObject(DataFactory.ASSET_KEY, price.id) == null)
            {
                System.out.println("GoblinStore setVariable price Error -> type : " + goods.type + " id:" + goods.id + " num:" + goods.value);
            }
        }

        else
            super.setVariable(key, value);
    }

    @Override
    public String toString() {
        return "Store{" +
                "goods=" + goods +
                ", price=" + price +
                "} " + super.toString();
    }
}
