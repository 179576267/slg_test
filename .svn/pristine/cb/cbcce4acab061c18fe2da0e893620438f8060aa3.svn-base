package com.douqu.game.battle.entity;


import com.douqu.game.battle.entity.aobject.BattleAObject;
import com.douqu.game.battle.entity.tmp.SpriteBattleTmp;
import com.douqu.game.core.config.battle.SkillConfig;
import com.douqu.game.core.config.common.Position;
import com.douqu.game.core.factory.ConstantFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 暴风雪等特殊效果
 * @Author: Bean
 * @Description:
 * @Date: 2017-10-28 17:12
 */
public class ExtendEffect {


    private SpriteBattleTmp releaser;

    public BattleSkill skill;

    /**
     * 剩余时间(毫秒)
     */
    public int surplusTime;

    public Position sourcePoint;

    public Position directionPoint;

    /**
     * 范围类型
     */
    public int rangeType;

    /**
     * 范围值
     * 2圆形 类型;半径
         3扇形 类型;半径;角度
         4矩形 类型;长;宽
     */
    public int[] rangeValue;

    private long updateTime;

    public ExtendEffect(SpriteBattleTmp releaser, BattleSkill skill, Position sourcePoint, Position directionPoint)
    {
        this.releaser = releaser;
        this.skill = skill;
        SkillConfig skillConfig = skill.getSkillConfig();
        this.surplusTime = skillConfig.effectTime;
        this.sourcePoint = sourcePoint;
        this.directionPoint = directionPoint;

        this.rangeType = skillConfig.effectRange[0];

        if(rangeType == ConstantFactory.SKILL_RANGE_TYPE_CIRCLE)
        {
            rangeValue = new int[1];
            rangeValue[0] = skillConfig.effectRange[1];
        }
        else if(rangeType == ConstantFactory.SKILL_RANGE_TYPE_SECTOR || rangeType == ConstantFactory.SKILL_RANGE_TYPE_RECTANGLE)
        {
            rangeValue = new int[2];
            rangeValue[0] = skillConfig.effectRange[1];
            rangeValue[1] = skillConfig.effectRange[2];
        }
    }


    public void update(long currentTime)
    {
        if(updateTime > 0)
        {
            if(currentTime - updateTime < ConstantFactory.UPDATE_BATTLE_BUFF_TIME)
                return;
        }

        if(surplusTime > 0)
        {
            final int effectTargetGroup = skill.getSkillConfig().effectTargetGroup;

            List<BattleAObject> tempList = new CopyOnWriteArrayList<>();
            if(effectTargetGroup == ConstantFactory.EFFECT_TARGET_GROUP_ALL)
            {
                tempList.addAll(releaser.getBattleController().getSoldiers());
                tempList.add(releaser.getMasterSoldier());
                tempList.add(releaser.getTargetBattleTmp().getMasterSoldier());
            }
            else if(effectTargetGroup == ConstantFactory.EFFECT_TARGET_GROUP_PARTNER)
            {
                tempList.addAll(releaser.getSoldierList());
                tempList.add(releaser.getMasterSoldier());
            }
            else if(effectTargetGroup == ConstantFactory.EFFECT_TARGET_GROUP_ENEMY)
            {
                tempList.addAll(releaser.getTargetBattleTmp().getSoldierList());
                tempList.add(releaser.getTargetBattleTmp().getMasterSoldier());
            }

            for(BattleAObject battleAObject : tempList)
            {
                skill.takeEffect(battleAObject);
            }

            surplusTime -= ConstantFactory.UPDATE_BATTLE_BUFF_TIME;
            surplusTime = surplusTime < 0 ? 0 : surplusTime;
        }

        updateTime = currentTime;
    }


    public boolean isEnd()
    {
        return surplusTime <= 0;
    }


}
