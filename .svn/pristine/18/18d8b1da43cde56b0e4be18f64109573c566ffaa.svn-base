package com.douqu.game.auth.platform;

import com.alibaba.fastjson.JSONObject;
import com.bean.core.util.HttpStringUtil;
import com.douqu.game.auth.database.model.RoleRecordModel;
import com.douqu.game.auth.service.PlayerService;
import com.douqu.game.auth.utils.TwoTuple;
import com.douqu.game.core.e.ReturnMessage;
import com.douqu.game.core.factory.ConfigFactory;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.util.Utils;
import com.mysql.jdbc.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * Author : Bean
 * 2017-05-20 17:07
 */
@Component
public class WechatImpl implements PlatformSDK{

    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private PlayerService playerService;

    @Override
    public TwoTuple<RoleRecordModel, ReturnMessage> auth(int serverId, String account, String password, String deviceId)
    {
        logger.info("微信登录参数:" + account+"  " + password);
        TwoTuple<RoleRecordModel, ReturnMessage> twoTuple = new TwoTuple<>();
        String wechatUrl = MessageFormat.format(ConfigFactory.WECHAT_USERINFO_URL, password, account);
        String str = HttpStringUtil.sendHttpGet(wechatUrl);
        JSONObject result = JSONObject.parseObject(str);
        //JSONObject result = HttpJsonUtils.httpGet(wechatUrl);
        /**
         * openid	普通用户的标识，对当前开发者帐号唯一
         nickname	普通用户昵称
         sex	普通用户性别，1为男性，2为女性
         province	普通用户个人资料填写的省份
         city	普通用户个人资料填写的城市
         country	国家，如中国为CN
         headimgurl	用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
         privilege	用户特权信息，json数组，如微信沃卡用户为（chinaunicom）
         unionid	用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
         */
        logger.info("微信获取数据:" + result);
        String nickName = "";
        int gender = 0;
        String avatar = "";
        try {
            nickName = result.getString("nickname");
            gender = result.getInteger("sex");
            avatar = result.getString("headimgurl");
        }catch (Exception e){
            logger.debug("微信获取数据失败:" + account);
            twoTuple.setSecond(ReturnMessage.AUTH_FAILURE);
            return twoTuple;
        }

        if(StringUtils.isNullOrEmpty(nickName) || StringUtils.isNullOrEmpty(avatar))
        {
            logger.info("2QQ获取数据失败:" + account);
//            Player temp = new Player();
//            temp.setId(-1);
//            return temp;
            twoTuple.setSecond(ReturnMessage.AUTH_FAILURE);
            return twoTuple;
        }

        account = Utils.createAccount(account, SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_WECHAT);
        RoleRecordModel user = playerService.getUserRecordByAccount(account);
        if(user == null)
        {//需要创建
//            user = new UserModel();
//            user.setAccount(account);
//            user.setPassword(ConfigFactory.DEFAULT_USER_PASSWORD);
//            user.setChannel(SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_WECHAT_VALUE);
//            user.setCreateTime(new Date());
//            user.setIp(ip);
//            logger.debug("微信第一次登录，创建用户");
//            playerService.addUser(user);
        }
        twoTuple.setFirst(user);
        return twoTuple;
    }

}
