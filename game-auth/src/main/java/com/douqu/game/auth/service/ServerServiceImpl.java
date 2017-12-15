package com.douqu.game.auth.service;


import com.bean.core.util.MD5Utils;
import com.bean.core.util.TimeUtils;
import com.douqu.game.auth.database.mapper.ServerMapper;
import com.douqu.game.auth.database.model.ServerModel;
import com.douqu.game.core.e.ReturnMessage;
import com.douqu.game.core.factory.CodeFactory;
import com.douqu.game.core.web.response.BaseResponseDto;
import com.douqu.game.core.web.response.ErrorResponseDto;
import com.douqu.game.core.web.response.SuccessResponseDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Author : wangzhenfei
 * 2017-07-06 15:55
 */
@Service
public class ServerServiceImpl implements ServerService {
    private Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    ServerMapper serverMapper;


    @Override
    public BaseResponseDto changeStatus(String password, Integer serverId, Integer status) {
        logger.info("changeStatus 参数 serverId：" + serverId + ", password: " + password + "， status: " + status);
        if(serverId == null || serverId.intValue() == 0 || status == null || status.intValue() == 0){
            return new ErrorResponseDto(ReturnMessage.EMPTY_PARAM);
        }
        //验证密码
        String checkString = serverId + TimeUtils.getCurrentTime(TimeUtils.YYYYMMDD) + CodeFactory.CLOSE_MAIN_SERVER;
        String md5Code = MD5Utils.encodeUppercase(checkString);
        if(!md5Code.equals(password)){
            logger.info("changeStatus 密码错误, 正确密码：" + md5Code);
            return new ErrorResponseDto(ReturnMessage.PASSWORD_ERROR);
        }

        ServerModel serverModel = serverMapper.selectByPrimaryKey(serverId);
        if(serverModel == null){
            logger.info("没有对应的 serverId：" + serverId);
            return new ErrorResponseDto(ReturnMessage.SERVER_ID_ERROR);
        }

        serverMapper.changeStatus(serverId, status);
        return new SuccessResponseDto();
    }
}
