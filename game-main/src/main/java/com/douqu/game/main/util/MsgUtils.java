package com.douqu.game.main.util;


import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.controller.PlayerController;
import com.douqu.game.core.e.E_ServerType;
import com.douqu.game.core.e.ReturnMessage;
import com.douqu.game.core.config.challenge.LevelConfig;
import com.douqu.game.core.entity.battle.PlayerBattleData;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.entity.db.MailDB;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.factory.WordFactory;
import com.douqu.game.core.protobuf.*;
import com.douqu.game.core.web.response.BaseResponseDto;
import com.douqu.game.main.GameServer;
import com.douqu.game.main.server.ServerInfo;
import com.douqu.game.main.server.ServerManager;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by bean on 2016/12/1.
 */
public class MsgUtils {

    static Logger logger = Logger.getLogger(MsgUtils.class);

    public static BaseResponseDto createMsg(){

        return new BaseResponseDto(ReturnMessage.SUCCESS);
    }

    public static <T> BaseResponseDto createMsg(T data){

        return new BaseResponseDto(ReturnMessage.SUCCESS, data);
    }

    public static BaseResponseDto createMsg(ReturnMessage error){

        return new BaseResponseDto(error);
    }




    /**

     * @param playerController
     * @param host
     * @param port
     * @param player
     * @param battleId
     */


