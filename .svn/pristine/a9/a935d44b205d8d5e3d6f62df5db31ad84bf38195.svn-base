package com.douqu.game.core.config;

import com.douqu.game.core.config.card.CardConfig;
import com.douqu.game.core.config.goods.PropConfig;
import com.douqu.game.core.config.common.CommonData;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.util.LoadUtils;
import com.douqu.game.core.util.Utils;

import java.util.Arrays;

/**
 * 初始数据
 * Created by bean on 2017/7/14.
 */
public class InitDataConfig extends GameObject {

    public String defaultPlayerHead;

    /** 初始水晶(水晶;最大水晶数量) */
    public String crystal;
    /** 水晶成长
     * 60;1000|180;500
     * 时间单位是毫秒，最后一组的时间是战斗一局的最长时间
     * 前60秒每秒增加1点，60-180秒每0.5秒增加1点
     * */
    public CommonData[] crystalGrow;

    /** 初始水晶数量 */
    public int curCrystal;
    /** 初始最大水晶数量 */
    public int maxCrystal;
    /**
     * 初始资源
     */
    public CommonData[] initAssets;
    /**
     * 初始道具
     */
    public CommonData[] initProps;
    /**
     * 初始兵种(这里的兵种全都初始成1级,其它没有初始化的全都是0级，需要碎片才能升到1级)
     */
    public int[] initCards;



    @Override
    public void setVariable(String key, String value)
    {
        switch (key)
        {
            case "initAssets":
                if(Utils.isErrorValue(value))
                {
                    System.out.println("Load InitData initAssets is null, Please check!");
                    initAssets = new CommonData[0];
                }
                else
                {
                    initAssets = LoadUtils.loadDataToArray(key, value);
                    for(CommonData data : initAssets)
                    {
                        AssetConfig assetConfig = DataFactory.getInstance().getGameObject(DataFactory.ASSET_KEY, data.id);
                        if(assetConfig == null)
                        {
                            System.out.println("Load InitData Error: Asset is null! id=" + data.id);
                        }
                    }
                }
                break;
            case "initProps":
                if(Utils.isErrorValue(value))
                {
                    System.out.println("Load InitData initProps is null, Please check!");
                    initProps = new CommonData[0];
                }
                else
                {
                    initProps = LoadUtils.loadDataToArray(key, value);
                    for(CommonData data : initProps)
                    {
                        PropConfig prop = DataFactory.getInstance().getGameObject(DataFactory.PROP_KEY, data.id);
                        if(prop == null)
                        {
                            System.out.println("Load InitData Error: Prop is null! id=" + data.id);
                        }
                    }
                }
                break;
            case "initCards":
                if(Utils.isErrorValue(value))
                {
                    System.out.println("Load InitData initCards is null, Please check!");
                    initCards = new int[0];
                }
                else
                {
                    initCards = LoadUtils.loadIntArray(key, value);
                    for (int id : initCards) {
                        CardConfig cardConfig = DataFactory.getInstance().getGameObject(DataFactory.CARD_KEY, id);
                        if (cardConfig == null) {
                            System.out.println("Load InitData Error: Card is null! id=" + id);
                        }
                    }
                }
                break;
            case "crystal":
                crystal = value;
                int[] ints = LoadUtils.loadIntArray(key, value);
                curCrystal = ints[0];
                maxCrystal = ints[1];
                break;
            case "crystalGrow":
                crystalGrow = LoadUtils.loadDataToArray(key, value);
                break;

//            case "initInstance":
//                initInstance = LoadUtils.loadDataToArray(key, value);
//                for(CommonData data : initInstance)
//                {
//                    Asset asset = DataFactory.getInstance().getGameObject(DataFactory.INSTANCE_KEY, data.id);
//                    if(asset == null)
//                    {
//                        System.out.println("Load InitData Error: Asset is null! id=" + data.id);
//                    }
//                }
//                break;
//            case "initLevel":
//                initLevel = LoadUtils.loadDataToArray(key, value);
//                for(CommonData data : initLevel)
//                {
//                    Asset asset = DataFactory.getInstance().getGameObject(DataFactory.LEVEL_KEY, data.id);
//                    if(asset == null)
//                    {
//                        System.out.println("Load InitData Error: Asset is null! id=" + data.id);
//                    }
//                }
//                break;
            default:
                super.setVariable(key,value);
                break;
        }

    }

    @Override
    public String toString() {
        return "InitData{" +
                "crystal='" + crystal + '\'' +
                ", crystalGrow=" + Arrays.toString(crystalGrow) +
                ", curCrystal=" + curCrystal +
                ", maxCrystal=" + maxCrystal +
                ", initAssets=" + Arrays.toString(initAssets) +
                ", initProps=" + Arrays.toString(initProps) +
                ", initCards=" + Arrays.toString(initCards) +
//                ", masterModel='" + masterModel + '\'' +
//                ", masterSkill=" + masterSkill +
                "} " + super.toString();
    }
}
