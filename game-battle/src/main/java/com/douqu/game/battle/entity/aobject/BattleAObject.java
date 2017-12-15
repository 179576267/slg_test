package com.douqu.game.battle.entity.aobject;

import com.douqu.game.battle.controller.BattleController;
import com.douqu.game.battle.controller.sprite.SpriteController;
import com.douqu.game.battle.entity.BattleSkill;
import com.douqu.game.battle.entity.BuffBox;
import com.douqu.game.battle.entity.tmp.SpriteBattleTmp;
import com.douqu.game.battle.server.GameServer;
import com.douqu.game.battle.util.BattleUtils;
import com.douqu.game.core.astar.AbsCoord;
import com.douqu.game.core.astar.ObjectBound;
import com.douqu.game.core.config.*;
import com.douqu.game.core.config.battle.BuffConfig;
import com.douqu.game.core.config.battle.SkillConfig;
import com.douqu.game.core.config.sprite.MasterConfig;
import com.douqu.game.core.config.sprite.SoldierConfig;
import com.douqu.game.core.config.sprite.Sprite;
import com.douqu.game.core.e.E_Attribute;
import com.douqu.game.core.e.E_BattleAObjectStatus;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.config.common.Position;
import com.douqu.game.core.entity.common.TwoTuple;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.protobuf.SGCommonProto;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 战斗中的对象
 * Created by bean on 2017/7/27.
 */
public class BattleAObject {

    Logger logger = Logger.getLogger(this.getClass());

    protected int uniqueId;

    /**
     * 所属主人
     */
    public SpriteController masterController;

    public BattleController battleController;

    protected E_BattleAObjectStatus status;

    /**
     * 对应的作战基本对象
     */
    protected Sprite sprite;


//    /**
//     * 等级
//     */
//    protected int lv;

    protected int hp;

    protected int maxHp;

    /** 攻击 */
    public BattleAtt atk;
    /** 物理防御 */
    public BattleAtt def;
    /** 力量 */
    protected BattleAtt power;
    /** 敏捷 */
    protected BattleAtt agility;
    /** 智力 */
    protected BattleAtt wisdom;
    /** 暴击率(万分比) */
    protected BattleAtt cri;
    /** 抗暴率(万分比) */
    protected BattleAtt rec;
    /** 增伤率(万分比) */
    protected BattleAtt add;
    /** 免伤率(万分比) */
    protected BattleAtt exd;
    /** 命中率(万分比) */
    protected BattleAtt hit;
    /** 闪避率(万分比) */
    protected BattleAtt eva;
    /** 移动速度 */
    public BattleAtt moveSpeed;
    /** 攻击速度 */
    protected BattleAtt attackSpeed;

    protected BattleSkill[] skills;

    protected BattleSkill defaultSkill;

    protected int teamNo;

    /**
     * 出生点
     */
    public Position bornPoint;
    /** 所在的路线 */
    public int bornRoute;
    /**
     * 位置
     */
    public Position position;

    /**
     * 格子对象
     */
    protected ObjectBound objectBound;

    /**
     * 是否可穿越
     */
    protected boolean isFree = false;

    public BuffBox buffBox;

    /**
     * 前进的目标
     */
    protected BattleAObject goTarget;

    protected Position goTargetPosition;

    /**
     * 寻路的目标的原始点
     * key: 唯一ID
     * value: 寻路时目标所在的位置
     */
    protected Map<Integer, Position> goTargetOriginalPosMap;

    /**
     * 上一次攻击敌人的时间
     */
    protected long atkTime;

    /**
     * 是否能行走
     */
    public boolean isMove = false;


