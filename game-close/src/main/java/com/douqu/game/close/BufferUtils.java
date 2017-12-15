package com.douqu.game.close;

import io.netty.buffer.ByteBuf;

import java.nio.ByteOrder;

/**
 * Created by bean
 * 2017-03-21 22:20.
 */
public class BufferUtils {

    public static void writeInt(ByteBuf buffer, int value)
    {
        if(ByteOrder.nativeOrder().toString().equals(ByteOrder.BIG_ENDIAN.toString())){
            buffer.writeInt(value);
        }else{
            buffer.writeIntLE(value);
        }
    }

    public static void writeShort(ByteBuf buffer, short value)
    {
        if(ByteOrder.nativeOrder().toString().equals(ByteOrder.BIG_ENDIAN.toString())){
            buffer.writeShort(value);
        }else{
            buffer.writeShortLE(value);
        }
    }

    public static int readInt(ByteBuf buffer)
    {
        if(ByteOrder.nativeOrder().toString().equals(ByteOrder.BIG_ENDIAN.toString())){
            return buffer.readInt();
        }else{
            return buffer.readIntLE();
        }
    }

    public static short readShort(ByteBuf buffer)
    {
        if(ByteOrder.nativeOrder().toString().equals(ByteOrder.BIG_ENDIAN.toString())){
            return buffer.readShort();
        }else{
            return buffer.readShortLE();
        }
    }

    public static long readLong(ByteBuf buffer)
    {
        if(ByteOrder.nativeOrder().toString().equals(ByteOrder.BIG_ENDIAN.toString())){
            return buffer.readLong();
        }else{
            return buffer.readLongLE();
        }
    }


}
