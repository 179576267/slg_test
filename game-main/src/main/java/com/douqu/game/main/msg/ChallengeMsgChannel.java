package com.douqu.game.main.msg;

import com.bean.core.util.TimeUtils;
import com.douqu.game.core.config.*;
import com.douqu.game.core.config.challenge.*;
import com.douqu.game.core.config.drop.DropGroupConfig;
import com.douqu.game.core.config.drop.DropResult;
import com.douqu.game.core.config.task.TaskConfig;
import com.douqu.game.core.controller.PlayerController;
import com.douqu.game.core.database.model.PlayerModel;
import com.douqu.game.core.e.E_ExchangeRewardType;
import com.douqu.game.core.e.E_StableDataType;
import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.entity.db.ArenaRewardRecordDB;
import com.douqu.game.core.entity.db.InstanceDB;
import com.douqu.game.core.entity.db.LevelDB;
import com.douqu.game.core.entity.db.TaskDB;
import com.douqu.game.core.entity.ext.ChallengeInfo;
import com.douqu.game.core.config.common.CommonData;
import com.douqu.game.core.entity.ext.BagInfo;
import com.douqu.game.core.entity.ext.TaskInfo;
import com.douqu.game.core.entity.ext.data.challenge.ArenaData;
import com.douqu.game.core.entity.ext.data.challenge.HeroTempleData;
import com.douqu.game.core.entity.ext.data.challenge.InstanceData;
import com.douqu.game.core.entity.ext.data.challenge.OfficialRankData;
import com.douqu.game.core.entity.ext.data.world.WorldArenaRankBean;
import com.douqu.game.core.entity.ext.data.world.WorldArenaRankData;
import com.douqu.game.core.entity.ext.data.world.WorldOfficialRankBean;
import com.douqu.game.core.entity.ext.data.world.WorldOfficialRankData;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.factory.*;
import com.douqu.game.core.protobuf.*;
import com.douqu.game.core.protobuf.SGAreanProto.*;
import com.douqu.game.core.util.LoadUtils;
import com.douqu.game.core.util.SendUtils;
import com.douqu.game.main.GameServer;
import com.douqu.game.main.service.PlayerService;
import com.douqu.game.main.service.WorldService;
import com.douqu.game.main.util.MsgUtils;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mysql.jdbc.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author wangzhenfei
 *         2017-08-21 10:06
 *         挑战消息（副本，竞技场，官阶战）
 */
@Component
public class ChallengeMsgChannel implements AMsgChannel{
    Logger logger = Logger.getLogger(ChallengeMsgChannel.class);
    @Autowired
    private PlayerService playerService;

    @Autowired
    private WorldService worldService;

    @Override
    public void messageChannel(int code, PlayerController playerController, byte[] data)throws Exception{
        //获取相应的数据
        if(playerController == null)
            return;

        switch (code)
        {
            /*****************************副本***********************/
            case SGMainProto.E_MSG_ID.MsgID_Instance_GetInstanceInfo_VALUE:
                getInstanceList(playerController, data);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Instance_PassLevel_VALUE:
                addPassLevel(playerController, data);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Instance_ReceiveAward_VALUE:
                receiveAward(playerController, data);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Instance_RequestLevelBattle_VALUE:
                requestLevelBattle(playerController, data);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Instance_ReceiveLevelBoxReward_VALUE:
                receiveLevelAward(playerController, data);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Instance_GetLastPassLevel_VALUE:
                getLastPassLevel(playerController);
                break;
            /*****************************竞技场***********************/
            case SGMainProto.E_MSG_ID.MsgID_Arena_GetArenaInfo_VALUE:
                getPlayerArenaRankInfo(playerController, data);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Arena_Challenge_VALUE:
                arenaChallenge(playerController, data);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Arena_GetDailyReward_VALUE:
                getDailyReward(playerController);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Arena_PreviewRank_VALUE:
                previewRank(playerController);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Arena_RewardRecord_VALUE:
                rewardRecord(playerController);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Arena_ExchangeReward_VALUE:
                exchangeReward(playerController, data);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Arena_Sweep_VALUE:
                sweepArena(playerController, data);
                break;
            /*****************************官阶战***********************/
            case SGMainProto.E_MSG_ID.MsgID_OfficialWar_InitInfo_VALUE:
                initInfo(playerController, data, code);
                logger.debug("世界官阶榜信息：" + getOfficialRankInfo().toString());
                break;
            case SGMainProto.E_MSG_ID.MsgID_OfficialWar_PreviewRank_VALUE:
                previewRank(playerController, data, code);
                break;
            case SGMainProto.E_MSG_ID.MsgID_OfficialWar_ChallengeRank_VALUE:
                challengeRank(playerController, data, code);
                break;
            case SGMainProto.E_MSG_ID.MsgID_OfficialWar_SweepRank_VALUE:
                sweepRank(playerController, data, code);
                break;
            case SGMainProto.E_MSG_ID.MsgID_OfficialWar_GetDailyReward_VALUE:
                getDailyReward(playerController, data, code);
                break;
            case SGMainProto.E_MSG_ID.MsgID_OfficialWar_ExchangeReward_VALUE:
                exchangeReward(playerController, data, code);
                break;
            case SGMainProto.E_MSG_ID.MsgID_OfficialWar_RewardRecord_VALUE:
                rewardRecord(playerController, data, code);
                break;
            case SGMainProto.E_MSG_ID.MsgID_OfficialWar_IntegralReward_VALUE:
                integralReward(playerController, data, code);
                break;
            case SGMainProto.E_MSG_ID.MsgID_OfficialWar_IntegralRewardRecord_VALUE:
                integralRewardRecord(playerController, data, code);
                break;
            /*****************************英雄圣殿***********************/
            case SGMainProto.E_MSG_ID.MsgID_HeroTemple_InitInfo_VALUE:
                heroTempleInitInfo(playerController, data, code);
                break;
            case SGMainProto.E_MSG_ID.MsgID_HeroTemple_Challenge_VALUE:
                heroTempleChallenge(playerController, data, code);
                break;
            case SGMainProto.E_MSG_ID.MsgID_HeroTemple_Sweep_VALUE:
                heroTempleSweep(playerController, data, code);
                break;
            default:
                break;
        }

        if(CodeFactory.TEST){
            /**
             * 暂时的保存任务，调试完毕需要删除
             */
            worldService.update(GameServer.getInstance().getWorldManager().getWorldInfo());
        }
    }


    /*************************************副本star*************************************/

