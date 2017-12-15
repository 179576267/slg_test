package com.douqu.game.battle.server.netty;



import com.douqu.game.battle.controller.*;
import com.douqu.game.battle.controller.sprite.MonsterController;
import com.douqu.game.battle.controller.sprite.PlayerController;
import com.douqu.game.battle.entity.aobject.BattleAObject;
import com.douqu.game.battle.entity.tmp.PlayerBattleTmp;
import com.douqu.game.battle.entity.tmp.SpriteBattleTmp;
import com.douqu.game.battle.server.BattleInitInfo;
import com.douqu.game.battle.server.GameServer;
import com.douqu.game.battle.server.WorldManager;
import com.douqu.game.core.e.E_PlayerStatus;
import com.douqu.game.core.config.card.CardConfig;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.entity.db.CardDB;
import com.douqu.game.core.factory.CodeFactory;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.WordFactory;
import com.douqu.game.core.netty.NettyConnection;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.protobuf.SGCommonProto.E_BATTLE_TYPE;
import com.douqu.game.core.protobuf.SGMainProto;
import com.douqu.game.core.protobuf.SGSystemProto;
import com.douqu.game.core.protobuf.SGWarProto;
import com.douqu.game.core.util.BufferUtils;
import com.douqu.game.core.util.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.log4j.Logger;

import java.rmi.RemoteException;
import java.util.List;


/**
 * Created by bean on 2017/3/9.
 */
public class NettyTCPServerHandler extends SimpleChannelInboundHandler {
    Logger logger = Logger.getLogger(NettyTCPServerHandler.class);

    private NettyConnection connection;

//    private PlayerController playerController;

    private void close()
    {
        Object object = connection.getObject();
        if(object != null && object instanceof PlayerController)
        {
            PlayerController playerController = (PlayerController) object;
            if(playerController != null)
            {
                if(CodeFactory.TEST)
                {
                    if(playerController.getBattleController() != null)
                    {
                        int winTeam = 0;
                        if(playerController.getAttachment() != null)
                        {
                            winTeam = playerController.getAttachment().getTeamNo() == ConstantFactory.BATTLE_TEAM_1 ? ConstantFactory.BATTLE_TEAM_2 : ConstantFactory.BATTLE_TEAM_1;
                        }
                        playerController.getBattleController().end(winTeam);
                    }

                    GameServer.getInstance().getWorldManager().playerExitGame(playerController);
                }
                else
                {
                    try {
                        GameServer.getInstance().getiMainServer().updateStatus(GameServer.getInstance().getId(), playerController.getObjectIndex(), E_PlayerStatus.BATTLE_OFFLINE.name());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    playerController.destroy();
                }
            }
        }

    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE)
            {
                logger.info("Reader Timeout!"+connection);
                close();
            }
        }
    }

//    @Override
//    public void handlerRemoved(ChannelHandlerContext ctx)
//    {
//        logger.debug("Client handlerRemoved Disconnect:"+playerController);
//
//        close();
//    }


    /*
      * channelAction
      *
      * 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
      *
      */
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        logger.info("Client Connect Success:"+ctx.channel().remoteAddress());

        connection = new NettyConnection(ctx.channel());
