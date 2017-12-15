package com.douqu.game.core.util;

import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.controller.PlayerController;
import com.douqu.game.core.e.ReturnMessage;
import com.douqu.game.core.entity.ChangeGoods;
import com.douqu.game.core.netty.NettyConnection;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.protobuf.SGMainProto;
import com.douqu.game.core.protobuf.SGPlayerProto;
import com.douqu.game.core.protobuf.SGSystemProto;
import com.douqu.game.core.web.response.BaseResponseDto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.apache.log4j.Logger;

import java.nio.ByteOrder;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by bean
 * 2017-03-24 15:54.
 */
public class SendUtils {

    static Logger logger = Logger.getLogger(SendUtils.class);



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

    public static void sendAlert(PlayerController playerController, SGCommonProto.E_NOTIFY_TYPE notifyResult)
    {
        if(playerController == null)
            return;

        SGSystemProto.S2C_NotifyAlert.Builder response = SGSystemProto.S2C_NotifyAlert.newBuilder();
        response.setType(notifyResult);
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_System_NotifyAlert_VALUE, response.build().toByteArray());
    }



    public static BaseResponseDto createMsg(){

        return new BaseResponseDto(ReturnMessage.SUCCESS);
    }

    public static <T> BaseResponseDto createMsg(T data){

        return new BaseResponseDto(ReturnMessage.SUCCESS, data);
    }

    public static BaseResponseDto createMsg(ReturnMessage error){

        return new BaseResponseDto(error);
    }


//    public static SGCommonProto.GoodsGet createGoodsGet(SGCommonProto.E_GOODS_TYPE type, int id, int change, int curValue)
//    {
//        SGCommonProto.GoodsGet.Builder goodsGet = SGCommonProto.GoodsGet.newBuilder();
//        goodsGet.setType(type);
//        goodsGet.setId(id);
//        goodsGet.setCount(change);
//        goodsGet.setTotal(curValue);
//        return goodsGet.build();
//    }


    public static SGCommonProto.FlushData createFlushData(SGCommonProto.E_GOODS_TYPE type, int id, int change, int curValue)
    {
        SGCommonProto.FlushData.Builder flushData = SGCommonProto.FlushData.newBuilder();
        flushData.setType(type);
        flushData.setId(id);
        flushData.setChange(change);
        flushData.setCurValue(curValue);
        return flushData.build();
    }



    /**
     * 推送
     * @param playerController
     */
    public static void sendChangeData(int code, PlayerController playerController)
    {
        ChangeGoods changeGoods = playerController.getPlayer().getChangeGoods();
        if(changeGoods.getShowList().size() > 0 || changeGoods.getOtherList().size() > 0)
        {
            SGPlayerProto.S2C_FlushData.Builder flushDataRes = SGPlayerProto.S2C_FlushData.newBuilder();
            flushDataRes.setMsgId(SGMainProto.E_MSG_ID.forNumber(code));
            flushDataRes.addAllShowData(changeGoods.getShowList());
            flushDataRes.addAllOtherData(changeGoods.getOtherList());
            playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Player_FlushData_VALUE, flushDataRes.build().toByteArray());

            logger.info("sendFlushData--->>" + flushDataRes.toString());

            changeGoods.clear();
        }

//        if(changeGoods.getFlushDataList().size() > 0)
//        {
//            SGPlayerProto.S2C_FlushData.Builder flushDataRes = SGPlayerProto.S2C_FlushData.newBuilder();
//            flushDataRes.setMsgId(SGMainProto.E_MSG_ID.forNumber(code));
//            flushDataRes.addAllData(changeGoods.getFlushDataList());
//            playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Player_FlushData_VALUE, flushDataRes.build().toByteArray());
//
//            logger.info("sendFlushData--->>" + flushDataRes.toString());
//        }


    }


//    public static void sendFlushData(PlayerController playerController, int id, int change, int curValue, SGCommonProto.E_GOODS_TYPE dataType){
//        SGPlayerProto.S2C_FlushData.Builder response = SGPlayerProto.S2C_FlushData.newBuilder();
//        SGCommonProto.FlushData.Builder flushData = SGCommonProto.FlushData.newBuilder();
//        flushData.setType(dataType);
//        flushData.setId(id);
//        flushData.setChange(change);
//        flushData.setCurValue(curValue);
//        response.addData(flushData);
//        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Player_FlushData_VALUE, response.build().toByteArray());
//    }





    public static void sendRedPointRemind(PlayerController playerController, SGCommonProto.E_RED_POINT_TYPE type) {
        SGPlayerProto.S2C_RedPointRemind.Builder response = SGPlayerProto.S2C_RedPointRemind.newBuilder();
        if (type != null ) {
            response.addTypes(type);
            playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Player_RedPointRemind_VALUE, response.build().toByteArray());
        }
    }

    public static void sendRedPointRemind(PlayerController playerController, List<SGCommonProto.E_RED_POINT_TYPE> types){
        SGPlayerProto.S2C_RedPointRemind.Builder response = SGPlayerProto.S2C_RedPointRemind.newBuilder();
        if(types != null && types.size() > 0){
            response.addAllTypes(types);
            playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Player_RedPointRemind_VALUE, response.build().toByteArray());
        }else {
//            switch (e_red_point_type.getNumber()){
//                case SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_MASTER_VALUE:
//                    SettingInfo settingInfo = playerController.getPlayer().getExtInfo(SettingInfo.class);
//                    if(settingInfo.hasNewUnlockSkill()){
//                        response.addTypes(SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_MASTER);
//                    }
//                    break;
//                case SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_LOTTERY_VALUE:
//                    break;
//                case SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_HERO_VALUE:
//                    break;
//                case SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_INSTANCE_VALUE:
//                    break;
//                case SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_OFFICIAL_RANK_VALUE:
//                    break;
//                case SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_ARENA_VALUE:
//                    break;
//                case SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_TASK_VALUE:
//                    break;
//                case SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_ALTAR_VALUE:
//                    break;
//                case SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_GOBLIN_STORE_VALUE:
//                    break;
//                case SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_FIRST_RECHARGE_VALUE:
//                    break;
//                case SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_EMAIL_VALUE:
//                    break;
//                default:
//                    break;
//            }
        }

    }

}
