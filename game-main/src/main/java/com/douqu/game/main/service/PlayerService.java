package com.douqu.game.main.service;


import com.douqu.game.core.controller.PlayerController;
import com.douqu.game.core.database.model.PlayerModel;
import com.douqu.game.core.database.model.UserModel;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.netty.NettyConnection;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.main.database.model.LoginLogModel;

import java.util.List;

/**
 * Author : Bean
 * 2017-04-11 12:23
 */
public interface PlayerService {

    int update(Player player);

    UserModel getUserByAccount(String account);

    UserModel getUserByDeviceId(String deviceId);

    PlayerModel getPlayerByIndex(String objectIndex);

    PlayerModel getPlayerByUid(Integer uid);

    PlayerModel getPlayerByName(String name);

    void login(LoginLogModel loginLogModel);

    Player regist(String account, String nickName, String password, String avatar, SGCommonProto.E_CHANNEL_TYPE channel, String ip);

    void enterGame(PlayerController playerController, Player player, SGCommonProto.E_CHANNEL_TYPE channel, NettyConnection connection);

    void saveOnlinePlayer();

//    void offlineMatch(PlayerController playerController);

    int getAllPlayerCount();

    List<PlayerModel> getPlayerList(Integer page, Integer pageCount);

}