    public BattleAObject(SpriteController masterController, Sprite sprite, int uniqueId, int teamNo, Position position)
    {
        this.uniqueId = uniqueId;
        this.sprite = sprite;
        this.teamNo = teamNo;
        this.masterController = masterController;
        if(masterController.getBattleController() != null)
        {
            this.battleController = masterController.getBattleController();
        }
        else
        {
            logger.error("BattleAObject Init Error: parent is not BattleController!" + masterController.getBattleController());
        }

        //战斗力 = 生命 * 0.1 + 攻击 + 防御 + （暴击率 + 抗暴率 + 命中率 + 闪避率 + 增伤率 + 减伤率）* 0.5
//        this.fc = (int) (hp * 0.1 + atk + def + (cri + rec + add + exd + hit + eva) * 0.5);

        objectBound = new ObjectBound(sprite.gridArea);
        setPosition(position);

        this.buffBox = new BuffBox(battleController, this);

        this.defaultSkill = new BattleSkill(masterController, this, sprite.defaultSkill);

        goTargetOriginalPosMap = new ConcurrentHashMap<>();
    }


    public void update(long currentTime)
    {
        defaultSkill.update();

//        if(defaultSkill.checkTimeEffectStart())
//        {
//            defaultSkill.timeEffectStart();
//        }

        for(BattleSkill skill : skills)
        {
            skill.update();

//            if(skill.checkTimeEffectStart())
//            {
//                skill.timeEffectStart();
//            }
        }

        buffBox.update(currentTime);
    }



    public BattleSkill getReleaseSkill(long currentTime)
    {
        return null;
    }

    /**
     * 是否是同一个目标对象
     * @param target
     * @return
     */
    public boolean isSameGoTarget(BattleAObject target)
    {
        if(goTarget == null || target == null)
            return false;

        return goTarget.uniqueId == target.uniqueId && goTarget.getPosition().isSame(target.getPosition());
    }

    protected void clearGoTarget()
    {
        if(goTarget != null)
        {
            this.goTargetOriginalPosMap.remove(goTarget.uniqueId);
            this.goTarget = null;
            this.goTargetPosition = null;
        }
    }


    protected void setDefaultAtk(BattleAObject target)
    {
        this.status = E_BattleAObjectStatus.BATTLING;

        defaultSkill.setDefaultAtkTarget(target);

        clearGoTarget();
    }


    protected void checkDefaultAtk()
    {
        BattleAObject defaultTarget = defaultSkill.getDefaultAtkTarget();
        if(defaultTarget == null)
            return;

        if(defaultTarget.isDie())
        {
            defaultSkill.clearDefaultAtkTarget();
            this.status = E_BattleAObjectStatus.FREE;
        }
    }


