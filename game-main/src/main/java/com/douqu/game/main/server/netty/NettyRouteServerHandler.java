package com.douqu.game.main.server.netty;


import com.alibaba.fastjson.JSONObject;
import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.database.model.GMModel;
import com.douqu.game.core.e.E_ProfileVersion;
import com.douqu.game.core.factory.CodeFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.netty.NettyConnection;
import com.douqu.game.core.util.BufferUtils;
import com.douqu.game.core.util.SendUtils;
import com.douqu.game.core.util.Utils;
import com.douqu.game.main.GameServer;
import com.douqu.game.main.msg.GMChannel;
import com.douqu.game.main.msg.RouteChannel;
import com.douqu.game.main.server.SpringContext;
import com.douqu.game.main.service.GMService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;


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
        RouteChannel.messageChannel(connection, (ByteBuf)msg);
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
