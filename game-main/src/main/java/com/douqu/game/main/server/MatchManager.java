//package com.douqu.game.main.server;
//
//import com.douqu.game.core.controller.PlayerController;
//import com.douqu.game.core.e.E_PlayerStatus;
//import com.douqu.game.core.entity.ext.BattleInfo;
//import com.douqu.game.core.protobuf.SGCommonProto;
//
//import java.util.concurrent.CopyOnWriteArrayList;
//
///**
// * @Author: Bean
// * @Description:
// * @Date: 2017-10-27 15:30
// */
//public class MatchManager {
//
//        /**
//     * 匹配池
//     */
//    private List<MatchJob> matchPool = new CopyOnWriteArrayList<>();
//    /***/
//    private Map<String, MatchJob> matchJobMap = new ConcurrentHashMap<>();
//    /**
//     * key: 段位
//     * value：此段位所有正在匹配的玩家
//     */
//    private Map<Integer, List<MatchJob>> gradeMatchPool = new ConcurrentHashMap<>();
//
//    public void updateMatch()
//    {
//        for(MatchJob job : matchPool)
//        {
//            if(job.isLock())
//                continue;
//
//            if(job.isSuccess())
//                removeMatch(job.getPlayer());
//            else
//                job.update();
//        }
//    }
//
//    public synchronized void addMatch(PlayerController playerController)
//    {
//        playerController.setStatus(E_PlayerStatus.MATCHING);
//
//        BattleInfo battleInfo = (BattleInfo) playerController.getPlayer().getExtInfo(BattleInfo.class);
//
//        MatchJob job = new MatchJob(playerController, battleInfo.getLastTarget(SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVP_ARENA.getNumber()));
//
//        int grade = playerController.getPlayer().grade;
//        List<MatchJob> gradePool = gradeMatchPool.get(grade);
//        if(gradePool == null)
//        {
//            gradePool = new CopyOnWriteArrayList<>();
//            gradeMatchPool.put(grade, gradePool);
//        }
//
//        gradePool.add(job);
//        matchPool.add(job);
//        matchJobMap.put(playerController.getObjectIndex(), job);
//    }
//
//
//    public synchronized void removeMatch(PlayerController playerController)
//    {
//        playerController.setStatus(E_PlayerStatus.FREE);
//
//        MatchJob job = matchJobMap.get(playerController.getObjectIndex());
//        if(job != null)
//        {
//            matchPool.remove(job);
//            List<MatchJob> list = gradeMatchPool.get(playerController.getPlayer().grade);
//            if(list != null)
//                list.remove(job);
//        }
//
//        System.out.println("WorldManager removeMatch 从匹配池删除玩家：" + playerController);
//    }
//
//    public List<MatchJob> getMatchPoolByGrade(int grade)
//    {
//        return gradeMatchPool.get(grade);
//    }
//
//    public List<MatchJob> getMatchPool()
//    {
//        return matchPool;
//    }
//}
