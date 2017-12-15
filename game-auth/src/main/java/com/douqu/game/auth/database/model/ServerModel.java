package com.douqu.game.auth.database.model;

import java.util.Date;

public class ServerModel {
    private Integer id;

    private Integer areaId;

    private String name;

    private String ip;

    private Integer port;

    private Integer gmPort;

    private Byte status;

    private Date firstOpenTime;

    private Byte isRecommend;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getFirstOpenTime() {
        return firstOpenTime;
    }

    public void setFirstOpenTime(Date firstOpenTime) {
        this.firstOpenTime = firstOpenTime;
    }

    public Byte getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(Byte isRecommend) {
        this.isRecommend = isRecommend;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public Integer getGmPort() {
        return gmPort;
    }

    public void setGmPort(Integer gmPort) {
        this.gmPort = gmPort;
    }
}