//package com.douqu.game.main.job;
//
//import com.douqu.game.core.controller.PlayerController;
//import com.douqu.game.core.protobuf.SGCommonProto;
//import com.douqu.game.main.server.*;
//import com.douqu.game.main.server.thread.MatchOfflineThread;
//import com.douqu.game.main.util.MsgUtils;
//import com.mysql.jdbc.StringUtils;
//import org.apache.log4j.Logger;
//
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//
///**
// * Created by bean on 2017/7/28.
// */
//public class MatchJob {
//
//    private Logger logger = Logger.getLogger(MatchJob.class);
//
//    public long createTime;
//
//    /**
//     * 进行时间，从0开始
//     */
//    public int doingTime;
//
//    private PlayerController player;
//
//    /**
//     * 开始匹配的时候要锁住
//     */
//    private boolean lock;
//
//    /**
//     * 是否匹配成功,成功后要从匹配池删除
//     */
//    private boolean success;
//
////    /**
////     * 匹配成功的时间
////     */
////    private long successTime;
//
//    /**
//     * 上一场匹配的对手
//     */
//    private String lastTargetIndex;
//
//
//    public MatchJob(PlayerController player, String lastTargetIndex)
//    {
//        this.player = player;
//        this.createTime = GameServer.currentTime;
//        this.doingTime = 0;
//        this.lastTargetIndex = lastTargetIndex;
//    }
//
//
//    public void update()
//    {
//        logger.info(player.getName()+"  开始匹配!");
//        doingTime += ScheduledTask.UPDATE_MATCH_TIME;
//
//        if(isLock())
//            return;
//
//        if(isSuccess())
//            return;
//
//        this.lock = true;
////        if(doingTime > 30000)
//        if(doingTime >= 3000)
//        {
//            logger.info("匹配池没有人，开始离线匹配!");
//            GameServer.getInstance().startThread(new MatchOfflineThread(player));
//        }
//        else
//        {
//            List<MatchJob> targetList = null;
//            WorldManager worldManager = GameServer.getInstance().getWorldManager();
//            int grade = player.getPlayer().grade;
//            if(doingTime <= 8000)
//            {
//                targetList = worldManager.getMatchPoolByGrade(grade);
//                logger.info("8秒内匹配规则!");
//            }
//            else if(doingTime <= 15000)
//            {
//                targetList = worldManager.getMatchPoolByGrade(grade + 1);
//                if(targetList == null)
//                    targetList = worldManager.getMatchPoolByGrade(grade -1);
//                else
//                    targetList.addAll(worldManager.getMatchPoolByGrade(grade -1));
//                logger.info("15秒内匹配规则!");
//            }
//            else if(doingTime <= 25000)
//            {
//                targetList = worldManager.getMatchPoolByGrade(grade + 2);
//                if(targetList == null)
//                    targetList = worldManager.getMatchPoolByGrade(grade -2);
//                else
//                    targetList.addAll(worldManager.getMatchPoolByGrade(grade -2));
//                logger.info("25秒内匹配规则!");
//            }
//            else if(doingTime <= 30000)
//            {
//                targetList = worldManager.getMatchPool();
//                logger.info("30秒内匹配规则!");
//            }
//            logger.info("匹配结果列表:" + targetList);
//            if(targetList != null && targetList.size() > 1)
//            {
//                Collections.sort(targetList, new Comparator<MatchJob>() {
//                    @Override
//                    public int compare(MatchJob o1, MatchJob o2) {
//                        return new Long(o1.createTime).compareTo(o2.createTime);
//                    }
//                });
//
//                MatchJob target = null;
//                for(MatchJob job : targetList)
//                {
//                    //不是自己并且不是自己的上一场目标
//                    if(!job.isSelf(player.getObjectIndex()) && !job.isLastTarget(player.getObjectIndex()))
//                    {
//                        target = job;
//                        break;
//                    }
//                }
//                logger.info("匹配结果："+target);
//                if(target != null)
//                {
//                    MsgUtils.startBattle(player, target.getPlayer(), SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVP_ARENA);
//                    this.success = true;
//                    target.success = true;
//                }
//            }
//
//            this.lock = false;
//        }
//    }
//
//
//    public boolean isSelf(String playerIndex)
//    {
//        return playerIndex.equals(player.getObjectIndex());
//    }
//
//    public boolean isLastTarget(String playerIndex)
//    {
//        return !StringUtils.isNullOrEmpty(lastTargetIndex) && lastTargetIndex.equals(playerIndex);
//    }
//
//
//
//    public boolean isLock() {
//        return lock;
//    }
//
//    public void setLock(boolean lock) {
//        this.lock = lock;
//    }
//
//    public boolean isSuccess() {
//        return success;
//    }
//
//    public void setSuccess(boolean success) {
//        this.success = success;
//    }
//
//    public PlayerController getPlayer() {
//        return player;
//    }
//
//    @Override
//    public String toString() {
//        return "MatchJob{" +
//                "createTime=" + createTime +
//                ", doingTime=" + doingTime +
//                ", player=" + player +
//                ", lock=" + lock +
//                ", success=" + success +
//                ", lastTargetIndex='" + lastTargetIndex + '\'' +
//                '}';
//    }
//}