//
//        GameServer.getInstance().getWorldManager().addConnection(connection);
    }



    /*
     * channelInactive
     *
     *
     * 当客户端主动断开服务端的 链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     *
     */
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        logger.info("Client Disconnect:" + connection);

        close();
    }


    /**
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg)
    {
        ByteBuf byteBuf = (ByteBuf) msg;
        int code = BufferUtils.readShort(byteBuf);

        logger.info("收到指令->" + SGMainProto.E_MSG_ID.forNumber(code) + "->" + code + "  connection:" + connection.getChannel().remoteAddress());

        try{

            if(SGMainProto.E_MSG_ID.MsgID_System_BattleReconnect_VALUE == code)
            {
                if(connection.getObject() != null)
                    return;

                byte[] data = Utils.byteBufToBytes(byteBuf);
                SGSystemProto.C2S_BattleReconnect request = SGSystemProto.C2S_BattleReconnect.parseFrom(data);
                String battleId = request.getBattleId();
                String playerIndex = request.getPlayerIndex();
                WorldManager worldManager = GameServer.getInstance().getWorldManager();
                BattleController battleController = worldManager.getBattleController(battleId);
                if(battleController == null)
                {
                    //TODO 战斗已经结束或者战斗不存在，提示客户端去连接主服务器
                    return;
                }

                PlayerController playerController = worldManager.getPlayerController(playerIndex);
                if(playerController == null || battleController.getPlayerByIndex(playerIndex) == null)
                {
                    //玩家数据不存在，参数错误
                    return;
                }

                try {
                    GameServer.getInstance().getiMainServer().updateStatus(GameServer.getInstance().getId(), playerController.getObjectIndex(), playerController.getStatus().name());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                playerController.setTcpConnection(connection);
                connection.setObject(playerController);

                //返回数据
                SGSystemProto.S2C_BattleReconnect.Builder response = SGSystemProto.S2C_BattleReconnect.newBuilder();
                response.setBattleId(battleId);
                response.setPlayerIndex(playerIndex);
                response.setBattleType(battleController.getBattleType());

                PlayerBattleTmp playerBattleTmp = (PlayerBattleTmp) playerController.getAttachment();

                response.setTeamNo(playerBattleTmp.getTeamNo());
                response.setSelfMaster(playerBattleTmp.getMasterSoldier().parseBattleUnit());
                response.setOtherMaster(playerBattleTmp.getTargetBattleTmp().getMasterSoldier().parseBattleUnit());
                for(CardDB cardDB : playerBattleTmp.getCardList())
                {
                    response.addCardId(cardDB.id);
                }

                for(BattleAObject battleAObject : battleController.getSoldiers())
                {
                    response.addSoldier(battleAObject.parseBattleUnit());
                }

                playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_System_BattleReconnect_VALUE, response.build().toByteArray());
            }
            else if(SGMainProto.E_MSG_ID.MsgID_War_Create_VALUE == code)
            {
                if(connection.getObject() != null)
                    return;

                //创建战斗
                byte[] data = Utils.byteBufToBytes(byteBuf);
                SGWarProto.C2S_Create request = SGWarProto.C2S_Create.parseFrom(data);
                String battleId = request.getBattleId();
                WorldManager worldManager = GameServer.getInstance().getWorldManager();
                BattleInitInfo battleInitInfo = worldManager.getBattleInitInfo(battleId);
                if(battleInitInfo == null)
                {
                    //非法请求
                    System.out.println("战斗不存在:" + battleId);
                    return;
                }
                //玩家流水号
                String playerIndex = request.getPlayerIndex();
                if(!battleInitInfo.isExist(playerIndex))
                {
                    //数据异常
                    System.out.println("数据异常:" + playerIndex);
                    return;
                }

                Player player = GameServer.getInstance().getiMainServer().getOnlinePlayer(playerIndex);
                if(player == null)
                {
                    //玩家不在线了
                    System.out.println("玩家不在线:" + playerIndex);
                    return;
                }

                logger.info("收到创建战斗的指令:" + playerIndex + "  battleInitInfo: " + battleInitInfo);

                PlayerController playerController = new PlayerController(player, connection);
                battleInitInfo.initPlayerData(playerController);
                worldManager.playerEnterGame(playerController);

                battleInitInfo.ready(playerIndex);

                if(battleInitInfo.isReady())
                {
                    //双方都准备好了，初始化战斗

                    E_BATTLE_TYPE battleType = battleInitInfo.getBattleType();

                    SGCommonProto.Pos bottomLeft = request.getBattleFieldBottomLeft();
                    SGCommonProto.Pos topRight = request.getBattleFieldTopRight();

                    BattleController battleController = null;
                    if(E_BATTLE_TYPE.BATTLE_TYPE_PVP_ARENA == battleType)
                    {
                        battleController = new PVPController(battleId, battleInitInfo.getPlayer().getPlayer(),
                                battleInitInfo.getTarget().getPlayer(), battleType, bottomLeft, topRight);
                    }
                    else if(E_BATTLE_TYPE.BATTLE_TYPE_PVP_ARENA_AI == battleType)
                    {
                        Player offlinePlayer = GameServer.getInstance().getiMainServer().getOfflinePlayer(battleInitInfo.getTarget().getPlayerIndex());
                        if(offlinePlayer != null)
                        {
                            battleController = new PVPController(battleId, battleInitInfo.getPlayer().getPlayer(),
                                    new PlayerController(offlinePlayer, null), battleType, bottomLeft, topRight);
                        }
                    }
                    else if(E_BATTLE_TYPE.BATTLE_TYPE_PVE_INSTANCE == battleType)
                    {
                        battleController = new PVEInstanceController(battleId, battleInitInfo.getPlayer().getPlayer(),
                                                new MonsterController(battleType, battleInitInfo.getTarget().getPlayerIndex()),
                                                battleType, bottomLeft, topRight, battleInitInfo.getTarget().getPlayerIndex());
                    }
                    else if(E_BATTLE_TYPE.BATTLE_TYPE_PVE_OFFICIAL_RANK == battleType)
                    {
                        battleController = new PVEOfficialRankController(battleId, battleInitInfo.getPlayer().getPlayer(),
                                                new MonsterController(battleType, battleInitInfo.getTarget().getPlayerIndex()),
                                                battleType, bottomLeft, topRight);
                    }
                    else if(E_BATTLE_TYPE.BATTLE_TYPE_PVP_OFFICIAL_RANK == battleType)
                    {
                        String[] strs = battleInitInfo.getTarget().getPlayerIndex().split(ConstantFactory.SEMICOLON);
                        Player offlinePlayer = GameServer.getInstance().getiMainServer().getOfflinePlayer(strs[2]);
                        if(offlinePlayer != null)
                        {
                            battleController = new PVPOfficialRankController(battleId, battleInitInfo.getPlayer().getPlayer(),
                                    new PlayerController(offlinePlayer, null), battleType, bottomLeft, topRight, battleInitInfo.getTarget().getPlayerIndex());
                        }
                    }
                    else if(E_BATTLE_TYPE.BATTLE_TYPE_PVE_HERO_TEMPLE == battleType)
                    {
                        battleController = new PVEHeroTempleController(battleId, battleInitInfo.getPlayer().getPlayer(),
                                new MonsterController(battleType, battleInitInfo.getTarget().getPlayerIndex()),
                                battleType, bottomLeft, topRight);
                    }

                    logger.info("双方都准备好了，初始化战斗 -> " + battleController);
                    if(battleController != null)
                    {
                        battleController.init();

                        SpriteBattleTmp playerBattleTmp = battleController.getPlayer().getAttachment();
                        SGWarProto.S2C_Create.Builder response = SGWarProto.S2C_Create.newBuilder();
                        response.setTeamNo(playerBattleTmp.getTeamNo());
                        BattleAObject master1 = playerBattleTmp.getMasterSoldier();
                        BattleAObject master2 = playerBattleTmp.getTargetBattleTmp().getMasterSoldier();

                        response.setSelfMasterGuid(master1.getUniqueId());
                        response.setOtherMasterGuid(master2.getUniqueId());
                        response.setSelfMasterGradeId(master1.getObjectId());
                        response.setOtherMasterGradeId(master2.getObjectId());
                        response.setSelfMasterHP(master1.getHp());
                        response.setOtherMasterHP(master2.getHp());
                        response.setBattleType(battleType);

                        logger.info("队伍:" + playerBattleTmp.getTeamNo());
                        logger.info("response:" + response);

                        List<CardDB> cardList = playerBattleTmp.getCardList();
                        for(CardDB cardDB : cardList)
                        {
                            response.addBattleCardId(cardDB.id);
                        }
                        battleController.getPlayer().sendMsg(SGMainProto.E_MSG_ID.MsgID_War_Create_VALUE, response.build().toByteArray());

                        if(battleController.isRealPlayerBattle())
                        {
                            SpriteBattleTmp targetBattleTmp = battleController.getTarget().getAttachment();
                            response = SGWarProto.S2C_Create.newBuilder();
                            response.setTeamNo(targetBattleTmp.getTeamNo());
                            master1 = targetBattleTmp.getMasterSoldier();
                            master2 = targetBattleTmp.getTargetBattleTmp().getMasterSoldier();
                            response.setSelfMasterGuid(master1.getUniqueId());
                            response.setOtherMasterGuid(master2.getUniqueId());
                            response.setSelfMasterGradeId(master1.getObjectId());
                            response.setOtherMasterGradeId(master2.getObjectId());
                            response.setSelfMasterHP(master1.getHp());
                            response.setOtherMasterHP(master2.getHp());

                            response.setBattleType(battleType);
                            cardList = targetBattleTmp.getCardList();
                            for(CardDB cardDB : cardList)
                            {
                                response.addBattleCardId(cardDB.id);
                            }

                            battleController.getTarget().sendMsg(SGMainProto.E_MSG_ID.MsgID_War_Create_VALUE, response.build().toByteArray());
                        }

                        GameServer.getInstance().getWorldManager().addBattleController(battleController);

                    }

                    worldManager.removeBattleInitInfo(battleInitInfo);
                }
            }
            else
            {
                Object object = connection.getObject();
                if(object == null)
                    return;

                if(!(object instanceof PlayerController))
                    return;

                PlayerController playerController = (PlayerController) object;

                if(playerController.getPlayer().isDel)
                {
                    //被封号了
                    playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.LOCK_ACCOUNT));
                    return;
                }

//                logger.info("玩家请求战斗信息:" + playerController.getName() + "  " + playerController.getParent());

                if(playerController.getBattleController() != null)
                {
                    playerController.getBattleController().messageChannel(code, playerController, Utils.byteBufToBytes(byteBuf));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }





    /*
     * channelReadComplete
     *
     * channel  通道
     * Read     读取
     * Complete 完成
     *
     * 在通道读取完成后会在这个方法里通知，对应可以做刷新操作
     * ctx.flush()
     *
     */
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    /*
     * exceptionCaught
     *
     * exception	异常
     * Caught		抓住
     *
     * 抓住异常，当发生异常的时候，可以做一些相应的处理，比如打印日志、关闭链接
     *
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        logger.debug(cause.getLocalizedMessage());

        cause.printStackTrace();

        ctx.close();
    }


}
