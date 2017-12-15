package com.douqu.game.core.entity.challenge;

import com.douqu.game.core.entity.EntityObject;

/**
 * 战斗基本信息记录
 * Created by bean on 2017/8/17.
 */
public class BattleData extends EntityObject {

    private String id;

    private int createTime;

    private int startTime;

    private int endTime;

    private int winTeam;

    private int battleType;

    private int team1Star;

    private int team2Star;

    private String team1PlayerIndex;

    private String team2PlayerIndex;

//    private int levelId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBattleType() {
        return battleType;
    }

    public void setBattleType(int battleType) {
        this.battleType = battleType;
    }

    public int getWinTeam() {
        return winTeam;
    }

    public void setWinTeam(int winTeam) {
        this.winTeam = winTeam;
    }

    public String getTeam1PlayerIndex() {
        return team1PlayerIndex;
    }

    public void setTeam1PlayerIndex(String team1PlayerIndex) {
        this.team1PlayerIndex = team1PlayerIndex;
    }

    public String getTeam2PlayerIndex() {
        return team2PlayerIndex;
    }

    public void setTeam2PlayerIndex(String team2PlayerIndex) {
        this.team2PlayerIndex = team2PlayerIndex;
    }

    public int getTeam1Star() {
        return team1Star;
    }

    public void setTeam1Star(int team1Star) {
        this.team1Star = team1Star;
    }

    public int getTeam2Star() {
        return team2Star;
    }

    public void setTeam2Star(int team2Star) {
        this.team2Star = team2Star;
    }

//    public int getLevelId() {
//        return levelId;
//    }
//
//    public void setLevelId(int levelId) {
//        this.levelId = levelId;
//    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String showInfo()
    {
        return "id=" + id + ", [" + team1PlayerIndex + " -VS- " + team2PlayerIndex + "]";
    }

    @Override
    public String toString() {
        return "BattleControllerInfo{" +
                "id='" + id + '\'' +
                ", createTime=" + createTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", winTeam=" + winTeam +
                ", battleType=" + battleType +
                ", team1Star=" + team1Star +
                ", team2Star=" + team2Star +
                ", team1PlayerIndex='" + team1PlayerIndex + '\'' +
                ", team2PlayerIndex='" + team2PlayerIndex + '\'' +
                "} " + super.toString();
    }
}