    public int getBaseAtt(int key)
    {
        if(E_Attribute.ATK.getCode() == key)
            return atk.base;
        else if(E_Attribute.DEF.getCode() == key)
            return def.base;
        else if(E_Attribute.HP.getCode() == key)
            return hp;
        else if(E_Attribute.CRI.getCode() == key)
            return cri.base;
        else if(E_Attribute.REC.getCode() == key)
            return rec.base;
        else if(E_Attribute.ADD.getCode() == key)
            return add.base;
        else if(E_Attribute.EXD.getCode() == key)
            return exd.base;
        else if(E_Attribute.HIT.getCode() == key)
            return hit.base;
        else if(E_Attribute.EVA.getCode() == key)
            return eva.base;
        else if(E_Attribute.POWER.getCode() == key)
            return power.base;
        else if(E_Attribute.WISDOM.getCode() == key)
            return wisdom.base;
        else if(E_Attribute.AGILITY.getCode() == key)
            return agility.base;
        else if(E_Attribute.MOVE_SPEED.getCode() == key)
            return moveSpeed.base;
        else if(E_Attribute.ATTACK_SPEED.getCode() == key)
            return attackSpeed.base;

        return 0;
    }

//    public int getPlusAtt(int key)
//    {
//        if(E_Attribute.ATK.getCode() == key)
//            return atk.plus;
//        else if(E_Attribute.DEF.getCode() == key)
//            return def.plus;
//        else if(E_Attribute.HP.getCode() == key)
//            return hp;
//        else if(E_Attribute.CRI.getCode() == key)
//            return cri.plus;
//        else if(E_Attribute.REC.getCode() == key)
//            return rec.plus;
//        else if(E_Attribute.ADD.getCode() == key)
//            return add.plus;
//        else if(E_Attribute.EXD.getCode() == key)
//            return exd.plus;
//        else if(E_Attribute.HIT.getCode() == key)
//            return hit.plus;
//        else if(E_Attribute.EVA.getCode() == key)
//            return eva.plus;
//        else if(E_Attribute.POWER.getCode() == key)
//            return power.plus;
//        else if(E_Attribute.WISDOM.getCode() == key)
//            return wisdom.plus;
//        else if(E_Attribute.AGILITY.getCode() == key)
//            return agility.plus;
//        else if(E_Attribute.MOVE_SPEED.getCode() == key)
//            return moveSpeed.plus;
//        else if(E_Attribute.ATTACK_SPEED.getCode() == key)
//            return attackSpeed.plus;
//
//        return 0;
//    }
//
//    public int getTotalAtt(int key)
//    {
//        if(E_Attribute.ATK.getCode() == key)
//            return atk.getAtt();
//        else if(E_Attribute.DEF.getCode() == key)
//            return def.getAtt();
//        else if(E_Attribute.HP.getCode() == key)
//            return hp;
//        else if(E_Attribute.CRI.getCode() == key)
//            return cri.getAtt();
//        else if(E_Attribute.REC.getCode() == key)
//            return rec.getAtt();
//        else if(E_Attribute.ADD.getCode() == key)
//            return add.getAtt();
//        else if(E_Attribute.EXD.getCode() == key)
//            return exd.getAtt();
//        else if(E_Attribute.HIT.getCode() == key)
//            return hit.getAtt();
//        else if(E_Attribute.EVA.getCode() == key)
//            return eva.getAtt();
//        else if(E_Attribute.POWER.getCode() == key)
//            return power.getAtt();
//        else if(E_Attribute.WISDOM.getCode() == key)
//            return wisdom.getAtt();
//        else if(E_Attribute.AGILITY.getCode() == key)
//            return agility.getAtt();
//        else if(E_Attribute.MOVE_SPEED.getCode() == key)
//            return moveSpeed.getAtt();
//        else if(E_Attribute.ATTACK_SPEED.getCode() == key)
//            return attackSpeed.getAtt();
//
//        return 0;
//    }

    public void addAtt(int key, int value)
    {
        if(value == 0)
            return;

       if(E_Attribute.ATK.getCode() == key)
           atk.addPlus(value);
       else if(E_Attribute.DEF.getCode() == key)
           def.addPlus(value);
       else if(E_Attribute.CRI.getCode() == key)
           cri.addPlus(value);
       else if(E_Attribute.REC.getCode() == key)
           rec.addPlus(value);
       else if(E_Attribute.ADD.getCode() == key)
           add.addPlus(value);
       else if(E_Attribute.EXD.getCode() == key)
           exd.addPlus(value);
       else if(E_Attribute.HIT.getCode() == key)
           hit.addPlus(value);
       else if(E_Attribute.EVA.getCode() == key)
           eva.addPlus(value);
       else if(E_Attribute.POWER.getCode() == key)
           power.addPlus(value);
       else if(E_Attribute.WISDOM.getCode() == key)
           wisdom.addPlus(value);
       else if(E_Attribute.AGILITY.getCode() == key)
           agility.addPlus(value);
       else if(E_Attribute.MOVE_SPEED.getCode() == key)
           moveSpeed.addPlus(value);
       else if(E_Attribute.ATTACK_SPEED.getCode() == key)
           attackSpeed.addPlus(value);
    }


    public int addHP(int value)
    {
        int before = hp;

        hp += value;
        hp = hp < 0 ? 0 : hp;
        hp = hp > maxHp ? maxHp : hp;

        return hp - before;
    }

