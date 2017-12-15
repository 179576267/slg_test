package com.douqu.game.core.database.model;


import java.util.Date;

public class PlayerModel {
    private Integer id;

    private String objectIndex;

    private String avatar;

    private Integer uid;

    private String name;

    private String account;

    private Integer channel;

    private Integer camp;

    private Integer master;

    private Integer level;

    private Integer vipLevel;

    private Integer vipExp;

    private Integer exp;

    private Integer fc;

    private Integer money;

    private Boolean isDel;

    private Boolean online;

    private byte[] bagInfo;

    private byte[] challengeInfo;

    private byte[] taskInfo;

    private byte[] boonInfo;

    private byte[] settingInfo;

    private Date createTime;

    private Date lastLoginTime;

    private Date lastLogoutTime;


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCamp() {
        return camp;
    }

    public void setCamp(Integer camp) {
        this.camp = camp;
    }

    public Integer getLevel() {
        return level == null ? 0 : level.intValue();
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getExp() {
        return exp == null ? 0 : exp.intValue();
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

    public Boolean getIsDel() {
        return isDel;
    }

    public void setIsDel(Boolean isDel) {
        this.isDel = isDel;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public byte[] getBagInfo() {
        return bagInfo;
    }

    public void setBagInfo(byte[] bagInfo) {
        this.bagInfo = bagInfo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getObjectIndex() {
        return objectIndex;
    }

    public void setObjectIndex(String objectIndex) {
        this.objectIndex = objectIndex;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getMaster() {
        return master;
    }

    public void setMaster(Integer master) {
        this.master = master;
    }

    public byte[] getChallengeInfo() {
        return challengeInfo;
    }

    public void setChallengeInfo(byte[] challengeInfo) {
        this.challengeInfo = challengeInfo;
    }

    public byte[] getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(byte[] taskInfo) {
        this.taskInfo = taskInfo;
    }

    public byte[] getBoonInfo() {
        return boonInfo;
    }

    public void setBoonInfo(byte[] boonInfo) {
        this.boonInfo = boonInfo;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getLastLogoutTime() {
        return lastLogoutTime;
    }

    public void setLastLogoutTime(Date lastLogoutTime) {
        this.lastLogoutTime = lastLogoutTime;
    }

    public Integer getFc() {
        return fc;
    }

    public void setFc(Integer fc) {
        this.fc = fc;
    }

    public byte[] getSettingInfo() {
        return settingInfo;
    }

    public void setSettingInfo(byte[] settingInfo) {
        this.settingInfo = settingInfo;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    public Integer getVipLevel() {

        return vipLevel == null ? 0 : vipLevel.intValue();
    }

    public void setVipLevel(Integer vipLevel) {
        this.vipLevel = vipLevel;
    }

    public Integer getVipExp() {
        return vipExp == null ? 0 : vipExp.intValue();
    }

    public void setVipExp(Integer vipExp) {
        this.vipExp = vipExp;
    }

    @Override
    public String toString() {
        return "PlayerModel{" +
                "id=" + id +
                ", objectIndex='" + objectIndex + '\'' +
                ", avatar='" + avatar + '\'' +
                ", uid=" + uid +
                ", name='" + name + '\'' +
                ", account='" + account + '\'' +
                ", camp=" + camp +
                ", master=" + master +
                ", level=" + level +
                ", exp=" + exp +
                ", fc=" + fc +
                ", money=" + money +
                ", isDel=" + isDel +
                ", online=" + online +
                ", createTime=" + createTime +
                ", lastLoginTime=" + lastLoginTime +
                ", lastLogoutTime=" + lastLogoutTime +
                '}';
    }
}