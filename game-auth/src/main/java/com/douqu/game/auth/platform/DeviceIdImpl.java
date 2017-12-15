package com.douqu.game.auth.platform;

import com.douqu.game.auth.database.model.RoleRecordModel;
import com.douqu.game.auth.utils.TwoTuple;
import com.douqu.game.core.e.ReturnMessage;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.util.Utils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Author : Bean
 * 2017-05-20 17:07
 */
@Component
public class DeviceIdImpl implements PlatformSDK {

    Logger logger = Logger.getLogger(this.getClass());

    @Override
    public TwoTuple<RoleRecordModel, ReturnMessage> auth(int serverId, String account, String password, String deviceId)
    {

        TwoTuple<RoleRecordModel, ReturnMessage> twoTuple = new TwoTuple<>();
//        RoleRecordModel record = playerService.getUserRecordByDevice(deviceId);
//        if(record == null)
//        {   //需要创建
            RoleRecordModel record = new RoleRecordModel();
          record.setDeviceId(Utils.createAccount(deviceId, SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_QUICK));
          record.setChannel(SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_QUICK_VALUE);
          record.setServerId(serverId);
//        }
        twoTuple.setFirst(record);
        return twoTuple;
    }


}
