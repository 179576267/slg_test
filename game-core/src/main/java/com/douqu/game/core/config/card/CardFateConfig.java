package com.douqu.game.core.config.card;

import com.douqu.game.core.config.common.CommonData;
import com.douqu.game.core.entity.ext.BagInfo;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.util.LoadUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: Gavin.
 * Description:
 * Date: 2017/11/1 0001 下午 7:38
 * Huan Yu Copyright (c) 2017 All Rights Reserved.
 */
public class CardFateConfig extends GameObject{

    /**宿命激活*/
    public int[] needCards;
    /**激活属性*/
    public CommonData[] attr;
    /**所属卡牌*/
    public int belong;


    /**
     * 是否是这张卡的宿命
     * @param cardId
     * @return
     */
    public boolean isCard(int cardId)
    {
        return belong == cardId;
    }

    /**
     * 检测此宿命列表是否包含此卡片
     * @return
     */
    public  boolean  checkNeedCards(BagInfo bagInfo) {
        for(int i = 0 ; i < needCards.length; i++){
            if(bagInfo.getCardData().getCard(needCards[i]) != null)
                return  true;
        }
        return false;
    }

    /**
     * 判断多个卡片是否是宿命关系
     * @param ids
     * @return
     */
    public boolean isCardHasFate(Integer... ids){
        List<Integer> needCardsList = new ArrayList<>();
        for(int i = 0 ; i < needCards.length; i++){
            needCardsList.add(needCards[i]);
        }

       return needCardsList.containsAll(Arrays.asList(ids));
    }

    @Override
    public void check() {
        if(needCards == null)
        {
            System.out.println("CardFate check needCards is null -> id:" + id + " name:" + name);
        }

        if(attr == null)
        {
            System.out.println("CardFate check attr is null -> id:" + id + " name:" + name);
        }
    }

    public int getAttribute(int attrId)
    {
        for(CommonData commonData : attr)
        {
            if(commonData.id == attrId)
                return commonData.value;
        }

        return 0;
    }

    @Override
    public void setVariable(String key, String value)
    {
        if("needCards".equals(key)) {

            needCards = LoadUtils.loadIntArray(key, value);
            for(Integer cardId : needCards)
            {
                if(DataFactory.getInstance().getGameObject(DataFactory.CARD_KEY, cardId) == null)
                {
                    System.out.println("CardFate setVariable needCards Error -> cardId : " + cardId + " id:" + id);
                }
            }

        }else if("attr".equals(key)) {
            String[] strs = value.split(ConstantFactory.DIVISION);
            attr = new CommonData[strs.length];
            for(int i = 0; i < strs.length; i++)
            {
                attr[i] = new CommonData(strs[i]);
                if(DataFactory.getInstance().getGameObject(DataFactory.ATTRIBUTE_KEY, attr[i].id) == null)
                {
                    System.out.println("CardFate setVariable attr Error -> attr : " + attr[i].id + " id:" + id);
                }
            }
        }else if("belong".equals(key)){
            belong = Integer.parseInt(value);
            if(DataFactory.getInstance().getGameObject(DataFactory.CARD_KEY, belong) == null)
            {
                System.out.println("CardFate setVariable needCards Error -> belong : " + belong + " id:" + id);
            }
        } else{
            super.setVariable(key, value);
        }

    }

}
