package com.douqu.game.battle.server.netty;

import com.douqu.game.battle.server.GameServer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.apache.log4j.Logger;

import java.nio.ByteOrder;


/**
* Created by bean on 2016/11/9.
*/
public class NettyRouteClient {

    Logger logger = Logger.getLogger(this.getClass());

    private EventLoopGroup workerGroup;

    public void start()
    {

        workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN,Integer.MAX_VALUE,0,4,2,4,true));
                    ch.pipeline().addLast(new NettyRouteClientHandler());

                }
            });
            // 发起异步连接操作
            ChannelFuture future = bootstrap.connect("127.0.0.1", GameServer.getInstance().getRoutePort()).sync();

            if(future.isSuccess())
            {
                logger.info("Route Client Start Success------Port---【" + GameServer.getInstance().getRoutePort() + "】");
//                future.channel().closeFuture().sync();
            }
            else
            {
                logger.info("Route Client Start Failed------Port---【" + GameServer.getInstance().getRoutePort() + "】");
            }
        } catch (Exception e) {
            e.printStackTrace();
            stop();
        }
    }

    public synchronized void stop()
    {
        logger.info("Close TCPClient..................");
        workerGroup.shutdownGracefully();
    }


    public static void main(String[] args) throws InterruptedException {

        new NettyRouteClient().start();
    }
}
