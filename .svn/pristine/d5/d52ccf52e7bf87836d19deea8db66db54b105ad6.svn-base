package com.douqu.game.core.entity.battle;

import com.douqu.game.core.entity.EntityObject;

/**
 * 战斗基本信息记录
 * Created by bean on 2017/8/17.
 */
public class BattleDetail extends EntityObject {

    private String id;

    private int createTime;

    private int startTime;

    private int endTime;

    private int winTeam;

    private int battleType;

    private BattleTemp team1Info;

    private BattleTemp team2Info;

    public BattleDetail()
    {
        team1Info = new BattleTemp();
        team2Info = new BattleTemp();
    }

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

    public BattleTemp getTeam1Info() {
        return team1Info;
    }

    public void setTeam1Info(BattleTemp team1Info) {
        this.team1Info = team1Info;
    }

    public BattleTemp getTeam2Info() {
        return team2Info;
    }

    public void setTeam2Info(BattleTemp team2Info) {
        this.team2Info = team2Info;
    }

    public String showInfo()
    {
        return "id=" + id + ", [" + team1Info.getIndexInfo() + " -VS- " + team2Info.getIndexInfo() + "]";
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", createTime=" + createTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", winTeam=" + winTeam +
                ", battleType=" + battleType +
                ", team1Info=" + team1Info +
                ", team2Info=" + team2Info +
                "} ";
    }
}
