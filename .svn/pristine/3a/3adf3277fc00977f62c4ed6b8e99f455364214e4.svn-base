package com.douqu.game.battle.entity.tmp;

import com.douqu.game.battle.entity.aobject.BattleAObject;
import org.apache.log4j.Logger;


/**
 * Created by bean on 2017/9/13.
 */
public class MonsterBattleTmp extends SpriteBattleTmp {

    Logger logger = Logger.getLogger(MonsterBattleTmp.class);


//    @Override
//    public boolean checkAI()
//    {
//        if(spriteController.isPlayer())
//            return false;
//
//        if(CodeFactory.TEST && soldierList.size() > 0)
//        {
//            //测试时只出一个兵
////            logger.info("AI已经出兵:" + soldierList.size());
//            return false;
//        }
//
//        //对手场上的兵
//        List<BattleAObject> list = targetBattleTmp.getSoldierList();
//
//        if(list.size() > 0)
//        {
//            logger.info(spriteController.getName() + " team:" + teamNo + " 开始检测AI出牌");
//
//            Card restrictTo = null, restrictToMe = null;
//            DataFactory dataFactory = DataFactory.getInstance();
//            Soldier soldier = null;
//            Soldier targetSoldier = null;
//            choose:for(Card card : cardList)
//            {
//                soldier = dataFactory.getGameObject(DataFactory.SOLDIER_KEY, card.soldierId);
//                for(BattleAObject ba : list)
//                {
//                    if(ba.isDie())
//                    {
//                        logger.info(ba.getId()+" 已经死亡 ");
//                        continue;
//                    }
//
//                    if(restrictTo != null && restrictToMe != null)
//                        break choose;
//
//                    targetSoldier = ((BattleSoldierAObject) ba).getSoldier();
//                    if(soldier.isRestrict(targetSoldier))
//                    {
////                        logger.info("检查到克制关系,再检测水晶是否够:当前水晶:" + curCrystal+" 需要的水晶:" + card.crystal);
//                        //被我克制
//                        restrictTo = card;
//                    }
//
//                    if(!targetSoldier.isRestrict(soldier))
//                    {
////                        logger.info("检查到对方对我的克制关系,再检测水晶是否够:当前水晶:" + curCrystal+" 需要的水晶:" + card.crystal);
//                        restrictToMe = card;
//                    }
//                }
//            }
//            logger.info("restrictTo:" + restrictTo + "  restrictToMe:" + restrictToMe);
//            //若没克制关系，则出战力最高的兵放在中间的路线
//            Card result = null;
//            if(restrictTo == null)
//            {
//                result = restrictToMe == null ? getMaxFCCard() : restrictToMe;
//            }
//            else
//            {
//                result = restrictTo;
//            }
//
//            if(result != null)
//            {
//                logger.info("当前水晶:" + curCrystal + " 需要水晶:" + result.crystal);
//                result = curCrystal >= result.crystal ? result : null;
//            }
//
//            logger.info("result:" + result);
//            if(result != null)
//            {
//                //兵种上场
//                soldierBorn(result, null);
//            }
//            else
//            {
//                return false;
//            }
//        }
//        else
//        {
//            if(isMaxCrystal())
//            {
//                BattleMasterAObject masterAObject = (BattleMasterAObject) masterSoldier;
//                long currentTime = System.currentTimeMillis();
//                //检测是否主将技能是否可以释放（CD时间是否到了）
//                BattleSkill skill = masterAObject.getReleaseSkill(currentTime);
//                if(skill != null)
//                {
//                    //技能选择目标规则
//                    BattleAObject target = null;
//                    if (skill.getSkill().effectType == ConstantFactory.SKILL_EFFECT_TYPE_ADD_HP) {
//                        if (skill.getSkill().effectTargetGroup == ConstantFactory.EFFECT_TARGET_GROUP_PARTNER) {
//                            // 1.己方增益->治疗->己方损失血量最多的单位
//                            BattleAObject temp = getMaxLossHPSoldier();
//                            if (temp == null)
//                                target = masterAObject;
//                            else
//                                target = temp.getLossHp() < masterSoldier.getLossHp() ? masterAObject : temp;
//
//                            if (target.getLossHp() == 0)
//                                target = null;
//                        }
//                    } else if (skill.getSkill().effectType == ConstantFactory.SKILL_EFFECT_TYPE_SUB_HP) {
//                        if (skill.getSkill().effectTargetGroup == ConstantFactory.EFFECT_TARGET_GROUP_ENEMY) {
//                            // 2.敌方减益->对方场上是否有兵->有->敌方战力最高的兵
//                            //                          ->无->敌方主将
//                            target = targetBattleTmp.getMaxFCSoldier();
//                            target = target == null ? targetBattleTmp.getMasterSoldier() : target;
//                        }
//                    }
//
//                    if (target != null)
//                    {
//                        logger.info(getTeamNo() + " AI 释放主将技能! 目标:" + target);
//                        skill.release(target.getPosition(), GameServer.currentTime, target);
//                        addCrystal(-skill.getSkill().crystal);
//                    }
//                }
//                else
//                {
//                    //若无，则出战力最强的兵，放在中间路线
//                    logger.info("出战力最强的兵，放在中间路线");
//                    Card card = getMaxFCCard();
//                    if(card != null)
//                    {
//                        soldierBorn(card, null);
//                    }
//                }
//
//            }
//            else
//            {
//                return false;
//            }
//        }
//
//        return true;
//    }




//    @Override
//    public BattleAObject createSoldier(Soldier soldier, Card card, Position pos)
//    {
//        BattleAObject battleSoldierAObject = new BattleSoldierAObject(spriteController, soldier, battleController.getSoldierUniqueId(), card);
//        battleSoldierAObject.setTeamNo(teamNo);
//        if(pos == null)
//            battleSoldierAObject.setPosition(battleController.getMiddlePos(teamNo));
//        else
//            battleSoldierAObject.setPosition(pos);
//
//        return battleSoldierAObject;
//    }

    @Override
    public String getSpriteObjectIndex()
    {
        return null;
    }


}
