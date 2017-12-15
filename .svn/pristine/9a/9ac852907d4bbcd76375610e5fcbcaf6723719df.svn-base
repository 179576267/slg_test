package com.douqu.game.close;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.nio.ByteOrder;

/**
 * Created by bean
 * 2017-03-24 15:54.
 */
public class SendUtils {

    public static void sendMsg(Channel channel, int msgID, byte[] data)
    {
        ByteBuf byteBuf = Unpooled.buffer();
        int length = data == null ? 0 : data.length;
        if(ByteOrder.nativeOrder().toString().equals(ByteOrder.LITTLE_ENDIAN.toString())){
            byteBuf.writeIntLE(length);//总包长
            byteBuf.writeShortLE(msgID);//长度为2
        }else{
            byteBuf.writeInt(length);//总包长
            byteBuf.writeShort(msgID);//长度为2
        }
        if(data != null)
            byteBuf.writeBytes(data);

        channel.writeAndFlush(byteBuf);
    }




}
