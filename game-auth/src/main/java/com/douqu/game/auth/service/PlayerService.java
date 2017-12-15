package com.douqu.game.auth.service;


import com.douqu.game.auth.database.model.RoleRecordModel;
import com.douqu.game.auth.web.request.AuthReqDto;
import com.douqu.game.auth.web.request.LoginReqDto;
import com.douqu.game.auth.web.request.RegisterReqDto;
import com.douqu.game.auth.web.request.UpdateRecordReqDto;
import com.douqu.game.core.web.response.BaseResponseDto;

/**
 * Author : wamgzhenfei
 * 2017-07-06 15:55
 */
public interface PlayerService {
    BaseResponseDto auth(AuthReqDto loginReqDto);
    public RoleRecordModel getUserRecordByAccount(String account);
    public RoleRecordModel getUserRecordByDevice(String device);

    BaseResponseDto register(RegisterReqDto register);

    BaseResponseDto loginSuccess(LoginReqDto loginReqDto);

    BaseResponseDto getServerList(String account, Integer channel);

    BaseResponseDto updateRecord(UpdateRecordReqDto updateRecordReqDto);

    BaseResponseDto getCaptcha(String phone);
}
