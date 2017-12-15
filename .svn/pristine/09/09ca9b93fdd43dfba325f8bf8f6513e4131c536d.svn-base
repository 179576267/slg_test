package com.douqu.game.main.server.netty;

import com.douqu.game.core.factory.ConfigFactory;
import com.douqu.game.core.i.IServer;
import com.douqu.game.main.gui.MainFrame;
import com.douqu.game.main.util.PrintUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.apache.log4j.Logger;

import java.nio.ByteOrder;

/**
 * Created by bean on 2017/3/9.
 */
public class NettyGMServer implements IServer {

    Logger logger = Logger.getLogger(NettyGMServer.class);

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    @Override
    public void start()
    {
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
                    ch.pipeline().addLast("handler", new NettyGMServerHandler());
                }
            });
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);//检测客户端连接是否有效的参数
            ChannelFuture f = serverBootstrap.bind(ConfigFactory.MAIN_SERVER_GM_TCP_PORT).sync();
            if(f.isSuccess())
            {
                PrintUtils.info(logger, "GM Server Start Success------Port---【" + ConfigFactory.MAIN_SERVER_GM_TCP_PORT + "】");
                f.channel().closeFuture().sync();
            }
            else
            {
                PrintUtils.info(logger, "GM Server Start Failed------Port---【" + ConfigFactory.MAIN_SERVER_GM_TCP_PORT + "】");
            }
        } catch (Exception e) {
            e.printStackTrace();
            stop();
        }
    }


    @Override
    public synchronized void stop()
    {
        PrintUtils.info(logger, "Close GMServer..................");
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }


    public static void main(String[] args) {
        try {
            new NettyGMServer().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
