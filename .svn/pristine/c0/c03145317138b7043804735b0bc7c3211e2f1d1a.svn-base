package com.douqu.game.core.config;

import com.douqu.game.core.config.card.CardConfig;
import com.douqu.game.core.config.goods.PropConfig;
import com.douqu.game.core.config.drop.DropObject;
import com.douqu.game.core.config.common.CommonData;
import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.util.Utils;

import java.util.*;

/**
 * @author: Gavin.
 * Description: 十连抽
 * Date: 2017/8/18 0018 下午 3:47
 * Huan Yu Copyright (c) 2017 All Rights Reserved.
 */
public class LotteryConfig extends GameObject {

    public int type;   //类型
    public int level;  //级别
    public int freeCount;   //免费次数
    public int cdTime;     //cd时间单位（秒）
    public DropObject[] props; //道具掉落配置
    public DropObject[] assets; //资源掉落配置
    public DropObject[] cards; //卡片掉落配置
    public GoodsData reward; //必送奖励
    public CommonData needAsset; //资源消耗
    public int nextLevelCount; //到下一级的次数
    public int nextLevelPond;  //下一级奖池

    /**
     * 总的权重
     */
    private int totalOdds;


    @Override
    public void check()
    {
        if(props == null)
        {
            System.out.println("LotteryObject check props is null -> id:" + id + " name:" + name);
        }

        if(assets == null)
        {
            System.out.println("LotteryObject check assets is null -> id:" + id + " name:" + name);
        }

        if(cards == null)
        {
            System.out.println("LotteryObject check cards is null -> id:" + id + " name:" + name);
        }

        if(needAsset == null)
        {
            System.out.println("LotteryObject check needAsset is null -> id:" + id + " name:" + name);
        }
        if(reward == null)
        {
            System.out.println("LotteryObject check reward is null -> id:" + id + " name:" + name);
        }

        totalOdds = 0;
        for(int i = 0 ; i < cards.length; i++){
            totalOdds += cards[i].odds;
        }
        for(int i = 0 ; i < assets.length; i++){
            totalOdds += assets[i].odds;
        }
        for(int i = 0 ; i < props.length; i++){
            totalOdds += props[i].odds;
        }
    }


//    /***
//     * 获取道具
//     * @return
//     */
//    public DropObject getProp(){
//        //道具掉落类型如果是随机
//            int c = 0;
//            for(int i = 0 ; i < props.length; i++){
//                c += props[i].odds;
//            }
//            int r = new Random(c).nextInt();
//            int temp = 0;
//            for (int j = 0 ; j < props.length; j++){
//                temp += props[j].odds;
//                if(r <= temp){
//                    return props[j];
//                }
//            }
//        return null;
//    }
//
//
//    /***
//     * 获取资源
//     * @return
//     */
//    public DropObject getAsset(){
//        //道具掉落类型如果是随机
//        int c = 0;
//        for(int i = 0 ; i < assets.length; i++){
//            c += assets[i].odds;
//        }
//        int r = new Random(c).nextInt();
//        int temp = 0;
//        for (int j = 0 ; j < assets.length; j++){
//            temp += assets[j].odds;
//            if(r <= temp){
//                return assets[j];
//            }
//        }
//        return null;
//    }
//
//    /***
//     * 获取卡片
//     * @return
//     */
//    public DropObject getCard(){
//        //道具掉落类型如果是随机
//        int c = 0;
//        for(int i = 0 ; i < cards.length; i++){
//            c += cards[i].odds;
//        }
//        int r = new Random(c).nextInt();
//        int temp = 0;
//        for (int j = 0 ; j < cards.length; j++){
//            temp += cards[j].odds;
//            if(r <= temp){
//                return cards[j];
//            }
//        }
//        return null;
//    }

    /***
     * 根据权重值来计算走哪一个掉落库
     * @return
     */
    public GoodsData getReward() {
        if(totalOdds > 0){
            int r = new Random().nextInt(totalOdds);
            int temp = 0;
            for (int j = 0 ; j < cards.length; j++){
                temp += cards[j].odds;
                if(r <= temp){
                    return new GoodsData(SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_CARDS_VALUE, cards[j].id, cards[j].count);
                }
            }
            for (int j = 0 ; j < assets.length; j++){
                temp += assets[j].odds;
                if(r <= temp){
                    return new GoodsData(SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_ASSETS_VALUE, assets[j].id, assets[j].count);
                }
            }
            for (int j = 0 ; j < props.length; j++){
                temp += props[j].odds;
                if(r <= temp){
                    return new GoodsData(SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_PROPS_VALUE, props[j].id, props[j].count);
                }
            }
        }
        return null;
    }

    @Override
    public void setVariable(String key, String value)
    {
        if("props".equals(key))
        {
            if(Utils.isErrorValue(value)){
                System.out.println("Load Lottery props is null,Please check!");
                props = new DropObject[0];
            }else{
                String[] strs = value.split(ConstantFactory.DIVISION);
                props = new DropObject[strs.length];
                for(int i = 0; i < strs.length; i++)
                {
                    props[i] = new DropObject(strs[i]);
                    PropConfig prop = DataFactory.getInstance().getGameObject(DataFactory.PROP_KEY, props[i].id);
                    if(prop == null)
                    {
                        System.out.println("Load Lottery Error: Prop is null! id=" + props[i].id);
                    }
                }
            }
        }
        else if("assets".equals(key))
        {
            if(Utils.isErrorValue(value)){
                System.out.println("Load Lottery assets is null,Please check!");
                assets = new DropObject[0];
            }else{
                String[] strs = value.split(ConstantFactory.DIVISION);
                assets = new DropObject[strs.length];
                for(int i = 0; i < strs.length; i++)
                {
                    assets[i] = new DropObject(strs[i]);
                    AssetConfig assetConfig = DataFactory.getInstance().getGameObject(DataFactory.ASSET_KEY, assets[i].id);
                    if(assetConfig == null)
                    {
                        System.out.println("Load Lottery Error: Asset is null! id=" + assets[i].id);
                    }
                }
            }

        }
        else if("cards".equals(key))
        {
            if(Utils.isErrorValue(value)){
                System.out.println("Load Lottery cards is null,Please check!");
                cards = new DropObject[0];
            }else{
                String[] strs = value.split(ConstantFactory.DIVISION);
                cards = new DropObject[strs.length];
                for(int i = 0; i < strs.length; i++)
                {
                    cards[i] = new DropObject(strs[i]);
                    CardConfig cardConfig = DataFactory.getInstance().getGameObject(DataFactory.CARD_KEY, cards[i].id);
                    if(cardConfig == null)
                    {
                        System.out.println("Load Lottery Error: Card is null! id=" + cards[i].id);
                    }
                }
            }

        }
        else if("needAsset".equals(key))
        {
            needAsset = new CommonData(value);
            AssetConfig assetConfig = DataFactory.getInstance().getGameObject(DataFactory.ASSET_KEY, needAsset.id);
            if(assetConfig == null)
            {
                System.out.println("Load Lottery Error: Asset is null! id=" + needAsset.id);
            }
        }
        else if("reward".equals(key))
        {
            reward = new GoodsData(value);
            PropConfig prop = DataFactory.getInstance().getGameObject(DataFactory.PROP_KEY, reward.id);
            if(prop == null)
            {
                System.out.println("Load Lottery Error: reward is null! id=" + reward.id);
            }
        }
        else
            super.setVariable(key, value);
    }






}
