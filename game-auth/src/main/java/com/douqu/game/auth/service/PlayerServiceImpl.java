package com.douqu.game.auth.service;


import com.alibaba.fastjson.JSONObject;
import com.bean.core.util.FormatUtils;
import com.douqu.game.auth.database.mapper.AreaMapper;
import com.douqu.game.auth.database.mapper.LoginLogMapper;
import com.douqu.game.auth.database.mapper.RoleRecordMapper;
import com.douqu.game.auth.database.mapper.ServerMapper;
import com.douqu.game.auth.database.model.AreaModel;
import com.douqu.game.auth.database.model.LoginLogModel;
import com.douqu.game.auth.database.model.RoleRecordModel;
import com.douqu.game.auth.database.model.ServerModel;
import com.douqu.game.auth.platform.*;
import com.douqu.game.auth.server.SpringContext;
import com.douqu.game.auth.utils.SmsMessageUtil;
import com.douqu.game.auth.utils.TwoTuple;
import com.douqu.game.auth.web.request.AuthReqDto;
import com.douqu.game.auth.web.request.LoginReqDto;
import com.douqu.game.auth.web.request.RegisterReqDto;
import com.douqu.game.auth.web.request.UpdateRecordReqDto;
import com.douqu.game.auth.web.response.AreaIncludeServerDto;
import com.douqu.game.auth.web.response.PlayerResDto;
import com.douqu.game.auth.web.response.ServerResDto;
import com.douqu.game.auth.web.response.ServesInfoResDto;
import com.douqu.game.core.e.ReturnMessage;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.util.Utils;
import com.douqu.game.core.web.response.BaseResponseDto;
import com.douqu.game.core.web.response.ErrorResponseDto;
import com.douqu.game.core.web.response.SuccessResponseDto;
import com.mysql.jdbc.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author : wangzhenfei
 * 2017-07-06 15:55
 */
@Service
public class PlayerServiceImpl implements PlayerService {
    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    LoginLogMapper loginLogMapper;

    @Autowired
    ServerMapper serverMapper;

    @Autowired
    AreaMapper areaMapper;

    @Autowired
    RoleRecordMapper roleRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseResponseDto auth(AuthReqDto reqDto) {
        logger.info("auth res:" + reqDto.toString());
        String account = reqDto.getAccount();
        String password = reqDto.getPassword();
        String deviceId = reqDto.getDeviceId();
        if(StringUtils.isNullOrEmpty(account) && StringUtils.isNullOrEmpty(deviceId)){
            return new ErrorResponseDto(ReturnMessage.ACCOUNT_ILLEGAL);
        }
        Class  cls = PhoneImpl.class;
        switch (reqDto.getChannel()){
            case SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_QUICK_VALUE:
                if(StringUtils.isNullOrEmpty(deviceId)){
                    return new ErrorResponseDto(ReturnMessage.DEVICE_ID_NOT_NULL);
                }
                cls = DeviceIdImpl.class;
                break;
            case SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_PHONE_VALUE:
                if(StringUtils.isNullOrEmpty(account))
                {
                    logger.debug("账号为空!");
                    return new ErrorResponseDto(ReturnMessage.ACCOUNT_ILLEGAL);
                }
                cls = PhoneImpl.class;

                break;

            case SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_QQ_VALUE:
                if(StringUtils.isNullOrEmpty(account))
                {
                    logger.debug("账号为空!");
                    return new ErrorResponseDto(ReturnMessage.ACCOUNT_ILLEGAL);
                }
                cls = QQImpl.class;
                break;
            case SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_WECHAT_VALUE:
                if(StringUtils.isNullOrEmpty(account))
                {
                    logger.debug("账号为空!");
                    return new ErrorResponseDto(ReturnMessage.ACCOUNT_ILLEGAL);
                }
                cls = WechatImpl.class;
                break;
            case SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_UC_VALUE:
                if(StringUtils.isNullOrEmpty(account))
                {
                    logger.debug("账号为空!");
                    return new ErrorResponseDto(ReturnMessage.ACCOUNT_ILLEGAL);
                }
                cls = UCImpl.class;
                break;
        }

        TwoTuple<RoleRecordModel, ReturnMessage> twoTuple = SpringContext.getPlatformSDK(cls).auth(0, account,  password,  deviceId);

        if(twoTuple.getSecond() != null){ // 有错误
            return new ErrorResponseDto(twoTuple.getSecond());
        }

        RoleRecordModel recordModel = twoTuple.getFirst();

        AreaIncludeServerDto resDto = new AreaIncludeServerDto();
        List<ServerResDto> servers = new ArrayList<>();

        ServerModel serverModel = null;
        Integer serverId = 0;
        if(recordModel != null){
            //获取最近登录的服务器
            serverId = roleRecordMapper.gerRecentLoginServerId(reqDto.getChannel() != SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_QUICK_VALUE?
                    recordModel.getAccount() : recordModel.getDeviceId());
        }

        serverId = serverId == null? 0: serverId.intValue();

        if(serverId != 0){
            serverModel = serverMapper.selectByPrimaryKey(serverId);
        }
        if(serverModel == null){
            serverModel = serverMapper.getNewServer();
        }
        if(serverModel != null){
            servers.add(new ServerResDto(serverModel));
        }

        resDto.setServers(servers);
        if(serverModel != null){
            AreaModel areaModel = areaMapper.selectByPrimaryKey(serverModel.getAreaId());
            if(areaModel != null){
                resDto.setAreaId(areaModel.getId());
                resDto.setAreaName(areaModel.getName());
            }
        }
//        resDto.getServers().get(0).setPort(11111);
        logger.info("auth res:" + resDto);
        return new SuccessResponseDto(resDto);
    }


