package com.douqu.game.core.config.card;

import com.douqu.game.core.config.common.CommonData;
import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.entity.ext.BagInfo;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.util.LoadUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Gavin.
 * Description:
 * Date: 2017/11/1 0001 下午 7:11
 * Huan Yu Copyright (c) 2017 All Rights Reserved.
 */
public class CardStarConfig extends GameObject{

    /**需要的资源*/
//    public GoodsData [] needAssets;
    public Map<Integer, CommonData> needAssets;
    /**需要的兵种碎片数量
     * 这里的ID是不固定的，根据不同的卡牌检测不同的碎片
     * */
    public GoodsData [] needDebris;
    /**需要的突破令数量
     * 这里的ID是不固定的，根据不同的卡牌检测不同的碎片
     * */
    public Map<Integer, CommonData> needProps;
    /**返还的资源*/
    public GoodsData [] restitution;
    /**重生返回的碎片数量**/
    public int backDebris;

    public CardStarConfig() {
        needAssets = new ConcurrentHashMap<>();
        needProps = new ConcurrentHashMap<>();
    }

    @Override
    public void check()
    {
        if(needAssets == null)
        {
            System.out.println("CardStar check needAssets is null -> id:" + id + " name:" + name);
        }
        if(needDebris == null)
        {
            System.out.println("CardStar check needDebris is null -> id:" + id + " name:" + name);
        }
        if(needProps == null)
        {
            System.out.println("CardStar check needProps is null -> id:" + id + " name:" + name);
        }
        if(restitution == null)
        {
            System.out.println("CardStar check restitution is null -> id:" + id + " name:" + name);
        }

//        Card card = DataFactory.getInstance().getGameObject(DataFactory.CARD_KEY, id);
//        for(GoodsData goodsData : needDebris)
//        {
//            goodsData.id = card.lvUpProp;
//        }
    }


    public boolean checkAsset(BagInfo bagInfo, int quality){

        CommonData need = needAssets.get(quality);
        if(need == null)
            return true;

        return bagInfo.getAsset(need.id) >= need.value;
    }


    public boolean checkProps(BagInfo bagInfo ,int quality){
        CommonData need  = needProps.get(quality);
        if(need == null)
            return true;
        return bagInfo.getPropCount(need.id) >= need.value;
    }

    public boolean checkDebris(BagInfo bagInfo, int quality, int propId){
        for (int i =0 ; i  < needDebris.length; i++){
            //品质
            if(quality == needDebris[i].type){
                return bagInfo.getPropCount(propId) >= needDebris[i].value;
            }
        }
        return false;
    }


    @Override
    public void setVariable(String key, String value) {


        if("needAssets".equals(key)){
            String[] strs = value.split(ConstantFactory.DIVISION);
            GoodsData temp = null;
            for(int i = 0; i < strs.length; i++)
            {
                temp = new GoodsData(strs[i]);
                needAssets.put(temp.type, new CommonData(temp.id, temp.value));

                if(DataFactory.getInstance().getGameObject(DataFactory.ASSET_KEY, temp.id) == null)
                {
                    System.out.println("CardStar setVariable needAssets Error -> type : " + temp.type + " id:" + temp.id + " num:" + temp.value);
                }
            }
        }

        else if("needDebris".equals(key))
        {
            String[] strs = value.split(ConstantFactory.DIVISION);
            needDebris = new GoodsData[strs.length];
            String[] temp = null;
            for(int i = 0; i < strs.length; i++)
            {
                temp = strs[i].split(ConstantFactory.SEMICOLON);
                needDebris[i] = new GoodsData(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
            }
        }

        else if("needProps".equals(key))
        {
            String[] strs = value.split(ConstantFactory.DIVISION);
            GoodsData temp = null;
            for(int i = 0; i < strs.length; i++)
            {
                temp = new GoodsData(strs[i]);
                needProps.put(temp.type, new CommonData(temp.id, temp.value));
                if(DataFactory.getInstance().getGameObject(DataFactory.PROP_KEY, temp.id) == null)
                {
                    System.out.println("CardStar setVariable needProps Error -> type : " + temp.type + " id:" + temp.id + " num:" + temp.value);
                }
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


}
