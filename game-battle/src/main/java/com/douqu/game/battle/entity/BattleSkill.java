package com.douqu.game.battle.entity;


import com.bean.core.util.TimeUtils;
import com.douqu.game.battle.controller.sprite.SpriteController;
import com.douqu.game.battle.entity.aobject.BattleAObject;
import com.douqu.game.battle.entity.aobject.BattleMasterAObject;
import com.douqu.game.battle.server.GameServer;
import com.douqu.game.battle.util.BattleUtils;
import com.douqu.game.core.config.battle.BuffConfig;
import com.douqu.game.core.entity.EntityObject;
import com.douqu.game.core.config.battle.SkillConfig;
import com.douqu.game.core.config.common.Position;
import com.douqu.game.core.entity.common.TwoTuple;
import com.douqu.game.core.factory.ConstantFactory;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by bean on 2017/8/10.
 */
public class BattleSkill extends EntityObject {

    Logger logger = Logger.getLogger(BattleSkill.class);

    private SpriteController spriteController;

    /**
     * 技能释放者
     */
    private BattleAObject releaser;

    private SkillConfig skillConfig;

    /**
     * 释放时间
     */
    public long releaseTime;

    public int releaseCount;

//    /**
//     * 当前剩余CD时间
//     */
//    public int cdTime;

    /**
     * 普通攻击锁定目标
     */
    private BattleAObject defaultAtkTarget;

    /**
     * 延时效果伤害集合
     */
    private List<BattleTimeSkillDamage> timeSkillDamages;

    public BattleSkill(SpriteController spriteController, BattleAObject battleAObject, SkillConfig skillConfig)
    {
        this.spriteController = spriteController;
        this.releaser = battleAObject;
        this.skillConfig = skillConfig;
        this.timeSkillDamages = new CopyOnWriteArrayList<>();
    }


    public void update()
    {
//        if(cdTime > 0)
//        {
//            cdTime -= ConstantFactory.UPDATE_BATTLE_TIME;
//            cdTime = cdTime < 0 ? 0 : cdTime;
//        }

//        if(effectEndTime > 0)
//        {
//            effectTime -= ConstantFactory.UPDATE_BATTLE_TIME;
//            effectTime = effectTime < 0 ? 0 : effectTime;

//            logger.warn("skill:" + skillConfig.getName() + "  effectTime:" + effectTime);
//        }

        for(BattleTimeSkillDamage skillDamage : timeSkillDamages)
        {
            if(skillDamage.isTimeEnd())
            {
                skillDamage.timeEffectStart();

                timeSkillDamages.remove(skillDamage);
            }
        }
    }

    /**
     * 检测水晶是否足够
     * @param curCrystal
     * @return
     */
    public boolean checkCrystal(int curCrystal)
    {
        return skillConfig.crystal <= curCrystal;
    }

//    public boolean isHaveTarget()
//    {
//        return timeSkillDamages.size() > 0;
////        return effectTargets != null && effectTargets.size() > 0;
//    }

//    public void checkTargetDie()
//    {
//        for(BattleTimeSkillDamage skillDamage : timeSkillDamages)
//        {
//            skillDamage.checkTargetDie();
//        }
//    }

    public void setDefaultAtkTarget(BattleAObject target)
    {
        defaultAtkTarget = target;
    }



    public void clearDefaultAtkTarget()
    {
        defaultAtkTarget = null;
    }

    public BattleAObject getDefaultAtkTarget()
    {
        return defaultAtkTarget;
    }


//    /**
//     * 效果动作是否在播放中,还不能进行伤害
//     * @return
//     */
//    public boolean isEffectFlying()
//    {
//        return effectEndTime > 0 && skillConfig.effectSpeed > 0 && GameServer.currentTime < effectEndTime;
//    }
//
//
//    /**
//     * 是否在播放动作中
//     * @return
//     */
//    public boolean isTimeActioning()
//    {
//        return skillConfig.effectTime > 0 && effectEndTime > 0 && GameServer.currentTime < effectEndTime;
//    }


    /**
     * 是否能释放了
     * @return
     */
    public boolean isCanRelease()
    {
        return releaseTime == 0 || GameServer.currentTime - releaseTime >= skillConfig.cdTime;
    }

    /**
     * 是否能释放了
     * @return
     */
    public boolean isCanRelease(int cdTime)
    {
        return releaseTime == 0 || GameServer.currentTime - releaseTime >= cdTime;
    }


