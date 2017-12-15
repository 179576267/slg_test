package com.douqu.game.auth.web.response;

import com.douqu.game.auth.database.model.ServerModel;
import com.douqu.game.core.util.Utils;

import java.util.Date;

/**
 * @author wangzhenfei
 *         2017-07-26 17:12
 */
public class ServerResDto {

    private Integer id;

    private Integer areaId;

    private String name;

    private String ip;

    private Integer port;

    private Integer gmPort;

    private int status;

    private int isNew;

    private int isRecommend;

    public ServerResDto(ServerModel model) {
        if(model != null){
            this.id = model.getId();
            this.areaId = model.getAreaId();
            this.ip = model.getIp();
            this.port = model.getPort();
            this.gmPort = model.getGmPort();
            this.status = model.getStatus();
            this.isRecommend = model.getIsRecommend();
            this.name = model.getName();
            this.isNew = Utils.inDays(model.getFirstOpenTime(), 10) ? 1: 0;
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    public int getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(int isRecommend) {
        this.isRecommend = isRecommend;
    }

    public Integer getGmPort() {
        return gmPort;
    }

    public void setGmPort(Integer gmPort) {
        this.gmPort = gmPort;
    }

    @Override
    public String toString() {
        return "ServerResDto{" +
                "id=" + id +
                ", areaId=" + areaId +
                ", name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", gmPort=" + gmPort +
                ", status=" + status +
                ", isNew=" + isNew +
                ", isRecommend=" + isRecommend +
                '}';
    }
}
