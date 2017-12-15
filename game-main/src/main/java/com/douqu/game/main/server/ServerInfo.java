package com.douqu.game.main.server;


import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.controller.PlayerController;
import com.douqu.game.core.e.E_ServerType;
import com.douqu.game.core.entity.battle.BattleDetail;
import com.douqu.game.core.factory.CodeFactory;
import com.douqu.game.core.factory.ConfigFactory;
import com.douqu.game.core.netty.NettyConnection;
import com.douqu.game.core.util.SendUtils;
import com.douqu.game.core.util.Utils;
import com.douqu.game.main.GameServer;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Author : Bean
 * 2017-06-19 17:07
 */
public class ServerInfo {

    private NettyConnection connection;

    private String ip;

    private int port;

    /**
     * 是否已经启动
     */
    private boolean isRunning;

    private E_ServerType serverType;


    private List<BattleDetail> battleList = new CopyOnWriteArrayList<>();

    private Map<String, BattleDetail> battleMap = new ConcurrentHashMap<>();


    public ServerInfo(E_ServerType serverType, NettyConnection connection, int port)
    {
        this.serverType = serverType;
        this.connection = connection;
        InetSocketAddress address = ((InetSocketAddress) connection.getChannel().remoteAddress());
        this.ip = address.getAddress().getHostAddress();
        if(ip.indexOf("127.0.0.1") != -1 || ip.indexOf("localhost") != -1)
        {
            this.ip = Utils.getInternetIp();
            System.out.println("IP:" + ip);
            if(ip.indexOf("127.0.0.1") != -1 || ip.indexOf("localhost") != -1)
                this.ip = ConfigFactory.SERVER_IP;
        }
        if("192.168.46.1".equals(ip)){
            this.ip = "192.168.2.202";
        }
        this.port = port;
        this.isRunning = false;
    }



    public void destroy()
    {
        WorldManager worldManager = GameServer.getInstance().getWorldManager();
        PlayerController playerController = null;
        for(BattleDetail info : battleList)
        {
            playerController = worldManager.getPlayerController(info.getTeam1Info().getIndexInfo());
            if(playerController != null)
            {
                playerController.clearBattle();
            }
            playerController = worldManager.getPlayerController(info.getTeam2Info().getIndexInfo());
            if(playerController != null)
            {
                playerController.clearBattle();
            }
        }

    }
    /**
     * 发消息
     * @param code
     * @param data
     */
    public void sendMsg(int code, byte[] data)
    {
        SendUtils.sendMsg(connection.getChannel(), code, data);
    }

    /**
     * 向战斗服务器发送战斗创建消息
     * @param battleId
     * @param battleType
     * @param playerIndex
     * @param targetIndex
     */
    public void sendBattleInit(String battleId, int battleType, String playerIndex, String targetIndex)
    {
        if(connection == null)
            return;

        ByteBuffer buffer = new ByteBuffer();
        buffer.writeUTF(battleId);
        buffer.writeByte(battleType);
        buffer.writeUTF(playerIndex);
        buffer.writeUTF(targetIndex);
        SendUtils.sendMsg(connection.getChannel(), CodeFactory.BATTLE_INIT, buffer.getBytes());

        System.out.println("通知战斗服务器创建战斗了 -> connection.id:" + connection.getId() + connection.getChannel().remoteAddress());
    }



    public void sendServerStop()
    {
        SendUtils.sendMsg(connection.getChannel(), CodeFactory.STOP_SERVER, null);
    }



    public int getId()
    {
        return port;
    }


//    public void addBattleCount(int value)
//    {
//        battleCount += value;
//        battleCount = battleCount < 0 ? 0 : battleCount;
//    }

    public void addBattle(BattleDetail battle)
    {
        if(battleMap.get(battle.getId()) == null)
        {
            battleMap.put(battle.getId(), battle);
            battleList.add(battle);
        }
    }

    public void removeBattle(BattleDetail battle)
    {
        battleMap.remove(battle.getId());
        for(BattleDetail battleData : battleList)
        {
            if(battleData.getId() == battle.getId())
            {
                battleList.remove(battleData);
                break;
            }
        }

    }

    public BattleDetail getBattleController(String battleId)
    {
        return battleMap.get(battleId);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public int getBattleCount() {
        return battleList.size();
    }
//
//    public void setBattleCount(int battleCount) {
//        this.battleCount = battleCount;
//    }

    public NettyConnection getConnection() {
        return connection;
    }

    public void setConnection(NettyConnection connection) {
        this.connection = connection;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public E_ServerType getServerType() {
        return serverType;
    }

    public void setServerType(E_ServerType serverType) {
        this.serverType = serverType;
    }


    @Override
    public String toString() {
        return "ServerInfo{" +
                "battleCount=" + battleList.size() +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", serverType=" + serverType +
                '}';
    }
}
