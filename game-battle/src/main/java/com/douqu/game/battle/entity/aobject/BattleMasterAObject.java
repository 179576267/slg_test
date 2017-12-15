package com.douqu.game.battle.entity.aobject;

import com.douqu.game.battle.controller.sprite.PlayerController;
import com.douqu.game.battle.controller.sprite.SpriteController;
import com.douqu.game.battle.entity.BattleSkill;
import com.douqu.game.battle.util.BattleUtils;
import com.douqu.game.core.config.common.Position;
import com.douqu.game.core.e.E_Attribute;
import com.douqu.game.core.entity.common.TwoTuple;
import com.douqu.game.core.entity.ext.SettingInfo;
import com.douqu.game.core.config.sprite.MasterConfig;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 主将
 * Created by bean on 2017/7/27.
 */
public class BattleMasterAObject extends BattleAObject {

    Logger logger = Logger.getLogger(BattleMasterAObject.class);

    public BattleMasterAObject(SpriteController masterController, MasterConfig masterConfig, int uniqueId, int lv, int teamNo, Position position)
    {
        super(masterController, masterConfig, uniqueId, teamNo, position);

        this.hp = maxHp = masterConfig.getAttribute(E_Attribute.HP.getCode(), lv);

        this.atk     = new BattleAtt(masterConfig.getAttribute(E_Attribute.ATK.getCode(), lv));
        this.def     = new BattleAtt(masterConfig.getAttribute(E_Attribute.DEF.getCode(), lv));
        this.cri     = new BattleAtt(masterConfig.getAttribute(E_Attribute.CRI.getCode(), lv));
        this.rec     = new BattleAtt(masterConfig.getAttribute(E_Attribute.REC.getCode(), lv));
        this.add     = new BattleAtt(masterConfig.getAttribute(E_Attribute.ADD.getCode(), lv));
        this.exd     = new BattleAtt(masterConfig.getAttribute(E_Attribute.EXD.getCode(), lv));
        this.hit     = new BattleAtt(masterConfig.getAttribute(E_Attribute.HIT.getCode(), lv));
        this.eva     = new BattleAtt(masterConfig.getAttribute(E_Attribute.EVA.getCode(), lv));
        this.power   = new BattleAtt(masterConfig.getAttribute(E_Attribute.POWER.getCode(), lv));
        this.agility = new BattleAtt(masterConfig.getAttribute(E_Attribute.AGILITY.getCode(), lv));
        this.wisdom  = new BattleAtt(masterConfig.getAttribute(E_Attribute.WISDOM.getCode(), lv));

        List<Integer> skillIds = null;
        if(masterController instanceof PlayerController)
        {
            PlayerController playerController = (PlayerController) masterController;
            SettingInfo playerSettingInfo = playerController.getPlayer().getExtInfo(SettingInfo.class);
            skillIds = playerSettingInfo.getEquipSkillIds();
        }
        else
        {
            skillIds = masterConfig.getSkills(lv);
        }

        this.skills = new BattleSkill[skillIds.size()];
        for(int i = 0; i < skills.length; i++)
        {
            skills[i] = new BattleSkill(masterController, this, DataFactory.getInstance().getGameObject(DataFactory.SKILL_KEY, skillIds.get(i)));
        }

        logger.info("主将初始化 -> " + detailInfo());
    }




    @Override
    public BattleSkill getReleaseSkill(long currentTime)
    {
        for(BattleSkill skill : skills)
        {
            if(skill.isCanRelease())
                return skill;
        }
        return null;
    }

    @Override
    public TwoTuple<BattleAObject, BattleAObject> checkDefaultSkillTarget(List<BattleAObject> targetList)
    {
        List<BattleAObject> tempList = new CopyOnWriteArrayList<>();
        tempList.addAll(targetList);
        //按照离我的距离排序,最近的排在前面
        Collections.sort(tempList, new Comparator<BattleAObject>() {
            @Override
            public int compare(BattleAObject o1, BattleAObject o2) {
                return new Float(BattleUtils.getDistance(position, o1.position)).compareTo(BattleUtils.getDistance(position, o2.position));
            }
        });

        BattleAObject atkTarget = null;
        for(BattleAObject battleAObject : targetList)
        {
            atkTarget = defaultSkill.isInAtkRange(this, battleAObject) ? battleAObject : null;
            if(atkTarget != null)
                break;
        }

        return new TwoTuple<>(atkTarget, null);
    }

    @Override
    public void checkDefaultSkillTarget(long currentTime)
    {
        if(!isCanAtk())
        {
            //被眩晕了，不能发动攻击
            logger.debug("被眩晕了，不能发动攻击 -> " + this);
            return;
        }

//        if(defaultSkill.isEffectFlying())
//        {
//            return;
//        }

        if(!defaultSkill.isCanRelease())
        {
            return;
        }

        if(defaultSkill.getSkillConfig().crystal > masterController.getAttachment().getCurCrystal())
        {
            return;
        }

        checkDefaultAtk();

        //是否还在攻击范围内
        BattleAObject defaultAtkTarget = defaultSkill.getDefaultAtkTarget();
        if(defaultAtkTarget != null)
        {
            if(defaultSkill.isInAtkRange(this, defaultAtkTarget))
            {
                defaultSkill.release(defaultAtkTarget);

                checkDefaultAtk();
                return;
            }
            else
            {
                defaultSkill.clearDefaultAtkTarget();
            }
        }


        TwoTuple<BattleAObject,BattleAObject> targets = checkDefaultSkillTarget(masterController.getAttachment().getTargetBattleTmp().getSoldierList());
        if(targets.getFirst() != null)
        {
            battleController.sendSoldierBattleStart(this, targets.getFirst());

            defaultSkill.setDefaultAtkTarget(targets.getFirst());

            defaultSkill.release(targets.getFirst());

            checkDefaultAtk();
        }
    }





    @Override
    public String getName()
    {
        return "Master_" + teamNo;
    }


    @Override
    public int getUnitType()
    {
        return ConstantFactory.UNIT_TYPE_LAND;
    }



}