    /**
     * 获取伤害值
     * @param target
     * @return
     */
    public TwoTuple<SGCommonProto.E_DAMAGE_TYPE, Integer> getDamage(BattleAObject target, SkillConfig skillConfig)
    {
//        if(target.isInvincible())
//            return new TwoTuple<>(SGCommonProto.E_DAMAGE_TYPE.DAMAGE_TYPE_INVIN, 0);

        //一般情况下，伤害值 =[（攻击方最终攻击力-防御方最终防御力）*技能系数+技能基础伤害 ] *[ 1+（攻击方复合属性-防御方复合属性）*0.005 ] *（1+增伤率-免伤率）
        //如果是 skillConfig 表的 effectType 为 14 时，伤害值=（防御方生命上限 * 技能系数 - 防御方最终防御力 + 技能基础伤害）*[ 1+（攻击方复合属性-防御方复合属性）*0.005 ] *（1+增伤率-免伤率）
        //攻击方最终攻击力=初始攻击力+（等级-1）*攻击成长+装备强化+装备进阶+法宝强化+法宝进阶+英雄突破
        //防御方最终防御力=初始防御力+（等级-1）*防御成长+装备强化+装备进阶+法宝强化+法宝进阶+英雄突破
        //闪避的计算：在攻击之前先判定是否闪避，如果闪避则不计算伤害。直接显示“闪避”。闪避判定规则：攻方命中>守方闪避,则必然命中。攻方命中<守方闪避，则守方闪避-攻方命中=实际闪避率
        //暴击的计算：如果判定暴击，则在伤害计算后，给予双倍伤害。并且会显示“暴击”+具体数值的红字显示。暴击判定规则跟闪避一样，只是对抗属性为抗暴率
        //攻击方最终攻击力根据技能的效果类型来定
        //防御方最终防御力根据技能的效果类型来定
        //攻击方复合属性：物理攻击->力量，穿刺攻击->敏捷，魔法攻击->智力
        //防御方复合属性：根据攻击方的攻击类型业取值，取值方式同上
        //技能系数：技能里的ratio值
        //技能等级：技能是有成长体系的，会升级

        int evaValue = hit.getAtt() >= target.eva.getAtt() ? 0 : target.eva.getAtt() - hit.getAtt();
        if(evaValue > 0)
        {
            int r = (int) (Math.random() * 10001);
            if(r <= evaValue)
            {
                logger.debug("闪避成功 -> 攻击方:" + this + " 防御方:" + target);
                return new TwoTuple<>(SGCommonProto.E_DAMAGE_TYPE.DAMAGE_TYPE_MISS, 0);
            }
        }

        //（攻击方最终攻击力-防御方最终防御力）* 技能系数
        float temp1 = 0f;
        if(skillConfig.effectType == ConstantFactory.SKILL_EFFECT_TYPE_DAMAGE_TARGET_MAX_HP_RATE)
        {
            temp1 = target.maxHp * skillConfig.coefficient - target.def.getAtt();
        }
        else
        {
            temp1 = (getFinalAtk(skillConfig) - target.def.getAtt()) * skillConfig.coefficient;
        }
        temp1 = temp1 < temp1 * 0.05 ? (float) (temp1 * 0.05) : temp1;
        //(攻击方复合属性-防御方复合属性)，若< -100 则= -100
        int extAtt = getExtAttr() - target.getExtAttr();
        extAtt = extAtt < -100 ? -100 : extAtt;
        float temp = (float) ((temp1 + skillConfig.effectValue) * (1 + extAtt * 0.005));
        temp = (float) (temp * (1 + add.getAtt() * 0.0001 - exd.getAtt() * 0.0001));//万分比
        int result = (int) Math.ceil(temp);
        result = result <= 0 ? 1 : result;

        result = isRestrict(target) ? (int)(result * (1 + getDamagePlus(target))) : result;

        int criValue = cri.getAtt() >= target.rec.getAtt() ? 0 : target.rec.getAtt() - cri.getAtt();
        if(criValue > 0 && result > 0)        {
            int r = (int) (Math.random() * 10001);//万分比
            if(r <= criValue)
            {
                logger.debug("暴击成功 ->  攻击方:" + this + " 防御方:" + target);
                return new TwoTuple<>(SGCommonProto.E_DAMAGE_TYPE.DAMAGE_TYPE_CRIT, result * 2);
            }
        }

        result = result < atk.getAtt() ? atk.getAtt() : result;

        return new TwoTuple<>(SGCommonProto.E_DAMAGE_TYPE.DAMAGE_TYPE_DEFAULT, result);
    }

