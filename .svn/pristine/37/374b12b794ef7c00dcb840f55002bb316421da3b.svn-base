package com.douqu.game.battle.server;

import com.douqu.game.battle.controller.sprite.PlayerController;
import com.douqu.game.core.protobuf.SGCommonProto.E_BATTLE_TYPE;

/**
 * Created by bean on 2017/7/27.
 */
public class BattleInitInfo {

    /**
     * 过期时间
     */
    public final static int OVERDUE_TIME = 30 * 1000;

    private String battleId;

    private BattleInitTarget player;

    private BattleInitTarget target;

    private E_BATTLE_TYPE battleType;

    private long createTime;


    public BattleInitInfo(String battleId, E_BATTLE_TYPE battleType, String playerIndex, String targetIndex) {
        this.battleId = battleId;
        this.battleType = battleType;
        player = new BattleInitTarget(playerIndex, false);
        target = new BattleInitTarget(targetIndex, battleType!=E_BATTLE_TYPE.BATTLE_TYPE_PVP_ARENA);
        this.createTime = GameServer.currentTime;
    }

    public boolean isOverdue()
    {
        return GameServer.currentTime - createTime >= OVERDUE_TIME;
    }


    public boolean isExist(String objectIndex)
    {
        return objectIndex.equals(player.getPlayerIndex()) || objectIndex.equals(target.getPlayerIndex());
    }

    public void ready(String objectIndex)
    {
        if(player.getPlayerIndex().equals(objectIndex))
            player.setReady(true);
        else if(target.getPlayerIndex().equals(objectIndex))
            target.setReady(true);
    }

    public boolean isReady()
    {
        return player.isReady() && target.isReady();
    }

    public void initPlayerData(PlayerController data)
    {
        if(player.getPlayerIndex().equals(data.getObjectIndex()))
        {
            player.setPlayer(data);
        }
        else if(target.getPlayerIndex().equals(data.getObjectIndex()))
        {
            target.setPlayer(data);
        }
    }

    public String getBattleId() {
        return battleId;
    }

    public void setBattleId(String battleId) {
        this.battleId = battleId;
    }

    public BattleInitTarget getPlayer() {
        return player;
    }

    public void setPlayer(BattleInitTarget player) {
        this.player = player;
    }

    public BattleInitTarget getTarget() {
        return target;
    }

    public void setTarget(BattleInitTarget target) {
        this.target = target;
    }

    public E_BATTLE_TYPE getBattleType() {
        return battleType;
    }

    public void setBattleType(E_BATTLE_TYPE battleType) {
        this.battleType = battleType;
    }

    @Override
    public String toString() {
        return "BattleInitInfo{" +
                "battleId='" + battleId + '\'' +
                ", player=" + player +
                ", target=" + target +
                ", battleType=" + battleType +
                '}';
    }
}
