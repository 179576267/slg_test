package com.douqu.game.core.config.sprite;

import com.douqu.game.core.config.ProfessionConfig;
import com.douqu.game.core.config.battle.SkillConfig;
import com.douqu.game.core.config.card.CardConfig;
import com.douqu.game.core.e.E_Attribute;
import com.douqu.game.core.config.common.CommonData;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.util.LoadUtils;
import com.douqu.game.core.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bean on 2017/7/14.
 */
public class SoldierConfig extends Sprite {


    /** 释放范围(召唤出来的时候最远能放到几格) */
    public int initRange;

    /** AI */
    public int ai;

    /** 视野(在视野范围内就要被吸引过去攻击) */
    public int vision;

    /** CD技能 */
    public SkillConfig[] skills;

    /** 升星增加攻击
     * 索引 对应星级
     * */
    public int[] starAtk;

    /** 升星增加防御
     * 索引 对应星级
     * */
    public int[] starDef;

    /** 升星增加生命
     * 索引 对应星级
     * */
    public int[] starHp;

    /** 升星增加技能
     * id 对应星级
     * */
    public CommonData[] starSkills;


    /**
     * 品质,从卡牌里面继承而来
     */
    private CardConfig cardConfig;

    /**饰品*/
    public int [] accessory;


    @Override
    public void check()
    {
        super.check();

        if(skills == null)
        {
            System.out.println("Load Soldier skills is null -> id:" + id);
        }
        if(starSkills == null)
        {
            System.out.println("Load Soldier starSkills is null -> id:" + id);
        }
        if(starHp == null)
        {
            System.out.println("Load Soldier starHp is null -> id:" + id);
        }
        if(starDef == null)
        {
            System.out.println("Load Soldier starDef is null -> id:" + id);
        }
        if(starAtk == null)
        {
            System.out.println("Load Soldier starAtk is null -> id:" + id);
        }
        if(accessory == null)
        {
            System.out.println("Load Soldier accessory is null -> id:" + id);
        }
    }

    /**
     * 获取星级属性
     * @param attrId
     * @param star
     * @return
     */
    @Override
    public int getStarAttribute(int attrId, int star)
    {
        int[] data = null;
        if(attrId == E_Attribute.ATK.getCode())
            data = starAtk;
        else if(attrId == E_Attribute.DEF.getCode())
            data = starDef;
        else if(attrId == E_Attribute.HP.getCode())
            data = starHp;

        if(data == null)
            return 0;

        if(star <= 0 || star > data.length)
            return 0;

        return data[star-1];
    }


    /**
     * 获取星级技能
     * @param star
     * @return
     */
    public List<Integer> getStarSkills(int star)
    {
        List<Integer> result = new ArrayList<>();
        for(CommonData commonData : starSkills)
        {
            if(commonData.id <= star)
                result.add(commonData.value);
        }

        return result;
    }

    /**
     * 是否能克制对方
     * @param target
     * @return
     */
    @Override
    public boolean isRestrict(Sprite target)
    {
        if(target instanceof SoldierConfig)
        {
            ProfessionConfig pro = DataFactory.getInstance().getGameObject(DataFactory.PROFESSION_KEY, profession);
            ProfessionConfig targetPro = DataFactory.getInstance().getGameObject(DataFactory.PROFESSION_KEY, ((SoldierConfig) target).profession);
            return pro.restrictType == targetPro.restrictType;
        }

        return false;
    }



    @Override
    public void setVariable(String key, String value)
    {
        if("skills".equals(key))
        {
            if(Utils.isErrorValue(value))
            {
                System.out.println("Load Soldier skills is null,Please check!");
                skills = new SkillConfig[0];
            }
            else
            {
                int[] ids = LoadUtils.loadIntArray(key, value);
                skills = new SkillConfig[ids.length];
                for(int i = 0; i < ids.length; i++)
                {
                    skills[i] = DataFactory.getInstance().getGameObject(DataFactory.SKILL_KEY, ids[i]);
                    if(skills[i] == null)
                    {
                        System.out.println("Load Soldier Error -> skill is null:" + ids[i] + " spriteId:" + id);
                    }
                }
            }
        }
        else if("starAtk".equals(key))
        {
            starAtk = LoadUtils.loadIntArray(key, value);
        }
        else if("starDef".equals(key))
        {
            starDef = LoadUtils.loadIntArray(key, value);
        }
        else if("starHp".equals(key))
        {
            starHp = LoadUtils.loadIntArray(key, value);
        }
        else if("starSkills".equals(key))
        {
            if(Utils.isErrorValue(value))
                starSkills = new CommonData[0];
            else
            {
                String[] strs = value.split(ConstantFactory.DIVISION);
                starSkills = new CommonData[strs.length];
                for(int i = 0; i < strs.length; i++)
                {
                    starSkills[i] = new CommonData(strs[i]);
                    if(DataFactory.getInstance().getGameObject(DataFactory.SKILL_KEY, starSkills[i].value) == null)
                    {
                        System.out.println("Load Soldier starSkills Error -> id:" + id + "  skillId:" + starSkills[i].value);
                    }
                }
            }
        } else if("accessory".equals(key)) {
            accessory = LoadUtils.loadIntArray(key, value);
            for(Integer accessoryId : accessory)
            {
                if(DataFactory.getInstance().getGameObject(DataFactory.ACCESSORY_KEY, accessoryId) == null)
                {
                    System.out.println("Load Soldier accessory Error -> id:" + id + "  accessoryId:" + accessoryId);
                }
            }
        }
        else
            super.setVariable(key, value);
    }





    public CardConfig getCardConfig() {
        return cardConfig;
    }

    public void setCardConfig(CardConfig cardConfig) {
        this.cardConfig = cardConfig;
    }





}
