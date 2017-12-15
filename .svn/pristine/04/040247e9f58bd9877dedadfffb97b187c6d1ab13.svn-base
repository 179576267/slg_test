package com.douqu.game.main.client;


import com.douqu.game.core.protobuf.*;
import com.douqu.game.core.util.BufferUtils;
import com.douqu.game.core.util.SendUtils;
import com.douqu.game.core.util.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

/**
* Created by Administrator on 2016/11/9.
*/
public class NettyTCPHandler extends ChannelInboundHandlerAdapter {

    Logger logger = Logger.getLogger(this.getClass());

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        int code = BufferUtils.readShort(byteBuf);

        logger.info("data length:" + byteBuf.readableBytes());
        logger.info("code:"+code);

        try{
            byte[] data = Utils.byteBufToBytes(byteBuf);

            switch (code) {
               /* case  SGMainProto.E_MSG_ID.MsgID_System_NotifyAlert_VALUE:
                    SGSystemProto.S2C_NotifyAlert res = SGSystemProto.S2C_NotifyAlert.parseFrom(data);
                    logger.info("error:"+ res.getType());
                    break;
                case  SGMainProto.E_MSG_ID.MsgID_System_SuperLogin_VALUE:
//                    SGBagProto.C2S_CardLevelUp.Builder b = SGBagProto.C2S_CardLevelUp.newBuilder();
//                    b.setSid(1);
                    SendUtils.sendMsg(ctx.channel(), SGMainProto.E_MSG_ID.MsgID_Bag_MyCard_VALUE, null);
                    break;
//                case  SGMainProto.E_MSG_ID.MsgID_Bag_CardLevelUp_VALUE://升级
//                    SGBagProto.S2C_CardLevelUp r = SGBagProto.S2C_CardLevelUp.parseFrom(data);

//                    logger.info("升级成功返回数据：" + r.toString());
//                    SendUtils.sendMsg(ctx.channel(), SGMainProto.E_MSG_ID.MsgID_Bag_MyCard_VALUE, null);
//                    break;
                case  SGMainProto.E_MSG_ID.MsgID_Bag_MyCardTeam_VALUE://我的背包

                    SGBagProto.S2C_MyCardList r = SGBagProto.S2C_MyCardList.parseFrom(data);

                    logger.info("我的背包列表：" + r.toString() );
                    break;*/

                case  SGMainProto.E_MSG_ID.MsgID_Pub_LotteryInit_VALUE://我的背包

                    SGPlayerProto.S2C_LotteryInit rs = SGPlayerProto.S2C_LotteryInit.parseFrom(data);
                    logger.info("酒馆初始化：" + rs.toString() );
                    break;

                case  SGMainProto.E_MSG_ID.MsgID_Pub_LotteryClick_VALUE://我的背包

                    SGPlayerProto.S2C_LotteryClick s = SGPlayerProto.S2C_LotteryClick.parseFrom(data);
                    logger.info("抽奖：" + s.toString() );
                    break;

            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();// 将消息发送队列中的消息写入到SocketChannel中发送给对方。
        logger.info("flush");
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        super.channelActive(ctx);


        SGSystemProto.C2S_Login.Builder data = SGSystemProto.C2S_Login.newBuilder();
        data.setChannel(SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_QUICK);
        data.setAccount("2tj8eg");
        data.setPassword("123456");
        data.setNormal(true);
        SendUtils.sendMsg(ctx.channel(), SGMainProto.E_MSG_ID.MsgID_System_Login_VALUE, data.build().toByteArray());


//        SendUtils.sendMsg(ctx.channel(), SGMainProto.E_MSG_ID.MsgID_System_SuperLogin_VALUE, null);



        //升级
//        SGBagProto.C2S_CardLevelUp.Builder b = SGBagProto.C2S_CardLevelUp.newBuilder();
//        b.setSid(1);
//
//        SendUtils.sendMsg(ctx.channel(), SGMainProto.E_MSG_ID.MsgID_Bag_CardLevelUp_VALUE, null);



        SGPlayerProto.C2S_LotteryClick.Builder c = SGPlayerProto.C2S_LotteryClick.newBuilder();
        c.setType(1);
        c.setBuyType(1);
//        SendUtils.sendMsg(ctx.channel(), SGMainProto.E_MSG_ID.MsgID_Pub_LotteryClick_VALUE, c.build().toByteArray());

        logger.info("客户端连接成功");
        SendUtils.sendMsg(ctx.channel(), SGMainProto.E_MSG_ID.MsgID_Pub_LotteryInit_VALUE, null);



//        b.setSid(1);
//




    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        super.channelInactive(ctx);

        logger.info("客户端断开连接");

        ctx.close();

    }


}