    public static void sendArenaBattle(PlayerController playerController, String host, int port, Player player, String battleId)
    {
        SGPlayerProto.S2C_ArenaMatch.Builder response = SGPlayerProto.S2C_ArenaMatch.newBuilder();

        SGCommonProto.ServerInfo.Builder serverInfoData = SGCommonProto.ServerInfo.newBuilder();
        serverInfoData.setHost(host);
        serverInfoData.setPort(port);

        SGCommonProto.PlayerMatchInfo.Builder playerInfo = SGCommonProto.PlayerMatchInfo.newBuilder();
        SGCommonProto.PlayerBaseInfo.Builder baseInfo = SGCommonProto.PlayerBaseInfo.newBuilder();
        baseInfo.setPlayerIndex(player.getObjectIndex());
        baseInfo.setNickName(player.getName());
        baseInfo.setAvatar(player.avatar);
        baseInfo.setLv(player.getLv());
        playerInfo.setGrade(0);
        playerInfo.setStar(0);
        playerInfo.setBaseInfo(baseInfo);

        response.setBattleId(battleId);
        response.setServerInfo(serverInfoData);
        response.setTargetInfo(playerInfo);

        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Player_ArenaMatch_VALUE, response.build().toByteArray());
    }


    public static void sendInstanceBattle(PlayerController playerController, int mapId, int levelId, String host, int port, String battleId)
    {
        SGInstanceProto.S2C_RequestLevelBattle.Builder response = SGInstanceProto.S2C_RequestLevelBattle.newBuilder();
        response.setChapterId(mapId);
        response.setLevelId(levelId);

        SGCommonProto.ServerInfo.Builder serverInfoData = SGCommonProto.ServerInfo.newBuilder();
        serverInfoData.setHost(host);
        serverInfoData.setPort(port);

        response.setServerInfo(serverInfoData);
        response.setBattleId(battleId);

        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Instance_RequestLevelBattle_VALUE, response.build().toByteArray());
    }

    /**
     * 官阶战战斗
     * @param playerController
     * @param host
     * @param port
     * @param battleId
     */


    public static void sendOfficialMatch(PlayerController playerController, String host, int port,String battleId)
    {
        SGOfficialWarProto.S2C_ChallengeRank.Builder response = SGOfficialWarProto.S2C_ChallengeRank.newBuilder();

        SGCommonProto.ServerInfo.Builder serverInfoData = SGCommonProto.ServerInfo.newBuilder();
        serverInfoData.setHost(host);
        serverInfoData.setPort(port);

        response.setBattleId(battleId);
        response.setServerInfo(serverInfoData);

        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_OfficialWar_ChallengeRank_VALUE, response.build().toByteArray());
    }


    /**
     * 英雄圣殿战斗
     * @param playerController
     * @param host
     * @param port
     * @param battleId
     */


    public static void sendHeroTemple(PlayerController playerController, String host, int port,String battleId)
    {

        SGPlayerProto.S2C_HeroTempleChallenge.Builder response = SGPlayerProto.S2C_HeroTempleChallenge.newBuilder();

//        SGOfficialWarProto.S2C_ChallengeRank.Builder response = SGOfficialWarProto.S2C_ChallengeRank.newBuilder();

        SGCommonProto.ServerInfo.Builder serverInfoData = SGCommonProto.ServerInfo.newBuilder();
        serverInfoData.setHost(host);
        serverInfoData.setPort(port);

        response.setBattleId(battleId);
        response.setServerInfo(serverInfoData);

        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_HeroTemple_Challenge_VALUE, response.build().toByteArray());
    }

    public static void startBattle(PlayerController playerController, SGCommonProto.E_BATTLE_TYPE battleType, Object target)
    {
        if(target == null)
        {
            logger.info("传入数据异常");
            return;
        }

        ServerInfo serverInfo = ServerManager.getInstance().getFreeServer(E_ServerType.BATTLE);
        if(serverInfo == null)
        {
            logger.info("战斗服务器没有开启!");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, DataFactory.getInstance().getWord(WordFactory.BATTLE_SERVER_ERROR));
        }
        else
        {
            String battleId = GameServer.getInstance().createBattleId();

            logger.info("通知战斗服务器创建战斗 -> battleId:" + battleId + " type:" + battleType + " target:" + target);
            PlayerBattleData battleData = new PlayerBattleData(battleId, battleType, serverInfo.getIp(), serverInfo.getPort());
            playerController.setBattleData(battleData);

            if(battleType == SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVP_ARENA)
            {
                if(target instanceof PlayerController)
                {
                    PlayerController targetController = (PlayerController) target;

                    serverInfo.sendBattleInit(battleId, battleType.getNumber(), playerController.getObjectIndex(), targetController.getObjectIndex());

                    targetController.setBattleData(battleData);

                    MsgUtils.sendArenaBattle(playerController, serverInfo.getIp(), serverInfo.getPort(), targetController.getPlayer(), battleId);
                    MsgUtils.sendArenaBattle(targetController, serverInfo.getIp(), serverInfo.getPort(), playerController.getPlayer(), battleId);
                }
            }
            else if(battleType == SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVP_ARENA_AI)
            {
                if(target instanceof Player)
                {
                    Player player = (Player) target;
                    serverInfo.sendBattleInit(battleId, battleType.getNumber(), playerController.getObjectIndex(), player.getObjectIndex());

                    MsgUtils.sendArenaBattle(playerController, serverInfo.getIp(), serverInfo.getPort(), player, battleId);
                }
            }
            else if(battleType == SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVE_INSTANCE)
            {
                if(target instanceof LevelConfig)
                {
                    LevelConfig levelConfig = (LevelConfig) target;

                    serverInfo.sendBattleInit(battleId, battleType.getNumber(), playerController.getObjectIndex(), levelConfig.getId()+"");

                    MsgUtils.sendInstanceBattle(playerController, levelConfig.getInstanceId(), levelConfig.getId(), serverInfo.getIp(), serverInfo.getPort(), battleId);
                }
            }
            else if(battleType == SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVE_OFFICIAL_RANK
                    || battleType == SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVP_OFFICIAL_RANK)
            {

                serverInfo.sendBattleInit(battleId, battleType.getNumber(), playerController.getObjectIndex(), target.toString());

                MsgUtils.sendOfficialMatch(playerController, serverInfo.getIp(), serverInfo.getPort(), battleId);

            }else if(battleType == SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVE_HERO_TEMPLE){

                serverInfo.sendBattleInit(battleId, battleType.getNumber(), playerController.getObjectIndex(), target.toString());

                MsgUtils.sendHeroTemple(playerController, serverInfo.getIp(), serverInfo.getPort(), battleId);
            }
        }
    }



//    /**
//     * 推送一些重要数据(比如积分,金币)的更新
//     * @param playerController
//     * @param data
//     */
//    public static void sendFlushData(PlayerController playerController, List<SGCommonProto.FlushData> data){
//        SGPlayerProto.S2C_FlushData.Builder response = SGPlayerProto.S2C_FlushData.newBuilder();
//        response.addAllData(data);
//        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Player_FlushData_VALUE, response.build().toByteArray());
//    }


    /***
     * 发送邮件
     * @param title
     * @param content
     * @param auxiliaryList
     */
    public static void sendMail (PlayerController playerController,String targetIndex,String title,String content,List<GoodsData> auxiliaryList){



    }

}