    public void setReleaseInfo(long time)
    {
        releaseTime = time;
        releaseCount++;
    }

    public List<BattleAObject> getTargetTempList(boolean checkAtkRange)
    {
        final int effectTargetGroup = skillConfig.effectTargetGroup;

        List<BattleAObject> tempList = new CopyOnWriteArrayList<>();
        List<BattleAObject> masterList = new CopyOnWriteArrayList<>();
        if(effectTargetGroup == ConstantFactory.EFFECT_TARGET_GROUP_ALL)
        {
            tempList.addAll(releaser.battleController.getSoldiers());
            BattleAObject attackerMaster = spriteController.getAttachment().getMasterSoldier();
            BattleAObject otherMaster= spriteController.getAttachment().getTargetBattleTmp().getMasterSoldier();
            if(BattleUtils.getDistance(releaser.position, attackerMaster.position) <= BattleUtils.getDistance(releaser.position, otherMaster.position))
            {
                masterList.add(attackerMaster);
                masterList.add(otherMaster);
            }
            else
            {
                masterList.add(otherMaster);
                masterList.add(attackerMaster);
            }
        }
        else if(effectTargetGroup == ConstantFactory.EFFECT_TARGET_GROUP_PARTNER)
        {
            tempList.addAll(spriteController.getAttachment().getSoldierList());
            masterList.add(spriteController.getAttachment().getMasterSoldier());
        }
        else if(effectTargetGroup == ConstantFactory.EFFECT_TARGET_GROUP_ENEMY)
        {
            tempList.addAll(spriteController.getAttachment().getTargetBattleTmp().getSoldierList());
            masterList.add(spriteController.getAttachment().getTargetBattleTmp().getMasterSoldier());
        }

        List<BattleAObject> result = new CopyOnWriteArrayList<>();
        if(effectTargetGroup != ConstantFactory.EFFECT_TARGET_GROUP_SELF)
        {
            //检测是否在攻击范围和攻击对象
            for(BattleAObject battleAObject : tempList)
            {
                if(battleAObject.isDie())
                    continue;

                if(!checkUnitType(battleAObject))
                    continue;

                if(checkAtkRange && !isInAtkRange(releaser, battleAObject))
                    continue;

                result.add(battleAObject);
            }

            if(result.size() == 0)
            {
                for(BattleAObject battleAObject : masterList)
                {
                    if(checkAtkRange && !isInAtkRange(releaser, battleAObject))
                        continue;

                    result.add(battleAObject);
                }
            }
        }
        else
        {
            result.addAll(tempList);
        }

        return result;
    }


    /**
     * 获取技能目标参考点
     * @return
     */
    public BattleAObject getTargetStartPoint(List<BattleAObject> tempList)
    {
        if(tempList == null | tempList.size() == 0)
            return null;

        if(skillConfig.effectTargetType == ConstantFactory.EFFECT_TARGET_TYPE_POINT)
            return null;

        //参考目标
        BattleAObject tempTarget = null;

        if(skillConfig.type == ConstantFactory.SKILL_TYPE_MASTER && spriteController.getAttachment().isAuto())
        {
            final int aiType = skillConfig.aiType;
            if(aiType == ConstantFactory.SKILL_AI_TYPE_MAX_ATK)
            {
                tempTarget = BattleUtils.getMaxAtkUnit(tempList);
            }
            else if(aiType == ConstantFactory.SKILL_AI_TYPE_MIN_HP)
            {
                tempTarget = BattleUtils.getMinHPUnit(tempList);
            }
            else if(aiType == ConstantFactory.SKILL_AI_TYPE_MAX_DEF)
            {
                tempTarget = BattleUtils.getMaxDefUnit(tempList);
            }
            else if(aiType == ConstantFactory.SKILL_AI_TYPE_NEAREST)
            {
                //按照距离排序
                Collections.sort(tempList, new Comparator<BattleAObject>() {
                    @Override
                    public int compare(BattleAObject o1, BattleAObject o2) {
                        return new Float(BattleUtils.getDistance(releaser.position, o1.position)).compareTo(BattleUtils.getDistance(releaser.position, o2.position));
                    }
                });
                tempTarget = tempList.get(0);
            }
        }
        else
        {
            final int effectTargetType = skillConfig.effectTargetType;

            if(effectTargetType == ConstantFactory.EFFECT_TARGET_TYPE_LINE || effectTargetType == ConstantFactory.EFFECT_TARGET_TYPE_ENEMY || effectTargetType == ConstantFactory.EFFECT_TARGET_TYPE_ALL)
            {
                //按照距离排序
                Collections.sort(tempList, new Comparator<BattleAObject>() {
                    @Override
                    public int compare(BattleAObject o1, BattleAObject o2) {
                        return new Float(BattleUtils.getDistance(releaser.position, o1.position)).compareTo(BattleUtils.getDistance(releaser.position, o2.position));
                    }
                });
                tempTarget = tempList.get(0);
            }
            else if(effectTargetType == ConstantFactory.EFFECT_TARGET_TYPE_SELF)
            {
                tempTarget = releaser;
            }
            else if(effectTargetType == ConstantFactory.EFFECT_TARGET_TYPE_MIN_HP)
            {
                tempTarget = BattleUtils.getMinHPUnit(tempList);
            }
            else if(effectTargetType == ConstantFactory.EFFECT_TARGET_TYPE_MAX_ATK)
            {
                tempTarget = BattleUtils.getMaxAtkUnit(tempList);
            }
            else if(effectTargetType == ConstantFactory.EFFECT_TARGET_TYPE_MAX_DEF)
            {
                tempTarget = BattleUtils.getMaxDefUnit(tempList);
            }
        }

        return tempTarget;
    }

