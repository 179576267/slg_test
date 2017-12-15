package com.douqu.game.core.config;

import com.douqu.game.core.config.drop.DropGroupConfig;
import com.douqu.game.core.controller.PlayerController;
import com.douqu.game.core.config.common.CommonData;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.util.Utils;

/**
 * @author: Gavin.
 * Description:
 * Date: 2017/10/17 0017 下午 3:58
 * Huan Yu Copyright (c) 2017 All Rights Reserved.
 */
public class AltarConfig extends GameObject {

    /**类型*/
    public int type;

    public String icon;

    /**祭坛奖励数组
     * 最小等级;最大等级;掉落组ID|最小等级;最大等级;掉落组ID
     * */
    public AltarDrop[] altarDrops;
    /**
     * 消耗的资源
     1;0|1;2|1;5
     表示第一次消耗1资源0个
     第二次消耗1资源2个
     第三次消耗1资源5个
     * */
    public CommonData[] needAssets;
//    /**每天免费次数*/
//    public int everydayFreeCount;
//    /**每天最大次数*/
//    public int everydayMaxCount;

    /**开启等级*/
    public int openLevel;

    @Override
    public void check()
    {
        if(altarDrops == null)
        {
            System.out.println("Altar check altarDrops is null -> id:" + id + " name:" + name);
        }

        if(needAssets == null)
        {
            System.out.println("Altar check needAssets is null -> id:" + id + " name:" + name);
        }

    }



    @Override
    public void setVariable(String key, String value) {

        if("altarDrops".equals(key))
        {
            if(Utils.isErrorValue(value)){
                System.out.println("Load Altar altarDrops is null,Please check!");
                altarDrops = new AltarDrop[0];
            }else{
                String[] strs = value.split(ConstantFactory.DIVISION);
                altarDrops = new AltarDrop[strs.length];
                for(int i = 0; i < strs.length; i++)
                {
                    altarDrops[i] = new AltarDrop(strs[i]);
                    DropGroupConfig dropGroupConfig = DataFactory.getInstance().getGameObject(DataFactory.DROP_GROUP_KEY, altarDrops[i].dropId);
                    if(dropGroupConfig == null)
                    {
                        System.out.println("Load Altar Error: altarDrops is null! id=" + altarDrops[i].dropId);
                    }
                }
            }

        }else if("needAssets".equals(key)){
            if(Utils.isErrorValue(value)){
                System.out.println("Load Altar needAssets is null,Please check!");
                needAssets = new CommonData[0];
            }else{
                String[] strs = value.split(ConstantFactory.DIVISION);
                needAssets = new CommonData[strs.length];
                for(int i = 0; i < strs.length; i++)
                {
                    needAssets[i] = new CommonData(strs[i]);
                    AssetConfig assetConfig = DataFactory.getInstance().getGameObject(DataFactory.ASSET_KEY, needAssets[i].id);
                    if(assetConfig == null)
                    {
                        System.out.println("Load Altar Error: needAssets is null! id=" + needAssets[i].id);
                    }
                }
            }
        } else
            super.setVariable(key, value);
    }



    /**
     *根据用户的等级获取奖励区间
     * */
    public int getAltarDrop(PlayerController playerController){
        //获取用户的等级
        int lv = playerController.getPlayer().getLv();
        for(int i = 0 ; i < altarDrops.length; i++){
            if(lv <= altarDrops[i].maxGrade && lv >= altarDrops[i].minGrade){
                return altarDrops[i].dropId;
            }

        }
        return 0;
    }

    /***
     * 根据当前的次数获取对象
     * @param count
     * @return
     */
    public CommonData getDropCommonData(int count){
        return count >= needAssets.length ? needAssets[needAssets.length-1] : needAssets[count];
    }



}
