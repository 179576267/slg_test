package com.douqu.game.auth.platform;

import com.bean.core.util.MD5Utils;
import com.douqu.game.auth.database.model.RoleRecordModel;
import com.douqu.game.auth.service.PlayerService;
import com.douqu.game.auth.utils.TwoTuple;
import com.douqu.game.core.e.ReturnMessage;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.util.Utils;
import com.mysql.jdbc.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Author : Bean
 * 2017-05-20 17:07
 */
@Component
public class PhoneImpl implements PlatformSDK {

    Logger logger = Logger.getLogger(this.getClass());


    @Autowired
    private PlayerService playerService;

    @Override
    public TwoTuple<RoleRecordModel, ReturnMessage> auth(int serverId, String account, String password, String deviceId)
    {
        logger.info("Phone登录参数:" + account+"  " + password);

        account = Utils.createAccount(account, SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_PHONE);

        TwoTuple<RoleRecordModel, ReturnMessage> twoTuple = new TwoTuple<>();
        if(StringUtils.isNullOrEmpty(password)){
            twoTuple.setSecond(ReturnMessage.PASSWORD_ERROR);
            return twoTuple;
        }


        RoleRecordModel record = playerService.getUserRecordByDevice(deviceId);
        if(record == null)
        {   //需要创建
            record = new RoleRecordModel();
            record.setDeviceId(deviceId);
            record.setAccount(account);
            record.setChannel(SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_PHONE_VALUE);
            record.setServerId(serverId);
        }

//        if(model == null)
//        {//需要创建
////            model = new UserModel();
////            model.setAccount(account);
////            model.setPassword(MD5Utils.encodeUppercase(password));
////            model.setChannel(SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_WECHAT_VALUE);
////            model.setCreateTime(new Date());
////            model.setIp(ip);
////            logger.debug("Phone第一次登录，创建用户");
////            playerService.addUser(model);
//        }else {
////            if(!Utils.verifyMD5(model.getPassword(), password)){
////                twoTuple.setSecond(ReturnMessage.PASSWORD_ERROR);
////                return twoTuple;
////            }
//        }
        twoTuple.setFirst(record);
        return twoTuple;

    }


}
