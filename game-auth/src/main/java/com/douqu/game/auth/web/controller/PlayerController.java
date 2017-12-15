package com.douqu.game.auth.web.controller;

import com.douqu.game.auth.service.PlayerService;
import com.douqu.game.auth.web.request.AuthReqDto;
import com.douqu.game.auth.web.request.LoginReqDto;
import com.douqu.game.auth.web.request.RegisterReqDto;
import com.douqu.game.auth.web.request.UpdateRecordReqDto;
import com.douqu.game.core.web.response.BaseResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Author : wangzhenfei
 * 2017-07-06 15:55
 */
@RestController
@RequestMapping(value = "/player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @RequestMapping(value = "/auth", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public BaseResponseDto auth(String account, String password, String ip, Integer channel)
    {
        AuthReqDto loginReqDto = new AuthReqDto();
        loginReqDto.setIp(ip == null? "": ip);
        loginReqDto.setDeviceId(account);
        loginReqDto.setAccount(account);
        loginReqDto.setChannel(channel == null ? 0 : channel.intValue());
        loginReqDto.setPassword(password);
        return playerService.auth(loginReqDto);
    }

    @RequestMapping(value = "/getCaptcha", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public BaseResponseDto getCaptcha(String phone)
    {
        return  playerService.getCaptcha(phone);
    }


    @RequestMapping(value = "/getServerList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public BaseResponseDto getServerList(String account, Integer channel)
    {
        return  playerService.getServerList(account, channel == null ? 0 : channel.intValue());
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public BaseResponseDto register(@RequestBody RegisterReqDto register)
    {
        return playerService.register(register);
    }

    @RequestMapping(value = "/loginSuccess", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public BaseResponseDto loginSuccess(@RequestBody LoginReqDto loginReqDto)
    {
        return playerService.loginSuccess(loginReqDto);
    }

    @RequestMapping(value = "/updateRecord", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public BaseResponseDto updateRecord(@RequestBody UpdateRecordReqDto updateRecordReqDto)
    {
        return playerService.updateRecord(updateRecordReqDto);
    }


}
