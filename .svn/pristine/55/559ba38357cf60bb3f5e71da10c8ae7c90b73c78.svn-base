package com.douqu.game.main.server;

import com.douqu.game.core.controller.PlayerController;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.util.Utils;
import com.douqu.game.main.GameServer;
import com.douqu.game.main.service.PlayerService;
import com.douqu.game.main.service.WorldService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

@Component
public class ScheduledTask {

    Logger logger = Logger.getLogger(this.getClass());

    /**
     * 每次批量更新的玩家数量
     */
    public final static int BATCH_UPDATE_COUNT = 1000;

    /**
     * 定时保存玩家数据的间隔时间
     * 4分钟
     */
    private final static int SAVE_PLAYER_TIME = 4 * 60 * 1000;

    /**
     * 定时检测是否有配置文件更新(后期这个功能移植到GM工具里面)
     */
    private final static int CONFIG_CHECK_TIME = 3 * 60 * 1000;

    /**
     * 更新时间
     */
    private final static int UPDATE_TIME = 500;

    /**
     * 匹配池任务
     */
    public final static int UPDATE_MATCH_TIME = 1000;

    /**
     * 每天凌晨4点更新所有玩家的段位
     */
    private final static String UPDATE_PLAYER_GRADE_TIME = "0 0 4 * * ?";


    @Autowired
    private PlayerService playerService;

    @Autowired
    private WorldService worldService;

    @Scheduled(fixedRate = SAVE_PLAYER_TIME, initialDelay  = 3000)//延迟3秒执行
    public void savePlayer() throws InterruptedException
    {
        worldService.update(GameServer.getInstance().getWorldManager().getWorldInfo());

        playerService.saveOnlinePlayer();
    }

    @Scheduled(fixedRate = CONFIG_CHECK_TIME, initialDelay  = 300000)//延迟5分钟执行
    public void configCheck() throws InterruptedException
    {
        InputStream input = null;
        try{
            String filePath = "jspatch.properties";
            File file = new File(Utils.getBasePath() + filePath);
            if(!file.exists())
                file = new File(Utils.getRootPath() + filePath);

            if(file.exists())
            {
//                logger.info("用文件目录的方式加载 -> " + filePath);
                input = new BufferedInputStream(new FileInputStream(file));
            }
            else
            {
//                logger.info("用文件流的方式加载 -> " + filePath);
                input = ScheduledTask.class.getClassLoader().getResourceAsStream(filePath);
            }

            if(input != null)
            {
                Properties properties = new Properties();
                properties.load(input);

                int data_version = Integer.parseInt(properties.get("data_version").toString().trim());
//                logger.info("文件里面的版本:" + data_version + "  本地版本:" + DataFactory.DATA_VERSION);
                if(data_version > DataFactory.DATA_VERSION)
                {
                    //重新加载配置
                    logger.info("有配置文件更新,开始重新加载!");
                    LoadManager.loadData();
                    ServerManager.getInstance().sendLoadData();

                    DataFactory.DATA_VERSION = data_version;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    /**
     * 更新
     * @throws InterruptedException
     */
    @Scheduled(fixedDelay = UPDATE_TIME, initialDelay = 1000)//延迟1秒执行
    public void updateTime() throws InterruptedException
    {
        GameServer.currentTime = System.currentTimeMillis();
    }

    /**
     * 更新
     * @throws InterruptedException
     */
    @Scheduled(fixedDelay = UPDATE_TIME * 2, initialDelay = 1000)//延迟1秒执行
    public void updatePlayer() throws InterruptedException
    {
        GameServer.getInstance().getWorldManager().update();
    }




    /**
     * 每天凌晨4点执行
     */
    @Transactional(rollbackFor = Exception.class)
    @Scheduled(cron = UPDATE_PLAYER_GRADE_TIME)
    public void updateTask() throws Exception
    {
        List<PlayerController> playerList = GameServer.getInstance().getWorldManager().getPlayerList();
        for (int i = 0; i < playerList.size(); i++) {
            playerList.get(i).getPlayer().reset();
        }

    }




}