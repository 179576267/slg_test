package com.douqu.game.main.msg;


import com.douqu.game.core.controller.PlayerController;

/**
 * Created by bean on 2017/7/21.
 */
public interface AMsgChannel {

    public void messageChannel(int code, PlayerController playerController, byte[] data) throws Exception;
}
