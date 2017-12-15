package com.douqu.game.main.server.rmi;

import com.douqu.game.core.controller.PlayerController;
import com.douqu.game.core.database.model.PlayerModel;
import com.douqu.game.core.e.E_PlayerStatus;
import com.douqu.game.core.entity.battle.BattleDetail;
import com.douqu.game.core.config.challenge.LevelConfig;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.entity.battle.BattleTemp;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.i.IMainServer;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.protobuf.SGInstanceProto;
import com.douqu.game.core.protobuf.SGMainProto;
import com.douqu.game.main.GameServer;
import com.douqu.game.main.database.mapper.PlayerMapper;
import com.douqu.game.main.msg.ChallengeMsgChannel;
import com.douqu.game.main.server.*;
import org.apache.log4j.Logger;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by bean on 2017/7/19.
 */
public class MainServerImpl extends UnicastRemoteObject implements IMainServer {

    Logger logger = Logger.getLogger(this.getClass());

    /**
     * 因为UnicastRemoteObject的构造方法抛出了RemoteException异常，因此这里默认的构造方法必须写，必须声明抛出RemoteException异常
     *
     * @throws RemoteException
     */
    public MainServerImpl() throws RemoteException {
    }

    @Override
    public void battleStart(int serverId, BattleDetail battleInfo) throws RemoteException {
        ServerInfo serverInfo = ServerManager.getInstance().getServerInfo(serverId);
        if (serverInfo != null) {
            serverInfo.addBattle(battleInfo);

            WorldManager worldManager = GameServer.getInstance().getWorldManager();
            PlayerController playerController = worldManager.getPlayerController(battleInfo.getTeam1Info().getIndexInfo());
            if (playerController != null) {
                logger.info("战斗服务器通知主服务器战斗开始 ->" + battleInfo.showInfo());
                playerController.setStatus(E_PlayerStatus.BATTLING);
                playerController.serverDestroy();

                playerController.setLastUpdateTime(GameServer.currentTime);
            }

            //目前还没有真实玩家的PVP,竞技场,官阶战都是获取的离线数据,所以这里不处理队伍2玩家的数据
            if(battleInfo.getBattleType() == SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVP_ARENA_VALUE)
            {
                PlayerController targetController = worldManager.getPlayerController(battleInfo.getTeam2Info().getIndexInfo());
                if (targetController != null) {
                    logger.info("战斗服务器通知主服务器战斗开始 ->" + battleInfo.showInfo());
                    targetController.setStatus(E_PlayerStatus.BATTLING);
                    targetController.serverDestroy();
                }
            }
        }
    }

    @Override
    public void battleEnd(int serverId, BattleDetail battleData) throws Exception {
        ServerInfo serverInfo = ServerManager.getInstance().getServerInfo(serverId);
        logger.info("收到战斗服务器发来的战斗结束消息:" + battleData);
        logger.info("serverInfo:" + serverInfo);
        if (serverInfo != null)
        {
            WorldManager worldManager = GameServer.getInstance().getWorldManager();

            int battleType = battleData.getBattleType();
            PlayerController playerController = worldManager.getPlayerController(battleData.getTeam1Info().getIndexInfo());
            logger.info("获取到玩家数据:" + playerController + "   " + battleData.getTeam1Info().getIndexInfo());
            if (playerController != null)
            {
                playerController.setLastUpdateTime(GameServer.currentTime);
                playerController.clearBattle();

                BattleTemp team2Info = battleData.getTeam2Info();
                if (battleType == SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVE_INSTANCE_VALUE)
                {
                    if(battleData.getWinTeam() == ConstantFactory.BATTLE_TEAM_1)
                    {
                        int levelId = Integer.parseInt(team2Info.getIndexInfo());
                        LevelConfig levelConfig = DataFactory.getInstance().getGameObject(DataFactory.LEVEL_KEY, levelId);
                        if (levelConfig != null) {
                            byte[] data = SGInstanceProto.C2S_PassLevel.newBuilder()
                                    .setChapterId(levelConfig.getInstanceId())
                                    .setLevelId(levelId)
                                    .setStarts(team2Info.getStar())
                                    .build().toByteArray();
                            SpringContext.getMessageChannel(ChallengeMsgChannel.class).messageChannel(
                                    SGMainProto.E_MSG_ID.MsgID_Instance_PassLevel_VALUE, playerController, data);
                        } else {
                            logger.info("副本战参数错误:" + team2Info);
                        }
                    }
                    else
                    {
                        logger.info("副本战斗失败");
                    }

                }
                else if (battleType == SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVP_ARENA_AI_VALUE)
                {
                    ChallengeMsgChannel arenaMsgChannel = (ChallengeMsgChannel) SpringContext.getMessageChannel(ChallengeMsgChannel.class);
                    arenaMsgChannel.arenaBattleEnd(playerController, team2Info.getIndexInfo(), battleData.getWinTeam() == ConstantFactory.BATTLE_TEAM_1);
                }
                else if (battleType == SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVE_OFFICIAL_RANK_VALUE ||
                        battleType == SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVP_OFFICIAL_RANK_VALUE)
                {
                    ChallengeMsgChannel msgChannel = (ChallengeMsgChannel) SpringContext.getMessageChannel(ChallengeMsgChannel.class);
                    String[] str = team2Info.getIndexInfo().split(ConstantFactory.SEMICOLON);
                    msgChannel.challengeResult(playerController, Integer.parseInt(str[0]), Integer.parseInt(str[1]), battleData.getWinTeam() == ConstantFactory.BATTLE_TEAM_1);
                }
                else if(battleType == SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVE_HERO_TEMPLE_VALUE)
                {
                    ChallengeMsgChannel msgChannel = (ChallengeMsgChannel) SpringContext.getMessageChannel(ChallengeMsgChannel.class);
                    msgChannel.heroTempleChallengeResult(playerController, Integer.parseInt(team2Info.getIndexInfo()), battleData.getWinTeam() == ConstantFactory.BATTLE_TEAM_1);
                }
            }


            //TODO 只有真实玩家对战才处理队伍2的玩家
            if(battleType == SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVP_ARENA_VALUE)
            {
                playerController = worldManager.getPlayerController(battleData.getTeam2Info().getIndexInfo());
                if (playerController != null)
                {
                    playerController.setLastUpdateTime(GameServer.currentTime);
                    playerController.clearBattle();
                }
            }

            serverInfo.removeBattle(battleData);
        }
    }



    @Override
    public void updateStatus(int serverId, String playerIndex, String status) throws RemoteException {
        PlayerController playerController = GameServer.getInstance().getWorldManager().getPlayerController(playerIndex);
        if (playerController != null) {
            playerController.setStatus(E_PlayerStatus.valueOf(status));
            playerController.setLastUpdateTime(GameServer.currentTime);
        }
    }


    @Override
    public Player getOnlinePlayer(String playerIndex) throws RemoteException {
        PlayerController playerController = GameServer.getInstance().getWorldManager().getPlayerController(playerIndex);
        if (playerController != null) {
            return playerController.getPlayer();
        }

        return null;
    }

    @Override
    public Player getOfflinePlayer(String playerIndex) throws RemoteException {
        PlayerMapper playerMapper = SpringContext.getBean(PlayerMapper.class);
        PlayerModel playerModel = playerMapper.getPlayerByIndex(playerIndex);
        if (playerModel != null) {

            return new Player(GameServer.getInstance().getWorldManager().getWorldInfo(), playerModel);
        }
        return null;
    }

    @Override
    public DataFactory getDataFactory() throws RemoteException {
        return DataFactory.getInstance();
    }

}
