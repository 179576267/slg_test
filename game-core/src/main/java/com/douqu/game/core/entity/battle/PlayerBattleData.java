package com.douqu.game.core.entity.battle;


import com.douqu.game.core.protobuf.SGCommonProto;

/**
 * Created by bean on 2017/9/22.
 */
public class PlayerBattleData {

    private String battleId;

    private SGCommonProto.E_BATTLE_TYPE battleType;

    private String ip;

    private int port;

    public PlayerBattleData(String battleId, SGCommonProto.E_BATTLE_TYPE battleType, String ip, int port) {
        this.battleId = battleId;
        this.battleType = battleType;
        this.ip = ip;
        this.port = port;
    }

    public PlayerBattleData() {
    }

    public String getBattleId() {
        return battleId;
    }

    public void setBattleId(String battleId) {
        this.battleId = battleId;
    }

    public SGCommonProto.E_BATTLE_TYPE getBattleType() {
        return battleType;
    }

    public void setBattleType(SGCommonProto.E_BATTLE_TYPE battleType) {
        this.battleType = battleType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
