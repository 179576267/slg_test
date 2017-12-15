package com.douqu.game.battle.server.netty;

import com.douqu.game.battle.server.GameServer;
import com.douqu.game.core.factory.CodeFactory;
import com.douqu.game.core.i.IServer;
import com.douqu.game.core.util.SendUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.log4j.Logger;

import java.nio.ByteOrder;
import java.util.concurrent.TimeUnit;

/**
 * Created by bean on 2017/3/9.
 */
public class NettyTCPServer implements IServer {

    Logger logger = Logger.getLogger(NettyTCPServer.class);

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    @Override
    public void start()
    {
        int port = GameServer.getInstance().getTcpPort();
        if(port == 0)
        {
            logger.info("未获取到端口号");
            return;
        }
        //bossGroup线程池用来接受客户端的连接请求
        bossGroup = new NioEventLoopGroup();
        //workerGroup线程池用来处理boss线程池里面的连接的数据
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN,Integer.MAX_VALUE,0,4,2,4,true));
//                    ch.pipeline().addLast(new IdleStateHandler(30, 0, 30, TimeUnit.SECONDS));//心跳30秒检测一次
                    ch.pipeline().addLast("handler", new NettyTCPServerHandler());
                };
            });
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);//检测客户端连接是否有效的参数
            ChannelFuture f = serverBootstrap.bind(port).sync();
            if(f.isSuccess())
            {
                logger.info("TCP Server Start Success------Port---【" + port + "】");
                logger.info("TCP Thread:" + Thread.currentThread().getId() + " Name:" + Thread.currentThread().getName());

                SendUtils.sendMsg(GameServer.getInstance().getServerChannel(), CodeFactory.START_BATTLE_TCP_SERVER, null);

                f.channel().closeFuture().sync();
            }
            else
            {
                logger.info("TCP Server Start Failed------Port---【" + port + "】");
            }
        } catch (Exception e) {
            e.printStackTrace();
            stop();
        }
    }


    @Override
    public synchronized void stop()
    {
        logger.info("Close TCPServer..................");
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }


    public static void main(String[] args) {
        try {
            new NettyTCPServer().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
