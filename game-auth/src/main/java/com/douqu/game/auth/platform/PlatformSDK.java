package com.douqu.game.auth.platform;


import com.douqu.game.auth.database.model.RoleRecordModel;
import com.douqu.game.auth.utils.TwoTuple;
import com.douqu.game.core.e.ReturnMessage;

/**
 * Author : Bean
 * 2017-05-20 17:05
 */
public interface PlatformSDK {

    TwoTuple<RoleRecordModel, ReturnMessage> auth(int serverId, String account, String password, String deviceId);
}
