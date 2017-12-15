package com.douqu.game.core.config.goods;

import com.douqu.game.core.e.E_Attribute;
import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.entity.ext.BagInfo;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.util.LoadUtils;

import java.util.Arrays;

/**
 * @author: Gavin.
 * Description: 装备升级
 * Date: 2017/11/1 0001 下午 6:36
 * Huan Yu Copyright (c) 2017 All Rights Reserved.
 */
public class EquipConfig extends Goods {

    /**类型
     * 1.武器
     * 2.头盔
     * 3.护甲
     * 4.鞋子
     * */
    public int type;
    /** 攻击力 */
    public int atk;
    /** 攻击成长 */
    public float atkGrow;
    /** 物理防御 */
    public int def;
    /** 防御成长 */
    public float defGrow;
    /** 生命值 */
    public int hp;
    /** 生命成长 */
    public float hpGrow;
    /**下一级成长*/
    public int nextEquip;
    /**需要的道具，资源，装备*/
    public GoodsData [] synNeed;
    /**返还的道具，资源，装备*/
    public GoodsData [] restitution;

    @Override
    public void copyTo(GameObject obj) {
        super.copyTo(obj);
        EquipConfig equip = (EquipConfig)obj;
        equip.type = this.type;
        equip.atk = this.atk;
        equip.atkGrow = this.atkGrow;
        equip.def = this.def;
        equip.defGrow = this.defGrow;
        equip.hpGrow = this.hpGrow;
        equip.nextEquip = this.nextEquip;
        equip.synNeed = this.synNeed;
        equip.restitution = this.restitution;
    }

    @Override
    public void check() {
        if(synNeed == null)
        {
            System.out.println("Equip check synNeed is null -> id:" + id + " name:" + name);
        }

        if(restitution == null)
        {
            System.out.println("Equip check restitution is null -> id:" + id + " name:" + name);
        }
    }

    /**
     * 检查背包资源是否足够
     *
     * */
    public boolean checkEnough(BagInfo bagInfo, int type) {
        for(GoodsData goodsData : synNeed) {
            if(goodsData.type == type)
            {
                if(bagInfo.getAsset(goodsData.id) < goodsData.value)
                    return false;
                else
                    break;
            }
        }
        return true;
    }

    /**
     * 检查背包资源是否足够
     *
     * */
    public boolean checkEnoughAsset(BagInfo bagInfo) {
        for(GoodsData goodsData : synNeed) {
            if(goodsData.type == SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_ASSETS_VALUE)
            {
                if(bagInfo.getAsset(goodsData.id) < goodsData.value)
                    return false;
            }
        }
        return true;
    }

    /**
     * 检查背包资源是否足够
     *
     * */
    public boolean checkEnoughProp(BagInfo bagInfo) {
        for(GoodsData goodsData : synNeed) {
            if(goodsData.type == SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_PROPS_VALUE)
            {
                if(bagInfo.getPropCount(goodsData.id) < goodsData.value)
                    return false;
            }
        }
        return true;
    }

    /**
     * 检查背包资源是否足够
     *
     * */
    public boolean checkEnoughCard(BagInfo bagInfo) {
        for(GoodsData goodsData : synNeed) {
            if(goodsData.type == SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_CARDS_VALUE)
            {
                if(bagInfo.getCardData().getCard(goodsData.id) == null)
                    return false;
            }
        }
        return true;
    }

    public int getAttribute(int attrId, int lv)
    {
        if(attrId == E_Attribute.ATK.getCode())
            return (int) (atk + lv * atkGrow);

        if(attrId == E_Attribute.DEF.getCode())
            return (int) (def + lv * defGrow);

        if(attrId == E_Attribute.HP.getCode())
            return (int) (hp + lv * hpGrow);

        E_Attribute attribute = E_Attribute.forNumber(attrId);
        if(attribute == null)
            return 0;

        return Integer.parseInt(getVariable(attribute.getMsg()));
    }

    @Override
    public void setVariable(String key, String value) {
        if("synNeed".equals(key)){
            synNeed = LoadUtils.loadGoodDataToArray(key, value);
            for(GoodsData data : synNeed)
            {
                data.check(this.getClass(), key);
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



    @Override
    public String toString() {
        return "Equip{" +
                "type=" + type +
                ", icon='" + icon + '\'' +
                ", quality=" + quality +
                ", atk=" + atk +
                ", atkGrow=" + atkGrow +
                ", def=" + def +
                ", defGrow=" + defGrow +
                ", hp=" + hp +
                ", hpGrow=" + hpGrow +
                ", nextEquip=" + nextEquip +
                ", synNeed=" + Arrays.toString(synNeed) +
                ", restitution=" + Arrays.toString(restitution) +
                '}';
    }
}
