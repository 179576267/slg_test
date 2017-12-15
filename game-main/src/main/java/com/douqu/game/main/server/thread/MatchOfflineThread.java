//package com.douqu.game.main.server.thread;
//
//import com.douqu.game.core.controller.PlayerController;
//import com.douqu.game.main.server.SpringContext;
//import com.douqu.game.main.service.PlayerService;
//
///**
// * 离线匹配
// * Created by bean on 2017/7/28.
// */
//public class MatchOfflineThread implements Runnable {
//
//    private PlayerController playerController;
//
//    public MatchOfflineThread(PlayerController playerController)
//    {
//        this.playerController = playerController;
//    }
//
//    @Override
//    public void run() {
//
//        PlayerService playerService = SpringContext.getBean(PlayerService.class);
//        playerService.offlineMatch(playerController);
//
//    }
//}