    /**
     * 获取关卡的宝箱奖励
     * @param playerController
     * @param data
     */
    private void receiveLevelAward(PlayerController playerController, byte[] data) {
        SGInstanceProto.S2C_ReceiveLevelBoxReward.Builder response = SGInstanceProto.S2C_ReceiveLevelBoxReward.newBuilder();
        SGInstanceProto.C2S_ReceiveLevelBoxReward request = null;
        try {
            request = SGInstanceProto.C2S_ReceiveLevelBoxReward.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if(request == null){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }
        int mapId = request.getChapterId();
        int levelId = request.getLevelId();

        InstanceData instanceInfo = playerController.getPlayer().getInstanceData();
        logger.debug("领取关卡宝箱请求参数-->地图id : " + mapId);
        logger.debug("领取关卡宝箱请求参数-->关卡id : " + levelId);
        //判断mapid和levelid是否合法
        InstanceConfig instanceConfig = DataFactory.getInstance().getGameObject(DataFactory.INSTANCE_KEY, mapId);
        if(instanceConfig == null)
        {
            logger.debug("领取关卡宝箱结果 ： " + "参数异常");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        //判断该关卡是否有奖励
        LevelConfig configLLevelConfig = DataFactory.getInstance().getGameObject(DataFactory.LEVEL_KEY, levelId);
        if(configLLevelConfig == null || configLLevelConfig.boxReward == 0){
            //返回给客户端
            logger.debug("领取关卡宝箱结果 ： " + "该关卡没有宝箱可以领取");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.BOX_NOTHING_REWARD));
            return;
        }

        InstanceDB instanceConfigMap = instanceInfo.getInstanceMap(mapId);
        if(instanceConfigMap == null){
            logger.debug("领取关卡宝箱结果 ： " + "地图未解锁");
            //返回给客户端
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.MAP_NOT_UNLOCK));
            return;
        }

        LevelDB levelConfig = instanceConfigMap.getLevelMap().get(levelId);

        if(levelConfig == null){//未解锁
            logger.debug("领取关卡宝箱结果 ： " + "地图未解锁");
            //返回给客户端
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.MAP_NOT_UNLOCK));
            return;
        }

        if(levelConfig.isReceive()){
            logger.debug("领取关卡宝箱结果 ： " + "已经领取过了");
            //返回给客户端
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.GET_REWARD_ED));
            return;
        }

        levelConfig.setReceive(true);

        DropGroupConfig dropGroupConfig = DataFactory.getInstance().getGameObject(DataFactory.DROP_GROUP_KEY, configLLevelConfig.boxReward);
        DropResult result = dropGroupConfig.reward(playerController);

        for(CommonData card : result.getCards())
        {
            response.addCard(card.parseCommonObject());
        }
        for(CommonData prop : result.getProps())
        {
            response.addProp(prop.parseCommonObject());
        }
        for(CommonData asset : result.getAssets())
        {
            response.addAsset(asset.parseCommonObject());
        }

        //返回给客户端
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Instance_ReceiveLevelBoxReward_VALUE, response.build().toByteArray());

    }

    /**
     * 获取副本宝箱奖励
     * @param playerController
     * @param data
     */
    private void receiveAward(PlayerController playerController, byte[] data)
    {

        SGInstanceProto.C2S_ReceiveInstanceAward request = null;
        try {
            request = SGInstanceProto.C2S_ReceiveInstanceAward.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if(request == null){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }
        int mapId = request.getChapterId();
        int id = request.getRewardId();//领取的ID
        logger.debug("领取奖励请求参数--->mapId" + mapId);
        logger.debug("领取奖励请求参数--->rewardId" + id);
        InstanceConfig instanceConfig = DataFactory.getInstance().getGameObject(DataFactory.INSTANCE_KEY, mapId);
        if(instanceConfig == null)
        {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }


        InstanceStarBox starBox = instanceConfig.getInstanceStarBox(id);
        if(starBox == null)
        {
            logger.debug("不存在宝箱id--->" + id);
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }


        InstanceData instanceInfo = playerController.getPlayer().getInstanceData();
        InstanceDB instanceConfigMap = instanceInfo.getInstanceMap(mapId);
        if(instanceConfigMap == null){
            logger.debug("该地图还未解锁");
            //返回给客户端
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.MAP_NOT_UNLOCK));
            return;
        }
        if(instanceConfigMap.getHasReward().contains(id)){
            logger.debug("已经领取过本次次奖励");
            //返回给客户端
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.GET_REWARD_ED));
            return;
        }


        int mapStars = 0;
        for(LevelDB levelConfig : instanceConfigMap.getFinishLevelConfigs()){
            mapStars += levelConfig.getMaxStars();
        }
        logger.debug("用户总星数：" + mapStars + ", 请求星数： " + mapStars);
        if(mapStars < starBox.star)
        {
            logger.debug("星级不满足条件" + mapStars);
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CONDITION_ERROR));
            return;
        }



        DropGroupConfig dropGroupConfig = DataFactory.getInstance().getGameObject(DataFactory.DROP_GROUP_KEY, starBox.dropId);

        DropResult result = dropGroupConfig.reward(playerController);

        instanceConfigMap.getHasReward().add(id);

        SGInstanceProto.S2C_ReceiveInstanceAward.Builder response = SGInstanceProto.S2C_ReceiveInstanceAward.newBuilder();
        for(CommonData card : result.getCards())
        {
            response.addCard(card.parseCommonObject());
        }
        for(CommonData prop : result.getProps())
        {
            response.addProp(prop.parseCommonObject());
        }
        for(CommonData asset : result.getAssets())
        {
            response.addAsset(asset.parseCommonObject());
        }

        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Instance_ReceiveAward_VALUE, response.build().toByteArray());
    }

    /**
     * 通过某一关卡
     * @param playerController
     * @param data
     */
    private void addPassLevel(PlayerController playerController ,byte [] data) {

        SGInstanceProto.C2S_PassLevel request = null;
        try {
            request = SGInstanceProto.C2S_PassLevel.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if(request == null){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        int mapId = request.getChapterId();
        int levelId = request.getLevelId();
        int stars = request.getStarts();

        InstanceData instanceInfo = playerController.getPlayer().getInstanceData();
        logger.debug("通过副本请求参数-->地图id : " + mapId);
        logger.debug("通过副本请求参数-->关卡id : " + levelId);
        logger.debug("通过副本请求参数-->通过星星数 : " + stars);
        logger.debug("通过副本请求参数-->通过关卡前 ： " + instanceInfo.toString());
        //判断mapid和levelid是否合法
        InstanceConfig instanceConfig = DataFactory.getInstance().getGameObject(DataFactory.INSTANCE_KEY, mapId);
        if(instanceConfig == null)
        {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        if( stars > 3 || stars < 1){
            //返回给客户端
            logger.debug("副本过关结果 ： " + "参数异常，星星数不满足条件");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }



        if(!instanceInfo.getCanPassMap().contains(mapId)){
            logger.debug("副本过关结果 ： " + "地图未解锁");
            //返回给客户端
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.MAP_NOT_UNLOCK));
            return;
        }
        InstanceDB instanceConfigMap = instanceInfo.getInstanceMap(mapId);

        //获取正在进行的副本
        if(instanceConfigMap == null){ // 需要开启新的地图
//            //检查前一个地图是否都过关了
//            if(mapId > 1 ){
//                instanceMap = instanceInfo.getInstanceMap().get(mapId - 1);
//                if(instanceMap == null || instanceMap.getFinishLevels().size() != 10){
//                    //返回给客户端
//                    response.setResult(E_INSTANCE_PASS.INSTANCE_PASS_MAP_UNLOCK);
//                    playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Instance_PassLevel_VALUE, response.build().toByteArray());
//                    return;
//                }
//            }
            if(instanceInfo.getNextPassLevel().contains(levelId)){
                instanceConfigMap = new InstanceDB(mapId);
                LevelDB lv = new LevelDB(levelId);
                lv.setMaxStars(stars);
                lv.setReceive(lv.getConfig().boxReward == 0);
                instanceConfigMap.addLevel(lv);
                instanceInfo.addInstanceMap(instanceConfigMap);
                instanceInfo.changeNext();
                sendFirstPassReward(playerController, lv.getConfig());

            }else {
                //返回给客户端
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.MAP_NOT_UNLOCK));
                return;
            }

        }else {// 还在当前地图

            if(instanceInfo.getNextPassLevel().contains(levelId)){// 新的关卡
                LevelDB lv = new LevelDB(levelId);
                lv.setMaxStars(stars);
                lv.setReceive(lv.getConfig().boxReward == 0);
                instanceConfigMap.addLevel(lv);
                instanceInfo.changeNext();
                sendFirstPassReward(playerController, lv.getConfig());


            }else if(instanceConfigMap.getLevelMap().get(levelId) != null){//重复通过老的关卡
                LevelDB levelConfig = instanceConfigMap.getLevelMap().get(levelId);
                if(stars > levelConfig.getMaxStars()){
                    levelConfig.setMaxStars(stars);
                }
            }else {
                //返回给客户端
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.MAP_NOT_UNLOCK));
                return;
            }
        }

        if(instanceInfo.hasRedPointRemind()){
            SendUtils.sendRedPointRemind(playerController, SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_INSTANCE);
        }

