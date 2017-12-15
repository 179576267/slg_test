package com.douqu.game.main.platform;

import com.alibaba.fastjson.JSONObject;
import com.bean.core.util.HttpStringUtil;
import com.douqu.game.core.database.model.UserModel;
import com.douqu.game.core.factory.ConfigFactory;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.util.Utils;
import com.douqu.game.main.service.PlayerService;
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
    public UserModel login(String account, String password)
    {
        logger.info("QQ登录参数:" + account+"  " + password);

        String qqUrl = MessageFormat.format(ConfigFactory.QQ_USERINFO_URL, password, account);
        String str = HttpStringUtil.sendHttpGet(qqUrl);//https的必须要用这个类的方法，涉及到ssl
//        logger.info("从QQ获取到数据:"+str);
        JSONObject result = JSONObject.parseObject(str);
        String nickName = "";
        int gender = 0;
        String avatar = "";
        try{
            nickName = result.getString("nickname");
            avatar = result.getString("figureurl_qq_2");
            String genderName = result.getString("gender");
        }catch (Exception e){
            logger.info("1QQ获取数据失败:" + account);
            e.printStackTrace();
            return new UserModel();
        }

        if(StringUtils.isNullOrEmpty(nickName) || StringUtils.isNullOrEmpty(avatar))
        {
            logger.info("2QQ获取数据失败:" + account);
            return new UserModel();
        }

        account = Utils.createAccount(account, SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_QQ);
        UserModel player = playerService.getUserByAccount(account);

        return player;
    }


}
