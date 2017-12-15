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
import com.douqu.game.main.msg.*;
import com.douqu.game.main.GameServer;
import com.douqu.game.main.server.SpringContext;
import com.douqu.game.main.service.GMService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;


/**
 * Created by bean on 2017/3/9.
 */
public class NettyGMServerHandler extends SimpleChannelInboundHandler
{
    Logger logger = Logger.getLogger(NettyGMServerHandler.class);

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
        ByteBuf byteBuf = (ByteBuf) msg;
        int code = BufferUtils.readShort(byteBuf);

        logger.info("code: " + Integer.toHexString(code) + "-> data length: " + byteBuf.readableBytes());

        if(code == CodeFactory.GM_LOGIN)
        {
//            if(connection.getObject() != null)
//            {
//                connection.setObject(null);
//                return;
//            }

            ByteBuffer data = new ByteBuffer(Utils.byteBufToBytes(byteBuf));

            String account = data.readUTF();
            String password = data.readUTF();

            logger.info("GM用户登录 -> account:" + account + " password:" + password);

            GMService gmService = SpringContext.getBean(GMService.class);
            GMModel gmModel = gmService.findByName(account);
            if(gmModel == null || !gmModel.getPassword().equals(password))
            {
                GMChannel.sendError(connection, "账号或密码错误!");
            }
            else
            {
                connection.setObject(gmModel);
                GameServer.getInstance().getWorldManager().addGM(connection);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("account", account);
                jsonObject.put("version", E_ProfileVersion.forKey(SpringContext.getProfile()).getMsg());
                jsonObject.put("rechargeConfig", DataFactory.getInstance().getDataToJson(DataFactory.RECHARGE_KEY));

//                //总内存：
//                int totalMemory = (int) (Runtime.getRuntime().totalMemory()/1024/1024);
//                //最大内存：
//                int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024/1024);
//                //空闲内存：
//                int freeMemory = (int) (Runtime.getRuntime().freeMemory()/1024/1024);
//                // 已占用的内存：
//                int currentMemory = (int) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) /1024/1024;
//
//                System.out.println("总的内存 -> " + totalMemory + "MB");
//                System.out.println("最大内存 -> " + maxMemory + "MB");
//                System.out.println("空闲内存 -> " + freeMemory + "MB");
//                System.out.println("占用内存 -> " + currentMemory + "MB");

                ByteBuffer out = new ByteBuffer();
                out.writeUTF(jsonObject.toJSONString());
                SendUtils.sendMsg(connection.getChannel(), CodeFactory.GM_LOGIN, out.getBytes());
            }
        }
        else if(code == CodeFactory.GM_LOGOUT)
        {
            GameServer.getInstance().getWorldManager().delGM(connection);

            Object userInfo = connection.getObject();
            ByteBuffer out = new ByteBuffer();
            if(userInfo != null && userInfo instanceof GMModel)
            {
                out.writeUTF(((GMModel) userInfo).getUsername());
            }
            SendUtils.sendMsg(connection.getChannel(), CodeFactory.GM_LOGOUT, out.getBytes());

            connection.destroy();
        }
        else
        {
            if(connection.getObject() == null)
                return;

            if(!(connection.getObject() instanceof GMModel))
                return;

            GMChannel.messageChannel(code, connection, byteBuf);
        }

    }


    /**
     * 登录
     * @param byteBuf
     */
    private void login(ByteBuf byteBuf)
    {
        if(connection.getObject() != null)
        {
            //重复登录
            return;
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
