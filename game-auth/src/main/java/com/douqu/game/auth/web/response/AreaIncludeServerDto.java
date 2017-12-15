package com.douqu.game.auth.web.response;


import java.util.List;

/**
 * @author wangzhenfei
 *         2017-07-26 17:11
 */
public class AreaIncludeServerDto {
    private Integer areaId;

    private String areaName;

    private List<ServerResDto> servers ;


    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public List<ServerResDto> getServers() {
        return servers;
    }

    public void setServers(List<ServerResDto> servers) {
        this.servers = servers;
    }

    @Override
    public String toString() {
        return "AreaIncludeServerDto{" +
                "areaId=" + areaId +
                ", areaName='" + areaName + '\'' +
                ", servers=" + servers +
                '}';
    }
}
