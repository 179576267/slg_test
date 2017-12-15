package com.douqu.game.main.msg;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.factory.CodeFactory;
import com.douqu.game.core.netty.NettyConnection;
import com.douqu.game.core.util.BufferUtils;
import com.douqu.game.core.util.Utils;
import io.netty.buffer.ByteBuf;
import org.apache.log4j.Logger;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-12-08 18:34
 */
public class RouteChannel {

    static Logger logger = Logger.getLogger(GMChannel.class);

    public static void messageChannel(NettyConnection connection, ByteBuf byteBuf)
    {
        int code = BufferUtils.readShort(byteBuf);

        logger.info("code: " + Integer.toHexString(code) + "-> data length: " + byteBuf.readableBytes());

        if(CodeFactory.ROUTE_GET_PATH == code)
        {
            ByteBuffer data = new ByteBuffer(Utils.byteBufToBytes(byteBuf));


        }

    }
}
