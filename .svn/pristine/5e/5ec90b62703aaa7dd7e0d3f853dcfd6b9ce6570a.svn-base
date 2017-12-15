package com.douqu.game.core.factory;

import com.douqu.game.core.util.Utils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

/**
 * Author : Bean
 * 2017-07-06 16:13
 */
public class LoadFactory {

    static Logger logger = Logger.getLogger(LoadFactory.class);
    private final static String CONFIG_NAME = "config.properties";


    public static InputStream getInputStream(String filePath)
    {
        InputStream input = null;
        try {
            if(filePath.endsWith("properties"))
                input = LoadFactory.class.getClassLoader().getResourceAsStream(filePath);
            else
            {
                File file = new File(Utils.getBasePath() + filePath);
                if(!file.exists())
                    file = new File(Utils.getRootPath() + filePath);

                if(file.exists())
                {
//                logger.info("用文件目录的方式加载 -> " + filePath);
                    input = new BufferedInputStream(new FileInputStream(file));
                }
                else
                {
//                logger.info("用文件流的方式加载 -> " + filePath);
                    input = LoadFactory.class.getClassLoader().getResourceAsStream(filePath);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return input;
    }




    public static void loadConfig()
    {
        logger.info("【Load Config Start FileName=" + CONFIG_NAME + "】");
        Properties properties = new Properties();
        InputStream input =  null;
        try {
//            input = LoadFactory.class.getClassLoader().getResourceAsStream(CONFIG_NAME);
            input = getInputStream(CONFIG_NAME);
            properties.load(input);

            ConfigFactory.LANGUAGE = properties.get("LANGUAGE").toString().trim();
            ConfigFactory.SERVER_IP = properties.get("SERVER_IP").toString().trim();
            ConfigFactory.MAIN_SERVER_TCP_PORT = Integer.parseInt(properties.get("MAIN_SERVER_TCP_PORT").toString().trim());
            ConfigFactory.MAIN_SERVER_GM_TCP_PORT = ConfigFactory.MAIN_SERVER_TCP_PORT + 1;
            ConfigFactory.ROUTE_SERVER_TCP_PORT = ConfigFactory.MAIN_SERVER_TCP_PORT + 2;
            ConfigFactory.BATTLE_SERVER_PORT_START = Integer.parseInt(properties.get("BATTLE_SERVER_PORT_START").toString().trim());
            ConfigFactory.AUTH_SERVER_HTTP_BASE = properties.get("AUTH_SERVER_HTTP_BASE").toString().trim();

            ConfigFactory.TENCENT_COS_APP_ID = Long.parseLong(properties.get("TENCENT_COS_APP_ID").toString().trim());
            ConfigFactory.TENCENT_COS_SECRET_ID = properties.get("TENCENT_COS_SECRET_ID").toString().trim();
            ConfigFactory.TENCENT_COS_SECRET_KEY = properties.get("TENCENT_COS_SECRET_KEY").toString().trim();
            ConfigFactory.TENCENT_COS_BUCKET = properties.get("TENCENT_COS_BUCKET").toString().trim();

            ConfigFactory.EASEMOB_API_HTTP_SCHEMA = properties.get("EASEMOB_API_HTTP_SCHEMA").toString().trim();
            ConfigFactory.EASEMOB_API_SERVER_HOST = properties.get("EASEMOB_API_SERVER_HOST").toString().trim();
            ConfigFactory.EASEMOB_DEFAULT_PASSWORD = properties.get("EASEMOB_DEFAULT_PASSWORD").toString().trim();
            ConfigFactory.EASEMOB_USER_ROLE_APPADMIN = properties.get("EASEMOB_USER_ROLE_APPADMIN").toString().trim();
            ConfigFactory.EASEMOB_APPKEY = properties.get("EASEMOB_APPKEY").toString().trim();
            ConfigFactory.EASEMOB_APP_CLIENT_ID = properties.get("EASEMOB_APP_CLIENT_ID").toString().trim();
            ConfigFactory.EASEMOB_APP_CLIENT_SECRET = properties.get("EASEMOB_APP_CLIENT_SECRET").toString().trim();

            ConfigFactory.PHONE_MESSAGE_SIGN = properties.get("PHONE_MESSAGE_SIGN").toString().trim();
            ConfigFactory.PHONE_MESSAGE_CONTENT = properties.get("PHONE_MESSAGE_CONTENT").toString().trim();
            ConfigFactory.PHONE_MESSAGE_URL = properties.get("PHONE_MESSAGE_URL").toString().trim();
            ConfigFactory.PHONE_MESSAGE_CAPTCHA = properties.get("PHONE_MESSAGE_CAPTCHA").toString().trim();

            ConfigFactory.WECHAT_USERINFO_URL = properties.get("WECHAT_USERINFO_URL").toString().trim();
            ConfigFactory.QQ_USERINFO_URL = properties.get("QQ_USERINFO_URL").toString().trim();

            ConfigFactory.XIAOMI_VERIFY_URL = properties.get("XIAOMI_VERIFY_URL").toString().trim();
            ConfigFactory.XIAOMI_APP_ID = properties.get("XIAOMI_APP_ID").toString().trim();
            ConfigFactory.XIAOMI_APP_SECRET = properties.get("XIAOMI_APP_SECRET").toString().trim();

            ConfigFactory.DEFAULT_USER_PASSWORD = properties.get("DEFAULT_USER_PASSWORD").toString().trim();

            ConfigFactory.CLOSE_SERVER_KEY = properties.get("CLOSE_SERVER_KEY").toString().trim();

            logger.info("LANGUAGE------------------【" + ConfigFactory.LANGUAGE + "】");
            logger.info("SERVER_IP-----------------【" + ConfigFactory.SERVER_IP + "】");
            logger.info("MAIN_SERVER_TCP_PORT------【" + ConfigFactory.MAIN_SERVER_TCP_PORT + "】");
            logger.info("MAIN_SERVER_GM_TCP_PORT---【" + ConfigFactory.MAIN_SERVER_GM_TCP_PORT + "】");
            logger.info("ROUTE_SERVER_TCP_PORT-----【" + ConfigFactory.ROUTE_SERVER_TCP_PORT + "】");
            logger.info("BATTLE_SERVER_PORT_START--【" + ConfigFactory.BATTLE_SERVER_PORT_START + "】");
            logger.info("AUTH_SERVER_HTTP_BASE-----【" + ConfigFactory.AUTH_SERVER_HTTP_BASE + "】");

            logger.info("TENCENT_COS_APP_ID--------【" + ConfigFactory.TENCENT_COS_APP_ID + "】");
            logger.info("TENCENT_COS_SECRET_ID-----【" + ConfigFactory.TENCENT_COS_SECRET_ID + "】");
            logger.info("TENCENT_COS_SECRET_KEY----【" + ConfigFactory.TENCENT_COS_SECRET_KEY + "】");
            logger.info("TENCENT_COS_BUCKET--------【" + ConfigFactory.TENCENT_COS_BUCKET + "】");

            logger.info("PHONE_MESSAGE_SIGN--------【" + ConfigFactory.PHONE_MESSAGE_SIGN + "】");
            logger.info("PHONE_MESSAGE_CONTENT-----【" + ConfigFactory.PHONE_MESSAGE_CONTENT + "】");
            logger.info("PHONE_MESSAGE_URL---------【" + ConfigFactory.PHONE_MESSAGE_URL + "】");
            logger.info("PHONE_MESSAGE_CAPTCHA-----【" + ConfigFactory.PHONE_MESSAGE_CAPTCHA + "】");

            logger.info("WECHAT_USERINFO_URL-------【" + ConfigFactory.WECHAT_USERINFO_URL + "】");
            logger.info("QQ_USERINFO_URL-----------【" + ConfigFactory.QQ_USERINFO_URL + "】");

            logger.info("XIAOMI_VERIFY_URL---------【" + ConfigFactory.XIAOMI_VERIFY_URL + "】");
            logger.info("XIAOMI_APP_ID-------------【" + ConfigFactory.XIAOMI_APP_ID + "】");
            logger.info("XIAOMI_APP_SECRET---------【" + ConfigFactory.XIAOMI_APP_SECRET + "】");

            logger.info("DEFAULT_USER_PASSWORD-----【" + ConfigFactory.DEFAULT_USER_PASSWORD + "】");

            logger.info("CLOSE_KEY-----------------【" + ConfigFactory.CLOSE_SERVER_KEY + "】");
            logger.info("Load Config Success==============================");

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }




    public static void main(String[] args) {
//        LoadFactory.loadGameObject("data/soldierLv", CardLv.class, DataFactory.SOLDIER_LV_KEY);

    }
}
