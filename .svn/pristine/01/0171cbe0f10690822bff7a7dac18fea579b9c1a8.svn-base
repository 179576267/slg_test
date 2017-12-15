package com.douqu.game.auth.web.controller;

import com.douqu.game.auth.database.mapper.ServerMapper;
import com.douqu.game.auth.service.PlayerService;
import com.douqu.game.auth.service.ServerService;
import com.douqu.game.auth.web.request.AuthReqDto;
import com.douqu.game.auth.web.request.LoginReqDto;
import com.douqu.game.auth.web.request.RegisterReqDto;
import com.douqu.game.auth.web.request.UpdateRecordReqDto;
import com.douqu.game.core.web.response.BaseResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author : wangzhenfei
 * 2017-07-06 15:55
 */
@RestController
@RequestMapping(value = "/server")
public class ServerController {
    @Autowired
    private ServerService serverService;

    @RequestMapping(value = "/changeStatus", method = RequestMethod.POST)
    public BaseResponseDto changeStatus(String password, Integer serverId, Integer status)
    {
        return serverService.changeStatus(password, serverId, status);
    }

}
