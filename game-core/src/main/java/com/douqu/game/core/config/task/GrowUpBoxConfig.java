package com.douqu.game.core.config.task;

import com.douqu.game.core.config.AssetConfig;
import com.douqu.game.core.config.card.CardConfig;
import com.douqu.game.core.config.goods.PropConfig;
import com.douqu.game.core.config.common.CommonData;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.util.Utils;

/**
 * @author: Gavin.
 * Description:
 * Date: 2017/10/24 0024 下午 7:05
 * Huan Yu Copyright (c) 2017 All Rights Reserved.
 */
public class GrowUpBoxConfig extends GameObject {
    /**成长值*/
    public int growthValue;
    /**道具掉落配置*/
    public CommonData[] props;
    /**资源掉落配置*/
    public CommonData[] assets;
    /**卡片掉落配置*/
    public CommonData[] cards;


    @Override
    public void check() {
        if(props == null)
        {
            System.out.println("GrowUpBox check props is null -> id:" + id + " name:" + name);
        }

        if(assets == null)
        {
            System.out.println("GrowUpBox check assets is null -> id:" + id + " name:" + name);
        }

        if(cards == null)
        {
            System.out.println("GrowUpBox check cards is null -> id:" + id + " name:" + name);
        }
    }

    @Override
    public void setVariable(String key, String value)
    {
        if("props".equals(key))
        {
            if(Utils.isErrorValue(value)){
                System.out.println("Load GrowUpBox props is null,Please check!");
                props = new CommonData[0];
            }else{
                String[] strs = value.split(ConstantFactory.DIVISION);
                props = new CommonData[strs.length];
                for(int i = 0; i < strs.length; i++)
                {
                    props[i] = new CommonData(strs[i]);
                    PropConfig prop = DataFactory.getInstance().getGameObject(DataFactory.PROP_KEY, props[i].id);
                    if(prop == null)
                    {
                        System.out.println("Load GrowUpBox Error: props is null! id=" + props[i].id);
                    }
                }
            }

        }else if("assets".equals(key)){
            if(Utils.isErrorValue(value)){
                System.out.println("Load GrowUpBox assets is null,Please check!");
                assets = new CommonData[0];
            }else{
                String[] strs = value.split(ConstantFactory.DIVISION);
                assets = new CommonData[strs.length];
                for(int i = 0; i < strs.length; i++)
                {
                    assets[i] = new CommonData(strs[i]);
                    AssetConfig assetConfig = DataFactory.getInstance().getGameObject(DataFactory.ASSET_KEY, assets[i].id);
                    if(assetConfig == null)
                    {
                        System.out.println("Load GrowUpBox Error: assets is null! id=" + assets[i].id);
                    }
                }
            }
        }else if("cards".equals(key)){
            if(Utils.isErrorValue(value)){
                System.out.println("Load GrowUpBox cards is null,Please check!");
                cards = new CommonData[0];
            }else{
                String[] strs = value.split(ConstantFactory.DIVISION);
                cards = new CommonData[strs.length];
                for(int i = 0; i < strs.length; i++)
                {
                    cards[i] = new CommonData(strs[i]);
                    CardConfig cardConfig = DataFactory.getInstance().getGameObject(DataFactory.CARD_KEY, cards[i].id);
                    if(cardConfig == null)
                    {
                        System.out.println("Load GrowUpBox Error: cards is null! id=" + cards[i].id);
                    }
                }
            }
        }
        else
            super.setVariable(key, value);

    }

}