//        InstanceDB instanceDB = instanceInfo.getInstanceMap().get(mapId);
//        LevelDB levelDB =  instanceDB.getLevelMap().get(levelId);



        //检测副本通关任务
        TaskInfo taskInfo = playerController.getPlayer().getExtInfo(TaskInfo.class);
        List<TaskDB> doingList = taskInfo.getDoingTask(ConstantFactory.TASK_TARAGET_PASSLEVEL);
        for(TaskDB taskDB : doingList) {
            taskDB.check(ConstantFactory.TASK_TARAGET_PASSLEVEL, levelId);
        }

        //检测副本通关任务
        doingList = taskInfo.getDoingEveryTask(ConstantFactory.TASK_TARAGET_EVERYDAY_INSTANCE);
        for(TaskDB taskDB : doingList) {
            if(taskDB.status == SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY_VALUE){
                taskDB.check(ConstantFactory.TASK_TARAGET_EVERYDAY_INSTANCE, 1);
            }
        }



        //缓存结算数据，等待客户端重连主服务器获取
        InstanceData.PassLevelCache response = new InstanceData.PassLevelCache(mapId, levelId, stars);
        logger.debug("缓存过关数据数据：" + response);
        instanceInfo.responseTemp = response;
    }

    /**
     * 发放首次过关奖励
     * @param playerController
     * @param lv
     */
    private void sendFirstPassReward(PlayerController playerController, LevelConfig lv) {
        BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
        bagInfo.addGoods(lv.passReward, null);
    }

    /**
     * 获取最后一次的挑战结果
     * @param playerController
     */
    private void getLastPassLevel(PlayerController playerController) {
        InstanceData instanceInfo = playerController.getPlayer().getInstanceData();
        SGInstanceProto.S2C_GetLastPassLevel.Builder response = SGInstanceProto.S2C_GetLastPassLevel.newBuilder();
        if(instanceInfo.responseTemp != null){
            response.setChapterId(instanceInfo.responseTemp.mapId);
            response.setLevelId(instanceInfo.responseTemp.levelId);
            response.setStarts(instanceInfo.responseTemp.stars);
        }

        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Instance_GetLastPassLevel_VALUE, response.build().toByteArray());
        logger.debug("返回给客户端最近的挑战信息 ： " + response.toString());
        logger.debug("通过关卡后 ： " + instanceInfo.toString());
        instanceInfo.responseTemp = null;
    }

    /**
     * 请求副本战斗
     * @param playerController
     * @param data
     */
    private void requestLevelBattle(PlayerController playerController, byte [] data)
    {

        SGInstanceProto.C2S_RequestLevelBattle request = null;
        try {
            request = SGInstanceProto.C2S_RequestLevelBattle.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if(request == null){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        int mapId = request.getChapterId();
        int levelId = request.getLevelId();

        LevelConfig levelConfig = DataFactory.getInstance().getGameObject(DataFactory.LEVEL_KEY, levelId);
        if(levelConfig == null || levelConfig.getInstanceId() != mapId)
        {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        InstanceData instanceInfo = playerController.getPlayer().getInstanceData();
        logger.debug("副本战斗请求参数-->地图id : " + mapId);
        logger.debug("副本战斗请求参数-->关卡id : " + levelId);
        //判断mapid和levelid是否合法

        InstanceDB instanceConfig = instanceInfo.getInstanceMap(mapId);

        //获取正在进行的副本
        if(instanceConfig == null){ // 需要开启新的地图
            if(!instanceInfo.getNextPassLevel().contains(levelId)){
                //返回给客户端
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.MAP_NOT_UNLOCK));
                return;
            }
        }


        MsgUtils.startBattle(playerController, SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVE_INSTANCE, levelConfig);

    }

    /**
     * 获取过关列表信息
     * @param playerController
     * @param data
     */
    private void getInstanceList(PlayerController playerController, byte[] data) {

        SGInstanceProto.S2C_GetInstanceInfo.Builder response = SGInstanceProto.S2C_GetInstanceInfo.newBuilder();
        SGInstanceProto.S2C_GetInstanceInfo request = null;
        try {
            request = SGInstanceProto.S2C_GetInstanceInfo.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if(request == null){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        int targetMapId = request.getChapterId();

        logger.debug("获取关卡列表信息传入参数 ： " + " 地图id：" + targetMapId);


        InstanceData instanceInfo = playerController.getPlayer().getInstanceData();
        InstanceDB targetMap = instanceInfo.getInstanceMap(targetMapId);
        if(targetMap == null && targetMapId != 0){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.MAP_NOT_UNLOCK));
            return;
        }

        logger.debug("获取关卡列表信息 ： " + instanceInfo.toString());

        SGCommonProto.InstanceMap.Builder proInstanceMap;
        SGCommonProto.Level.Builder proLevel;
        for(InstanceDB map : instanceInfo.getInstanceList()){
            proInstanceMap = SGCommonProto.InstanceMap.newBuilder();
            proInstanceMap.setChapterId(map.id);
            for(LevelDB lv : map.getFinishLevelConfigs()){
                proLevel = SGCommonProto.Level.newBuilder();
                proLevel.setMaxStars(lv.getMaxStars());
                proLevel.setLevelId(lv.id);
                proLevel.setBoxReward(lv.isReceive());
                proInstanceMap.addLevels(proLevel);
            }
            map.getHasReward().forEach(proInstanceMap::addReciverRewards);
            response.addMaps(proInstanceMap);
        }
        response.setChapterId(targetMapId);
        logger.debug("返回给客户端关卡列表信息 ： " + response.toString());
        //返回给客户端
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Instance_GetInstanceInfo_VALUE, response.build().toByteArray());
    }

    /*************************************副本end***********************************************************************************/

    /*************************************竞技场star***********************************************************************************/

    /**
     * 扫荡竞技场
     * @param playerController
     * @param data
     */
    private void sweepArena(PlayerController playerController, byte[] data) {
        C2S_ArenaSweep request = null;
        try {
            request = C2S_ArenaSweep.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if(request == null){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        ArenaData arenaData = playerController.getPlayer().getArenaData();
        logger.debug("当前已经挑战的挑战次数剩余 :" + arenaData.getRemainTodayChallengeTimes());
        if(!arenaData.isChallengeTimesEnough()){
            //TODO 挑战次数已经用完
            logger.debug("挑战次数已经用完,当前挑战次数剩余 :" + arenaData.getRemainTodayChallengeTimes());
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ARENA_TIMES_NOT_ENOUGH));
            return;
        }

        int targetRank = request.getTargetRank();
        int myRank =  GameServer.getInstance().getWorldManager().getWorldArenaData().getArenaRankByObjectIndex(playerController.getObjectIndex());
        logger.debug("sweepArena， myRank: " + myRank + ", targetRank: " + targetRank);
        if(myRank >= targetRank){ // 自己的排名比目标高
            logger.debug("扫荡失败，自己的排名比目标低");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ARENA_NOT_SWEEP_HEIGHT_PLAYER));
            return;
        }
        arenaData.addChallengeTimes(GameServer.currentTime);
        //发放扫荡奖励
        StableData stableData = DataFactory.getInstance().getGameObject(DataFactory.STABLE_DATA_KEY, E_StableDataType.ARENA_PASS_REWARD.getCode());
        BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
        bagInfo.addGoods(stableData.goods, null);
        S2C_ArenaSweep.Builder response = S2C_ArenaSweep.newBuilder();
        logger.debug("返回给客户端竞技场扫荡信息 ： " + response.toString());


        //任务检测
        TaskInfo taskInfo = playerController.getPlayer().getExtInfo(TaskInfo.class);

        List<TaskDB> doingList = taskInfo.getDoingTask(ConstantFactory.TASK_TARAGET_MAIN_ARENA_BATTLE);
//        TaskConfig taskConfig = null;
        for(TaskDB taskDB : doingList) {
//            taskConfig = DataFactory.getInstance().getGameObject(DataFactory.TASK_KEY,taskDB.id);
            taskDB.check(ConstantFactory.TASK_TARAGET_MAIN_ARENA_BATTLE, 1);
        }

        List<TaskDB> everyDayList = taskInfo.getDoingEveryTask(ConstantFactory.TASK_TARAGET_MAIN_ARENA_BATTLE);
        for(TaskDB taskDB : everyDayList) {
            if(taskDB.status == SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY_VALUE){
                taskDB.check(ConstantFactory.TASK_TARAGET_MAIN_ARENA_BATTLE, 1);
            }
        }
        //检测副本通关任务
        List<TaskDB> everyList = taskInfo.getDoingEveryTask(ConstantFactory.TASK_TARAGET_MAIN_OFFICIALWAR);
        for(TaskDB taskDB : everyList) {
            if(taskDB.status == SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY_VALUE){
                taskDB.check(ConstantFactory.TASK_TARAGET_MAIN_OFFICIALWAR, 1);
            }
        }
        //返回给客户端
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Arena_Sweep_VALUE, response.build().toByteArray());

    }


    /**
     * 兑换奖励
     * @param playerController
     */
    private void exchangeReward(PlayerController playerController, byte[] data) {
        C2S_ArenaExchangeReward request = null;
        try {
            request = C2S_ArenaExchangeReward.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if(request == null){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }
        ArenaData arenaData = playerController.getPlayer().getArenaData();
        int rewardId = request.getRewardId();
        if(arenaData.getAlreadyRewardIds().contains(rewardId)){
            logger.debug("竞技场该资源已经兑换过了");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ARENA_REWARD_GROUP_ALREADY_REWARD));
            return;
        }
        ExchangeRewardConfig rewardConfig = DataFactory.getInstance().getGameObject(DataFactory.REWARD_KEY, rewardId);
        if(rewardConfig == null || rewardConfig.type != E_ExchangeRewardType.ARENA.getCode()){
            logger.debug("竞技场兑换 rewardId 不存在：" + rewardId);
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }
        int myRank =  GameServer.getInstance().getWorldManager().getWorldArenaData().getArenaRankByObjectIndex(playerController.getObjectIndex());
        if(myRank > rewardConfig.minRank){ // 自己的排名大于要求的最低排名，不符合领取条件
            //TODO error
            logger.debug("竞技场自己的排名大于要求的最低排名，不符合领取条件 ");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ARENA_RANK_NOT_ENOUGH));
            return;
        }
        BagInfo bagInfo =  playerController.getPlayer().getExtInfo(BagInfo.class);
        //循环配置表检测自己的资源是否足够
        for(CommonData assets : rewardConfig.needAssets){
            if(bagInfo.getAsset(assets.id) < assets.value){//自己拥有的小于配置的
               //TODO 资源不足
                //需要的资源不够
                logger.debug("竞技场兑换奖励资源不足 ");
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ASSET_NOT_ENOUGH, assets.id));
                return;
            }
        }
        arenaData.getAlreadyRewardIds().add(rewardId);
        GoodsData[] consumeData = new GoodsData[rewardConfig.needAssets.length];
        for(int i = 0; i <rewardConfig.needAssets.length; i++ ){
            consumeData[i] = new GoodsData(SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_ASSETS_VALUE, rewardConfig.needAssets[i].id, rewardConfig.needAssets[i].value);
        }
        bagInfo.addGoods(rewardConfig.reward, consumeData);
