package com.douqu.game.main.server.thread;

import com.douqu.game.core.entity.Player;
import com.douqu.game.main.server.SpringContext;
import com.douqu.game.main.service.PlayerService;

import java.util.Date;

/**
 * Created by bean on 2017/8/11.
 */
public class SavePlayerThread implements Runnable {

    private Player player;

    private boolean isLogout;

    public SavePlayerThread(Player player, boolean isLogout)
    {
        this.player = player;
        this.isLogout = isLogout;
    }


    @Override
    public void run() {
        PlayerService playerService = SpringContext.getBean(PlayerService.class);

        if(isLogout)
        {
            player.setLastLogoutTime(new Date());
        }

        playerService.update(player);
    }
}
