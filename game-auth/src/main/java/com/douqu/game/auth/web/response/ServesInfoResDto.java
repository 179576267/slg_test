package com.douqu.game.auth.web.response;

import java.util.List;

/**
 * @author wangzhenfei
 *         2017-07-28 16:28
 */
public class ServesInfoResDto {
    private List<AreaIncludeServerDto> areaServers;
    private List<ServerResDto> recommendServers;
    private List<PlayerResDto> players;

    public List<PlayerResDto> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerResDto> players) {
        this.players = players;
    }

    public List<AreaIncludeServerDto> getAreaServers() {
        return areaServers;
    }

    public void setAreaServers(List<AreaIncludeServerDto> areaServers) {
        this.areaServers = areaServers;
    }

    public List<ServerResDto> getRecommendServers() {
        return recommendServers;
    }

    public void setRecommendServers(List<ServerResDto> recommendServers) {
        this.recommendServers = recommendServers;
    }

}
