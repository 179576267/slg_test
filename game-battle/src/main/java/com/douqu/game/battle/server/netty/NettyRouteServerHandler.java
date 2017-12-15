package com.douqu.game.battle.server.netty;


import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.battle.controller.BattleController;
import com.douqu.game.battle.entity.aobject.BattleAObject;
import com.douqu.game.battle.entity.aobject.BattleSoldierAObject;
import com.douqu.game.battle.server.GameServer;
import com.douqu.game.battle.util.BattleUtils;
import com.douqu.game.core.astar.AbsCoord;
import com.douqu.game.core.astar.AstarMap;
import com.douqu.game.core.astar.Node;
import com.douqu.game.core.config.common.Position;
import com.douqu.game.core.e.E_BattleAObjectStatus;
import com.douqu.game.core.factory.CodeFactory;
import com.douqu.game.core.netty.NettyConnection;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.protobuf.SGMainProto;
import com.douqu.game.core.protobuf.SGWarProto;
import com.douqu.game.core.util.BufferUtils;
import com.douqu.game.core.util.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;

import java.rmi.server.ExportException;
import java.util.List;


/**
 * Created by bean on 2017/3/9.
 */
public class NettyRouteServerHandler extends SimpleChannelInboundHandler
{
    Logger logger = Logger.getLogger(NettyRouteServerHandler.class);

    private NettyConnection connection;


    /*
      * channelAction
      *
      * 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
      *
      */
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        logger.info("Client Connect Success:"+ctx.channel().remoteAddress());

        connection = new NettyConnection(ctx.channel());
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

        connection.destroy();
    }


    /**
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg)
    {
//        RouteChannel.messageChannel(connection, (ByteBuf)msg);

        ByteBuf byteBuf = (ByteBuf) msg;

        int code = BufferUtils.readShort(byteBuf);

        logger.debug("data length:" + byteBuf.readableBytes());
        logger.debug("code:" + code);

        try {
            if (code == BattleUtils.GET_ROUTE_PATH)
            {
                ByteBuffer buffer = new ByteBuffer();
                buffer.writeBytes(Utils.byteBufToBytes(byteBuf));

                String battleId = buffer.readUTF();
                int sourceId = buffer.readInt();
                int targetTeamNo = buffer.readByte();
                int targetId = buffer.readInt();

                BattleController battleController = GameServer.getInstance().getWorldManager().getBattleController(battleId);
                if(battleController == null || battleController.isEnd())
                {
                    logger.debug("战斗不存在或战斗已经结束");
                    return;
                }

                if(!battleController.isStarted())
                {
                    logger.debug("战斗还没开始");
                    return;
                }

                BattleAObject source = battleController.getSoldier(sourceId);
                if(source == null || !source.isMove || source.isDie())
                {
                    logger.debug("士兵不存在或者已经死亡");
                    return;
                }

                BattleAObject target = battleController.getBattleAObject(targetTeamNo, targetId);
                if(target == null || target.isDie())
                {
                    logger.debug("目标不存在或者已经死亡");
                    return;
                }

                Position targetPosition = null;

                BattleSoldierAObject soldier = (BattleSoldierAObject) source;
                targetPosition = soldier.getAtkPosition(target);
                if(targetPosition == null)
                {
                    logger.debug("目标点找不到");
                    return;
                }

                if(soldier.goTargetSame(targetPosition))
                {
                    logger.debug("跟上一次寻路目标点相同");
                    return;
                }

                soldier.setRouteTarget(target, targetPosition);

                logger.info(source.getName() + "  收到寻路请求 -> start:(" + source.position.x + "," + source.position.y + ") end:(" + targetPosition.x + "," + targetPosition.y + ")");

//                if(battleController.getAstar().getAstarMap().isObstacleToApplication(targetPosition.getIntX(), targetPosition.getIntY()))
//                {
//                    logger.info("寻路目标点现在有障碍了,重新寻找目标点");
//
//                }

//                try{
                    battleController.getAstar().getPath(new Node(source.position.getIntX(), source.position.getIntY()), new Node(targetPosition.getIntX(), targetPosition.getIntY()), pathList -> {

                        logger.info("寻路收到回复 -> ");

                        if (soldier.isDie())
                            return;

                        logger.info(soldier.getUniqueId() + " name:" + soldier.getName() + " 获取路线:" + pathList);
                        List<Position> changeRouteList = BattleUtils.changeRoute(pathList, soldier.moveSpeed.getAtt());
//                        if (soldier instanceof BattleSoldierAObject) {
//                            BattleSoldierAObject battleSoldierAObject = (BattleSoldierAObject) soldier;
                            soldier.initRoute(changeRouteList);

                            soldier.setStatus(E_BattleAObjectStatus.GOING);

                            SGWarProto.S2C_SoldierRoute.Builder sendToClient = SGWarProto.S2C_SoldierRoute.newBuilder();
                            sendToClient.setUniqueId(sourceId);
                            SGCommonProto.Pos.Builder pos = null;
                            for (Node node : pathList) {
                                pos = SGCommonProto.Pos.newBuilder();
                                pos.setX(node.grid.x);
                                pos.setY(node.grid.y);
                                sendToClient.addRoute(pos);
                            }
                            battleController.dispatchMsg(SGMainProto.E_MSG_ID.MsgID_War_SoldierRoute_VALUE, sendToClient.build().toByteArray());
//                        }

                        logger.info(soldier.getUniqueId() + " name:" + soldier.getName() + " 从客户获取转换后的路线:" + changeRouteList);
                    });
//                }catch (Exception e){
//                    e.printStackTrace();
//
//                    logger.info(battleController.getAstar().getAstarMap());
//                }

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
        try{
            logger.debug(cause.getLocalizedMessage());
            cause.printStackTrace();
            ctx.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }



}
