package com.douqu.game.auth.web.request;

/**
 * @author wangzhenfei
 * 2017-07-17 15:01
 * 认证请求
 */
public class LoginReqDto {
    private Integer serverId;
    private String  ip;
    private int     channel;
    private String  account;
    private String  deviceId;

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "LoginReqDto{" +
                "serverId=" + serverId +
                ", ip='" + ip + '\'' +
                ", channel=" + channel +
                ", account='" + account + '\'' +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
