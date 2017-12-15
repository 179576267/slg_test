package com.douqu.game.core.config.task;

import com.douqu.game.core.config.AssetConfig;
import com.douqu.game.core.config.card.CardConfig;
import com.douqu.game.core.config.goods.PropConfig;
import com.douqu.game.core.config.common.CommonData;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.util.LoadUtils;
import com.douqu.game.core.util.Utils;

import java.util.Arrays;

/**
 * @author: Gavin.
 * Description:
 * Date: 2017/10/21 0021 下午 5:14
 * Huan Yu Copyright (c) 2017 All Rights Reserved.
 */
public class TaskConfig extends GameObject {

    public String icon;

    /**类型*/
    public int type;

    /**目标*/
    public int target;

    /**需求*/
    public int [] demand ;

    /**需求描述*/
    public String describe;

    /**任务接取等级*/
    public int takeGrade;

    /**前置任务*/
    public int beforeTask;

    /**后续任务*/
    public int afterTask;

    /**道具掉落*/
    public CommonData [] props;

    /**资源掉落*/
    public CommonData [] assets;

    /**卡片掉落配置*/
    public CommonData [] cards;

    /**跳转*/
    public String skip;

    /**成长值*/
    public int growthValue;

    /**活跃值*/
    public int activeValue;

    /****/
    public int showNum;


    @Override
    public void check() {
        if(props == null)
        {
            System.out.println("Task check props is null -> id:" + id + " name:" + name);
        }

        if(assets == null)
        {
            System.out.println("Task check assets is null -> id:" + id + " name:" + name);
        }

        if(cards == null)
        {
            System.out.println("Task check cards is null -> id:" + id + " name:" + name);
        }
        if(demand == null || demand.length == 0)
        {
            System.out.println("Load Task demand Error1 -> id:" + id + " name:" + name);
        }
    }





    @Override
    public void setVariable(String key, String value) {


        if("props".equals(key))
        {
            if(Utils.isErrorValue(value)){
//                System.out.println("Load Task props is null,Please check!");
                props = new CommonData[0];
            }else{
                String[] strs = value.split(ConstantFactory.DIVISION);

                props = new CommonData[strs.length];
                for(int i = 0; i < strs.length; i++)
                {
                    props[i] = new CommonData(strs[i]);
//                    CommonData commonData = DataFactory.getInstance().getGameObject(DataFactory.PROP_KEY, props[i].id);
                    PropConfig prop =   DataFactory.getInstance().getGameObject(DataFactory.PROP_KEY, props[i].id);
                    if(prop == null)
                    {
                        System.out.println("Load Task Error: props is null! id=" + props[i].id);
                    }
                }
            }

        }

        else if("assets".equals(key)){

            if(Utils.isErrorValue(value)){
//                System.out.println("Load Altar needAssets is null,Please check!");
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
                        System.out.println("Load Altar Error: needAssets is null! id=" + assets[i].id);
                    }
                }
            }
        }
       else if("cards".equals(key)){

            if(Utils.isErrorValue(value)){
//                System.out.println("Load Task cards is null,Please check!");
                cards = new CommonData[0];
            }else{
                String[] strs = value.split(ConstantFactory.DIVISION);
                cards = new CommonData[strs.length];
                for(int i = 0; i < strs.length; i++)
                {
                    cards[i] = new CommonData(strs[i]);
//                    CommonData commonData = DataFactory.getInstance().getGameObject(DataFactory.CARD_KEY, cards[i].id);
//                    Asset asset = DataFactory.getInstance().getGameObject(DataFactory.ASSET_KEY, assets[i].id);
                    CardConfig cardConfig =  DataFactory.getInstance().getGameObject(DataFactory.CARD_KEY, cards[i].id);
                    if(cardConfig == null)
                    {
                        System.out.println("Load Task Error: cards is null! id=" + assets[i].id);
                    }
                }
            }
        }else if("demand".equals(key))
        {
            if(Utils.isNullOrEmpty(value))
            {
                System.out.println("Load Task demand Error -> id:" + id + " value:" + value);
            }
            else
            {
                demand = LoadUtils.loadIntArray(key, value);
            }
        }



        else
            super.setVariable(key, value);
    }

    @Override
    public String toString() {
        return "Task{" +
                "type=" + type +
                ", target=" + target +
                ", demand=" + demand +
                ", describe='" + describe + '\'' +
                ", takeGrade=" + takeGrade +
                ", beforeTask=" + beforeTask +
                ", afterTask=" + afterTask +
                ", props=" + Arrays.toString(props) +
                ", assets=" + Arrays.toString(assets) +
                ", cards=" + Arrays.toString(cards) +
                ", skip='" + skip + '\'' +
                ", growthValue=" + growthValue +
                ", activeValue=" + activeValue +
                '}';
    }
}
