package com.douqu.game.main.client;

import com.douqu.game.core.entity.sprite.Card;
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
import java.util.HashMap;
import java.util.Map;


/**
* Created by Administrator on 2016/11/9.
*/
public class NettyTCPClient {

    Logger Log = Logger.getLogger(this.getClass());

    /*
	 * 服务器端口号
	 */
    private int port;

    /*
     * 服务器IP
     */
    private String host;

    public NettyTCPClient(int port, String host) throws InterruptedException {
        this.port = port;
        this.host = host;
    }

    private void start() throws InterruptedException {

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN,Integer.MAX_VALUE,0,4,2,4,true));
                    ch.pipeline().addLast(new NettyTCPHandler());

                }
            });
            // 发起异步连接操作
            ChannelFuture future = bootstrap.connect(host, port).sync();

            future.channel().closeFuture().sync();
        } catch (Exception e) {
            workerGroup.shutdownGracefully();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    private static void testRunnable()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NettyTCPClient client = null;
                try {
                    client = new NettyTCPClient(11111,"127.0.0.1");
//                    client = new NettyTCPClient(11111,"182.254.247.160");
//                    client = new NettyTCPClient(11111,"58.87.93.57");
                    client.start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


    public static void main(String[] args) throws InterruptedException {


        testRunnable();
        System.out.println("********************************************************");
//        testRunnable();


    }

}
