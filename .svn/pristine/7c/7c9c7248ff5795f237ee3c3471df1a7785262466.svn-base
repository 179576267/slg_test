package com.douqu.game.close;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by Administrator on 2016/11/9.
 */
public class Handler extends ChannelInboundHandlerAdapter {

    private String sourcePassword;

    public Handler(String sourcePassword)
    {
        this.sourcePassword = sourcePassword;
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
//        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        int code = BufferUtils.readShort(byteBuf);

        System.out.println("code: " + code + " data length: " + byteBuf.readableBytes());

        byte[] data = Utils.byteBufToBytes(byteBuf);
        switch (code){
            case CodeFactory.CLOSE_MAIN_SERVER:
                System.out.println("收到关闭主服务器消息回包 -> ");
                ByteBuffer buffer = new ByteBuffer(data);
                boolean result = buffer.readBoolean();
                System.out.println("结果:" + result);
                System.out.println("备注:" + buffer.readUTF());

                if(!result)
                {
                    System.out.println("发生错误，请重新运行关闭程序!");
                    System.exit(0);
                }
                break;
        }


    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();// 将消息发送队列中的消息写入到SocketChannel中发送给对方。
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception
    {
        ByteBuffer buffer = new ByteBuffer();
        buffer.writeUTF("bean");
        String password = MD5Utils.encodeUppercase(sourcePassword);
        System.out.println("发送关闭服务器的密码 -> sourcePassword:" + sourcePassword);
        System.out.println("发送关闭服务器的密码 -> password:" + password);
        buffer.writeUTF(password);

        SendUtils.sendMsg(ctx.channel(), CodeFactory.CLOSE_MAIN_SERVER, buffer.getBytes());

        System.out.println("连接成功，发送关闭服务器请求!");
    }



    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception
    {
        System.out.println("服务器已关闭，我也要关服了！");
        ctx.close();
        System.exit(0);
    }


}