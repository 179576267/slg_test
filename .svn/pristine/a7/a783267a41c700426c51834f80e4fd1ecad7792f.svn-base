package com.douqu.game.battle.controller.sprite;

import com.douqu.game.battle.controller.BattleController;
import com.douqu.game.battle.entity.tmp.SpriteBattleTmp;
import com.douqu.game.core.e.E_PlayerStatus;


/**
* Created by bean on 2017/7/25.
*/
public abstract class SpriteController {

    protected static final long serialVersionUID = 1L;

    /**
     * 父容器
     */
    private BattleController battleController;


    private SpriteBattleTmp attachment;

    /**
     * 当前状态
     */
    protected E_PlayerStatus status;


//    public SpriteController()
//    {
//
//    }

    public void sendMsg(int code, byte[] data)
    {

    }

    /**
     * 是否在战斗中
     * @return
     */
    public boolean isBattling()
    {
        return status == E_PlayerStatus.BATTLING || status == E_PlayerStatus.BATTLE_WAIT_START || status == E_PlayerStatus.MATCH_WAIT_BATTLE;
    }

    public boolean isFree()
    {
        return status == E_PlayerStatus.FREE || status == E_PlayerStatus.MATCHING;
    }

    public void clearBattle()
    {
        setAttachment(null);
        setBattleController(null);
        setStatus(E_PlayerStatus.FREE);
    }




    public String getObjectIndex()
    {
        return "";
    }

    public String getName()
    {
        return "";
    }

    public boolean isOnlinePlayer()
    {
        return false;
    }




    public SpriteBattleTmp getAttachment()
    {
        return attachment;
    }

    public void setAttachment(SpriteBattleTmp attachment)
    {
        this.attachment = attachment;
    }

    public E_PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(E_PlayerStatus status) {
        this.status = status;
    }

    public BattleController getBattleController() {
        return battleController;
    }

    public void setBattleController(BattleController battleController) {
        this.battleController = battleController;
    }

    @Override
    public String toString() {
        return "SpriteController{" +
                "battleController=" + battleController +
                ", attachment=" + attachment +
                '}';
    }
}
