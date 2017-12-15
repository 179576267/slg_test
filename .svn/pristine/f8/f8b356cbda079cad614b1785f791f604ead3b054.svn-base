package com.douqu.game.main.server;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.database.model.GMModel;
import com.douqu.game.core.database.model.ServerModel;
import com.douqu.game.core.entity.ext.data.world.WorldArenaRankData;
import com.douqu.game.core.entity.ext.data.world.WorldOfficialRankData;
import com.douqu.game.core.entity.WorldInfo;
import com.douqu.game.core.controller.PlayerController;
import com.douqu.game.core.netty.NettyConnection;
import com.douqu.game.main.GameServer;
import com.douqu.game.main.service.WorldService;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by bean on 2017/7/27.
 */
public class WorldManager {

    Logger logger = Logger.getLogger(WorldManager.class);

    /**
     * 所有连接(包含未登录的)
     */
    private List<NettyConnection> connections = new CopyOnWriteArrayList<>();
    /**
     * 所有在线的GM
     */
    private List<NettyConnection> gmList = new CopyOnWriteArrayList<>();

    /**
     * 在线用户列表
     */
    private List<PlayerController> playerList = new CopyOnWriteArrayList<>();

    private Map<String,PlayerController> playerIndexMap = new ConcurrentHashMap<>();
    private Map<String,PlayerController> playerNameMap = new ConcurrentHashMap<>();
    private Map<Integer,PlayerController> playerUidMap = new ConcurrentHashMap<>();


    /**
     * 保存一些世界全局数据
     */
    private WorldInfo worldInfo = new WorldInfo();

    public void load()
    {
        //从数据库加载流水号，防止重复，加载的时候加100000
        WorldService worldService = SpringContext.getBean(WorldService.class);

        ServerModel serverModel = worldService.find();

        worldInfo.load(serverModel);

        if(serverModel.getWorldInfo() == null){
            worldInfo.init();
        }else {
            worldInfo.loadFrom(new ByteBuffer(serverModel.getWorldInfo()));
        }

    }

    public void update()
    {
        long currentTime = GameServer.currentTime;
        for(PlayerController playerController : playerList)
        {
            playerController.update(currentTime);

            if(playerController.isOverdue(currentTime))
            {
//                playerExitGame(playerController);
            }
        }
    }




//    public byte[] saveWorldInfo()
//    {
//        ByteBuffer buffer = new ByteBuffer();
//        worldInfo.writeTo(buffer);
//        return buffer.getBytes();
//    }



    public void playerModifyName(String beforeName, String newName, PlayerController playerController)
    {
        playerNameMap.remove(beforeName);
        playerNameMap.put(newName, playerController);
    }


    /**
     * 玩家进入游戏世界
     * @param playerController
     */
    public synchronized void playerEnterGame(PlayerController playerController)
    {
        if(getPlayerController(playerController.getObjectIndex()) == null)
        {
            playerList.add(playerController);

            playerIndexMap.put(playerController.getObjectIndex(), playerController);

            playerNameMap.put(playerController.getName(), playerController);

            playerUidMap.put(playerController.getPlayer().getUid(), playerController);
        }
    }


    public synchronized void playerExitGame(PlayerController playerController)
    {
        logger.info("玩家:" + playerController.getObjectIndex() + " -> " + playerController.getName() + " 离开游戏!");

        playerController.destroy();

        playerList.remove(playerController);

        playerIndexMap.remove(playerController.getObjectIndex());

        playerNameMap.remove(playerController.getName());

        playerUidMap.remove(playerController.getPlayer().getUid());
    }


    public void addGM(NettyConnection connection)
    {
        if(connection == null || !(connection.getObject() instanceof GMModel))
            return;

        gmList.add(connection);
    }

    public void delGM(NettyConnection connection)
    {
        gmList.remove(connection);
    }


    public PlayerController getPlayerController(String playerIndex){
        return playerIndexMap.get(playerIndex);
    }

    public PlayerController getPlayerControllerByName(String playerName){
        return playerNameMap.get(playerName);
    }

    public PlayerController getPlayerControllerByUid(int uid){
        return playerUidMap.get(uid);
    }

    public WorldInfo getWorldInfo() {
        return worldInfo;
    }

    public void setWorldInfo(WorldInfo worldInfo) {
        this.worldInfo = worldInfo;
    }

    public List<PlayerController> getPlayerList() {
        return playerList;
    }

    public int getPlayerCount()
    {
        return playerList.size();
    }

    public int getConnectionCount()
    {
        return playerList.size();
    }


    public WorldArenaRankData getWorldArenaData()
    {
        return worldInfo.getArenaRankInfo();
    }

    public WorldOfficialRankData getWorldOfficialData()
    {
        return worldInfo.getOfficialRankInfo();
    }


    public void addConnection(NettyConnection connection)
    {
        connections.add(connection);
    }

    public void removeConnection(NettyConnection connection)
    {
        connections.remove(connection);
    }

    public List<NettyConnection> getConnections() {
        return connections;
    }
}
