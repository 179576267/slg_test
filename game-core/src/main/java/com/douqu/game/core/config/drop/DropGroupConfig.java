package com.douqu.game.core.config.drop;

import com.douqu.game.core.controller.PlayerController;
import com.douqu.game.core.e.E_DropGroupType;
import com.douqu.game.core.e.E_DropType;
import com.douqu.game.core.config.common.CommonData;
import com.douqu.game.core.entity.common.TwoTuple;
import com.douqu.game.core.entity.ext.BagInfo;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author: Gavin
 * Description: 掉落
 * Date: 2017-08-03 16:29-29
 * Huan Yu Copyright (c) 2017 All Rights Reserved.
 */

public class DropGroupConfig extends GameObject {

    /**
     * 对应E_DropType枚举里的值
     */
    public int propType; //道具掉落类型

    public int assetType; //资源掉落类型

    public int cardType; //兵种掉落类型

    public DropObject[] props; //道具掉落配置

    public DropObject[] assets; //资源掉落配置

    public DropObject[] cards; //卡片掉落配置

    public int type;        //类型


    /**
     * 领取奖励
     * @param playerController
     */
    public DropResult reward(PlayerController playerController) {
        BagInfo bagInfo =  playerController.getPlayer().getExtInfo(BagInfo.class);

        DropResult result = new DropResult();
        List<DropObject> cards = getCardList();
        TwoTuple<CommonData,CommonData> cardResult = null;
        Map<Integer, CommonData> cardPropMap = new ConcurrentHashMap<>();
        for(DropObject dropObject : cards) {
            cardResult = bagInfo.addCard(dropObject.id, dropObject.count);
            if(cardResult.getFirst() != null)
            {
                result.addCard(cardResult.getFirst());
            }
            if(cardResult.getSecond() != null)
            {
                cardPropMap.put(cardResult.getSecond().id, cardResult.getSecond());
            }
        }
        //掉落的道具
        List<DropObject> props = getPropList();
        int tempCount = 0;
        for(DropObject dropObject : props) {
            if(cardPropMap.get(dropObject.id) != null) {
                tempCount = dropObject.count + cardPropMap.get(dropObject.id).value;
                cardPropMap.remove(dropObject.id);
            } else {
                tempCount = dropObject.count;
            }

            bagInfo.addProp(dropObject.id, tempCount);
            result.addProp(new CommonData(dropObject.id, tempCount));
        }
        //掉落卡牌碎片
        for(Integer id : cardPropMap.keySet()) {
            bagInfo.addProp(id, cardPropMap.get(id).value);
            result.addProp(new CommonData(id, cardPropMap.get(id).value));
        }

        //掉落的资源
        List<DropObject> assets = getAssetList();
        for(DropObject dropObject : assets) {
            bagInfo.addAsset(dropObject.id, dropObject.count);
            result.addAsset(new CommonData(dropObject.id, dropObject.count));
        }

        return result;
    }

    /**
     * 领取奖励
     * @param bagInfo
     */
    public void dropRewardOnce(BagInfo bagInfo) {

        //掉落的卡牌
        for(DropObject dropObject : getCardList()) {
            bagInfo.addCard(dropObject.id, dropObject.count);
        }

        //掉落的道具
        for(DropObject dropObject : getPropList()) {
            bagInfo.addProp(dropObject.id, dropObject.count);
        }

        //掉落的资源
        for(DropObject dropObject : getAssetList()) {
            bagInfo.addAsset(dropObject.id, dropObject.count);
        }
    }


    public void dropRewardResult(BagInfo bagInfo){
        if(type == E_DropGroupType.SINGLE.getCode()){
           dropRewardOnce(bagInfo);
        }else if(type == E_DropGroupType.WHOLE.getCode()){
            dropRewardAll(bagInfo);
        }
    }

    /***
     * 获取掉落的道具List
     * @return
     */
    public List<DropObject> getPropList(){
            List<DropObject> propList = new CopyOnWriteArrayList<DropObject>();
            if( propType == E_DropType.RANDOM.getCode()){
                //根据概率计算是否掉落
                for(int i = 0 ; i < props.length; i++){
                    int r = new Random().nextInt(ConstantFactory.DROP_GROUP_PERCENTAGE);
                    if(r <= props[i].odds){
                        propList.add(props[i]);
                        break;
                    }
                }
            }
            if( propType == E_DropType.ONLY_ONE.getCode() && props.length > 0){
                int c = 0;
                for(int i = 0 ; i < props.length; i++){
                    c += props[i].odds;
                }
                int r = new Random().nextInt(c);
                int temp = 0;
                for (int j = 0 ; j < props.length; j++){
                    temp += props[j].odds;
                    if(r <= temp){
                        propList.add(props[j]);
                        break;
                    }
                }
            }

        return propList;
    }

    /***
     * 获取资源list
     * @return
     */
    public List<DropObject> getAssetList(){
        List<DropObject> assetList = new ArrayList<DropObject>();
        if( assetType == E_DropType.RANDOM.getCode()){
            //根据概率计算是否中
            for(int i = 0 ; i < assets.length; i++){
                int r = new Random().nextInt(ConstantFactory.DROP_GROUP_PERCENTAGE);
                if(r <= assets[i].odds){
                    assetList.add(assets[i]);
                    break;
                }
            }
        }
        if( assetType == E_DropType.ONLY_ONE.getCode() && assets.length > 0){
            int c = 0;
            for(int i = 0 ; i < assets.length; i++){
                c += assets[i].odds;
            }
            int r = new Random().nextInt(c);
            int temp = 0;
            for (int j = 0 ; j < assets.length; j++){
                temp += assets[j].odds;
                if(r <= temp){
                    assetList.add(assets[j]);
                    break;
                }
            }
        }

        return assetList;
    }


