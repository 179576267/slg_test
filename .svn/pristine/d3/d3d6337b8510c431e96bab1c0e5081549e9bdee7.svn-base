package com.douqu.game.battle.entity;

import com.bean.core.util.TimeUtils;
import com.douqu.game.battle.controller.sprite.SpriteController;
import com.douqu.game.battle.entity.aobject.BattleAObject;
import com.douqu.game.battle.server.GameServer;
import com.douqu.game.core.config.battle.SkillConfig;
import com.douqu.game.core.config.common.Position;
import com.douqu.game.core.entity.common.TwoTuple;
import com.douqu.game.core.factory.ConstantFactory;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-12-07 14:49
 */
public class BattleTimeSkillDamage {

    Logger logger = Logger.getLogger(this.getClass());

    public int id;
    /**
     * 技能生效的位置
     */
    private Position effectPos;

    /**
     * 技能锁敌
     */
    private List<BattleAObject> effectTargets;

    /**
     * 效果结束时间
     */
    private long effectEndTime;


    private BattleSkill skill;

    public BattleTimeSkillDamage(BattleSkill skill, long effectEndTime)
    {
        this.id = skill.createTimeSkillId();
        this.skill = skill;
        this.effectEndTime = effectEndTime;

        logger.debug("当前时间:" + TimeUtils.getCurrentTime("yyyyMMdd-HHmmss:SSS") + " 结束时间:" + TimeUtils.getFormatTime(effectEndTime, "yyyyMMdd-HHmmss:SSS"));
    }


    public BattleTimeSkillDamage(BattleSkill skill, long effectEndTime, List<BattleAObject> effectTargets)
    {
        this(skill, effectEndTime);

        this.effectTargets = effectTargets;
    }

    public BattleTimeSkillDamage(BattleSkill skill, long effectEndTime, BattleAObject effectTarget)
    {
        this(skill, effectEndTime);

        if(effectTarget != null)
        {
            this.effectTargets = new CopyOnWriteArrayList<>();
            this.effectTargets.add(effectTarget);
        }
    }

    public BattleTimeSkillDamage(BattleSkill skill, long effectEndTime, Position effectPos)
    {
        this(skill, effectEndTime);

        this.effectPos = effectPos;
    }

    public boolean isTimeEnd()
    {
        return effectEndTime <= GameServer.currentTime;
    }

//    /**
//     * 延时效果是否能生效
//     * @return
//     */
//    public boolean checkTimeEffectStart()
//    {
//        logger.warn("endTime: " + effectEndTime  + " 当前时间:" + GameServer.currentTime);
//        return (effectPos != null || effectTargets != null) && effectEndTime > 0 && skill.getSkillConfig().effectSpeed > 0 && effectEndTime <= GameServer.currentTime;
//    }

    /**
     * 延时效果生效
     */
    public void timeEffectStart()
    {
        BattleAObject releaser = skill.getReleaser();
        logger.info("BattleSkill timeEffectStart 延时效果时间到了，触发伤害 -> effectPos:" + effectPos + " targets:" + effectTargets + " 技能:" + skill.getSkillConfig().getName());
        if(effectPos != null)
        {
            if(skill.getSkillConfig().effectTime > 0)//暴风雪
            {
                logger.debug("释放大招技能");
                releaser.battleController.addExtendEffect(skill.getSpriteController().getAttachment(), skill, releaser.position, effectPos);
            }
            else
            {
                List<BattleAObject> tempList = skill.getTargetTempList(false);
                logger.debug("目标列表 :" + tempList);
                if(tempList.size() > 0)
                {
                    List<BattleAObject> targets = skill.getTarget(effectPos, tempList, null);
                    logger.debug("目标:" + targets);
                    if(targets != null)
                    {
                        for(BattleAObject target: targets)
                        {
                            skill.takeEffect(target);
                        }

                        skill.releaseBuff(targets);
                    }
                }
            }
            effectPos = null;
        }
        else if(effectTargets != null)
        {
            for(BattleAObject target: effectTargets)
            {
                skill.takeEffect(target);

                if(target.isDie())
                {
                    effectTargets.remove(target);
                }
            }

            skill.releaseBuff(effectTargets);

            effectTargets = null;
        }

        effectEndTime = 0;
    }





//    public void checkTargetDie()
//    {
//        if(effectTargets == null)
//            return;
//
//        for(BattleAObject target : effectTargets)
//        {
//            if(target.isDie())
//            {
//                effectTargets.remove(target);
//            }
//        }
//    }


    public boolean isHaveTarget()
    {
        return effectTargets != null && effectTargets.size() > 0;
    }


    public BattleAObject getFirstTarget()
    {
        if(isHaveTarget())
            return effectTargets.get(0);

        return null;
    }

    public Position getEffectPos() {
        return effectPos;
    }

    public void setEffectPos(Position effectPos) {
        this.effectPos = effectPos;
    }

    public List<BattleAObject> getEffectTargets() {
        return effectTargets;
    }

    public void setEffectTargets(List<BattleAObject> effectTargets) {
        this.effectTargets = effectTargets;
    }

    public long getEffectEndTime() {
        return effectEndTime;
    }

    public void setEffectEndTime(long effectEndTime) {
        this.effectEndTime = effectEndTime;
    }
}
