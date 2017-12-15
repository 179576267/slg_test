package com.douqu.game.close;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteOrder;


/**
 * Created by Administrator on 2016/11/9.
 */
public class Main {

    private EventLoopGroup workerGroup;

    public void start(String ip, int port, String sourcePassword)
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
                    ch.pipeline().addLast(new Handler(sourcePassword));

                }
            });
            // 发起异步连接操作
            ChannelFuture future = bootstrap.connect(ip, port).sync();

            if(future.isSuccess())
            {
                System.out.println("Main Start Success------Port---【" + port + "】");
//                future.channel().closeFuture().sync();
            }
            else
            {
                System.out.println("Main Start Failed------Port---【" + port + "】");
            }
        } catch (Exception e) {
            e.printStackTrace();
            stop();
        }
    }

    public synchronized void stop()
    {
        System.out.println("Close Main..................");
        workerGroup.shutdownGracefully();
    }


    public static void main(String[] args) throws InterruptedException
    {
        String password = args[0];

        new Main().start("127.0.0.1", 11111, password);
    }
}
