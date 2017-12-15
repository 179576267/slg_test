package com.douqu.game.auth.database.model;

import java.util.Date;

public class PlayerModel{
    private String flow;

    private Integer id;

    private Integer userid;

    private String name;

    private Byte camp;

    private Integer level;

    private Integer exp;

    private Date createTime;

    private Byte isDel;

    private Byte online;

    private byte[] bagInfo;

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getCamp() {
        return camp;
    }

    public void setCamp(Byte camp) {
        this.camp = camp;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Byte getIsDel() {
        return isDel;
    }

    public void setIsDel(Byte isDel) {
        this.isDel = isDel;
    }

    public Byte getOnline() {
        return online;
    }

    public void setOnline(Byte online) {
        this.online = online;
    }

    public byte[] getBagInfo() {
        return bagInfo;
    }

    public void setBagInfo(byte[] bagInfo) {
        this.bagInfo = bagInfo;
    }
}