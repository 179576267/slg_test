package com.douqu.game.main.server;


import com.douqu.game.core.e.E_ServerType;
import com.douqu.game.core.factory.CodeFactory;
import com.douqu.game.core.factory.ConfigFactory;
import com.douqu.game.core.netty.NettyConnection;
import com.douqu.game.core.util.SendUtils;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Author : Bean
 * 2017-06-19 16:59
 */
public class ServerManager {

    Logger logger = Logger.getLogger(this.getClass());

    public List<ServerInfo> battleServerList = new CopyOnWriteArrayList<>();

    public Map<Integer, ServerInfo> battleServerMap = new ConcurrentHashMap<>();

    private static ServerManager instance = null;
    private ServerManager(){}

    public static ServerManager getInstance()
    {
        if(instance == null)
            instance = new ServerManager();

        return instance;
    }


    public int getBattleServerPort()
    {
        return ConfigFactory.BATTLE_SERVER_PORT_START++;
    }


    public void addServerInfo(E_ServerType serverType, NettyConnection connection, int port)
    {
        connection.setServerType(serverType);
        ServerInfo serverInfo = new ServerInfo(serverType, connection, port);

        logger.info("添加新的分支服务器:"+serverType+" ip:"+connection);

        if(serverType == E_ServerType.BATTLE)
        {
            if(battleServerList.contains(serverInfo))
                return;

            battleServerList.add(serverInfo);
            battleServerMap.put(serverInfo.getPort(), serverInfo);

            logger.info("添加新的分支服务器成功:" + serverType + " ip:" + serverInfo.getIp());
        }

    }

    public void removeServerInfo(NettyConnection connection)
    {
        if(connection.getServerType() == E_ServerType.BATTLE)
        {
            for(ServerInfo serverInfo : battleServerList)
            {
                if(serverInfo.getConnection().getId() == connection.getId())
                {
                    logger.info("战斗服务器断开连接:"+serverInfo);
                    serverInfo.destroy();
                    battleServerList.remove(serverInfo);
                    battleServerMap.remove(serverInfo.getPort());
                    break;
                }
            }
        }
    }

    public ServerInfo getServerInfo(NettyConnection connection)
    {
        if(connection.getServerType() == E_ServerType.BATTLE)
        {
            for(ServerInfo serverInfo : battleServerList)
            {
                if(serverInfo.getConnection().getId() == connection.getId())
                {
                    return serverInfo;
                }
            }
        }

        return null;
    }

    public ServerInfo getServerInfo(int port)
    {
        return battleServerMap.get(port);
    }


    public ServerInfo getFreeServer(E_ServerType serverType)
    {
        ServerInfo result = null;
        if(serverType == E_ServerType.BATTLE)
        {
            if(battleServerList != null && battleServerList.size() > 0){
                result = Collections.min(battleServerList, new Comparator<ServerInfo>() {
                    @Override
                    public int compare(ServerInfo o1, ServerInfo o2) {
                        return o1.getBattleCount() - o2.getBattleCount();
                    }
                });
            }

        }

        logger.info("获取空闲战斗服务器:" + result);

        return result;
    }


    /**
     * 告诉战斗服务器配置文件重新加载了
     */
   public void sendLoadData()
   {
       for(ServerInfo serverInfo : battleServerList)
       {
           serverInfo.sendMsg(CodeFactory.LOAD_DATA, null);
       }
   }
}