    @Override
    public RoleRecordModel getUserRecordByDevice(String deviceId)
    {
        return roleRecordMapper.getByDeviceId(deviceId);
    }

    @Override
    public RoleRecordModel getUserRecordByAccount(String device)
    {
        return roleRecordMapper.getByAccount(device);
    }


    @Override
    public BaseResponseDto register(RegisterReqDto register) {
        logger.info("register:" + register.toString());
        if(register == null){
            return new ErrorResponseDto(ReturnMessage.EMPTY_PARAM);
        }

        Class  cls = PhoneImpl.class;
        switch (register.getChannel()){
            case SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_QUICK_VALUE:
                if(StringUtils.isNullOrEmpty(register.getDeviceId())){
                    return new ErrorResponseDto(ReturnMessage.DEVICE_ID_NOT_NULL);
                }
                cls = DeviceIdImpl.class;
                break;
            case SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_PHONE_VALUE:
                if(StringUtils.isNullOrEmpty(register.getAccount()))
                {
                    logger.debug("账号为空!");
                    return new ErrorResponseDto(ReturnMessage.ACCOUNT_ILLEGAL);
                }
                cls = PhoneImpl.class;

                break;

            case SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_QQ_VALUE:
                if(StringUtils.isNullOrEmpty(register.getAccount()))
                {
                    logger.debug("账号为空!");
                    return new ErrorResponseDto(ReturnMessage.ACCOUNT_ILLEGAL);
                }
                cls = QQImpl.class;
                break;
            case SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_WECHAT_VALUE:
                if(StringUtils.isNullOrEmpty(register.getAccount()))
                {
                    logger.debug("账号为空!");
                    return new ErrorResponseDto(ReturnMessage.ACCOUNT_ILLEGAL);
                }
                cls = WechatImpl.class;
                break;
            case SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_UC_VALUE:
                if(StringUtils.isNullOrEmpty(register.getAccount()))
                {
                    logger.debug("账号为空!");
                    return new ErrorResponseDto(ReturnMessage.ACCOUNT_ILLEGAL);
                }
                cls = UCImpl.class;
                break;
        }

        Integer serverId = register.getServerId() == null? 0 : register.getServerId().intValue();
        TwoTuple<RoleRecordModel, ReturnMessage> twoTuple = SpringContext.getPlatformSDK(cls).auth(serverId, register.getAccount(),
                register.getPassword(),  register.getDeviceId());
        if(twoTuple.getSecond() != null){ // 有错误
            return new ErrorResponseDto(twoTuple.getSecond());
        }

        //检查服务器是否存在
        ServerModel serverModel = serverMapper.selectByPrimaryKey(serverId);
        if(serverModel == null){
            return new ErrorResponseDto(ReturnMessage.SERVER_ID_ERROR);
        }

        //检查同名
        RoleRecordModel recordModel = roleRecordMapper.getByPlayName(register.getPlayName());
        if(recordModel != null){
            return new ErrorResponseDto(ReturnMessage.NICKNAME_EXITS);
        }

        String key = register.getChannel() != SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_QUICK_VALUE ?
                register.getAccount() : register.getDeviceId();
        //检查是否同一个服务器创建多个角色
        List<RoleRecordModel> recordModels = roleRecordMapper.gerRecordsByAccountOrDeviceId(Utils.createAccount(key,
                SGCommonProto.E_CHANNEL_TYPE.forNumber(register.getChannel())));
        for(RoleRecordModel model : recordModels){
            if(model.getServerId() == serverId){
                return new ErrorResponseDto(ReturnMessage.THIS_SERVER_PLAYER_EXITS);
            }
        }

        if(!register.isJustCheck()){
            twoTuple.getFirst().setPlayerName(register.getPlayName());
            twoTuple.getFirst().setIp(register.getIp());
            twoTuple.getFirst().setAvatar(register.getAvatar());
            twoTuple.getFirst().setLevel(register.getLevel());
            twoTuple.getFirst().setServerId(register.getServerId());
            roleRecordMapper.insert(twoTuple.getFirst());
        }

        return new SuccessResponseDto();
    }

