package com.douqu.game.battle.entity;

import com.douqu.game.battle.controller.BattleController;
import com.douqu.game.battle.entity.aobject.BattleAObject;
import com.douqu.game.core.config.battle.BuffConfig;
import com.douqu.game.core.config.sprite.Sprite;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.protobuf.SGMainProto;
import com.douqu.game.core.protobuf.SGWarProto;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-10-24 15:46
 */
public class BuffBox {

    Logger logger = Logger.getLogger(this.getClass());

    private BattleController battleController;

    /**
     * 谁的BUFF
     */
    private BattleAObject battleAObject;
    /**
     * BUFF列表
     */
    public List<BattleBuff> buffList;

    public BuffBox(BattleController battleController, BattleAObject battleAObject)
    {
        this.battleAObject = battleAObject;
        this.battleController = battleController;

        this.buffList = new CopyOnWriteArrayList<>();
    }


    public void update(long currentTime)
    {
        SGWarProto.S2C_SynBuff.Builder response = SGWarProto.S2C_SynBuff.newBuilder();

        for(BattleBuff buff : buffList)
        {
            if(buff.isEnd())
            {
                buff.destroy();
                buffList.remove(buff);

                logger.debug("BUFF时间到了:" + buff);
                response.addBuff(buff.parseBuffChange(SGCommonProto.E_BUFFCHANGE_TYPE.BUFFCHANGE_TYPE_CLEAR));
            }
            else
            {
                buff.update(currentTime);
            }
        }

        if(response.getBuffCount() > 0)
        {
            battleController.dispatchMsg(SGMainProto.E_MSG_ID.MsgID_War_SynBuff_VALUE, response.build().toByteArray());
        }

    }


    /**
     * 是否有冲锋BUFF
     * @return
     */
    public boolean isHaveToAttackBuff()
    {
        for(BattleBuff buff : buffList)
        {
            if(!buff.isEnd() && (buff.getType() == ConstantFactory.BUFF_TYPE_TO_ATTACK))
                return true;
        }
        return false;
    }

    /**
     * 是否可以行走
     * @return
     */
    public boolean isCanGo()
    {
        for(BattleBuff buff : buffList)
        {
            if(!buff.isEnd() && (buff.getType() == ConstantFactory.BUFF_TYPE_FROZEN || buff.getType() == ConstantFactory.BUFF_TYPE_VERTIGO))
                return false;
        }
        return true;
    }

    /**
     * 是否可以攻击
     * @return
     */
    public boolean isCanAtk()
    {
        for(BattleBuff buff : buffList)
        {
            if(!buff.isEnd() && buff.getType() == ConstantFactory.BUFF_TYPE_VERTIGO)
                return false;
        }
        return true;
    }


    public void addBuff(BattleAObject releaser, BuffConfig buffConfig, int skillId)
    {
        logger.info("释放BUFF -> 释放者:" + releaser + "  目标:" + battleAObject + " BUFF:" + buffConfig);
        SGWarProto.S2C_SynBuff.Builder response = SGWarProto.S2C_SynBuff.newBuilder();

        BattleBuff battleBuff = getBuffByReleaser(releaser.getUniqueId(), buffConfig.id);
        if(battleBuff == null)
        {
            battleBuff = new BattleBuff(buffConfig, releaser, battleAObject, skillId);
            buffList.add(battleBuff);
            //按照 优先级 排序
            Collections.sort(buffList, new Comparator<BattleBuff>() {
                @Override
                public int compare(BattleBuff o1, BattleBuff o2) {
                    return new Integer(o1.buffConfig.priority).compareTo(o2.buffConfig.priority);
                }
            });

            response.addBuff(battleBuff.parseBuffChange(SGCommonProto.E_BUFFCHANGE_TYPE.BUFFCHANGE_TYPE_ADD));

            logger.debug("新增BUFF");
        }
        else
        {
            if(buffConfig.overlayType == ConstantFactory.BUFF_OVERLAY_TYPE_COVER)//覆盖,只针对同一个来源的BUFF
            {
                battleBuff.surplusTime = buffConfig.effectTime;
                response.addBuff(battleBuff.parseBuffChange(SGCommonProto.E_BUFFCHANGE_TYPE.BUFFCHANGE_TYPE_REPLACE));

                logger.debug("替换BUFF");
            }
            else if(buffConfig.overlayType == ConstantFactory.BUFF_OVERLAY_TYPE_OVERLAP)//叠加,只针对同一个来源的BUFF
            {
                if(battleBuff.curOverlayer < buffConfig.maxOverlayer)
                {
                    battleBuff.surplusTime += buffConfig.effectTime;
                    battleBuff.curOverlayer++;
                    response.addBuff(battleBuff.parseBuffChange(SGCommonProto.E_BUFFCHANGE_TYPE.BUFFCHANGE_TYPE_OVERLAY));

                    logger.debug("叠加BUFF:" + battleBuff.curOverlayer);
                }
            }
        }

        battleBuff.checkValue();

        battleController.dispatchMsg(SGMainProto.E_MSG_ID.MsgID_War_SynBuff_VALUE, response.build().toByteArray());


        logger.debug("释放BUFF完成后的列表:" + buffList);
    }

    public BattleBuff getBuffByReleaser(int releaser, int buffId)
    {
        for(BattleBuff buff : buffList)
        {
            if(buff.releaser.getUniqueId() == releaser && buff.getBuffId() == buffId)
                return buff;
        }
        return null;
    }

    /**
     * 获取增伤
     * @param sprite
     * @return
     */
    public int getDamagePlus(Sprite sprite)
    {
        int result = 0;
        for(BattleBuff buff : buffList)
        {
            if(buff.buffConfig.effectType == ConstantFactory.BUFF_TYPE_ADD_DAMAGE_PROFESSION)
            {
                if(buff.buffConfig.referValue == ConstantFactory.PROFESSION_ALL || buff.buffConfig.referValue == sprite.profession)
                {
                    result += buff.buffConfig.effectValue;
                }
            }
        }

        return result;
    }

    /**
     * 获取免伤
     * @param sprite
     * @return
     */
    public int getExdamagePlus(Sprite sprite)
    {
        int result = 0;
        for(BattleBuff buff : buffList)
        {
            if(buff.buffConfig.effectType == ConstantFactory.BUFF_TYPE_SUB_DAMAGE_PROFESSION)
            {
                if(buff.buffConfig.referValue == ConstantFactory.PROFESSION_ALL || buff.buffConfig.referValue == sprite.profession)
                {
                    result += buff.buffConfig.effectValue;
                }
            }
        }

        return result;
    }
}