    public int getFinalAtk(SkillConfig skillConfig)
    {
        switch (skillConfig.effectType)
        {
            case ConstantFactory.SKILL_EFFECT_TYPE_DAMAGE_ATK:
            case ConstantFactory.SKILL_EFFECT_TYPE_ABSORB_HP:
                return atk.getAtt();
            case ConstantFactory.SKILL_EFFECT_TYPE_DAMAGE_DEF:
                return def.getAtt();
            case ConstantFactory.SKILL_EFFECT_TYPE_DAMAGE_MAX_HP:
                return maxHp;
        }
        return 0;
    }


    /**
     * 获取加血的值
     * @param skillConfig
     * @return
     */
    public int getAddHP(SkillConfig skillConfig)
    {
        //加血值=施法者攻击力*技能系数+技能基础加血
        if(skillConfig.effectType == ConstantFactory.SKILL_EFFECT_TYPE_ADD_HP_ATK)
        {
            float temp = atk.getAtt() * skillConfig.coefficient + skillConfig.effectValue;
            return (int) temp;
        }

        return 0;
    }

    /**
     * 获取伤害加成比率
     * @param target
     * @return
     */
    public float getDamagePlus(BattleAObject target)
    {
        if(sprite instanceof MasterConfig || target instanceof  BattleMasterAObject)
            return 0;

        ProfessionConfig professionConfig = DataFactory.getInstance().getGameObject(DataFactory.PROFESSION_KEY, sprite.profession);
        int damagePlus = professionConfig.getDamagePlus(target.getSprite().profession);

        damagePlus += buffBox.getDamagePlus(target.getSprite());

        ProfessionConfig targetProfessionConfig = DataFactory.getInstance().getGameObject(DataFactory.PROFESSION_KEY, target.getSprite().profession);
        int targetExdamagePlus = targetProfessionConfig.getExdamagePlus(sprite.profession);

        //获取BUFF免疫值
        targetExdamagePlus += target.buffBox.getExdamagePlus(sprite);

        int result = damagePlus > targetExdamagePlus ? damagePlus - targetExdamagePlus : 0;
        return (float) (result * 0.0001);
    }



    /**
     * 获取复合属性
     * @return
     */
    public int getExtAttr()
    {
        if(sprite instanceof MasterConfig)
            return 0;
        else if(sprite instanceof SoldierConfig)
        {
            SoldierConfig soldierConfig = (SoldierConfig) sprite;
            SpriteBattleTmp spriteBattleTmp = masterController.getAttachment();
            if(soldierConfig.profession == ConstantFactory.PROFESSION_WARRIOR)
                return spriteBattleTmp.masterSoldier.power.getAtt();

            if(soldierConfig.profession == ConstantFactory.PROFESSION_ARCHER)
                return spriteBattleTmp.masterSoldier.agility.getAtt();

            if(soldierConfig.profession == ConstantFactory.PROFESSION_ENCHANTER)
                return spriteBattleTmp.masterSoldier.wisdom.getAtt();
        }

        return 0;
    }



    /**
     * 前进
     */
    public void go()
    {
           //在子类实现
    }

    public boolean checkCanAtk(BattleAObject target)
    {
        return false;
    }


    /**
     * 检测攻击目标
     * @param targetList
     * @return
     */
    public TwoTuple<BattleAObject, BattleAObject> checkDefaultSkillTarget(List<BattleAObject> targetList)
    {
        //在子类实现
        return null;
    }



    public void checkDefaultSkillTarget(long currentTime)
    {

    }


    /**
     * 获取到目标的路线
     * @param target
     */
    public void checkRoute(BattleAObject target)
    {

    }