    @Override
    public BaseResponseDto loginSuccess(LoginReqDto loginReqDto) {
        logger.info("loginSuccess:" + loginReqDto.toString());
        roleRecordMapper.updateLoginTime(Utils.createAccount(loginReqDto.getAccount(),
                SGCommonProto.E_CHANNEL_TYPE.forNumber(loginReqDto.getChannel())), loginReqDto.getServerId());
        return new SuccessResponseDto();
    }

    @Override
    public BaseResponseDto getServerList(String account, Integer channel) {
        //获取区信息
        List<AreaModel> areas = areaMapper.getList();
        List<ServerModel> servers = serverMapper.getList();
        Map<Integer, List<ServerResDto>> map = new HashMap<>();
        for(ServerModel model : servers){
            List<ServerResDto> list = map.get(model.getAreaId());
            if(list == null){
                list = new ArrayList<>();
                map.put(model.getAreaId(), list);
            }
            list.add(new ServerResDto(model));
        }


        ServesInfoResDto resDto = new ServesInfoResDto();
        List<AreaIncludeServerDto> aSs = new ArrayList<>();
        for(AreaModel model : areas){
            AreaIncludeServerDto areaServers = new AreaIncludeServerDto();
            areaServers.setAreaId(model.getId());
            areaServers.setAreaName(model.getName());
            aSs.add(areaServers);
            List<ServerResDto> list = map.get(model.getId());
            areaServers.setServers(list == null? new ArrayList<ServerResDto>() : list);
        }
        resDto.setAreaServers(aSs);

        //获取推荐列表
        List<ServerModel> recommendList = serverMapper.getRecommendList();

        List<ServerResDto> list = new ArrayList<>();
        if(recommendList != null){
            for(ServerModel model : recommendList){
                list.add(new ServerResDto(model));
            }
        }
        resDto.setRecommendServers(list);

        //获取我所有的区角色
        List<RoleRecordModel> recordModels = roleRecordMapper.gerRecordsByAccountOrDeviceId(Utils.createAccount(account, SGCommonProto.E_CHANNEL_TYPE.forNumber(channel)));
        List<PlayerResDto> plays = new ArrayList<>();
        if(recordModels != null){
            for(RoleRecordModel model : recordModels){
                PlayerResDto player = new PlayerResDto();
                player.setLevel(model.getLevel());
                player.setAvatar(model.getAvatar());
                player.setNickName(model.getPlayerName());
                ServerModel sm = serverMapper.selectByPrimaryKey(model.getServerId());
                player.setServer(new ServerResDto(sm));
                plays.add(player);
            }
        }
        resDto.setPlayers(plays);

        logger.info("resDto:" + JSONObject.toJSONString(resDto));

        return new SuccessResponseDto(resDto);
    }

    @Override
    public BaseResponseDto updateRecord(UpdateRecordReqDto reqDto) {
        String key = Utils.createAccount(reqDto.getAccount(), SGCommonProto.E_CHANNEL_TYPE.
                forNumber(reqDto.getChannel()));
        RoleRecordModel recordModel = roleRecordMapper.gerRecordInServer(key, reqDto.getServerId());
        if(recordModel == null){
            return new ErrorResponseDto(ReturnMessage.NO_RECORD);
        }

        if(!StringUtils.isNullOrEmpty(reqDto.getPlayerName())){
            //检查同名
            RoleRecordModel samModel = roleRecordMapper.getByPlayName(reqDto.getPlayerName());
            if(samModel != null){
                return new ErrorResponseDto(ReturnMessage.NICKNAME_EXITS);
            }
        }
        roleRecordMapper.update(key,reqDto.getServerId(), reqDto.getAvatar(), reqDto.getPlayerName(), reqDto.getLevel());
        return new SuccessResponseDto();
    }

    @Override
    public BaseResponseDto getCaptcha(String phone) {
        if(!FormatUtils.checkPhoneFormat(phone))
        {
            logger.debug("手机号码格式错误:" + phone);
            return new ErrorResponseDto(ReturnMessage.PHONE_NUM_ILLEGAL);
        }
        String result = SmsMessageUtil.sendCaptcha(phone);
        return new SuccessResponseDto(result);
    }



}
