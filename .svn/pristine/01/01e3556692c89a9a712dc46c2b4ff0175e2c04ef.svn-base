package com.douqu.game.core.config.sprite;

import com.douqu.game.core.config.battle.SkillConfig;
import com.douqu.game.core.e.E_Attribute;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.DataFactory;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-10-16 19:52
 */
public class Sprite extends GameObject {

    public String model;

    public String avatar;

    public int modelSizeGrade;

    /**
     * 类型
     * 1。地面
     * 2。飞行
     */
    public int unitType;

    /**
     * 阵营
     * 1.联盟
     * 2.部落
     * 3.天灾
     */
    public int camp;

//    /** 攻击类型
//     * 1.物理
//     * 2.穿刺
//     * 3.魔法
//     * 4.英雄
//     * */
//    public int atkType;
    /**
     * 职业
     */
    public int profession;

    /** 攻击力 */
    public int atk;
    /** 攻击成长 */
    public float atkGrow;
    /** 物理防御 */
    public int def;
    /** 防御成长 */
    public float defGrow;
    /** 暴击率(万分比) */
    public int cri;
    /** 抗暴率(万分比) */
    public int rec;
    /** 增伤率(万分比) */
    public int add;
    /** 免伤率(万分比) */
    public int exd;
    /** 命中率(万分比) */
    public int hit;
    /** 闪避率(万分比) */
    public int eva;

    /** 生命值 */
    public int hp;
    /** 生命成长 */
    public float hpGrow;

    /** 力量 */
    public int power;
    /** 力量成长 */
    public float powerGrow;
    /** 敏捷 */
    public int agility;
    /** 敏捷成长 */
    public float agilityGrow;
    /** 智力 */
    public int wisdom;
    /** 智力成长 */
    public float wisdomGrow;

    /**
     * 移动速度(每秒移动多少米)
     */
    public int moveSpeed;

    /**
     * 普通攻击速度(普通攻击CD时间)
     */
    public int attackSpeed;

    /**
     * 占位几格 1*1 2*2
     */
    public int gridArea = 1;

    /** 普通攻击技能 */
    public SkillConfig defaultSkill;


    public Sprite()
    {

    }


    @Override
    public void check()
    {
        if(defaultSkill == null)
        {
            System.out.println("Load Sprite defaultSkill is null! id:" + id);
        }

    }


    /**
     * 获取星级属性
     * @param attrId
     * @param star
     * @return
     */
    public int getStarAttribute(int attrId, int star)
    {
        //子类实现
        return 0;
    }


    /**
     * 获取属性增加
     * @param attrId
     * @return
     */
    public int getAttribute(int attrId, int lv)
    {
        if(attrId == E_Attribute.ATK.getCode())
            return (int) (atk + (lv - 1) * atkGrow);

        if(attrId == E_Attribute.DEF.getCode())
            return (int) (def + (lv - 1) * defGrow);

        if(attrId == E_Attribute.HP.getCode())
            return (int) (hp + (lv - 1) * hpGrow);

        if(attrId == E_Attribute.POWER.getCode())
            return (int) (power + (lv - 1) * powerGrow);

        if(attrId == E_Attribute.AGILITY.getCode())
            return (int) (agility + (lv - 1) * agilityGrow);

        if(attrId == E_Attribute.WISDOM.getCode())
            return (int) (wisdom + (lv - 1) * wisdomGrow);

        E_Attribute attribute = E_Attribute.forNumber(attrId);
        if(attribute == null)
            return 0;

        return Integer.parseInt(getVariable(attribute.getMsg()));
    }

    /**
     * 获取战力
     * @return
     */
    public int getFC(int lv)
    {
        return (int) (getAttribute(E_Attribute.HP.getCode(), lv) * 0.1
                    + getAttribute(E_Attribute.ATK.getCode(), lv)
                    + getAttribute(E_Attribute.DEF.getCode(), lv)
                    +(getAttribute(E_Attribute.CRI.getCode(), lv)
                    + getAttribute(E_Attribute.REC.getCode(), lv)
                    + getAttribute(E_Attribute.ADD.getCode(), lv)
                    + getAttribute(E_Attribute.EXD.getCode(), lv)
                    + getAttribute(E_Attribute.HIT.getCode(), lv)
                    + getAttribute(E_Attribute.EVA.getCode(), lv)) * 0.5);
    }

    /**
     * 获取对应的属性加成比例
     * @return 如果是加成50%则是返回50
     */
    public int getAttributePlus(int attrId)
    {
        return 0;
    }




        /**
     * 是否能克制对方
     * @param target
     * @return
     */
    public boolean isRestrict(Sprite target)
    {
        return false;
    }


    @Override
    public void setVariable(String key, String value)
    {
        if("profession".equals(key))
        {
            profession = Integer.parseInt(value);
            if(DataFactory.getInstance().getGameObject(DataFactory.PROFESSION_KEY, profession) == null)
            {
                System.out.println("Load Sprite profession is null, profession = " + value);
            }
        }

        else if("defaultSkill".equals(key))
        {
            defaultSkill = DataFactory.getInstance().getGameObject(DataFactory.SKILL_KEY, Integer.parseInt(value));
            if(defaultSkill == null)
            {
                System.out.println("Load Sprite Error -> defaultSkill is null:" + id);
            }
            else
            {
                attackSpeed = defaultSkill.cdTime;
            }
        }
        else
            super.setVariable(key, value);
    }


    @Override
    public String toString() {
        return "Sprite{" +
                "camp=" + camp +
                ", profession=" + profession +
                ", moveSpeed=" + moveSpeed +
                "} " + super.toString();
    }
}