    /**
     * 是否能克制对方
     * @param target
     * @return
     */
    public boolean isRestrict(BattleAObject target)
    {
        return sprite.isRestrict(target.getSprite());
    }


//    /**
//     * 是否在播放效果动作中
//     * @return
//     */
//    public boolean isEffectActioning()
//    {
//        if(defaultSkill.isTimeActioning())
//            return true;
//
//        for(BattleSkill skill : skills)
//        {
//            if(skill.isTimeActioning())
//                return true;
//        }
//
//        return false;
//    }








    public BattleSkill getSkill(int skillId)
    {
        for(BattleSkill skill : skills)
        {
            if(skill.getId() == skillId)
                return skill;
        }
        return null;
    }


    /**
     * 检测出场技能
     */
    public void checkBornSkill()
    {
        if(!isCanAtk())
        {
            //被眩晕了，不能发动攻击
            return;
        }

        if(skills == null)
            return;

        for(BattleSkill skill : skills)
        {
            if(skill.getSkillConfig().type == ConstantFactory.SKILL_TYPE_BORN)
            {
                if(masterController.getAttachment().getCurCrystal() < skill.getSkillConfig().crystal)
                    continue;

                skill.release();
            }
        }
    }

    /**
     * 检测死亡技能
     */
    public void checkDieSkill()
    {
        if(!isCanAtk())
        {
            //被眩晕了，不能发动攻击
            return;
        }

        if(skills == null)
            return;

        for(BattleSkill skill : skills)
        {
            if(skill.getSkillConfig().type == ConstantFactory.SKILL_TYPE_DIE)
            {
                if(masterController.getAttachment().getCurCrystal() < skill.getSkillConfig().crystal)
                    continue;

                skill.release();
            }
        }
    }

    /**
     * 检测CD技能
     */
    public boolean checkCDSkill(long currentTime)
    {
        if(!isCanAtk())
        {
            //被眩晕了，不能发动攻击
            return false;
        }

        if(skills == null)
            return false;

        boolean result = false;

        for(BattleSkill skill : skills)
        {
            if(skill.getSkillConfig().type == ConstantFactory.SKILL_TYPE_CD)
            {
                if(!skill.isCanRelease())
                    continue;

                if(masterController.getAttachment().getCurCrystal() < skill.getSkillConfig().crystal)
                    continue;

                if(skill.release())
                {
                    result = true;
                }
            }
        }

        return result;
    }

    /**
     * 检测被攻击时是否有释放的技能
     * @return
     */
    public boolean checkBeAtkSkill()
    {
        if(!isCanAtk())
        {
            //被眩晕了，不能发动攻击
            return false;
        }

        if(skills == null)
            return false;

        boolean result = false;

        for(BattleSkill skill : skills)
        {
            if(skill.getSkillConfig().type == ConstantFactory.SKILL_TYPE_BE_ATK)
            {
                if(!skill.isCanRelease())
                    continue;

                if(masterController.getAttachment().getCurCrystal() < skill.getSkillConfig().crystal)
                    continue;

                if(skill.release())
                {
                    result = true;
                }
            }
        }

        return result;
    }



    /**
     * 获取损失的HP
     * @return
     */
    public int getLossHp()
    {
        return maxHp - hp;
    }


    /**
     * 是否可以行走
     * @return
     */
    public boolean isCanGo()
    {
        return buffBox.isCanGo();
    }

    /**
     * 是否可以攻击
     * @return
     */
    public boolean isCanAtk()
    {
        return buffBox.isCanAtk();
    }

    public void addBuff(BattleAObject releaser, BuffConfig buffConfig, int skillId)
    {
        if(releaser.isDie())
            return;

        buffBox.addBuff(releaser, buffConfig, skillId);
    }




    public int getRoute()
    {
        return ConstantFactory.ROUTE_TOP;
    }



