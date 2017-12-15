package com.douqu.game.auth.platform;

import cn.uc.g.sdk.cp.model.SDKException;
import cn.uc.g.sdk.cp.model.SessionInfo;
import cn.uc.g.sdk.cp.service.SDKServerService;
import com.alibaba.fastjson.JSONObject;
import com.douqu.game.auth.database.model.RoleRecordModel;
import com.douqu.game.auth.service.PlayerService;
import com.douqu.game.auth.utils.TwoTuple;
import com.douqu.game.core.e.ReturnMessage;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.util.Utils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
* Author : Bean
* 2017-05-22 18:28
*/
@Component
public class UCImpl implements PlatformSDK {

    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private PlayerService playerService;

    @Override
    public TwoTuple<RoleRecordModel, ReturnMessage> auth(int serverId, String account, String password, String deviceId)
    {

        TwoTuple<RoleRecordModel, ReturnMessage> twoTuple = new TwoTuple<>();
        try {
            SessionInfo sessionInfo = SDKServerService.verifySession(account);

            logger.debug("UC获取玩家数据："+ JSONObject.toJSONString(sessionInfo));

            account = Utils.createAccount(sessionInfo.getAccountId(), SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_UC);
            RoleRecordModel user = playerService.getUserRecordByAccount(account);
            if(user == null)
            {//需要创建
//                user = new UserModel();
//                user.setAccount(account);
//                user.setPassword(ConfigFactory.DEFAULT_USER_PASSWORD);
//                user.setChannel(SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_UC_VALUE);
//                user.setCreateTime(new Date());
//                user.setIp(ip);
//                logger.debug("UC第一次登录，创建用户");
//                playerService.addUser(user);
            }
            twoTuple.setFirst(user);
            return twoTuple;

        } catch (SDKException e) {
            e.printStackTrace();
            twoTuple.setSecond(ReturnMessage.AUTH_FAILURE);
            return twoTuple;
        }
    }
}