//        bagInfo.reduceAsset(rewardConfig.needAssets);
        for(GoodsData goodsData : rewardConfig.reward){
            addRewardRecord(arenaData, goodsData);
        }

        S2C_ArenaExchangeReward.Builder response = S2C_ArenaExchangeReward.newBuilder();
        logger.debug("返回给客户端竞技场兑换结果 ： " + response.toString());
        //返回给客户端
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Arena_ExchangeReward_VALUE, response.build().toByteArray());

    }


    /**
     * 竞技场兑换记录
     * @param playerController
     */
    private void rewardRecord(PlayerController playerController) {
        S2C_ArenaRewardRecord.Builder response = S2C_ArenaRewardRecord.newBuilder();

        ArenaData arenaData = playerController.getPlayer().getArenaData();
        List<ArenaRewardRecordDB> records = arenaData.getRewardRecords();
        for(ArenaRewardRecordDB record : records){
            SGCommonProto.RewardInfo.Builder info = SGCommonProto.RewardInfo.newBuilder();
            info.setId(record.getId());
            info.setNum(record.getNum());
            info.setType(SGCommonProto.E_GOODS_TYPE.forNumber(record.getType()));
            response.addRewardInfo(info);
        }

        List<Integer> ids = arenaData.getAlreadyRewardIds();
        for(Integer id : ids){
            response.addAlreadyRewardIds(id);
        }

        logger.debug("返回给客户端竞技场兑换记录 ： " + response.toString());
        //返回给客户端
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Arena_RewardRecord_VALUE, response.build().toByteArray());
    }


    /**
     * 查看竞技场前100名
     * @param playerController
     */
    private void previewRank(PlayerController playerController) {
        WorldArenaRankData worldArenaRankData = GameServer.getInstance().getWorldManager().getWorldArenaData();
        List<WorldArenaRankBean> rankList = worldArenaRankData.getUnderTargetRankList(100);
        S2C_ArenaPreviewRank.Builder response = S2C_ArenaPreviewRank.newBuilder();
        for(WorldArenaRankBean rank : rankList){
            SGCommonProto.ArenaPlayerInfo.Builder builder = SGCommonProto.ArenaPlayerInfo.newBuilder();
            builder.setRank(worldArenaRankData.getArenaRankByObjectIndex(rank.getObjectIndex()));
            PlayerModel player = playerService.getPlayerByIndex(rank.getObjectIndex());
            if(player != null){
                builder.setName(player.getName());
                builder.setCamp(SGCommonProto.E_CAMP_TYPE.forNumber(player.getCamp()));
                builder.setObjectIndex(player.getObjectIndex());
                builder.setFightingCapacity(player.getFc());
                builder.setMasterId(player.getMaster());
            }else {
                logger.error("根据玩家的索引无法查到玩家， 索引为: " + rank.getObjectIndex());
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.SERVER_DATA_ERROR));
                return;
            }
            response.addPlayers(builder);
        }

        logger.debug("返回给客户端竞技场前100名 ： " + response.toString());
        //返回给客户端
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Arena_PreviewRank_VALUE, response.build().toByteArray());
    }


    /**
     * 获取玩家的竞技场排名
     * @param playerController
     * @param data
     */
    private void getPlayerArenaRankInfo(PlayerController playerController, byte[] data) {
        ArenaData arenaData = playerController.getPlayer().getArenaData();
//        TODO  测试专用
//        if(CodeFactory.TEST){
//            arenaData.reset();
//        }

        S2C_ArenaGetArenaInfo.Builder response = S2C_ArenaGetArenaInfo.newBuilder();
        //自己的排名
        WorldArenaRankData worldArenaRankData = GameServer.getInstance().getWorldManager().getWorldArenaData();
        int myRank = worldArenaRankData.getArenaRankByObjectIndex(playerController.getObjectIndex());
        arenaData.changeMaxRank(myRank);
        List<WorldArenaRankBean> rankList = getArenaRanksWithMe(myRank);
        for(WorldArenaRankBean rank : rankList){
            SGCommonProto.ArenaPlayerInfo.Builder builder = SGCommonProto.ArenaPlayerInfo.newBuilder();
            builder.setRank(worldArenaRankData.getArenaRankByObjectIndex(rank.getObjectIndex()));
            PlayerModel player = playerService.getPlayerByIndex(rank.getObjectIndex());
            if(player != null){
                builder.setName(player.getName());
                builder.setCamp(SGCommonProto.E_CAMP_TYPE.forNumber(player.getCamp()));
                builder.setObjectIndex(player.getObjectIndex());
                builder.setFightingCapacity(player.getFc());
                builder.setMasterId(player.getMaster());
            }else {
                logger.error("根据玩家的索引无法查到玩家， 索引为: " + rank.getObjectIndex());
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.SERVER_DATA_ERROR));
                return;
            }
            response.addPlayers(builder);
        }

        ChallengeInfo challengeInfo = playerController.getPlayer().getExtInfo(ChallengeInfo.class);
        response.setHasRewardGoods(challengeInfo.getArenaData().checkHasExchangeReward(myRank));
        response.setMaxRank(arenaData.getMaxRank());
        response.setCurrentServerTime(GameServer.currentTime);
        response.setHasRewardToday(TimeUtils.isToday(arenaData.getLastRewardTime()));
        response.setTodayChallengeTimes(arenaData.getTodayChallengeTimes());
        response.setTodayRemainTimes(arenaData.getRemainTodayChallengeTimes());

        List<ArenaRewardRecordDB> records = arenaData.getRewardRecords();
        for(ArenaRewardRecordDB record : records){
            SGCommonProto.RewardInfo.Builder info = SGCommonProto.RewardInfo.newBuilder();
            info.setId(record.getId());
            info.setNum(record.getNum());
            info.setType(SGCommonProto.E_GOODS_TYPE.forNumber(record.getType()));
            response.addRewardInfo(info);
        }

        List<Integer> ids = arenaData.getAlreadyRewardIds();
        for(Integer id : ids){
            response.addAlreadyRewardIds(id);
        }

        logger.debug("返回给客户端玩家的竞技场排名 ： " + response.toString());
        //返回给客户端
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Arena_GetArenaInfo_VALUE, response.build().toByteArray());

    }


    /**
     * 客户端发起挑战成功某位玩家
     * @param playerController
     * @param data
     */
    private void arenaChallenge(PlayerController playerController, byte[] data) {
        C2S_ArenaChallenge request = null;
        try {
            request = C2S_ArenaChallenge.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if(request == null){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }
        ArenaData arenaData = playerController.getPlayer().getArenaData();
        logger.debug("当前挑战的挑战次数剩余 :" + arenaData.getRemainTodayChallengeTimes());
        if(!arenaData.isChallengeTimesEnough()){
            //TODO 挑战次数已经用完
            logger.debug("挑战次数已经用完,当前挑战次数剩余 :" + arenaData.getRemainTodayChallengeTimes());
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ARENA_TIMES_NOT_ENOUGH));
            return;
        }

        int targetRank = request.getTargetRank();
        WorldArenaRankData worldArenaRankData = GameServer.getInstance().getWorldManager().getWorldArenaData();
        int myRank =  worldArenaRankData.getArenaRankByObjectIndex(playerController.getObjectIndex());
        logger.info("arenaChallenge， myRank: " + myRank + ", targetRank: " + targetRank);
        if(targetRank < myRank){
            WorldArenaRankBean targetPlayerRank = worldArenaRankData.getArenaRankList().get(targetRank - 1);
            PlayerModel playerModel = playerService.getPlayerByIndex(targetPlayerRank.getObjectIndex());
            Player target = new Player(GameServer.getInstance().getWorldManager().getWorldInfo(), playerModel);
            targetPlayerRank.setBattle(true);
            //TODO 自己测试
            if(isTestAccount(playerController)){
                arenaBattleEnd(playerController, playerModel.getObjectIndex(), true);
            }

            MsgUtils.startBattle(playerController, SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVP_ARENA_AI, target);
        }else{
            //TODO 目标位置比自己低
            logger.debug("目标位置比自己高");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ARENA_TARGET_LOWER_THAN_ME));
            return;
        }
    }

    /**
     * 战斗服务器结束竞技场挑战回调
     * @param playerController
     * @param objectIndex
     */
    public void arenaBattleEnd(PlayerController playerController, String objectIndex, boolean win){
        ArenaData arenaData = playerController.getPlayer().getArenaData();
        WorldArenaRankData worldArenaRankData = GameServer.getInstance().getWorldManager().getWorldArenaData();
        int targetRank = worldArenaRankData.getArenaRankByObjectIndex(objectIndex);
        int myRank = worldArenaRankData.getArenaRankByObjectIndex(playerController.getObjectIndex());
        WorldArenaRankBean targetPlayerRank = worldArenaRankData.getArenaRankList().get(targetRank - 1);
        targetPlayerRank.setBattle(false);
        logger.debug("竞技场挑战结果数------>>>> ： " + (win ? "胜利" : "失败"));
        //任务检测
        TaskInfo taskInfo = playerController.getPlayer().getExtInfo(TaskInfo.class);
        if(win){
            if (worldArenaRankData.swapArenaRank(myRank, targetRank)) {
                myRank = targetRank;
                arenaData.winBattle();
                int winCount =  arenaData.getTotalWinCount();
                int todayCount = arenaData.getTodayChallengeTimes();

                List<TaskDB> doingList = taskInfo.getDoingTask(ConstantFactory.TASK_TARAGET_ARENA);
                for(TaskDB taskDB : doingList) {
                    taskDB.check(ConstantFactory.TASK_TARAGET_ARENA, winCount);
                }


            } else {
                //TODO 交换位置失败
                logger.error("交换位置失败");
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.SERVER_DATA_ERROR));
                return;
            }
            //发放扫荡奖励
            StableData stableData = DataFactory.getInstance().getGameObject(DataFactory.STABLE_DATA_KEY, E_StableDataType.ARENA_PASS_REWARD.getCode());
            BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
            bagInfo.addGoods(stableData.goods, null);
        }



        List<TaskDB> doingList = taskInfo.getDoingTask(ConstantFactory.TASK_TARAGET_MAIN_ARENA_BATTLE);
        for(TaskDB taskDB : doingList) {
            taskDB.check(ConstantFactory.TASK_TARAGET_MAIN_ARENA_BATTLE, 1);
        }

        List<TaskDB> everyDayList = taskInfo.getDoingEveryTask(ConstantFactory.TASK_TARAGET_MAIN_ARENA_BATTLE);
        for(TaskDB taskDB : everyDayList) {
            if(taskDB.status == SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY_VALUE){
                taskDB.check(ConstantFactory.TASK_TARAGET_MAIN_ARENA_BATTLE, 1);
            }
        }


        S2C_ArenaChallenge.Builder response = S2C_ArenaChallenge.newBuilder();
        response.setWin(win);
        List<WorldArenaRankBean> rankList = getArenaRanksWithMe(myRank);
        for (WorldArenaRankBean rank : rankList) {
            SGCommonProto.ArenaPlayerInfo.Builder builder = SGCommonProto.ArenaPlayerInfo.newBuilder();
            builder.setRank(worldArenaRankData.getArenaRankByObjectIndex(rank.getObjectIndex()));
            PlayerModel player = playerService.getPlayerByIndex(rank.getObjectIndex());
            if (player != null) {
                builder.setName(player.getName());
                builder.setCamp(SGCommonProto.E_CAMP_TYPE.forNumber(player.getCamp()));
                builder.setObjectIndex(player.getObjectIndex());
                builder.setFightingCapacity(player.getFc());
                builder.setMasterId(player.getMaster());
            } else {
                logger.debug("根据玩家的索引无法查到玩家， 索引为: " + rank.getObjectIndex());
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.SERVER_DATA_ERROR));
                return;
            }
            response.addPlayers(builder);
        }
        arenaData.changeMaxRank(myRank);
        arenaData.addChallengeTimes(GameServer.currentTime);
        logger.debug("已经挑战次数剩余 ： " + arenaData.getRemainTodayChallengeTimes());
        logger.debug("返回给客户端挑战成功某位玩家 ： " + response.toString());
        //返回给客户端
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Arena_Challenge_VALUE, response.build().toByteArray());
    }


    /**
     * 领取当日奖励
     * @param playerController
     */
    private void getDailyReward(PlayerController playerController) {
        ArenaData arenaData = playerController.getPlayer().getArenaData();
        long lastRewardTime = arenaData.getLastRewardTime();
        if(TimeUtils.isToday(lastRewardTime)){
            logger.debug("今天已经领取过奖励");
            //TODO 今天已经领取过奖励
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ARENA_DAILY_HAS_REWARD));
            return;
        }

        //TODO 领取奖励
        List<ArenaDailyRewardConfig> rewardConfigs = DataFactory.getInstance().getDataList(DataFactory.ARENA_DAILY_REWARD_KEY);
        int myRank =  GameServer.getInstance().getWorldManager().getWorldArenaData().getArenaRankByObjectIndex(playerController.getObjectIndex());
        ArenaDailyRewardConfig reward = null;
        for(ArenaDailyRewardConfig r : rewardConfigs){
            if(myRank >= r.minRank && myRank <= r.maxRank){//在最大和最小排名之间
                reward = r;
                break;
            }
        }

        BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
        bagInfo.addGoods(reward.dailyReward, null);

        S2C_ArenaGetDailyReward.Builder response = S2C_ArenaGetDailyReward.newBuilder();
        response.setCurrentServerTime(GameServer.currentTime);
        arenaData.setLastRewardTime(GameServer.currentTime);
        logger.debug("返回给客户端竞技场每日领取奖励结果 ： " + response.toString());
        //返回给客户端
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Arena_GetDailyReward_VALUE, response.build().toByteArray());
    }


    /**
     * 根据自己的排名获取竞技场排名情况
     * @param myRank
     * @return
     */
    private List<WorldArenaRankBean> getArenaRanksWithMe(int myRank) {
        List<WorldArenaRankBean> rankList = new ArrayList<WorldArenaRankBean>();
        int paramY = 0;
        WorldArenaRankData worldArenaRankData = GameServer.getInstance().getWorldManager().getWorldArenaData();
        if(myRank < 6){//1-5名都加载前五名
            rankList.addAll(worldArenaRankData.getUnderTargetRankList(5));
        }else if(myRank < 15){//加载1-myRank的所有数据
            rankList.addAll(worldArenaRankData.getUnderTargetRankList(myRank));
            paramY = 1;
        }else if(myRank < 31){
            paramY = 1;
        }else if(myRank < 101){
            paramY = 3;
        }else if(myRank < 501){
            paramY = 20;
        }else if(myRank < 1001){
            paramY = 30;
        }else if(myRank < 3001){
            paramY = 120;
        }else if(myRank < 6001){
            paramY = 200;
        }else if(myRank < 100001){
            paramY = 300;
        }else {

        }

        if(rankList.size() == 0){
            //取出前10名
            rankList.addAll(worldArenaRankData.getUnderTargetRankList(10));
            rankList.add(getEnableChallengeTarget(myRank, paramY, 4));
            rankList.add(getEnableChallengeTarget(myRank, paramY, 3));
            rankList.add(getEnableChallengeTarget(myRank, paramY, 2));
            //添加自己
            rankList.add(GameServer.getInstance().getWorldManager().getWorldArenaData().getArenaRankInfo(myRank));
        }
        if(paramY != 0){
            WorldArenaRankBean temp = getEnableSweepTarget(myRank, paramY);
            if(temp != null && !rankList.contains(temp)){
                rankList.add(temp);
            }

        }
//        Collections.sort(rankList, new Comparator<ArenaRank>() {
//            @Override
//            public int compare(ArenaRank o1, ArenaRank o2) {
//                return GameServer.getInstance().getWorldManager().getArenaRankByObjectIndex(o1.getObjectIndex()) -
//                        GameServer.getInstance().getWorldManager().getArenaRankByObjectIndex(o2.getObjectIndex());
//            }
//        });
        return rankList;
    }

    /**
     * 获取适合的扫荡对象
     * @param myRank
     * @param paramY
     * @return
     */
    private WorldArenaRankBean getEnableSweepTarget(int myRank, int paramY) {
        int size = GameServer.getInstance().getWorldManager().getWorldArenaData().getArenaRankList().size();
        if(myRank == size){ // 我就是最后一位
            return null;
        }
        if(myRank + paramY > size){ // 随机值会大于总数据长度
            paramY = size - myRank;
        }
        Random random = new Random();
        int targetRank = myRank + 1 + random.nextInt(paramY);
        return GameServer.getInstance().getWorldManager().getWorldArenaData().getArenaRankInfo(targetRank);
    }

    /**
     * 获取合适的空闲挑战对象
     * @param myRank
     * @param paramY
     * @param rate
     * @return
     */
    private WorldArenaRankBean getEnableChallengeTarget(int myRank, int paramY, int rate) {
        Random random = new Random();
        int targetRank = myRank -1 - paramY * (rate - 1) - random.nextInt(paramY);
        return GameServer.getInstance().getWorldManager().getWorldArenaData().getArenaRankInfo(targetRank);
    }

    /**
     * 添加资源领取记录
     * @param arenaData
     */
    private void addRewardRecord(ArenaData arenaData, GoodsData goodsData) {
        ArenaRewardRecordDB record = new ArenaRewardRecordDB(goodsData.type, goodsData.id);
        int index = arenaData.getRewardRecords().indexOf(record);
        if(index > -1){
            arenaData.getRewardRecords().get(index).addNum(goodsData.value);
        }else {
            record.setNum(goodsData.value);
            arenaData.getRewardRecords().add(record);
        }
    }

    /*************************************竞技场end***********************************************************************************/


    /*************************************官阶战star***********************************************************************************/

    /**
     * 初始化页面信息
     * @param playerController
     * @param data
     * @param code
     */
    private void initInfo(PlayerController playerController, byte[] data, int code) {
        OfficialRankData rankData = playerController.getPlayer().getOfficialData();
//        if(CodeFactory.TEST){
//            rankData.reset();
//        }
        WorldOfficialRankBean rankBean = getOfficialRankInfo().getRankInfoByObjectIndex(playerController.getPlayer());
        int myRank = rankBean== null ? 15 : rankBean.getRankId();
        SGOfficialWarProto.S2C_InitInfo.Builder response = SGOfficialWarProto.S2C_InitInfo.newBuilder();
        response.setCurrentServerTime(GameServer.currentTime);
        response.setHasRewardToday(TimeUtils.isToday(rankData.getLastRewardTime()));
        response.setTodayChallengeTimes(rankData.getTodayChallengeTimes());
        response.setTodayRemainTimes(rankData.getTodayRemainChallengeTimes());
        response.setOfficialRankId(myRank);
        response.setHasIntegralRewardGoods(rankData.checkHasIntegralExchangeReward());
        response.setHasRewardGoods(rankData.checkHasExchangeReward(myRank));

        List<WorldOfficialRankBean> top5Player = getOfficialRankInfo().getTop5Player(playerController.getPlayer());
        if(top5Player == null){
            logger.debug("获取前五名玩家返回空，可能是阵营错误！！");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.SERVER_DATA_ERROR));
            return;
        }

        SGCommonProto.OfficialRankObject.Builder obj;
        for(WorldOfficialRankBean bean : top5Player){
            obj = SGCommonProto.OfficialRankObject.newBuilder();
            obj.setOfficialRankId(bean.getRankId());
            obj.setName(bean.getName() + "");
            if(!StringUtils.isNullOrEmpty(bean.getObjectIndex())){
                obj.setObjectIndex(bean.getObjectIndex());
                PlayerModel player = playerService.getPlayerByIndex(bean.getObjectIndex());
                if (player != null) {
                    obj.setName(player.getName());
                    obj.setAvatar(player.getAvatar());
                    obj.setCamp(player.getCamp());
                    obj.setMasterId(player.getMaster());
                    obj.setFightingCapacity(player.getFc());
                } else {
                    logger.debug("根据玩家的索引无法查到玩家， 索引为: " + bean.getObjectIndex());
                    playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.SERVER_DATA_ERROR));
                    return;
                }
            }
            response.addPlayers(obj);
        }


        List<ArenaRewardRecordDB> records = rankData.getRewardRecords();
        for(ArenaRewardRecordDB record : records){
            SGCommonProto.RewardInfo.Builder info = SGCommonProto.RewardInfo.newBuilder();
            info.setId(record.getId());
            info.setNum(record.getNum());
            info.setType(SGCommonProto.E_GOODS_TYPE.forNumber(record.getType()));
            response.addRewardInfo(info);
        }

        List<Integer> ids = rankData.getAlreadyRewardIds();
        for(Integer id : ids){
            response.addAlreadyRewardIds(id);
        }

        response.addAllAlreadyRewardIds(rankData.getIntegralRewardIds());

        logger.debug("返回给客户端数据：" + response.toString());
        //返回给客户端
        playerController.sendMsg(code, response.build().toByteArray());
    }



    /**
     * 查看官阶对应的人员
     * @param playerController
     * @param data
     * @param code
     */
    private void previewRank(PlayerController playerController, byte[] data, int code) {
        SGOfficialWarProto.C2S_PreviewRank request = null;
        try {
            request = SGOfficialWarProto.C2S_PreviewRank.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if(request == null){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }
        int targetRankId = request.getOfficialRankId();
        logger.debug("previewRank targetRankId：" + targetRankId);
        SGOfficialWarProto.S2C_PreviewRank.Builder response = SGOfficialWarProto.S2C_PreviewRank.newBuilder();
        List<WorldOfficialRankBean> players = getOfficialRankInfo().getTargetRankPlayer(playerController.getPlayer(), targetRankId);
        if(players == null){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }
        SGCommonProto.OfficialRankObject.Builder obj;
        for(WorldOfficialRankBean bean : players){
            obj = SGCommonProto.OfficialRankObject.newBuilder();
            obj.setOfficialRankId(bean.getRankId());
            obj.setPosition(bean.getPosition());
            if(!StringUtils.isNullOrEmpty(bean.getObjectIndex())){
                obj.setObjectIndex(bean.getObjectIndex());
                PlayerModel player = playerService.getPlayerByIndex(bean.getObjectIndex());
                if (player != null) {
                    obj.setName(player.getName());
                    obj.setAvatar(player.getAvatar());
                    obj.setCamp(player.getCamp());
                    obj.setMasterId(player.getMaster());
                    obj.setFightingCapacity(player.getFc());
                } else {
                    logger.error("根据玩家的索引无法查到玩家， 索引为: " + bean.getObjectIndex());
                    playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.SERVER_DATA_ERROR));
                    return;
                }
            }else {
                obj.setName(LoadUtils.getRandomPlayerName());
            }
            response.addPlayers(obj);
        }

        logger.debug("返回给客户端数据：" + response.toString());
        //返回给客户端
        playerController.sendMsg(code, response.build().toByteArray());
    }

    /**
     * 挑战对应官阶
     * @param playerController
     * @param data
     * @param code
     */
    private void challengeRank(PlayerController playerController, byte[] data, int code) {
        SGOfficialWarProto.C2S_ChallengeRank request = null;
        try {
            request = SGOfficialWarProto.C2S_ChallengeRank.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if(request == null){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        int targetRankId = request.getOfficialRankId();
        OfficialRankConfig officialRankConfig = DataFactory.getInstance().getGameObject(DataFactory.OFFICIAL_RANK_KEY, playerController.getPlayer().camp * 100 + targetRankId);

        if(officialRankConfig == null)
        {
            logger.debug("参数错误");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }
        int position = request.getPosition();
        if(position < 0)
        {
            logger.debug("参数错误");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        OfficialRankData officialRankData = playerController.getPlayer().getOfficialData();
        if(!officialRankData.isChallengeTimesEnough()){
            //TODO 挑战次数已经用完
            logger.info("挑战次数已经用完,当前挑战次数剩余 :" + officialRankData.getTodayRemainChallengeTimes());
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ARENA_TIMES_NOT_ENOUGH));
            return;
        }

        logger.debug("挑战的官阶id：" + targetRankId + ", 位置： " + position);
        WorldOfficialRankBean targetPositionPlayer = getOfficialRankInfo().getTargetPositionPlayer(playerController.getPlayer(), targetRankId, position);
        if(targetPositionPlayer == null){
            logger.debug("挑战的目标找不到");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }
        //自己所在的官阶排行
        WorldOfficialRankBean myRankBean = getOfficialRankInfo().getRankInfoByObjectIndex(playerController.getPlayer());
        int myRankId = myRankBean == null? 0 : myRankBean.getRankId();
        if(myRankId == 0){ // 第一次挑战
            if(targetRankId != 14){// 首次挑战异常
                logger.debug("不能越阶挑战 myRank: " +  myRankId);
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.OFFICIAL_NOT_JUMP_RANK_CHALLENGE));
                return;
            }
            //TODO 自己测试
            if(isTestAccount(playerController)){
                challengeResult(playerController, playerController.getPlayer().camp * 100 + targetRankId, position, true);
            }
        }else {
            if(targetRankId != myRankId - 1){
                logger.debug("不能越阶挑战 myRank: " +  myRankId);
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.OFFICIAL_NOT_JUMP_RANK_CHALLENGE));
                return;
            }

            //TODO 自己测试
            if(isTestAccount(playerController)){
                challengeResult(playerController, playerController.getPlayer().camp * 100 + targetRankId, position, true);
            }
        }

        String param = playerController.getPlayer().camp * 100 + targetRankId + ConstantFactory.SEMICOLON + position + ConstantFactory.SEMICOLON;
        if(targetPositionPlayer.isPlayer())
        {
            MsgUtils.startBattle(playerController,
                    SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVP_OFFICIAL_RANK, param + targetPositionPlayer.getObjectIndex());
        }
        else
        {
            MsgUtils.startBattle(playerController,
                    SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVE_OFFICIAL_RANK, param + position);
        }
    }

    /**
     * 挑战结果
     * @param playerController
     * @param targetRankId
     * @param targetPosition
     * @param win
     */
    public void challengeResult(PlayerController playerController, int targetRankId, int targetPosition,boolean win) {
        targetRankId = targetRankId % 100;
        //自己所在的官阶排行
        WorldOfficialRankBean myRankBean = getOfficialRankInfo().getRankInfoByObjectIndex(playerController.getPlayer());
        int myRankId =  myRankBean == null? 0 : myRankBean.getRankId();
        int myPosition = myRankBean == null ? -1 : myRankBean.getPosition();
        OfficialRankData officialRank = playerController.getPlayer().getOfficialData();
        //增加挑战次数
        officialRank.addChallengeTimes(GameServer.currentTime);
        logger.debug("当前挑战次数剩余 : " + officialRank.getTodayRemainChallengeTimes());
        logger.debug("官阶挑战结果 ------->>>>: " +  (win?"胜利":"失败"));
        if(win){
            WorldOfficialRankBean targetPlayer = getOfficialRankInfo().getTargetPositionPlayer(playerController.getPlayer(), targetRankId, targetPosition);
            if(targetPlayer == null){
                logger.debug("挑战的目标找不到");
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
                return;
            }

            if(myRankId == 0){//第一次，直接赋值
                targetPlayer.setObjectIndex(playerController.getObjectIndex());
                firstAttachThisRank(playerController, targetRankId);
            }else {
                //交换位置
                WorldOfficialRankBean myTargetBean = getOfficialRankInfo().getTargetPositionPlayer(playerController.getPlayer(), myRankId, myPosition);
                myTargetBean.setObjectIndex(targetPlayer.getObjectIndex());
                myTargetBean.setName(targetPlayer.getName());
                targetPlayer.setObjectIndex(playerController.getObjectIndex());
                int myRank = officialRank.getMaxOfficialRankId();
                if(targetRankId < myRank){
                    firstAttachThisRank(playerController, targetRankId);
                }
            }

        }

        //检测副本通关任务
        TaskInfo taskInfo = playerController.getPlayer().getExtInfo(TaskInfo.class);
        List<TaskDB> doingList = taskInfo.getDoingTask(ConstantFactory.TASK_TARAGET_MAIN_OFFICIALWAR);
        for(TaskDB taskDB : doingList) {
            taskDB.check(ConstantFactory.TASK_TARAGET_MAIN_OFFICIALWAR, 1);
        }

        //检测副本通关任务
        List<TaskDB> everyList = taskInfo.getDoingEveryTask(ConstantFactory.TASK_TARAGET_MAIN_OFFICIALWAR);
        for(TaskDB taskDB : everyList) {
            if(taskDB.status == SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY_VALUE){
                taskDB.check(ConstantFactory.TASK_TARAGET_MAIN_OFFICIALWAR, 1);
            }
        }

        //发放通过奖励
        BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
        OfficialRankConfig rankConfig = DataFactory.getInstance().getGameObject(DataFactory.OFFICIAL_RANK_KEY, playerController.getPlayer().camp * 100 + targetRankId);
        bagInfo.addGoods(rankConfig.passReward, null);
        logger.debug("官阶战战斗结束：------>>>>" + (win ? " 胜利" : "失败"));
    }

    /**
     * 首次达到对应的官阶奖励
     * @param playerController
     * @param targetRankId
     */
    private void firstAttachThisRank(PlayerController playerController, int targetRankId)
    {
        OfficialRankConfig configObj = DataFactory.getInstance().getGameObject(DataFactory.OFFICIAL_RANK_KEY, playerController.getPlayer().camp * 100 + targetRankId);
        BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
        bagInfo.addGoods(configObj.firstReward,null);
        playerController.getPlayer().getOfficialData().changeMaxOfficialRankId(targetRankId);
    }


    /**
     * 扫荡对应官阶
     * @param playerController
     * @param data
     * @param code
     */
    private void sweepRank(PlayerController playerController, byte[] data, int code) {
        SGOfficialWarProto.C2S_SweepRank request = null;
        try {
            request = SGOfficialWarProto.C2S_SweepRank.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if(request == null){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        int targetRankId = request.getOfficialRankId();
        int position = request.getPosition();

        OfficialRankData officialRank = playerController.getPlayer().getOfficialData();
        if(!officialRank.isChallengeTimesEnough()){
            //TODO 挑战次数已经用完
            logger.info("挑战次数已经用完,当前挑战次数剩余 :" + officialRank.getTodayRemainChallengeTimes());
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.OFFICIAL_TIMES_NOT_ENOUGH));
            return;
        }

        logger.debug("扫荡的官阶id：" + targetRankId + ", 位置： " + position);
        WorldOfficialRankBean targetPositionPlayer = getOfficialRankInfo().getTargetPositionPlayer(playerController.getPlayer(), targetRankId, position);
        if(targetPositionPlayer == null){
            logger.debug("扫荡的目标找不到");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }
        //自己所在的官阶排行
        WorldOfficialRankBean myRankBean = getOfficialRankInfo().getRankInfoByObjectIndex(playerController.getPlayer());
        int myRankId = myRankBean == null? 0 : myRankBean.getRankId();
        if(myRankId > targetRankId){
            logger.debug("不能扫荡比自己排名高度 myRank: " +  myRankId);
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.OFFICIAL_NOT_SWEEP_HEIGHT_PLAYER));
            return;
        }
        officialRank.addChallengeTimes(GameServer.currentTime);
        logger.debug("当前挑战次数剩余 : " +  officialRank.getTodayRemainChallengeTimes());
        //发放通过奖励
        BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
        OfficialRankConfig rankConfig = DataFactory.getInstance().getGameObject(DataFactory.OFFICIAL_RANK_KEY, playerController.getPlayer().camp * 100 + targetRankId);
        bagInfo.addGoods(rankConfig.passReward, null);

        SGOfficialWarProto.S2C_SweepRank.Builder response = SGOfficialWarProto.S2C_SweepRank.newBuilder();
        response.setOfficialRankId(myRankId);
        logger.debug("返回给客户端数据：" + response.toString());
        //返回给客户端
        playerController.sendMsg(code, response.build().toByteArray());
    }

    /**
     * 每日领取奖励
     * @param playerController
     * @param data
     * @param code
     */
    private void getDailyReward(PlayerController playerController, byte[] data, int code)
    {
        OfficialRankData officialRank = playerController.getPlayer().getOfficialData();
        long lastRewardTime = officialRank.getLastRewardTime();
        if(TimeUtils.isToday(lastRewardTime)){
            logger.info("今天已经领取过奖励");
            //TODO 今天已经领取过奖励
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.OFFICIAL_DAILY_HAS_REWARD));
            return;
        }

        //自己所在的官阶排行
        WorldOfficialRankBean myRankBean = getOfficialRankInfo().getRankInfoByObjectIndex(playerController.getPlayer());
        int rankId = myRankBean == null ? 15 : myRankBean.getRankId();
        officialRank.setLastRewardTime(GameServer.currentTime);
        //TODO 领取奖励
        OfficialRankConfig rankConfig = DataFactory.getInstance().getGameObject(DataFactory.OFFICIAL_RANK_KEY, playerController.getPlayer().camp * 100 + rankId);
        BagInfo bagInfo =  playerController.getPlayer().getExtInfo(BagInfo.class);
        bagInfo.addGoods(rankConfig.dailyReward,null);
        SGAreanProto.S2C_ArenaGetDailyReward.Builder response = SGAreanProto.S2C_ArenaGetDailyReward.newBuilder();
        response.setCurrentServerTime(GameServer.currentTime);
        logger.info("返回给客户端官阶战每日领取奖励结果 ： " + response.toString());
        //返回给客户端
        playerController.sendMsg(code, response.build().toByteArray());
    }

    /**
     * 兑换商店奖励
     * @param playerController
     * @param data
     * @param code
     */
    private void exchangeReward(PlayerController playerController, byte[] data, int code) {
        SGOfficialWarProto.C2S_ExchangeReward request = null;
        try {
            request = SGOfficialWarProto.C2S_ExchangeReward.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if(request == null){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        OfficialRankData officialRank = playerController.getPlayer().getOfficialData();
        int rewardId = request.getRewardId();
        if(officialRank.getAlreadyRewardIds().contains(rewardId)){
            logger.info("官阶战该资源已经兑换过了");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.OFFICIAL_REWARD_GROUP_ALREADY_REWARD));
            return;
        }
        ExchangeRewardConfig rewardConfig = DataFactory.getInstance().getGameObject(DataFactory.REWARD_KEY, rewardId);
        if(rewardConfig == null ||rewardConfig.type != E_ExchangeRewardType.OFFICIAL_RANK.getCode()){
            logger.info("官阶战 rewardId 不存在：" + rewardId);
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }
        //自己所在的官阶排行
        WorldOfficialRankBean myRankBean = getOfficialRankInfo().getRankInfoByObjectIndex(playerController.getPlayer());
        int myRankId = myRankBean == null? 100 : myRankBean.getRankId();
        if(myRankId > rewardConfig.minRank){ // 自己的排名大于要求的最低排名，不符合领取条件
            //TODO error
            logger.info("官阶战自己的排名大于要求的最低排名，不符合领取条件 ");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.OFFICIAL_RANK_NOT_ENOUGH));
            return;
        }
        BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
        //循环配置表检测自己的资源是否足够
        for(CommonData assets : rewardConfig.needAssets){
            if(bagInfo.getAsset(assets.id) < assets.value){//自己拥有的小于配置的
                //TODO 资源不足
                logger.error("官阶战兑换奖励资源不足 ");
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ASSET_NOT_ENOUGH, assets.id));
                return;
            }
        }
        officialRank.getAlreadyRewardIds().add(rewardId);
        GoodsData[] consumeData = new GoodsData[rewardConfig.needAssets.length];
        for(int i = 0; i <rewardConfig.needAssets.length; i++ ){
            consumeData[i] = new GoodsData(SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_ASSETS_VALUE, rewardConfig.needAssets[i].id, rewardConfig.needAssets[i].value);
        }
        bagInfo.addGoods(rewardConfig.reward, consumeData);
