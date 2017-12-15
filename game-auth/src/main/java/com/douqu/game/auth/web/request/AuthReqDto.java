package com.douqu.game.auth.web.request;

/**
 * @author wangzhenfei
 * 2017-07-17 15:01
 * 认证请求
 */
public class AuthReqDto {
    private String account;
    private String deviceId;
    private String password;
    private int channel;
    private String ip;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "AuthReqDto{" +
                "account='" + account + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", password='" + password + '\'' +
                ", channel=" + channel +
                ", ip='" + ip + '\'' +
                '}';
    }
}
