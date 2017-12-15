package com.douqu.game.core.controller;

import com.douqu.game.core.e.E_PlayerStatus;
import com.douqu.game.core.entity.battle.PlayerBattleData;
import com.douqu.game.core.entity.ext.BoonInfo;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.netty.NettyConnection;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.protobuf.SGMainProto;
import com.douqu.game.core.util.SendUtils;
import org.apache.log4j.Logger;

/**
* Created by bean on 2017/7/18.
*/
public class PlayerController {

    Logger logger = Logger.getLogger(this.getClass());

    private NettyConnection tcpConnection;

    private Player player;

    private PlayerBattleData battleData;

    /**
     * 当前状态
     */
    private E_PlayerStatus status;

    public PlayerController(Player player, NettyConnection connection)
    {
        this.player = player;
        this.tcpConnection = connection;
        if(tcpConnection != null)
            this.tcpConnection.setObject(this);

        setStatus(E_PlayerStatus.FREE);
    }

    public void clearBattle()
    {
//        setAttachment(null);
//        setParent(null);
        setStatus(E_PlayerStatus.FREE);
        battleData = null;
    }

    public boolean isOverdue(long currentTime)
    {
        if(tcpConnection == null)
            return false;

        return tcpConnection.getLastUpdateTime() > 0 && currentTime - tcpConnection.getLastUpdateTime() >= ConstantFactory.PLAYER_OVERDUE_TIME;
    }

    /**
     * 是否在战斗中
     * @return
     */
    public boolean isBattling()
    {
        return status == E_PlayerStatus.BATTLING || status == E_PlayerStatus.BATTLE_WAIT_START || status == E_PlayerStatus.MATCH_WAIT_BATTLE;
    }

    public boolean isFree()
    {
        return status == E_PlayerStatus.FREE || status == E_PlayerStatus.MATCHING;
    }


    public void update(long currentMillis)
    {
        if(player.lock)
            return;

        BoonInfo info = player.getExtInfo(BoonInfo.class);
        if(info.getLotteryData().hasRedPointRemind(true, currentMillis)){
            SendUtils.sendRedPointRemind(this, SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_LOTTERY);
        }
    }

    public void setLastUpdateTime(long currentMillis)
    {
        if(tcpConnection != null)
        {
            tcpConnection.setLastUpdateTime(currentMillis);
        }
    }


//    /**
//     * 发送有改变的物品
//     */
//    public void sendChangeData(int code)
//    {
//        SendUtils.sendFlushData(code, this);
//
//        player.getChangeData().clear();
//    }

    public void updateFC()
    {
        //修改战斗力
        int beforeFC = player.fc;

        int afterFC = player.updateFC();

        player.addChangeGoods(SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_OTHER, SGCommonProto.E_GOODS_OTHER_ID.GOODS_OTHER_ID_FC_VALUE, afterFC - beforeFC, afterFC, true);
//        发送flushData消息告诉客户端战力更新
//        SendUtils.sendFlushData(this, SGCommonProto.E_GOODS_OTHER_ID.GOODS_OTHER_ID_FC_VALUE, afterFC - beforeFC, afterFC, SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_OTHER);
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

            logger.debug(player.getName() + " 发送消息给客户端：code:"+ SGMainProto.E_MSG_ID.forNumber(msgID));
        }
    }

    public void sendAlert(SGCommonProto.E_NOTIFY_TYPE notifyType, String content)
    {
        if(tcpConnection == null)
            return;

        tcpConnection.sendAlert(notifyType, content);
    }

    public void destroy()
    {
        if(tcpConnection != null)
        {
            tcpConnection.destroy();

            tcpConnection = null;
        }
    }

    /**
     * 服务器主动断开
     */
    public void serverDestroy()
    {
        logger.info("服务器主动断开连接:" + tcpConnection);
        if(tcpConnection != null)
        {
            tcpConnection.setServerDestroy(true);

            destroy();
        }
    }


    public String getObjectIndex()
    {
        return player.getObjectIndex();
    }

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


    public E_PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(E_PlayerStatus status) {
        this.status = status;
    }


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


    public void setPlayer(Player player) {
        this.player = player;
    }

    public PlayerBattleData getBattleData() {
        return battleData;
    }

    public void setBattleData(PlayerBattleData battleData) {
        this.battleData = battleData;
    }



    public String getIp()
    {
        return tcpConnection == null ? "" : tcpConnection.getIp();
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerController)) return false;

        PlayerController that = (PlayerController) o;

        if (!player.getObjectIndex().equals(that.player.getObjectIndex())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return player.getObjectIndex().hashCode();
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
