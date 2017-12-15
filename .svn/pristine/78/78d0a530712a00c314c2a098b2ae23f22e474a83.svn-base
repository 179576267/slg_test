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
public class QQImpl implements PlatformSDK {

    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private PlayerService playerService;

    @Override
    public  TwoTuple<RoleRecordModel, ReturnMessage> auth(int serverId, String account, String password, String deviceId)
    {
        logger.info("QQ登录参数:" + account+"  " + password);

        String qqUrl = MessageFormat.format(ConfigFactory.QQ_USERINFO_URL, password, account);
        String str = HttpStringUtil.sendHttpGet(qqUrl);//https的必须要用这个类的方法，涉及到ssl
        logger.info("从QQ获取到数据:"+str);
        JSONObject result = JSONObject.parseObject(str);
        String nickName = "";
        int gender = 0;
        String avatar;
        TwoTuple<RoleRecordModel, ReturnMessage> twoTuple = new TwoTuple<>();
        try{
            nickName = result.getString("nickname");
            avatar = result.getString("figureurl_qq_2");
            String genderName = result.getString("gender");
//            gender = E_Gender.forMsg(genderName).getCode();
        }catch (Exception e){
            logger.info("1QQ获取数据失败:" + account);
            twoTuple.setSecond(ReturnMessage.AUTH_FAILURE);
            e.printStackTrace();
            return twoTuple;
        }

        if(StringUtils.isNullOrEmpty(nickName) || StringUtils.isNullOrEmpty(avatar))
        {
            logger.info("2QQ获取数据失败:" + account);
            twoTuple.setSecond(ReturnMessage.AUTH_FAILURE);
            return twoTuple;
        }

        account = Utils.createAccount(account, SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_QQ);
        RoleRecordModel user = playerService.getUserRecordByAccount(account);
        if(user == null)
        {//需要创建
//            user= new UserModel();
//            user.setAccount(account);
//            user.setPassword(ConfigFactory.DEFAULT_USER_PASSWORD);
//            user.setChannel(SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_QQ_VALUE);
//            user.setCreateTime(new Date());
//            user.setIp(ip);
//            logger.debug("QQ第一次登录，创建用户");
//            playerService.addUser(user);
        }
        twoTuple.setFirst(user);
        return twoTuple;
    }


}