//        bagInfo.reduceAsset(rewardConfig.needAssets);

        for(GoodsData goodsData : rewardConfig.reward){
            addRewardRecord(officialRank, goodsData);
        }
        SGOfficialWarProto.S2C_ExchangeReward.Builder response = SGOfficialWarProto.S2C_ExchangeReward.newBuilder();
        response.setRewardId(rewardId);
        logger.info("返回给客户端官阶战兑换结果 ： " + response.toString());
        //返回给客户端
        playerController.sendMsg(code, response.build().toByteArray());
    }

    /**
     * 兑换记录
     * @param playerController
     * @param data
     * @param code
     */
    private void rewardRecord(PlayerController playerController, byte[] data, int code)
    {
        SGOfficialWarProto.S2C_RewardRecord.Builder response = SGOfficialWarProto.S2C_RewardRecord.newBuilder();

        OfficialRankData officialRank = playerController.getPlayer().getOfficialData();
        List<ArenaRewardRecordDB> records = officialRank.getRewardRecords();
        for(ArenaRewardRecordDB record : records){
            SGCommonProto.RewardInfo.Builder info = SGCommonProto.RewardInfo.newBuilder();
            info.setId(record.getId());
            info.setNum(record.getNum());
            info.setType(SGCommonProto.E_GOODS_TYPE.forNumber(record.getType()));
            response.addRewardInfo(info);
        }

        List<Integer> ids = officialRank.getAlreadyRewardIds();
        for(Integer id : ids){
            response.addAlreadyRewardIds(id);
        }

        logger.info("返回给客户端官阶战兑换记录 ： " + response.toString());
        //返回给客户端
        playerController.sendMsg(code, response.build().toByteArray());
    }

    /**
     * 积分兑换
     * @param playerController
     * @param data
     * @param code
     */
    private void integralReward(PlayerController playerController, byte[] data, int code) {
        SGOfficialWarProto.C2S_IntegralReward request = null;
        try {
            request = SGOfficialWarProto.C2S_IntegralReward.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if(request == null){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }
        final int rewardId = request.getRewardId();
        OfficialIntegralConfig integralConfig = DataFactory.getInstance().getGameObject(DataFactory.OFFICIAL_INTEGRAL_REWARD_KEY, rewardId);

        if(integralConfig == null){
            logger.info("官阶战积分奖励 rewardId 不存在：" + rewardId);
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        OfficialRankData officialRank =  playerController.getPlayer().getOfficialData();
        if(officialRank.getIntegralRewardIds().contains(rewardId)){
            logger.info("官阶战积分奖励已经领取过：" + rewardId);
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.OFFICIAL_INTEGRAL_REWARD_GROUP_ALREADY_REWARD));
            return;
        }

        BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
        int integral = bagInfo.getAsset(ConfigFactory.ASSET_INTEGRAL_KEY);
        if(integral < integralConfig.integral){//小于要求的积分
            logger.info("官阶战积分不够：当前积分：" + officialRank.getTodayChallengeTimes() + ", 需要积分: " + integralConfig.integral);
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.OFFICIAL_INTAGRAL_NOT_ENOUGH));
            return;
        }
        officialRank.getIntegralRewardIds().add(rewardId);

        bagInfo.addGoods(integralConfig.reward, null);
        bagInfo.addAsset(ConfigFactory.ASSET_INTEGRAL_KEY, -integralConfig.integral);

        SGOfficialWarProto.S2C_IntegralReward.Builder response = SGOfficialWarProto.S2C_IntegralReward.newBuilder();
        response.setRewardId(rewardId);

        logger.info("返回给客户端官阶战积分兑换结果 ： " + response.toString());
        //返回给客户端
        playerController.sendMsg(code, response.build().toByteArray());

    }


    /**
     * 积分兑换记录
     * @param playerController
     * @param data
     * @param code
     */
    private void integralRewardRecord(PlayerController playerController, byte[] data, int code) {
        SGOfficialWarProto.S2C_IntegralRewardRecord.Builder response = SGOfficialWarProto.S2C_IntegralRewardRecord.newBuilder();

        ChallengeInfo challengeInfo = (ChallengeInfo) playerController.getPlayer().getExtInfo(ChallengeInfo.class);
        OfficialRankData officialRank = challengeInfo.getOfficialData();
        response.addAllAlreadyRewardIds(officialRank.getIntegralRewardIds());
        logger.info("返回给客户端官阶战积分兑换记录 ： " + response.toString());
        //返回给客户端
        playerController.sendMsg(code, response.build().toByteArray());
    }


    /**
     * 获取世界官阶排名信息
     * @return
     */
    private WorldOfficialRankData getOfficialRankInfo() {
        return GameServer.getInstance().getWorldManager().getWorldInfo().getOfficialRankInfo();
    }

    /**
     * 添加资源领取记录
     * @param data
     */
    private void addRewardRecord(OfficialRankData data, GoodsData goods) {
        ArenaRewardRecordDB record = new ArenaRewardRecordDB(goods.type, goods.id);
        int index = data.getRewardRecords().indexOf(record);
        if(index > -1){
            data.getRewardRecords().get(index).addNum(goods.value);
        }else {
            record.setNum(goods.value);
            data.getRewardRecords().add(record);
        }
    }

    /*************************************官阶战end***********************************************************************************/
    /*************************************英雄圣殿start***********************************************************************************/

    /**
     * 英雄圣殿初始化
     * @param playerController
     * @param data
     * @param code
     */
    private void heroTempleInitInfo(PlayerController playerController, byte[] data, int code) {
        HeroTempleData heroTempleData = playerController.getPlayer().getHeroTempleData();
        SGPlayerProto.S2C_HeroTempleInitInfo.Builder response = SGPlayerProto.S2C_HeroTempleInitInfo.newBuilder();
        response.addAllHasPassLevelIds(heroTempleData.getHasPassLevel());
        response.setTodayRemainTimes(heroTempleData.getTodayRemainChallengeTimes());
        response.setTodayChallengeTimes(heroTempleData.getTodayChallengeTimes());
        logger.debug("英雄圣殿初始化:" + response.toString());
        playerController.sendMsg(code, response.build().toByteArray());
    }


    /**
     * 英雄圣殿挑战
     * @param playerController
     * @param data
     * @param code
     */
    private void heroTempleChallenge(PlayerController playerController, byte[] data, int code) {
        SGPlayerProto.C2S_HeroTempleChallenge request = null;
        try {
            request =  SGPlayerProto.C2S_HeroTempleChallenge.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if(request == null){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }
        int targetLevelId = request.getTargetLevelId();
        logger.debug("英雄圣殿挑战参数(目标关卡id): " + targetLevelId);

        HeroTempleData heroTempleData = playerController.getPlayer().getHeroTempleData();
        logger.debug("英雄圣殿已经通过的关卡：" + heroTempleData.getHasPassLevel());
        logger.debug("英雄圣殿剩余挑战次数：" + heroTempleData.getTodayRemainChallengeTimes());
        if(!heroTempleData.isChallengeTimesEnough()){
            logger.debug("英雄圣殿挑战次数不足");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.HERO_TEMPLE_CHALLENGE_TIMES_NOT_ENOUGH));
            return;
        }

        HeroTempleConfig heroTempleConfig = DataFactory.getInstance().getGameObject(DataFactory.HERO_TEMPLE_KEY, targetLevelId);
        if(heroTempleConfig == null){
            logger.debug("英雄圣殿目标关卡找不到：" + targetLevelId);
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }
        int maxLevelId = heroTempleData.getMaxLevelId();
        if(maxLevelId > 0){
            int nextId = ((HeroTempleConfig)DataFactory.getInstance().getGameObject(DataFactory.HERO_TEMPLE_KEY, maxLevelId)).unlockNext;
            if(targetLevelId != nextId){
                logger.debug("不能越级挑战当前最大阶数：" + heroTempleData.getMaxLevelId());
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
                return;
            }
        }
        //TODO 自己测试
        if(isTestAccount(playerController)){
            heroTempleChallengeResult(playerController, targetLevelId, true);
        }

        String param = targetLevelId + "";
        MsgUtils.startBattle(playerController,
                    SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVE_HERO_TEMPLE, param);

    }

    /**
     * 英雄圣殿战斗返回
     * @param playerController
     * @param targetLevelId
     * @param win
     */
    public void heroTempleChallengeResult(PlayerController playerController, int targetLevelId, boolean win) {
        logger.debug("英雄圣殿战斗结果--------->>>>" + (win? "胜利" : "失败"));
        if(win){
            HeroTempleData heroTempleData = playerController.getPlayer().getHeroTempleData();
            heroTempleData.addPassLevel(targetLevelId);
            heroTempleData.addChallengeTimes(GameServer.currentTime);
            //发放奖励
            HeroTempleConfig heroTempleConfig = DataFactory.getInstance().getGameObject(DataFactory.HERO_TEMPLE_KEY, targetLevelId);
            BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
            bagInfo.addGoods(heroTempleConfig.firstPassReward, null);
            DropGroupConfig dropGroupConfig = DataFactory.getInstance().getGameObject(DataFactory.DROP_GROUP_KEY, heroTempleConfig.sweepReward);
            dropGroupConfig.reward(playerController);
        }
    }

    /**
     * 英雄圣殿扫荡
     * @param playerController
     * @param data
     * @param code
     */
    private void heroTempleSweep(PlayerController playerController, byte[] data, int code) {
        SGPlayerProto.C2S_HeroTempleSweep request = null;
        try {
            request =  SGPlayerProto.C2S_HeroTempleSweep.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if(request == null){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }
        int targetLevelId = request.getTargetLevelId();
        logger.debug("英雄圣殿扫荡参数(目标关卡id): " + targetLevelId);

        HeroTempleData heroTempleData = playerController.getPlayer().getHeroTempleData();
        logger.debug("英雄圣殿已经通过的关卡：" + heroTempleData.getHasPassLevel());
        logger.debug("英雄圣殿剩余挑战次数：" + heroTempleData.getTodayRemainChallengeTimes());
        if(!heroTempleData.isChallengeTimesEnough()){
            logger.debug("英雄圣殿挑战次数不足");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.HERO_TEMPLE_CHALLENGE_TIMES_NOT_ENOUGH));
            return;
        }

        HeroTempleConfig heroTempleConfig = DataFactory.getInstance().getGameObject(DataFactory.HERO_TEMPLE_KEY, targetLevelId);
        if(heroTempleConfig == null){
            logger.debug("英雄圣殿扫荡目标关卡找不到：" + targetLevelId);
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        if(!heroTempleData.getHasPassLevel().contains(targetLevelId)){
            logger.debug("英雄圣殿还没通过的关卡不能扫荡：" + targetLevelId);
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        //发放过关奖励
        DropGroupConfig dropGroupConfig = DataFactory.getInstance().getGameObject(DataFactory.DROP_GROUP_KEY, heroTempleConfig.sweepReward);
        dropGroupConfig.reward(playerController);

        heroTempleData.addChallengeTimes(GameServer.currentTime);
        playerController.sendMsg(code, SGPlayerProto.S2C_HeroTempleSweep.newBuilder().build().toByteArray());

    }
    /*************************************英雄圣殿end***********************************************************************************/
    private boolean isTestAccount(PlayerController playerController){
        return CodeFactory.TEST && playerController.getPlayer().getUid() == 147;
    }
}