    /**
     * 获取目标(主将技能手动释放)
     * @param position
     * @return
     */
    public List<BattleAObject> getTarget(Position position)
    {
        final int[] effectRange = skillConfig.effectRange;
        List<BattleAObject> tempList = getTargetTempList(false);
        List<BattleAObject> result = new CopyOnWriteArrayList<>();
        if(effectRange[0] == ConstantFactory.SKILL_RANGE_TYPE_CIRCLE)
        {
            for(BattleAObject battleAObject : tempList)
            {
                if(BattleUtils.isInCircleRange(position, battleAObject.position, effectRange[1]))
                    result.add(battleAObject);
            }
        }
        else if(effectRange[0] == ConstantFactory.SKILL_RANGE_TYPE_RECTANGLE)
        {
            for(BattleAObject battleAObject : tempList)
            {
                if(BattleUtils.isInRectangleByCenter(releaser.position, position, battleAObject.position, effectRange[1], effectRange[2]))
                    result.add(battleAObject);
            }
        }
        return result;
    }

    /**
     * 获取技能目标
     * @return
     */
    public List<BattleAObject> getTarget(Position position, List<BattleAObject> tempList, BattleAObject tempTarget)
    {
        List<BattleAObject> result = new CopyOnWriteArrayList<>();

        final int[] effectRange = skillConfig.effectRange;
        final int effectTargetType = skillConfig.effectTargetType;

        if(effectRange[0] == ConstantFactory.SKILL_RANGE_TYPE_SINGLE)
        {
            if(tempTarget != null)
            {
                Collections.sort(tempList, new Comparator<BattleAObject>() {
                    @Override
                    public int compare(BattleAObject o1, BattleAObject o2) {
                        return new Float(BattleUtils.getDistance(releaser.position, o1.position)).compareTo(BattleUtils.getDistance(releaser.position, o2.position));
                    }
                });
                result.add(tempList.get(0));
            }
            else
            {
                result.add(tempTarget);
            }
        }
        else if(effectRange[0] == ConstantFactory.SKILL_RANGE_TYPE_CIRCLE)
        {
            for(BattleAObject battleAObject : tempList)
            {
                if(BattleUtils.isInCircleRange(position, battleAObject.position, effectRange[1]))
                    result.add(battleAObject);
            }
        }
        else if(effectRange[0] == ConstantFactory.SKILL_RANGE_TYPE_RECTANGLE)//矩形
        {
            for(BattleAObject battleAObject : tempList)
            {
                //连线,表示以自己为起点
                if(effectTargetType == ConstantFactory.EFFECT_TARGET_TYPE_LINE)
                {
                    if(BattleUtils.isInRectangleByBorder(releaser.position, position, battleAObject.position, effectRange[1], effectRange[2], true))
                        result.add(battleAObject);
                }
                else if(effectTargetType == ConstantFactory.EFFECT_TARGET_TYPE_ENEMY)//表示以敌人为起点
                {
                    if(BattleUtils.isInRectangleByBorder(releaser.position, position, battleAObject.position, effectRange[1], effectRange[2], false))
                        result.add(battleAObject);
                }
            }

        }
        else if(effectRange[0] == ConstantFactory.SKILL_RANGE_TYPE_SECTOR)//扇形
        {
            for(BattleAObject battleAObject : tempList)
            {
                //连线,表示以自己为起点
                if(effectTargetType == ConstantFactory.EFFECT_TARGET_TYPE_LINE)
                {
                    if(BattleUtils.isInSector(releaser.position, position, battleAObject.position, effectRange[1], effectRange[2], true))
                        result.add(battleAObject);
                }
                else if(effectTargetType == ConstantFactory.EFFECT_TARGET_TYPE_ENEMY)//表示以敌人为起点
                {
                    if(BattleUtils.isInSector(releaser.position, position, battleAObject.position, effectRange[1], effectRange[2], false))
                        result.add(battleAObject);
                }
            }
        }

        return result;
    }

