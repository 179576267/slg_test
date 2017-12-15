package com.douqu.game.battle.server.netty;



import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.battle.server.GameServer;
import com.douqu.game.core.factory.CodeFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.util.BufferUtils;
import com.douqu.game.core.util.SendUtils;
import com.douqu.game.core.util.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

/**
* Created by Administrator on 2016/11/9.
*/
public class NettyTCPClientHandler extends ChannelInboundHandlerAdapter {

    Logger logger = Logger.getLogger(this.getClass());

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
//        cause.printStackTrace();
        logger.info(cause.getLocalizedMessage());
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        int code = BufferUtils.readShort(byteBuf);

        logger.info("data length:" + byteBuf.readableBytes());
        logger.info("code:"+code);

        try{
            if(code == CodeFactory.GET_BATTLE_TCP_PORT)
            {
                if(!GameServer.getInstance().isRunning())
                {
                    int port = byteBuf.readInt();

                    logger.info("从主服务器获取到TCP端口:"+port);

                    GameServer.getInstance().startBattleTCP(port);

                    GameServer.getInstance().startRouteServer(Integer.parseInt("1" + port));

                    GameServer.getInstance().startRMI();
                }
            }
            else if(code == CodeFactory.LOAD_DATA)
            {
                DataFactory.getInstance().set(GameServer.getInstance().getiMainServer().getDataFactory());
            }
            else if(code == CodeFactory.BATTLE_INIT)
            {
                //接受双方玩家流水号
                ByteBuffer buffer = new ByteBuffer();
                buffer.writeBytes(Utils.byteBufToBytes(byteBuf));

                try{
                    String battleId = buffer.readUTF();
                    int battleTypeInt = buffer.readByte();
                    SGCommonProto.E_BATTLE_TYPE battleType = SGCommonProto.E_BATTLE_TYPE.forNumber(battleTypeInt);
                    String playerIndex = buffer.readUTF();
                    String targetIndex = buffer.readUTF();
                    GameServer.getInstance().getWorldManager().addBattleInitInfo(battleId, battleType, playerIndex, targetIndex);
                    logger.info("游戏服务器发来战斗初始化消息:battleId:"+ battleId + " playerIndex:"+playerIndex+" targetIndex:"+targetIndex);
                }catch (Exception e){
                    logger.info("主服务器发来战斗数据错误:" + buffer.length());
                }
            }
            else if(code == CodeFactory.STOP_SERVER)
            {
                logger.info("游戏主服务器关闭,我也要关闭了!");
                GameServer.getInstance().stop();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();// 将消息发送队列中的消息写入到SocketChannel中发送给对方。
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {

        logger.info("客户端连接服务器成功");

        GameServer.getInstance().setServerChannel(ctx.channel());

        SendUtils.sendMsg(ctx.channel(), CodeFactory.GET_BATTLE_TCP_PORT, null);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        GameServer.getInstance().stop();

        logger.info("服务器已关闭，我也要关服了！");

        ctx.close();

    }


}