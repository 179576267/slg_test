package com.douqu.game.battle.controller.sprite;

import com.douqu.game.core.e.E_PlayerStatus;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.netty.NettyConnection;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.protobuf.SGMainProto;
import com.douqu.game.core.protobuf.SGSystemProto;
import org.apache.log4j.Logger;

/**
 * Created by bean on 2017/7/18.
 */
public class PlayerController extends SpriteController {

    Logger logger = Logger.getLogger(PlayerController.class);

    private NettyConnection tcpConnection;

    private Player player;

//    private PlayerBattleData battleData;


    public PlayerController(Player player, NettyConnection connection)
    {
        this.player = player;
        this.tcpConnection = connection;
        if(tcpConnection != null)
        {
            this.tcpConnection.setObject(this);
        }
        setStatus(E_PlayerStatus.FREE);
    }

    @Override
    public boolean isOnlinePlayer()
    {
        return tcpConnection != null;
    }

    @Override
    public void clearBattle()
    {
        setAttachment(null);
        setBattleController(null);
        setStatus(E_PlayerStatus.FREE);
//        battleData = null;
    }


    public void update(long currentMillis)
    {

    }

    /**
     * 发送消息
     * @param msgID
     * @param data
     */
    public void sendMsg(int msgID, byte[] data)
    {
        if(tcpConnection != null)
        {
            tcpConnection.sendMsg(msgID, data);

            logger.info(player.getName() + " 发送消息给客户端：code:" + SGMainProto.E_MSG_ID.forNumber(msgID));
        }
    }

    public void sendAlert(SGCommonProto.E_NOTIFY_TYPE notifyType, String content)
    {
        if(tcpConnection == null)
            return;

        SGSystemProto.S2C_NotifyAlert.Builder response = SGSystemProto.S2C_NotifyAlert.newBuilder();
        response.setType(notifyType);
        response.setContent(content==null?"":content);
        sendMsg(SGMainProto.E_MSG_ID.MsgID_System_NotifyAlert_VALUE, response.build().toByteArray());
    }

    public void destroy()
    {
        if(tcpConnection != null)
        {
            logger.info(player.getName() + " destroy!");

            tcpConnection.destroy();

            tcpConnection = null;
        }
    }




    @Override
    public String getObjectIndex()
    {
        return player.getObjectIndex();
    }

    @Override
    public String getName()
    {
        return player.getName();
    }

//    public void sendAlert(SGCommonProto.E_NOTIFY_TYPE notifyType)
//    {
//        if(tcpConnection != null)
//        {
//            SGSystemProto.S2C_NotifyAlert.Builder response = SGSystemProto.S2C_NotifyAlert.newBuilder();
//            response.setType(notifyType);
//            tcpConnection.sendMsg(SGMainProto.E_MSG_ID.MsgID_System_NotifyAlert_VALUE, response.build().toByteArray());
//        }
//    }





    public boolean isMatching()
    {
        return status == E_PlayerStatus.MATCHING;
    }


    public Player getPlayer() {
        return player;
    }



    public NettyConnection getTcpConnection() {
        return tcpConnection;
    }

    public void setTcpConnection(NettyConnection tcpConnection) {
        this.tcpConnection = tcpConnection;
    }



    @Override
    public String toString() {
        return "PlayerController{" +
                "player=" + player +
                ", tcpConnection=" + (tcpConnection!=null?tcpConnection.getChannel():null) +
                ", status=" + status +
                "} " + super.toString();
    }
}
