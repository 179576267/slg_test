package com.douqu.game.auth.utils;


import com.bean.core.util.HttpUtils;
import com.douqu.game.core.factory.ConfigFactory;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;

public class SmsMessageUtil {

    static Logger logger = Logger.getLogger(SmsMessageUtil.class);



    public static String sendCaptcha(String phone)
    {
        try{
            //6位验证码
            int captcha = (int) (Math.random() * 900000) + 100000;
            String data = captcha + "";
            String content = ConfigFactory.PHONE_MESSAGE_SIGN + MessageFormat.format(ConfigFactory.PHONE_MESSAGE_CAPTCHA, data);

            sendSmsMessage(phone, content);

            return data;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 发送短信
     *
     * @param phone 接受短信的手机号,多个手机号用逗号隔开
     * @param content  短信内容
     * @return 返回码
     * @throws java.io.IOException
     */
    private static void sendSmsMessage(String phone, String content) throws IOException
    {
        try{
            logger.info("send to:" + phone + " content:" + content);

            content = URLEncoder.encode(content, "UTF-8");

            String param = MessageFormat.format(ConfigFactory.PHONE_MESSAGE_CONTENT, phone, content);

//            HttpStringUtil.sendHttpPost(ConfigFactory.PHONE_MESSAGE_URL, param);
            HttpUtils.doAccessHTTPPost(ConfigFactory.PHONE_MESSAGE_URL, param);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
//        String sign = "【逗趣网络】";
//        String content = sign + "测试短信";
//        content = URLEncoder.encode(content, "UTF-8");
//        String phone = "18521709590";
//        String param = "&phone=" + phone + "&message=" + content;
//        String url = "http://sdk4report.eucp.b2m.cn:8080/sdkproxy/sendsms.action?cdkey=6SDK-EMY-6688-KIQMQ&password=020929";
//        String response = HttpUtils.doAccessHTTPPost(url, param);
//        System.out.println("回复结果:"+response);
    }


}
