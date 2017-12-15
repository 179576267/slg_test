package com.douqu.game.auth.web.controller;

import com.douqu.game.auth.server.SpringContext;
import com.douqu.game.core.e.ReturnMessage;
import com.douqu.game.core.factory.ConfigFactory;
import com.douqu.game.core.util.SendUtils;
import com.douqu.game.core.web.response.BaseResponseDto;
import com.mysql.jdbc.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by bean on 2017/9/22.
 */
@RestController
@RequestMapping(value = "/manager")
public class ManagerController {

    Logger logger = Logger.getLogger(this.getClass());

    @RequestMapping(value = "/close", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public BaseResponseDto auth(String username, String password, HttpServletRequest request)
    {
        logger.info(username + " 执行认证关闭服务器指令 -> IP:" + request.getRemoteHost());

        if(StringUtils.isNullOrEmpty(username) || username.length() < 2 || username.length() > 10)
        {
            logger.info("关闭认证服务器失败 -> 操作人员错误(用户名长度2-10)!");
            return SendUtils.createMsg(ReturnMessage.PASSWORD_ERROR);
        }

        if(StringUtils.isNullOrEmpty(password) || !password.equals(ConfigFactory.CLOSE_SERVER_KEY))
        {
            logger.info("关闭认证服务器失败 -> 密码错误!");
            return SendUtils.createMsg(ReturnMessage.PASSWORD_ERROR);
        }

        SpringContext.stop();

        logger.info("关闭认证服务器成功!");

        return SendUtils.createMsg();
    }
}
