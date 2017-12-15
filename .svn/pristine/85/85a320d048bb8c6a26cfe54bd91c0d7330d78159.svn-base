package com.douqu.game.battle.server;


import com.douqu.game.battle.controller.sprite.PlayerController;

/**
 * Created by bean on 2017/8/1.
 */
public class BattleInitTarget {

    private PlayerController player;

    private boolean isReady;

    private String playerIndex;



    public BattleInitTarget(String playerIndex, boolean isReady)
    {
        this.isReady = isReady;
        this.playerIndex = playerIndex;
    }

    public PlayerController getPlayer() {
        return player;
    }

    public void setPlayer(PlayerController player) {
        this.player = player;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean isReady) {
        this.isReady = isReady;
    }

    public String getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(String playerIndex) {
        this.playerIndex = playerIndex;
    }

    @Override
    public String toString() {
        return "BattleInitPlayer{" +
                "playerIndex='" + playerIndex + '\'' +
                '}';
    }
}