    public SGCommonProto.BattleUnit.Builder parseBattleUnit()
    {
        SGCommonProto.BattleUnit.Builder data = SGCommonProto.BattleUnit.newBuilder();
        if(sprite instanceof MasterConfig)
            data.setBattleUnit(SGCommonProto.E_BATTLE_UNIT.BATTLE_UNIT_MASTER);
        else if(sprite instanceof SoldierConfig)
            data.setBattleUnit(SGCommonProto.E_BATTLE_UNIT.BATTLE_UNIT_SOLDIER);
        else
            data.setBattleUnit(SGCommonProto.E_BATTLE_UNIT.BATTLE_UNIT_STATIC_NPC);//暂时这样写

        data.setUniqueId(uniqueId);
        data.setObjectId(sprite.id);
        data.setPos(position.parsePos());
        data.setTeamNo(teamNo);
        data.setMaxHP(maxHp);
        data.setHp(hp);
        return data;
    }

    public void setPosition(Position position)
    {
        boolean born = this.position == null;

        this.position = position;

        if(position == null)
        {
            objectBound.setCenter(null);
        }
        else
        {
            objectBound.setCenter(new AbsCoord((float)(position.x+Math.random()),(float)(position.y+Math.random())));

            battleController.setToAstarMap(this);
        }

        if(born)
        {
            this.bornPoint = position;
        }
    }

    /**
     * 是否触发冲锋
     * @return
     */
    public boolean isTriggerToAttack()
    {
        if(GameServer.currentTime - atkTime >= 2000)
        {
            if(buffBox.isHaveToAttackBuff())
                return true;
        }
        return false;
    }



    public int getUniqueId()
    {
        return uniqueId;
    }

    public String getName()
    {
        return sprite.name;
    }

    public Sprite getSprite()
    {
        return sprite;
    }

    public int getUnitType(){
        return sprite.unitType;
    }


    public void setStatus(E_BattleAObjectStatus status) {
        this.status = status;
    }

    public E_BattleAObjectStatus getStatus() {
        return status;
    }

    public boolean isDie()
    {
        return hp <= 0;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean isFree) {
        this.isFree = isFree;
    }


    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getTeamNo() {
        return teamNo;
    }

    public void setTeamNo(int teamNo) {
        this.teamNo = teamNo;
    }

    public Position getPosition() {
        return position;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getObjectId()
    {
        return sprite.id;
    }

    public long getAtkTime() {
        return atkTime;
    }

    public void setAtkTime(long atkTime) {
        this.atkTime = atkTime;
    }

    public ObjectBound getObjectBound() {
        return objectBound;
    }

    public void setObjectBound(ObjectBound objectBound) {
        this.objectBound = objectBound;
    }

    private String getSkillInfo()
    {
        StringBuilder result = new StringBuilder();
        for(BattleSkill battleSkill : skills)
        {
            result.append(battleSkill.baseInfo());
            result.append("\n");
        }
        return result.length() > 0 ? result.substring(0, result.length()-1) : "";
    }




    public String baseInfo()
    {
        return "{" +
                "unique=" + uniqueId +
                ",id=" + sprite.id +
                ",name=" + getName() +
                ",teamNo=" + teamNo +
                ",position=" + position +
                "} ";
    }


    public String detailInfo() {
        return "{" +
                "unique=" + uniqueId +
                ",id=" + sprite.id +
                ",name=" + getName() +
                ",teamNo=" + teamNo +
                ",position=" + position +
                ",moveSpeed=" + moveSpeed +
                ",attackSpeed=" + attackSpeed +
                ",hp=" + hp +
                ",atk=" + atk +
                ",def=" + def +
                ",cri=" + cri +
                ",rec=" + rec +
                ",add=" + add +
                ",exd=" + exd +
                ",hit=" + hit +
                ",eva=" + eva +
                ",power=" + power +
                ",wisdom=" + wisdom +
                ",agility=" + agility +
                ",defaultSkill={" + defaultSkill.baseInfo() + "}" +
                ",skills={" + getSkillInfo() + "}" +
                "} ";
    }

    @Override
    public String toString() {
        return baseInfo();
    }
}