    /**
     * 需要手动选择目标点的主将技能
     * @param position
     */
    public void releaseByPoint(Position position)
    {
        BattleUtils.fixPosition(position);

        if(skillConfig.type != ConstantFactory.SKILL_TYPE_MASTER || skillConfig.effectTargetType != ConstantFactory.EFFECT_TARGET_TYPE_POINT)
            return;

        if(skillConfig.effectSpeed == 0)//及时释放
        {
            if(skillConfig.effectTime > 0)//暴风雪
            {
                logger.debug("释放大招技能");
                releaser.battleController.addExtendEffect(spriteController.getAttachment(), this, releaser.position, position);
            }
            else
            {
                List<BattleAObject> targets = getTarget(position);
                logger.debug("主将技能选定 目标:" + targets);
                if(targets != null && targets.size() > 0)
                {
                    releaseFlash(targets, position);
                }
                else
                {
                    releaser.battleController.sendUseSkill(skillConfig.getId(), releaser.getUniqueId(), null, 0, position);
                }
            }
        }
        else
        {
            float time = getEffectDelayTime(releaser.position, position);
            logger.debug("BattleSkill release:" + time + " Math.round(time):" + Math.round(time));
            if(skillConfig.lockType == ConstantFactory.SKILL_LOCK_YES)
            {
                List<BattleAObject> targets = getTarget(position);
                if(targets != null && targets.size() > 0)
                {
                    releaseTime(targets, Math.round(time));
                }
            }
            else
            {
                releaseTime(position, Math.round(time));
            }
        }

        setReleaseInfo(GameServer.currentTime);
        spriteController.getAttachment().addCrystal(-skillConfig.crystal);
    }

    /**
     * 释放技能
     * @return
     */
    public boolean release(BattleAObject... defaultTargets)
    {
        if(releaser.battleController.isEnd())
            return false;

        if(defaultTargets != null && defaultTargets.length > 0)
        {
            List<BattleAObject> targets = new CopyOnWriteArrayList<>();
            for(BattleAObject battleAObject : defaultTargets)
            {
                targets.add(battleAObject);
            }
            if(skillConfig.effectSpeed == 0)//及时释放
            {
                releaseFlash(targets, defaultTargets[0].position);
            }
            else
            {
                float time = getEffectDelayTime(releaser.position, defaultTargets[0].position);
                if(skillConfig.lockType == ConstantFactory.SKILL_LOCK_YES)
                {
                    releaseTime(targets, Math.round(time));
                }
                else
                {
                    releaseTime(defaultTargets[0].position, Math.round(time));
                }
            }
        }
        else
        {
            if(skillConfig.effectType == ConstantFactory.SKILL_EFFECT_TYPE_BUFF || skillConfig.effectType == ConstantFactory.SKILL_EFFECT_TYPE_ENTER)
            {
                List<BattleAObject> targets = new CopyOnWriteArrayList<>();
                targets.add(releaser);
                releaseFlash(targets, releaser.position);

                logger.debug("BattleSkill release 释放技能 -> 攻击者:" + releaser + " 技能:" + skillConfig.id + "-" + skillConfig.name + " 攻击:" + releaser);
            }
            else
            {
                List<BattleAObject> tempList = getTargetTempList(true);
                if(tempList == null || tempList.size() == 0)
                    return false;

                //参考点对象
                BattleAObject tempTarget = getTargetStartPoint(tempList);
                if(tempTarget == null)
                    return false;

                logger.debug("BattleSkill release 释放技能 -> 攻击者:" + releaser + " 技能:" + skillConfig.id + "-" + skillConfig.name + " 攻击参考人:" + tempTarget);

                if(skillConfig.effectSpeed == 0)//及时释放
                {
                    List<BattleAObject> targets = getTarget(tempTarget.position, tempList, tempTarget);
                    if(targets != null && targets.size() > 0)
                    {
                        releaseFlash(skillConfig.effectTargetType == ConstantFactory.EFFECT_TARGET_TYPE_ALL ? tempList : targets, tempTarget.position);
                    }
                }
                else
                {
                    float time = getEffectDelayTime(releaser.position, tempTarget.position);
                    if(skillConfig.lockType == ConstantFactory.SKILL_LOCK_YES)
                    {
                        List<BattleAObject> targets = getTarget(tempTarget.position, tempList, tempTarget);
                        if(targets != null && targets.size() > 0)
                        {
                            releaseTime(targets, Math.round(time));
                        }
                    }
                    else
                    {
                        releaseTime(tempTarget.position, Math.round(time));
                    }
                }
            }
        }

        setReleaseInfo(GameServer.currentTime);
        spriteController.getAttachment().addCrystal(-skillConfig.crystal);

        //伤害性技能设置攻击时间,用来用冲锋BUFF的检测
        if(skillConfig.isDamageSkill())
        {
            releaser.setAtkTime(GameServer.currentTime);
        }

        return true;
    }



