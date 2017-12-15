package com.douqu.game.core.config.goods;

import com.douqu.game.core.e.E_Attribute;
import com.douqu.game.core.util.LoadUtils;

import java.util.Arrays;

/**
 * @author: Gavin.
 * Description:
 * Date: 2017/11/6 0006 下午 5:16
 * Huan Yu Copyright (c) 2017 All Rights Reserved.
 */
public class AccessoryConfig extends Goods {

    /**类型*/
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
    /** 暴击率*/
    public int [] upCri;
    /** 闪避率*/
    public int [] upEva;
    /** 攻击力*/
    public int [] upAtk;
    /** 防御*/
    public int [] upDef;
    /** 生命值*/
    public int [] upHp;


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

    /**
     * 获取进阶属性
     * @param attrId
     * @return
     */
    public int getUpAttribute(int attrId, int upLv)
    {
        int[] data = null;
        if(attrId == E_Attribute.ATK.getCode())
            data = upAtk;
        else if(attrId == E_Attribute.DEF.getCode())
            data = upDef;
        else if(attrId == E_Attribute.HP.getCode())
            data = upHp;
        else if(attrId == E_Attribute.CRI.getCode())
            data = upCri;
        else if(attrId == E_Attribute.EVA.getCode())
            data = upEva;

        if(data == null || data.length <= 1)
            return 0;

        if(upLv < 0 || upLv >= data.length)
            return 0;

        return data[upLv];
    }


    @Override
    public void check() {
        if(upCri == null)
        {
            System.out.println("GoodsAccessory check upCri is null -> id:" + id + " name:" + name);
        }

        if(upEva == null)
        {
            System.out.println("GoodsAccessory check upEva is null -> id:" + id + " name:" + name);
        }
        if(upAtk == null)
        {
            System.out.println("GoodsAccessory check upAtk is null -> id:" + id + " name:" + name);
        }
        if(upDef == null)
        {
            System.out.println("GoodsAccessory check upDef is null -> id:" + id + " name:" + name);
        }
        if(upHp == null)
        {
            System.out.println("GoodsAccessory check upHp is null -> id:" + id + " name:" + name);
        }
    }


    @Override
    public void setVariable(String key, String value) {
        if ("upCri".equals(key)) {
            upCri = LoadUtils.loadIntArray(key, value);
        }else if("upEva".equals(key)) {
            upEva = LoadUtils.loadIntArray(key, value);
        }else if("upAtk".equals(key)) {
            upAtk = LoadUtils.loadIntArray(key, value);
        }else if("upDef".equals(key)) {
            upDef = LoadUtils.loadIntArray(key, value);
        }else if("upHp".equals(key)) {
            upHp = LoadUtils.loadIntArray(key, value);
        }else{
            super.setVariable(key, value);
        }
    }

    @Override
    public String toString() {
        return "Accessory{" +
                "type=" + type +
                ", icon='" + icon + '\'' +
                ", quality=" + quality +
                ", atk=" + atk +
                ", atkGrow=" + atkGrow +
                ", def=" + def +
                ", defGrow=" + defGrow +
                ", hp=" + hp +
                ", hpGrow=" + hpGrow +
                ", upCri=" + Arrays.toString(upCri) +
                ", upEva=" + Arrays.toString(upEva) +
                ", upAtk=" + Arrays.toString(upAtk) +
                ", upDef=" + Arrays.toString(upDef) +
                ", upHp=" + Arrays.toString(upHp) +
                '}';
    }
}
