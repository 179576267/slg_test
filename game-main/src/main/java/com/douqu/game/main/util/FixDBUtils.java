package com.douqu.game.main.util;

import com.bean.core.util.TimeUtils;
import com.douqu.game.core.database.model.PlayerModel;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.entity.ext.*;
import com.douqu.game.main.SpringServer;
import com.douqu.game.main.database.mapper.PlayerMapper;
import com.douqu.game.main.server.LoadManager;
import com.douqu.game.main.server.SpringContext;
import com.douqu.game.main.service.PlayerService;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 修正新增数据
 * @Author: Bean
 * @Description:
 * @Date: 2017-12-01 17:43
 */
public class FixDBUtils {


    public static void main(String[] args)
    {
        fixDB();
//        reload();
    }
    /**
     * 执行此操作前请确保游戏主服务器是关闭的
     * 把所有玩家数据和世界数据备份到新的数据库,在新的数据库里进行以下操作
     * 1.loadFrom和writeTo方法里添加新字段,并且一一对应
     * 2.注释掉所有loadFrom方法里新加的字段
     * 3.存储所有玩家数据和世界数据
     * 4.把loadFrom方法里注释掉的取消注释
     * 5.再load一次玩家数据和世界数据,确保已经正确修复
     * 6.如果没有错误,则把数据替换到正式数据库
     * 7.如果有错,则从头再来
     */
    public static void fixDB()
    {
        SpringServer.start();

        LoadManager.loadData();

        PlayerMapper playerMapper = SpringContext.getBean(PlayerMapper.class);

        List<Player> playerList = load(playerMapper);

        System.out.println("加载成功, 玩家数量 -> " + playerList.size() + " 休息3秒......");

        JdbcTemplate jdbcTemplate = SpringContext.getBean(JdbcTemplate.class);

        TimeUtils.sleep(1000);

        System.out.println("1");

        TimeUtils.sleep(1000);

        System.out.println("2");

        TimeUtils.sleep(1000);

        System.out.println("3");

        write(jdbcTemplate, playerList);

        System.out.println("写入完成!");

        System.exit(0);
    }


    public static List<Player> load(PlayerMapper playerMapper)
    {
        List<PlayerModel> playerModelList = playerMapper.findAll();
        Player player = null;

        List<Player> playerList = new CopyOnWriteArrayList<>();
        for(PlayerModel playerModel : playerModelList)
        {
            player = new Player(playerModel);

            playerList.add(player);
        }

        return playerList;
    }


    public static void write(JdbcTemplate jdbcTemplate, List<Player> playerList)
    {
        String sql = "update player set bagInfo=?,challengeInfo=?,taskInfo=?,boonInfo=?,settingInfo=? where objectIndex=?";
        //批量更新玩家数据
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException
            {
                Player player = playerList.get(i);
                ps.setBytes(1, player.getExtInfo(BagInfo.class).getData());
                ps.setBytes(2, player.getExtInfo(ChallengeInfo.class).getData());
                ps.setBytes(3, player.getExtInfo(TaskInfo.class).getData());
                ps.setBytes(4, player.getExtInfo(BoonInfo.class).getData());
                ps.setBytes(5, player.getExtInfo(SettingInfo.class).getData());
                ps.setString(6, player.getObjectIndex());
            }

            @Override
            public int getBatchSize() {
                return playerList.size();
            }
        });
    }

    public static void reload()
    {
        SpringServer.start();

        LoadManager.loadData();

        PlayerMapper playerMapper = SpringContext.getBean(PlayerMapper.class);

        List<Player> playerList = load(playerMapper);

        System.out.println("重新加载成功,玩家数量 -> " + playerList.size());

        System.exit(0);
    }

}
