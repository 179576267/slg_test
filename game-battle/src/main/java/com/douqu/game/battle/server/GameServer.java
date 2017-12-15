package com.douqu.game.battle.server;


import com.douqu.game.battle.server.netty.NettyRouteClient;
import com.douqu.game.battle.server.netty.NettyRouteServer;
import com.douqu.game.battle.server.netty.NettyTCPClient;
import com.douqu.game.battle.server.netty.NettyTCPServer;
import com.douqu.game.core.factory.CodeFactory;
import com.douqu.game.core.factory.ConfigFactory;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.i.IMainServer;
import com.douqu.game.core.i.IServer;
import com.douqu.game.core.util.SendUtils;
import io.netty.channel.Channel;
import org.apache.log4j.Logger;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Author : Bean
 * 2017-06-19 18:39
 */
public class GameServer {

    Logger logger = Logger.getLogger(GameServer.class);

//    public static boolean TEST = true;
//
    public static boolean ROUTE_TO_CLIENT = false;

    /**
     * 服务器连接
     */
    private IServer tcpServer;

    /**
     * 寻路服务器连接
     */
    private IServer routeServer;

    /**
     * 连接主服务器的
     */
    private NettyTCPClient tcpClient;

    /**
     * 寻路服务器客户
     */
    private NettyRouteClient routeClient;

    /**
     * RMI方式连接主服务器
     */
    private IMainServer iMainServer;

    /**
     * 连接主服务器的通道
     */
    private Channel serverChannel;

    /**
     * 连接寻路服务器的通道
     */
    private Channel routeChannel;

    private int tcpPort;

    private int routePort;

    public static boolean isPrint = false;

    public static long currentTime;

    /**
     * 所有线程任务
     */
    private ScheduledExecutorService service = Executors.newScheduledThreadPool(10);

    private WorldManager worldManager;


    private static GameServer instance = null;

    private GameServer() {
        worldManager = new WorldManager();
    }

    public static GameServer getInstance()
    {
        if(instance == null)
            instance = new GameServer();

        return instance;
    }

    public void start()
    {
        startThread(() -> {
            startTCPClient();
        });
    }

    public void startThread(Runnable runnable)
    {
        service.execute(runnable);
    }



    public void startTCPClient()
    {
        if(tcpClient == null)
        {
            tcpClient = new NettyTCPClient();
            tcpClient.start();

            //发心跳包,25秒一次,延迟2秒执行,服务器是30秒检测一次
            service.scheduleAtFixedRate(() -> {
                try{
                    SendUtils.sendMsg(serverChannel, CodeFactory.PING, null);
                }catch (Exception e){
                    logger.info("线程异常:" + e.getLocalizedMessage());
                    logger.info("线程异常:" + e.getMessage());
                }
            }, 2, 25, TimeUnit.SECONDS);

            service.scheduleAtFixedRate(() -> {

                try{
                    currentTime = System.currentTimeMillis();

                    GameServer.getInstance().getWorldManager().update(currentTime);

                }catch (Exception e){
                        e.printStackTrace();
//                    logger.info("线程异常:" + e.getLocalizedMessage());
//                    logger.info("线程异常:" + e.getMessage());
                }

            }, 3, ConstantFactory.UPDATE_BATTLE_TIME, TimeUnit.MILLISECONDS);

        }
    }

    public void startRouteClient()
    {
        if(routeClient == null)
        {
            startThread(() -> {
                routeClient = new NettyRouteClient();
                routeClient.start();
            });
        }
    }


    public void startBattleTCP(int port)
    {
        this.tcpPort = port;

        if(tcpServer == null)
        {
            startThread(() -> {
                tcpServer = new NettyTCPServer();
                tcpServer.start();
            });
        }
    }

    public void startRouteServer(int port)
    {
        this.routePort = port;

        if(routeServer == null)
        {
            startThread(() -> {
                routeServer = new NettyRouteServer();
                routeServer.start();
            });
        }
    }

    public void startRMI()
    {
        try {
            //在RMI服务注册表中查找名称为X的对象，并调用其上的方法
            String url = MessageFormat.format(ConfigFactory.RMI_SERVER_URL, ConfigFactory.RMI_SEVER_HOST, ConfigFactory.RMI_SEVER_PORT+"", ConfigFactory.RMI_SERVER_NAME);

            System.out.println("连接RMI:" + url);

            iMainServer = (IMainServer) Naming.lookup(url);

            System.out.println("RMI 链接成功: " + url);

            DataFactory.getInstance().set(iMainServer.getDataFactory());

        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }



    public void stop()
    {
        service.shutdown();


        if(tcpClient != null)
        {
            tcpClient.stop();
            tcpClient = null;
        }
        if(tcpServer != null)
        {
            tcpServer.stop();
            tcpServer = null;
        }
        if(routeClient != null)
        {
            routeClient.stop();
            routeClient = null;
        }
        if(routeServer != null)
        {
            routeServer.stop();
            routeServer = null;
        }

        System.exit(0);
    }

//    public void addPlayerInfo(PlayerInfo playerInfo)
//    {
//        playerInfoMap.put(playerInfo.getId(), playerInfo);
//    }
//
//    public PlayerInfo getPlayerInfo(init id)
//    {
//        return playerInfoMap.get(id);
//    }



    public boolean isRunning()
    {
        return tcpServer != null;
    }

    public int getId()
    {
        return tcpPort;
    }


    public Channel getServerChannel() {
        return serverChannel;
    }

    public void setServerChannel(Channel serverChannel) {
        this.serverChannel = serverChannel;
    }

    public Channel getRouteChannel() {
        return routeChannel;
    }

    public void setRouteChannel(Channel routeChannel) {
        this.routeChannel = routeChannel;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }

    public int getRoutePort() {
        return routePort;
    }

    public void setRoutePort(int routePort) {
        this.routePort = routePort;
    }



    public WorldManager getWorldManager() {
        return worldManager;
    }

    public IMainServer getiMainServer() {
        return iMainServer;
    }


}
