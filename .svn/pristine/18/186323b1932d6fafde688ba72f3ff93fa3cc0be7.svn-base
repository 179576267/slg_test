package com.douqu.game.core.config.battle;


import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.util.LoadUtils;
import com.douqu.game.core.util.Utils;

/**
 * Created by bean on 2017/7/27.
 */
public class SkillConfig extends GameObject {

    public String icon;

    public String action;

    public String skillScript;

    /**
     * 类型
     * 1。主将技能
     * 2。出场技能
     * 3。死亡技能
     * 4。周期技能
     * 5。普攻技能
     */
    public int type;

    public int cdTime;

    public int crystal;

    /**
     * 需要卡的突破等级
     */
    public int breakLv;

    /**
     * 攻击距离
     * 0表示全屏
     */
    public int atkRange;

    /** 效果范围
     * 1.单体 类型
     * 2.圆形 类型;半径
     * 3.扇形 类型;半径;角度
     * 4.矩形 类型;长;宽
     * */
    public int[] effectRange;

    /**
     * 效果类型
     */
    public int effectType;

    /**
     * 效果值
     */
    public int effectValue;

    /**
     * 作用对象
     * 1。敌方
     * 2。友方
     * 3。全局
     * 4。自己
     */
    public int effectTargetGroup;

    /**
     * 施法对象
     * 1.自己
     * 2.敌我连线
     * 3.敌方
     * 4.坐标点(主将技能手动选择)
     * 5.全体
     * 6.血量最少的
     * 7.攻击最少的
     * 8.防御最高的
     */
    public int effectTargetType;

    /**
     * 作用单位类型
     * 1。地面
     * 2。飞行
     */
    public int effectUnitType;

    /**
     * 是否锁敌
     * 1.锁敌
     * 2.不锁敌
     */
    public int lockType;

    /**
     * 时间类型
     * 1.瞬发
     * 2.持续
     */
    public int timeType;

    /**
     * 持续时间(毫秒)
     */
    public int effectTime;

    /**
     * 延迟类型
     * 1.子弹飞行速度(effectSpeed的单位为米)
     * 2.固定延迟(effectSpeed的单位为毫秒)
     */
    public int delayType;

    /**
     * 效果速度
     */
    public int effectSpeed;

    /**
     *
     */
    public BuffConfig[] buffList;

    /**
     * 技能系数
     */
    public float coefficient;

    /**
     * 伤害次数(客户端用)
     */
    public int damageCount;

    /**
     * AI类型
     * 1.血量最少的单位
     * 2.攻击最高的单位
     * 3.防御最高的单位
     */
    public int aiType;

    /**
     * 等级
     */
    public int lv;

    @Override
    public void check()
    {
        if(effectTargetGroup == ConstantFactory.EFFECT_TARGET_GROUP_SELF)
        {
            if(effectTargetType != ConstantFactory.EFFECT_TARGET_TYPE_SELF)
            {
                System.out.println("Load Skill effectTarget Error1 -> id:" + id + " effectTargetGroup:" + effectTargetGroup + " effectTargetType:" + effectTargetType);
            }
        }

        if(effectTargetType == ConstantFactory.EFFECT_TARGET_TYPE_SELF && effectRange[0] == ConstantFactory.SKILL_RANGE_TYPE_SINGLE)
        {
            if(effectTargetGroup == ConstantFactory.EFFECT_TARGET_GROUP_ENEMY)
            {
                System.out.println("Load Skill effectTarget Error0 -> id:" + id + " effectTargetGroup:" + effectTargetGroup + " effectTargetType:" + effectTargetType);
            }
        }

        switch (effectTargetGroup)
        {
            case ConstantFactory.EFFECT_TARGET_GROUP_ALL:
            case ConstantFactory.EFFECT_TARGET_GROUP_ENEMY:
            case ConstantFactory.EFFECT_TARGET_GROUP_PARTNER:
            case ConstantFactory.EFFECT_TARGET_GROUP_SELF:
                break;
            default:
                System.out.println("Load Skill effectTargetGroup Error -> id:" + id + " name:" + name);
                break;
        }

        if(effectRange == null || effectRange.length == 0)
        {
            System.out.println("Load Skill effectRange Error1 -> id:" + id + " name:" + name);
        }

        if(effectRange[0] == 0)
        {
            System.out.println("Load Skill effectRange Error11 -> id:" + id + " name:" + name);
        }

        if(effectRange[0] == ConstantFactory.SKILL_RANGE_TYPE_CIRCLE && effectRange.length != 2)
        {
            System.out.println("Load Skill effectRange Error2 -> id:" + id + " name:" + name);
        }
        if(effectRange[0] == ConstantFactory.SKILL_RANGE_TYPE_SECTOR && effectRange.length != 3)
        {
            System.out.println("Load Skill effectRange Error31 -> id:" + id + " name:" + name);
        }
        if(effectRange[0] == ConstantFactory.SKILL_RANGE_TYPE_SECTOR && (effectRange[2] <= 0 || effectRange[2] >= 90))
        {
            System.out.println("Load Skill effectRange Error32 -> id:" + id + " name:" + name);
        }
        if(effectRange[0] == ConstantFactory.SKILL_RANGE_TYPE_RECTANGLE && effectRange.length != 3)
        {
            System.out.println("Load Skill effectRange Error4 -> id:" + id + " name:" + name);
        }

        if(effectTargetType == ConstantFactory.EFFECT_TARGET_TYPE_POINT)
        {
            if(effectRange[0] == ConstantFactory.SKILL_RANGE_TYPE_SINGLE)
            {
                System.out.println("Load Skill effectRange Error6 -> id:" + id + " name:" + name);
            }
        }
    }

    /**
     * 是否是伤害性技能
     * @return
     */
    public boolean isDamageSkill()
    {
        switch (effectType)
        {
            case ConstantFactory.SKILL_EFFECT_TYPE_DAMAGE_ATK:
            case ConstantFactory.SKILL_EFFECT_TYPE_DAMAGE_DEF:
            case ConstantFactory.SKILL_EFFECT_TYPE_DAMAGE_MAX_HP:
            case ConstantFactory.SKILL_EFFECT_TYPE_DAMAGE_TARGET_MAX_HP_RATE:
            case ConstantFactory.SKILL_EFFECT_TYPE_ABSORB_HP:
                return true;
            default:
                return false;
        }
    }



    @Override
    public void setVariable(String key, String value)
    {
        if("effectRange".equals(key))
        {
            if(Utils.isNullOrEmpty(value))
            {
                System.out.println("Load Skill effectRange Error -> id:" + id + " value:" + value);
            }
            else
            {
                effectRange = LoadUtils.loadIntArray(key, value);
            }
        }
        else if("buffList".equals(key))
        {
            if(Utils.isErrorValue(value))
                buffList = new BuffConfig[0];
            else
            {
                String[] strs = value.split(ConstantFactory.SEMICOLON);
                buffList = new BuffConfig[strs.length];
                for(int i = 0; i < buffList.length; i++)
                {
                    buffList[i] = DataFactory.getInstance().getGameObject(DataFactory.BUFF_KEY, Integer.parseInt(strs[i]));
                    if(buffList[i] == null)
                    {
                        System.out.println("Load Skill buff is Null -> id:" + strs[i]);
                    }
                }
            }

        }
        else
            super.setVariable(key, value);
    }


}