    private float getEffectDelayTime(Position source, Position target)
    {
        float time = 0;
        if(skillConfig.delayType == ConstantFactory.SKILL_EFFECT_DELAY_SPEED)
        {
            float distance = BattleUtils.getDistance(source, target);
            time = distance / skillConfig.effectSpeed * 1000;
        }
        else if(skillConfig.delayType == ConstantFactory.SKILL_EFFECT_DELAY_TIME)
        {
            time = skillConfig.effectSpeed;
        }
        return time;
    }
    /**
     * 效果实施作用
     * @param target
     */
    public void takeEffect(BattleAObject target)
    {
        if(target == null)
            return;

        logger.debug(skillConfig.name + "   开始进行伤害..................................................." + skillConfig.effectType);
        try{
            switch (skillConfig.effectType)
            {
                case ConstantFactory.SKILL_EFFECT_TYPE_DAMAGE_ATK:
                case ConstantFactory.SKILL_EFFECT_TYPE_DAMAGE_DEF:
                case ConstantFactory.SKILL_EFFECT_TYPE_DAMAGE_MAX_HP:
                case ConstantFactory.SKILL_EFFECT_TYPE_DAMAGE_TARGET_MAX_HP_RATE:
                case ConstantFactory.SKILL_EFFECT_TYPE_ABSORB_HP:
                    TwoTuple damageObj = releaser.getDamage(target, skillConfig);
                    int damage = Integer.parseInt(damageObj.getSecond().toString());
                    target.addHP(-damage);
                    releaser.battleController.sendDamage(releaser.getUniqueId(), skillConfig.id, target.getUniqueId(), -damage, target.getHp());

                    logger.info("技能效果生效 -> " + skillConfig.getName() + " 释放者:" + releaser.getName() + " 目标:" + target.getName() + " 伤害值:" + damage + " 剩余生命:" + target.getHp());

                    if(skillConfig.effectType == ConstantFactory.SKILL_EFFECT_TYPE_ABSORB_HP)
                    {
                        releaser.addHP(damage);
                        releaser.battleController.sendDamage(releaser.getUniqueId(), skillConfig.id, releaser.getUniqueId(), damage, releaser.getHp());

                        logger.info("同时吸血加给自己!");
                    }

                    target.checkBeAtkSkill();

                    break;
                case ConstantFactory.SKILL_EFFECT_TYPE_ADD_HP_ATK:
                    int addHP = releaser.getAddHP(skillConfig);
                    target.addHP(addHP);

                    logger.info("技能效果生效 -> " + skillConfig.getName() + " 释放者:" + releaser.getName() + " 目标:" + target.getName() + " 加血值:" + addHP + " 剩余生命:" + target.getHp());

                    releaser.battleController.sendDamage(releaser.getUniqueId(), skillConfig.id, target.getUniqueId(), addHP, target.getHp());
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 及时效果释放
     * @param targets
     */
    public void releaseFlash(List<BattleAObject> targets, Position position)
    {
        logger.info("针对指定目标释放技能 -> " + skillConfig.getName() + " 攻击方:" + releaser.baseInfo() + " 目标:" + targets);

        releaser.battleController.sendUseSkill(skillConfig.getId(), releaser.getUniqueId(), targets, 0, position);

        for(BattleAObject target : targets)
        {
            takeEffect(target);
        }

        releaseBuff(targets);
    }


    /**
     * 延时效果释放
     */
    public void releaseTime(Position position, int time)
    {
        logger.info("针对指定位置释放技能 -> " + skillConfig.getName() + " 攻击方:" + releaser.baseInfo() + " 目标位置:" + position + " time:" + time);

        releaser.battleController.sendUseSkill(skillConfig.getId(), releaser.getUniqueId(), null, time, position);

        timeSkillDamages.add(new BattleTimeSkillDamage(this, GameServer.currentTime + time, position));
//        effectTargets = null;
//        effectPos = position;
//        effectEndTime = GameServer.currentTime + time;
    }

    /**
     * 延时效果释放
     */
    public void releaseTime(List<BattleAObject> targets, int time)
    {
        logger.info("针对指定目标释放技能 -> " + skillConfig.getName() + " 攻击方:" + releaser.baseInfo() + " 目标:" + targets + " time:" + time);

        if(targets == null || targets.size() == 0)
            return;

        releaser.battleController.sendUseSkill(skillConfig.getId(), releaser.getUniqueId(), targets, time, null);

        timeSkillDamages.add(new BattleTimeSkillDamage(this, GameServer.currentTime + time, targets));
//        effectPos = null;
//        addAtkTarget(targets);
//        effectEndTime = GameServer.currentTime + time;
//
//        logger.warn("结束时间:" + TimeUtils.getFormatTime(effectEndTime) + "  endTime:" + effectEndTime + "  currentTime:" + GameServer.currentTime);
    }

//    /**
//     * 延时效果生效
//     */
//    public void timeEffectStart()
//    {
//        logger.warn("BattleSkill timeEffectStart 延时效果时间到了，触发伤害 -> effectPos:" + effectPos + " targets:" + effectTargets + " 技能:" + skillConfig.getName());
//        if(effectPos != null)
//        {
//            if(skillConfig.effectTime > 0)//暴风雪
//            {
//                logger.debug("释放大招技能");
//                releaser.battleController.addExtendEffect(spriteController.getAttachment(), this, releaser.position, effectPos);
//            }
//            else
//            {
////                if(effectTime <= 0)
////                {
//                    List<BattleAObject> tempList = getTargetTempList();
//                    if(tempList != null)
//                    {
//                        BattleAObject tempTarget = getTargetStartPoint(tempList);
//                        if(tempTarget != null)
//                        {
//                            List<BattleAObject> targets = getTarget(tempTarget, tempList);
//                            if(targets != null)
//                            {
//                                for(BattleAObject target: targets)
//                                {
//                                    takeEffect(target);
//                                }
//
//                                releaseBuff(targets);
//                            }
//                        }
//                    }
//                    effectPos = null;
////                }
//            }
//        }
//        else if(effectTargets != null)
//        {
//            for(BattleAObject target: effectTargets)
//            {
//                takeEffect(target);
//
//                if(target.isDie())
//                {
//                    effectTargets.remove(target);
//                }
//            }
//
//            releaseBuff(effectTargets);
//
//            effectTargets = null;
//        }
//
//        effectEndTime = 0;
//    }
//
//    /**
//     * 延时效果是否能生效
//     * @return
//     */
//    public boolean checkTimeEffectStart()
//    {
//        logger.warn("endTime: " + effectEndTime  + " 当前时间:" + GameServer.currentTime);
//        return (effectPos != null || effectTargets != null) && effectEndTime > 0 && skillConfig.effectSpeed > 0 && effectEndTime <= GameServer.currentTime;
//    }


    /**
     * 检测技能是否能对此单位有效果
     * @param target
     * @return
     */
    public boolean checkUnitType(BattleAObject target)
    {
        if(target instanceof BattleAObject)
            return true;

        //检测是否是自己的攻击类型
        if(skillConfig.effectUnitType == ConstantFactory.UNIT_TYPE_ALL)
        {
            return true;
        }

        return skillConfig.effectUnitType == target.getUnitType();
    }

    public boolean checkTargetGroup(BattleAObject source, BattleAObject target)
    {
        if(skillConfig.effectTargetGroup == ConstantFactory.EFFECT_TARGET_GROUP_ALL)
            return true;

        if(skillConfig.effectTargetGroup == ConstantFactory.EFFECT_TARGET_GROUP_SELF)
            return source.getUniqueId() == target.getUniqueId();

        if(skillConfig.effectTargetGroup == ConstantFactory.EFFECT_TARGET_GROUP_ENEMY)
            return source.getTeamNo() != target.getTeamNo();

        if(skillConfig.effectTargetGroup == ConstantFactory.EFFECT_TARGET_GROUP_PARTNER)
            return source.getTeamNo() == target.getTeamNo();

        return false;
    }

    public void releaseBuff(List<BattleAObject> skillTargets)
    {
        for(BattleAObject target : skillTargets)
        {
            if(target.isDie())
                continue;

            for(BuffConfig buffConfig : skillConfig.buffList)
            {
                if(buffConfig.isFollowSkill())
                {
                    target.addBuff(releaser, buffConfig, skillConfig.id);
                }
            }
        }

        if(!releaser.isDie())
        {
            for(BuffConfig buffConfig : skillConfig.buffList)
            {
                if(buffConfig.isToSelf())
                {
                    releaser.addBuff(releaser, buffConfig, skillConfig.id);
                }
            }
        }
    }


    /**
     * 是否在攻击范围内
     * @param source
     * @param target
     * @return
     */
    public boolean isInAtkRange(BattleAObject source, BattleAObject target)
    {
//        logger.warn("source:" + source.getSprite());
//        logger.warn("target:" + target.getSprite());
//        logger.warn(skill.name + "  检测攻击范围: source:" + source.position + "  target:" + target.position);
        float ax = source.position.getX();
        float ay = source.position.getY();
        float bx = target.position.getX();
        float by = target.position.getY();
        float distance = BattleUtils.getDistance(source.position, target.position);
        if(distance > 0)
        {
            float aax = ax + (source.getSprite().gridArea - 1) / distance * (bx - ax);
            float aay = ay + (source.getSprite().gridArea - 1) / distance * (by - ay);
            float bbx = bx + (target.getSprite().gridArea - 1) / distance * (ax - bx);
            float bby = by + (target.getSprite().gridArea - 1) / distance * (ay - by);

            return BattleUtils.isInCircleRange(new Position(aax, aay), new Position(bbx, bby), skillConfig.atkRange);
        }
        else
        {
            return true;
        }

//        logger.warn("aax:" + aax + " aay:" + aay + " bbx:" + bbx + " bby:" + bby + "  atkRange:" + skill.atkRange);
    }



    public int getId()
    {
        return skillConfig.id;
    }

    public SkillConfig getSkillConfig()
    {
        return skillConfig;
    }

    public SpriteController getSpriteController() {
        return spriteController;
    }

    public BattleAObject getReleaser() {
        return releaser;
    }

    //    public static void main(String[] args) {
//        Position source = new Position(41.77208f, 12.500001f);
//        Position target = new Position(44.8308f, 14.060236f);
//        float ax = source.getX();
//        float ay = source.getY();
//        float bx = target.getX();
//        float by = target.getY();
//        float distance = BattleUtils.getDistance(source, target);
//        if(distance > 0)
//        {
//            float aax = ax + (2 - 1) / distance * (bx - ax);
//            float aay = ay + (2 - 1) / distance * (by - ay);
//            float bbx = bx + (3 - 1) / distance * (ax - bx);
//            float bby = by + (3 - 1) / distance * (ay - by);
//
//            boolean result = BattleUtils.isInCircleRange(new Position(aax, aay), new Position(bbx, bby), 1);
//            System.out.println("result:" + result);
//        }
//    }

    private int timeSkillId;

    public int createTimeSkillId()
    {
        return ++timeSkillId;
    }


    public String baseInfo()
    {
        return "{id=" + skillConfig.id +
                ",name=" + skillConfig.name +
                "}";
    }

    @Override
    public String toString() {
        return "BattleSkill{" +
                "releaser=" + releaser +
                ", skill=" + skillConfig +
                "} ";
    }
}
