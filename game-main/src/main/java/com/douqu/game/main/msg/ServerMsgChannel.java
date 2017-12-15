package com.douqu.game.main.msg;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.e.E_ServerType;
import com.douqu.game.core.factory.CodeFactory;
import com.douqu.game.core.factory.ConfigFactory;
import com.douqu.game.core.netty.NettyConnection;
import com.douqu.game.core.util.SendUtils;
import com.douqu.game.core.util.Utils;
import com.douqu.game.main.GameServer;
import com.douqu.game.main.server.ServerInfo;
import com.douqu.game.main.server.ServerManager;
import com.douqu.game.main.server.SpringContext;
import com.mysql.jdbc.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;

/**
 * Created by bean on 2017/7/21.
 */
public class ServerMsgChannel {

    static Logger logger = Logger.getLogger(ServerMsgChannel.class);

    public static void messageChannel(int code, NettyConnection connection, ByteBuf byteBuf)
    {
        if(code == CodeFactory.CLOSE_MAIN_SERVER)
        {
            logger.info("执行关闭主服务器指令 -> IP:" + connection.getChannel().remoteAddress());

            InetSocketAddress address = (InetSocketAddress) connection.getChannel().remoteAddress();
            if(address.getAddress().getHostAddress().indexOf("127.0.0.1") == -1)
            {
                logger.info("不允许的地址请求 -> " + address.getAddress().getHostAddress());
                ByteBuffer out = new ByteBuffer();
                out.writeBoolean(false);
                out.writeUTF("不允许的地址!");
                SendUtils.sendMsg(connection.getChannel(), CodeFactory.CLOSE_MAIN_SERVER, out.getBytes());
                return;
            }
            try{
                ByteBuffer buffer = new ByteBuffer();
                buffer.writeBytes(Utils.byteBufToBytes(byteBuf));

                String username = buffer.readUTF();
                String password = buffer.readUTF();

                logger.info("执行人 -> " + username);

                if(StringUtils.isNullOrEmpty(username) || username.length() < 2 || username.length() > 10)
                {
                    logger.info("关闭主服务器失败 -> 操作人员错误(用户名长度2-10)!");
                    ByteBuffer out = new ByteBuffer();
                    out.writeBoolean(false);
                    out.writeUTF("关闭主服务器失败 -> 操作人员错误(用户名长度2-10)!");
                    SendUtils.sendMsg(connection.getChannel(), CodeFactory.CLOSE_MAIN_SERVER, out.getBytes());
                    return;
                }

                if(StringUtils.isNullOrEmpty(password) || !password.equals(ConfigFactory.CLOSE_SERVER_KEY))
                {
                    logger.info("关闭主服务器失败 -> 密码错误 -> 收到的密码:" + password + " 正确的密码:" + ConfigFactory.CLOSE_SERVER_KEY);
                    ByteBuffer out = new ByteBuffer();
                    out.writeBoolean(false);
                    out.writeUTF("关闭主服务器失败 -> 密码错误!");
                    SendUtils.sendMsg(connection.getChannel(), CodeFactory.CLOSE_MAIN_SERVER, out.getBytes());
                    return;
                }

                GameServer.getInstance().stop();

                SpringContext.stop();

                ByteBuffer out = new ByteBuffer();
                out.writeBoolean(true);
                out.writeUTF("关闭主服务器成功!");
                SendUtils.sendMsg(connection.getChannel(), CodeFactory.CLOSE_MAIN_SERVER, out.getBytes());

                logger.info("关闭主服务器成功!");

                System.exit(0);

            }catch (Exception e){
                logger.info("执行关闭主服务器出现错误 -> " + e.getMessage());
            }

            return;
        }
        ServerManager serverManager = ServerManager.getInstance();
        if(CodeFactory.GET_BATTLE_TCP_PORT == code)
        {
            int port = ServerManager.getInstance().getBattleServerPort();
            logger.info("TCP战斗服务器获取端口:"+port + "  ip:" + connection.getChannel().remoteAddress());

            ByteBuf out = Unpooled.buffer();
            out.writeInt(port);
            SendUtils.sendMsg(connection.getChannel(), CodeFactory.GET_BATTLE_TCP_PORT, out.array());

            serverManager.addServerInfo(E_ServerType.BATTLE, connection, port);
        }
        else if(code == CodeFactory.START_BATTLE_TCP_SERVER)
        {
            logger.info("战斗服务器已经启动:"+connection);
            ServerInfo serverInfo = serverManager.getServerInfo(connection);
            if(serverInfo != null)
            {
                logger.info("TCP战斗服务器开启，设置运行:" + serverInfo);

                serverInfo.setRunning(true);

                ByteBuf out = Unpooled.buffer();
                out.writeBoolean(true);
                SendUtils.sendMsg(connection.getChannel(), CodeFactory.START_BATTLE_TCP_SERVER, out.array());
            }
        }
    }
}
