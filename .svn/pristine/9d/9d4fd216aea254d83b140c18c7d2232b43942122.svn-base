package com.douqu.game.main;

import com.douqu.game.core.e.E_ProfileVersion;
import com.douqu.game.core.i.IServer;
import com.douqu.game.main.server.LoadManager;
import com.douqu.game.main.server.SpringContext;
import com.douqu.game.main.server.WorldManager;
import com.douqu.game.main.server.netty.NettyGMServer;
import com.douqu.game.main.server.rmi.RMIServer;
import com.douqu.game.main.server.netty.NettyTCPServer;
import com.douqu.game.main.service.PlayerService;
import com.douqu.game.main.service.WorldService;
import org.apache.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Author : Bean
 * 2017-07-06 15:43
 */
public class GameServer {

    Logger logger = Logger.getLogger(GameServer.class);

    private IServer tcpServer;

    private IServer rmiServer;

    private IServer gmServer;

    private boolean isRunning;


    /**
     * 所有线程任务
     */
    private ScheduledExecutorService service = Executors.newScheduledThreadPool(20);

    private WorldManager worldManager;

    /**
     * 当前时间(毫秒)
     */
    public static long currentTime;

    private static GameServer instance = null;

    private GameServer()
    {
        worldManager = new WorldManager();
    }

    public static GameServer getInstance()
    {
        if(instance == null)
            instance = new GameServer();

        return instance;
    }

    public WorldManager getWorldManager()
    {
        return worldManager;
    }

    /**
     * 启动服务器
     */
    public void start()
    {
        SpringServer.start();

        String profile = SpringContext.getProfile();
        logger.info("Spring Profile -> " + profile + " -> " + E_ProfileVersion.forKey(profile));

        LoadManager.load();

        //加载服务器数据
        worldManager.load();

        isRunning = true;

        startThread(() -> {
            if(tcpServer == null)
            {
                tcpServer = new NettyTCPServer();
                tcpServer.start();
            }
        });

        startThread(() -> {
            if(rmiServer == null)
            {
                rmiServer = new RMIServer();
                rmiServer.start();
            }
        });

        startThread(() -> {
            if(gmServer == null)
            {
                gmServer = new NettyGMServer();
                gmServer.start();
            }
        });

    }


    /**
     * 停止服务器
     */
    public void stop()
    {
        //保存所有在线玩家数据
        logger.info("关闭主服务器时，保存服务器和所有在线玩家数据开始<---------->");
        WorldService worldService = SpringContext.getBean(WorldService.class);
        worldService.update(GameServer.getInstance().getWorldManager().getWorldInfo());

        PlayerService playerService = SpringContext.getBean(PlayerService.class);
        playerService.saveOnlinePlayer();
        logger.info("关闭主服务器时，保存服务器和所有在线玩家数据成功<---------->");

        if(tcpServer != null)
        {
            tcpServer.stop();
        }

        if(rmiServer != null)
        {
            rmiServer.stop();
        }

        if(gmServer != null)
        {
            gmServer.stop();
        }

        SpringContext.stop();

        isRunning = false;
    }


    public void startThread(Runnable runnable)
    {
        service.execute(runnable);
    }


    public int getServerId()
    {
        return worldManager.getWorldInfo().getServerId();
    }

//    /**
//     * 获取新的物品流水号
//     * @return
//     */
//    public String createGoodsIndex()
//    {
//        return worldManager.getWorldInfo().createGoodsIndex();
//    }

    /**
     * 获取新的玩家流水号
     * @return
     */
    public String createPlayerIndex(int userId)
    {
        return worldManager.getWorldInfo().createPlayerIndex(userId);
    }

    public String createBattleId()
    {
        return worldManager.getWorldInfo().createBattleId();
    }


    public boolean isRunning() {
        return isRunning;
    }
}
