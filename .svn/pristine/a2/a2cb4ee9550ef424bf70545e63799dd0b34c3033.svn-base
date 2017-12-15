package com.douqu.game.core.config.card;

import com.douqu.game.core.config.battle.SkillConfig;
import com.douqu.game.core.config.sprite.SoldierConfig;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.config.goods.PropConfig;
import com.douqu.game.core.factory.DataFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by bean on 2017/7/26.
 */
public class CardConfig extends GameObject {

    public String model;

    public String avatar;

    public int quality;

    /** 类型
     * 1.英雄
     * 2.兵种
     * */
    public int type;

    /** 消耗水晶 */
    public int crystal;

    /** 产地描述 */
    public String sourceRemark;

    /** 解锁条件 */
    public String unlockRemark;

    /** 升级所需碎片 */
    public int lvUpProp;

    /**
     * 召唤的兵种ID
     */
    public int soldierId;

    public SoldierConfig soldier;
    /**
     * 召唤的兵种数量
     */
    public int soldierCount;

    /***
     * 碎片的转换数量
     */
    public int transformation;

    /**
     * 所有宿命
     */
    private List<Integer> fateList;



    public CardConfig()
    {
        super();

        fateList = new CopyOnWriteArrayList<>();
    }


    @Override
    public void check()
    {
        soldier = DataFactory.getInstance().getGameObject(DataFactory.SOLDIER_KEY, soldierId);
        if(soldier == null)
        {
            System.out.println("Load Card Error: Soldier is null! soldierId=" + soldierId);
        }
        else
        {
            soldier.setCardConfig(this);
        }

        PropConfig prop = DataFactory.getInstance().getGameObject(DataFactory.PROP_KEY, lvUpProp);
        prop.setCardConfig(this);
    }

    public void initFateList()
    {
        List<CardFateConfig> list = DataFactory.getInstance().getDataList(DataFactory.CARD_FATE_KEY);
        for(CardFateConfig cardFateConfig : list)
        {
            if(cardFateConfig.isCard(id))
                fateList.add(cardFateConfig.id);
        }
    }



    public List<SkillConfig> getSkills(int star)
    {
        List<SkillConfig> result = new ArrayList<>();
        for(SkillConfig skillConfig : soldier.skills)
        {
            result.add(skillConfig);
        }

        for(Integer skillId : soldier.getStarSkills(star))
        {
            result.add(DataFactory.getInstance().getGameObject(DataFactory.SKILL_KEY, skillId));
        }

        return result;
    }

    public List<Integer> getFateList() {
        return fateList;
    }

    @Override
    public void setVariable(String key, String value)
    {
        if("soldierId".equals(key))
        {
            soldierId = Integer.parseInt(value);
            soldier = DataFactory.getInstance().getGameObject(DataFactory.SOLDIER_KEY, soldierId);
            if(soldier == null)
            {
                System.out.println("Load Card Error: Soldier is null! soldierId=" + soldierId);
            }
        }
        else if("soldierCount".equals(key))
        {
            soldierCount = Integer.parseInt(value);
            if(soldierCount <= 0)
            {
                System.out.println("Load Card Error: soldierCount=" + soldierCount);
            }
        }
        else if("lvUpProp".equals(key))
        {
            lvUpProp = Integer.parseInt(value);
            PropConfig prop = DataFactory.getInstance().getGameObject(DataFactory.PROP_KEY, lvUpProp);
            if(prop == null)
            {
                System.out.println("Load Card Error: GoodsProp is null! lvUpProp=" + lvUpProp);
            }
        }
        else
            super.setVariable(key, value);
    }



}
