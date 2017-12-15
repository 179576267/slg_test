package com.douqu.game.battle.server.netty;



import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.battle.server.GameServer;
import com.douqu.game.battle.util.BattleUtils;
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
* Created by bean on 2016/11/9.
*/
public class NettyRouteClientHandler extends ChannelInboundHandlerAdapter {

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
//            if(code == BattleUtils.GET_ROUTE_PATH)
//            {
//
//            }
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

        GameServer.getInstance().setRouteChannel(ctx.channel());
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        GameServer.getInstance().stop();

        logger.info("服务器已关闭，我也要关服了！");

        ctx.close();

    }


}