    /***
     * 获取资源list
     * @return
     */
    public List<DropObject> getCardList(){
        List<DropObject> cardList = new ArrayList<DropObject>();
        if( cardType == E_DropType.RANDOM.getCode()){
            //根据概率计算是否中
            for(int i = 0 ; i < cards.length; i++){
                int r = new Random().nextInt(ConstantFactory.DROP_GROUP_PERCENTAGE);
                if(r <= cards[i].odds){
                    cardList.add(cards[i]);
                    break;
                }
            }
        }
        if( cardType == E_DropType.ONLY_ONE.getCode() && cards.length > 0){
            int c = 0;
            for(int i = 0 ; i < cards.length; i++){
                c += cards[i].odds;
            }
            int r = new Random().nextInt(c);
            int temp = 0;
            for (int j = 0 ; j < cards.length; j++){
                temp += cards[j].odds;
                if(r <= temp){
                    cardList.add(cards[j]);
                    break;
                }
            }
        }

        return cardList;
    }


    /***
     * 所有的类型一起掉落
     * @return
     */
    public void dropRewardAll(BagInfo bagInfo) {
        int c = 0;
        for (int i = 0; i < props.length; i++) {
            c += props[i].odds;
        }

        for (int i = 0; i < assets.length; i++) {
            c += assets[i].odds;
        }

        for (int i = 0; i < cards.length; i++) {
            c += cards[i].odds;
        }
        if(c > 0 ){
            int  r = new Random().nextInt(c);
            int temp = 0;
            for (int j = 0; j < props.length; j++) {
                temp += props[j].odds;
                if (r <= temp) {
                    bagInfo.addProp(props[j].id,  props[j].count);
                    break;
                }
            }
            for (int j = 0; j < assets.length; j++) {
                temp += assets[j].odds;
                if (r <= temp) {
                    bagInfo.addAsset(assets[j].id, assets[j].count);
                    break;
                }
            }

            TwoTuple<CommonData,CommonData> tempCommonData = null;
            for (int j = 0; j < cards.length; j++) {
                temp += cards[j].odds;
                if (r <= temp) {
                    bagInfo.addCard(cards[j].id, cards[j].count);
                    break;
                }
            }
        }
    }






    @Override
    public void setVariable(String key, String value)
    {
        if("props".equals(key))
        {
            if(Utils.isErrorValue(value)){
//                System.out.println("Load DropGroup props is null,Please check!");
                props = new DropObject[0];
            }else{
                String[] strs = value.split(ConstantFactory.DIVISION);
                props = new DropObject[strs.length];
                for(int i = 0; i < strs.length; i++)
                {
                    props[i] = new DropObject(strs[i]);
                    if(DataFactory.getInstance().getGameObject(DataFactory.PROP_KEY, props[i].id) == null)
                    {
                        System.out.println("DropGroup setVariable props error -> Prop is null:" + props[i].id);
                    }
                    if(props[i].count <= 0)
                    {
                        System.out.println("DropGroup setVariable props error -> Prop count is zero:" + props[i].id);
                    }
                }
            }
        }
        else if("assets".equals(key))
        {
            if(Utils.isErrorValue(value)){
//                System.out.println("Load DropGroup assets is null,Please check!");
                assets = new DropObject[0];
            }else{
                String[] strs = value.split(ConstantFactory.DIVISION);
                assets = new DropObject[strs.length];
                for(int i = 0; i < strs.length; i++)
                {
                    assets[i] = new DropObject(strs[i]);
                    if(DataFactory.getInstance().getGameObject(DataFactory.ASSET_KEY, assets[i].id) == null)
                    {
                        System.out.println("DropGroup setVariable assets error -> Asset is null:" + assets[i].id);
                    }
                    if(assets[i].count <= 0)
                    {
                        System.out.println("DropGroup setVariable asset error -> Asset count is zero:" + assets[i].id);
                    }
                }
            }
        }
        else if("cards".equals(key))
        {
            if(Utils.isErrorValue(value)){
//                System.out.println("Load DropGroup cards is null,Please check!");
                cards = new DropObject[0];
            }else{
                String[] strs = value.split(ConstantFactory.DIVISION);
                cards = new DropObject[strs.length];
                for(int i = 0; i < strs.length; i++)
                {
                    cards[i] = new DropObject(strs[i]);
                    if(DataFactory.getInstance().getGameObject(DataFactory.CARD_KEY, cards[i].id) == null)
                    {
                        System.out.println("DropGroup setVariable cards error -> Card is null:" + cards[i].id);
                    }
                    if(cards[i].count <= 0)
                    {
                        System.out.println("DropGroup setVariable cards error -> Card count is zero:" + cards[i].id);
                    }
                }
            }
        }
        else
            super.setVariable(key, value);

    }

    public static void main(String args[]){
        int r = new Random().nextInt(100);
        System.out.print("r:" + r);
    }


}
