package com.douqu.game.battle.entity;

import com.douqu.game.battle.entity.aobject.BattleAObject;
import com.douqu.game.battle.util.BattleUtils;
import com.douqu.game.core.config.battle.BuffConfig;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.protobuf.SGCommonProto;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-10-11 18:48
 */
public class BattleBuff {

    Logger logger = Logger.getLogger(this.getClass());

    /**
     * 唯一ID
     */
    public long id;

    public int skillId;

    public BuffConfig buffConfig;

    /** BUFF释放者 */
    public BattleAObject releaser;

    /** BUFF在哪个身上 */
    public BattleAObject master;

    /**
     * 剩余时间
     */
    public int surplusTime;

    /***
     * 当前叠加层数
     */
    public int curOverlayer;

    /**
     * 增加的值
     */
    public int onceValue;

    public long updateTime;


    public BattleBuff(BuffConfig buffConfig, BattleAObject releaser, BattleAObject master, int skillId)
    {
        this.id = master.getUniqueId() * 10000000000l + releaser.getUniqueId() * 1000000l + buffConfig.id;
        this.skillId = skillId;
        this.buffConfig = buffConfig;
        this.surplusTime = buffConfig.effectTime;
        this.releaser = releaser;
        this.master = master;
        this.curOverlayer = 1;

        if(surplusTime > 0)
        {
            //计算每次要增加或减少多少值 参考属性 * BUFF系数 + BUFF基础值
            if(buffConfig.referTarget == ConstantFactory.BUFF_REFER_RELEASE)//参考释放者的属性
                onceValue = (int) (releaser.getBaseAtt(buffConfig.referValue) * buffConfig.coefficient) + buffConfig.effectValue;
            else if(buffConfig.referTarget == ConstantFactory.BUFF_REFER_MASTER)//参考持有者的属性
                onceValue = (int) (master.getBaseAtt(buffConfig.referValue) * buffConfig.coefficient) + buffConfig.effectValue;
        }
    }



    public void update(long currentTime)
    {
        if(updateTime > 0)
        {
            if(currentTime - updateTime < ConstantFactory.UPDATE_BATTLE_BUFF_TIME)
                return;
        }

        int addValue = 0;
        if(buffConfig.effectType == ConstantFactory.BUFF_TYPE_ADD_HP)
        {
            addValue = onceValue;
        }
        else if(buffConfig.effectType == ConstantFactory.BUFF_TYPE_SUB_HP)
        {
            addValue = -onceValue;
        }
        if(addValue != 0)
        {
            if(buffConfig.effectRange[0] == ConstantFactory.BUFF_RANGE_TYPE_SINGLE)
            {
                master.addHP(addValue);

                logger.debug("BUFF加血:" + addValue + "  master:" + master);

                master.battleController.sendDamage(releaser.getUniqueId(), skillId, master.getUniqueId(), addValue, master.getHp());
            }
            else if(buffConfig.effectRange[0] == ConstantFactory.BUFF_RANGE_TYPE_CIRCLE)
            {
                List<BattleAObject> targets = master.battleController.getTargetsByGroup(buffConfig.effectRange[2], master.masterController.getAttachment());
                for(BattleAObject battleAObject : targets)
                {
                    if(BattleUtils.isInCircleRange(master.position, battleAObject.position, buffConfig.effectRange[1]))
                    {
                        battleAObject.addHP(addValue);

                        logger.debug("BUFF加血:" + addValue + "  master:" + battleAObject);

                        battleAObject.battleController.sendDamage(releaser.getUniqueId(), skillId, master.getUniqueId(), addValue, battleAObject.getHp());
                    }
                }
            }
        }

        surplusTime -= ConstantFactory.UPDATE_BATTLE_BUFF_TIME;
        surplusTime = surplusTime < 0 ? 0 : surplusTime;
        curOverlayer = surplusTime% buffConfig.effectTime == 0 ? surplusTime/ buffConfig.effectTime : surplusTime/ buffConfig.effectTime + 1;

        updateTime = currentTime;
    }

    /**
     * 是否一直有效
     * @return
     */
    public boolean isForever()
    {
        return buffConfig.effectTime == 0;
    }


    public boolean isEnd()
    {
        if(isForever())
            return false;

        return surplusTime <= 0;
    }

    public void checkValue()
    {
        switch (buffConfig.effectType)
        {
            case ConstantFactory.BUFF_TYPE_ADD_ATT:
                master.addAtt(buffConfig.effectValue, onceValue);
                break;
            case ConstantFactory.BUFF_TYPE_SUB_HP:
                master.addAtt(buffConfig.effectValue, -onceValue);
                break;
        }
    }

    public void destroy()
    {
        switch (buffConfig.effectType)
        {
            case ConstantFactory.BUFF_TYPE_ADD_ATT:
                master.addAtt(buffConfig.effectValue, -onceValue);
                break;
            case ConstantFactory.BUFF_TYPE_SUB_HP:
                master.addAtt(buffConfig.effectValue, onceValue);
                break;
        }
    }


    public SGCommonProto.BuffChange.Builder parseBuffChange(SGCommonProto.E_BUFFCHANGE_TYPE changeType)
    {
        SGCommonProto.BuffChange.Builder data = SGCommonProto.BuffChange.newBuilder();
        data.setChangeType(changeType);
        data.setUniqueId(id);
        data.setLayerCount(curOverlayer);
        data.setRemainTime(surplusTime);
        return data;
    }


    public int getType()
    {
        return buffConfig.effectType;
    }

    public int getBuffId()
    {
        return buffConfig.id;
    }


    @Override
    public String toString() {
        return "BattleBuff{" +
                "id=" + id +
                ", buff=" + buffConfig +
                ", onceValue=" + onceValue +
                ", curOverlayer=" + curOverlayer +
                ", surplusTime=" + surplusTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
