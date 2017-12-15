package com.douqu.game.auth.web.response;

/**
 * @author wangzhenfei
 *         2017-07-22 14:47
 */
public class LoginSuccessResDto {
    private String mainServerIp;
    private int mainServerPort;
    private String token;

    public String getMainServerIp() {
        return mainServerIp;
    }

    public void setMainServerIp(String mainServerIp) {
        this.mainServerIp = mainServerIp;
    }

    public int getMainServerPort() {
        return mainServerPort;
    }

    public void setMainServerPort(int mainServerPort) {
        this.mainServerPort = mainServerPort;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
