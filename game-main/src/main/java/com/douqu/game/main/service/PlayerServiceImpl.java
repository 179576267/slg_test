package com.douqu.game.main.service;


import com.bean.core.util.MD5Utils;
import com.douqu.game.core.database.model.PlayerModel;
import com.douqu.game.core.database.model.UserModel;
import com.douqu.game.core.e.E_PlayerStatus;
import com.douqu.game.core.controller.PlayerController;
import com.douqu.game.core.entity.ext.*;
import com.douqu.game.core.config.sprite.MasterConfig;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.netty.NettyConnection;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.protobuf.SGMainProto;
import com.douqu.game.core.protobuf.SGSystemProto;
import com.douqu.game.core.util.Utils;
//import com.douqu.game.main.cache.RedisWrapperClient;
import com.douqu.game.main.database.mapper.LoginLogMapper;
import com.douqu.game.main.database.mapper.PlayerMapper;
import com.douqu.game.main.database.mapper.UserMapper;
import com.douqu.game.main.database.model.LoginLogModel;
import com.douqu.game.main.GameServer;
import com.douqu.game.main.util.PrintUtils;
import com.mysql.jdbc.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Author : Bean
 * 2017-04-11 12:23
 */
@Service
public class PlayerServiceImpl implements PlayerService {

    Logger logger = Logger.getLogger(this.getClass());

//    @Autowired
//    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private PlayerMapper playerMapper;

    @Autowired
    private LoginLogMapper loginLogMapper;

    @Autowired
    private UserMapper userMapper;


    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public int update(Player player)
    {

//        redisWrapperClient.set(user.getId(),user.getName());
//        String rs = redisWrapperClient.get(user.getId());
//        System.out.println("存储后:" + rs);
//        redisWrapperClient.del(user.getId());
//        rs = redisWrapperClient.get(user.getId());
//        System.out.println("删除后:" + rs);
        return playerMapper.update(player.save());
    }


    @Override
    public UserModel getUserByAccount(String account) {
        return userMapper.getByKey(account);
    }

    @Override
    public PlayerModel getPlayerByIndex(String objectIndex) {
        return playerMapper.getPlayerByIndex(objectIndex);
    }

    @Override
    public UserModel getUserByDeviceId(String deviceId) {
        return userMapper.getByKey(deviceId);
    }

    @Override
    public PlayerModel getPlayerByUid(Integer uid)
    {
        return playerMapper.getPlayerByUid(uid);
    }

    @Override
    public PlayerModel getPlayerByName(String name) {
        return playerMapper.getPlayerByName(name);
    }

    @Override
    public void login(LoginLogModel loginLogModel)
    {
        loginLogMapper.insert(loginLogModel);

        playerMapper.login(new Date(), loginLogModel.getPlayerId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Player regist(String account, String nickName, String password, String avatar,  SGCommonProto.E_CHANNEL_TYPE channel, String ip)
    {
        //创建角色
        UserModel user = new UserModel();
        user.setIp(ip);
        if(channel == SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_QUICK){
            user.setDeviceId(Utils.createAccount(account, channel));
        }else {
            user.setAccount(Utils.createAccount(account, channel));
        }
        user.setChannel(channel.getNumber());
        if(!StringUtils.isNullOrEmpty(password)){
            user.setPassword(MD5Utils.encodeUppercase(password));
        }

        userMapper.insert(user);

        Player player = new Player(GameServer.getInstance().getWorldManager().getWorldInfo());
        player.name = nickName;
        player.avatar = avatar;
        player.camp = SGCommonProto.E_CAMP_TYPE.CAMP_TYPE_ALLIENCE_VALUE;
        List<MasterConfig> masterConfigs = DataFactory.getInstance().getDataList(DataFactory.MASTER_KEY);
        for(MasterConfig masterConfig : masterConfigs)
        {
            if(masterConfig.camp == player.camp)
            {
                player.master = masterConfig.id;
                break;
            }
        }
        player.setUid(user.getId());
        player.setAccount(account);
        player.setObjectIndex(GameServer.getInstance().createPlayerIndex(user.getId()));

        PlayerModel playerModel = player.save();
        playerMapper.insert(playerModel);
        //插入后才有id
        player.setId(playerModel.getId());

        //写入登录日志
        LoginLogModel loginLogModel = new LoginLogModel();
        loginLogModel.setChannel(channel.getNumber());
        loginLogModel.setIp(ip);
        loginLogModel.setPlayerId(playerModel.getId());
        loginLogModel.setNormal(1);
        loginLogMapper.insert(loginLogModel);

        return  player;
    }


    @Override
    public void enterGame(PlayerController playerController, Player player, SGCommonProto.E_CHANNEL_TYPE channel, NettyConnection connection)
    {
        if(playerController == null)
            playerController = new PlayerController(player, connection);
        else
        {
            playerController.setTcpConnection(connection);
            connection.setObject(playerController);
        }

        playerController.setStatus(E_PlayerStatus.FREE);

        GameServer.getInstance().getWorldManager().playerEnterGame(playerController);

        //返回登录初始化信息
        SGSystemProto.S2C_Login.Builder logResponse = SGSystemProto.S2C_Login.newBuilder();
        logResponse.setResult(SGCommonProto.E_LOGIN_RESULT.LOGIN_RESULT_SUCCESS);
        SGCommonProto.PlayerBaseInfo userInfo = SGCommonProto.PlayerBaseInfo.newBuilder()
                .setPlayerIndex(player.getObjectIndex())
                .setNickName(player.getName())
                .setAvatar(player.avatar)
                .setLv(player.getLv())
                .setCamptypeValue(player.camp)
                .setMasterId(player.master).build();
        logResponse.setPlayerInfo(userInfo);

        logResponse.setChannel(channel);

        connection.sendMsg(SGMainProto.E_MSG_ID.MsgID_System_Login_VALUE, logResponse.build().toByteArray());

        logger.info("进入游戏:" + logResponse);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOnlinePlayer()
    {
        List<PlayerController> playerList = GameServer.getInstance().getWorldManager().getPlayerList();

        String sql = "update player set name=?,avatar=?,camp=?,level=?,exp=?,vipLevel=?,vipExp=?,fc=?,money=?,bagInfo=?,challengeInfo=?,taskInfo=?,boonInfo=?,settingInfo=? where objectIndex=?";
        int count = playerList.size();
        if(count > 0)
            logger.info("Task Save Players Start-------------->" + count);
        //批量更新玩家数据
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException
            {
                Player player = playerList.get(i).getPlayer();
                ps.setString(1, player.name);
                ps.setString(2, player.avatar);
                ps.setInt(3, player.camp);
                ps.setInt(4, player.getLv());
                ps.setInt(5, player.getExp());
                ps.setInt(6, player.getVipLevel());
                ps.setInt(7, player.getVipExp());
                ps.setInt(8, player.fc);
                ps.setInt(9, player.money);
                ps.setBytes(10, player.getExtInfo(BagInfo.class).getData());
                ps.setBytes(11, player.getExtInfo(ChallengeInfo.class).getData());
                ps.setBytes(12, player.getExtInfo(TaskInfo.class).getData());
                ps.setBytes(13, player.getExtInfo(BoonInfo.class).getData());
                ps.setBytes(14, player.getExtInfo(SettingInfo.class).getData());
                ps.setString(15, player.getObjectIndex());
                if(i%100 == 0 && i > 0)
                    PrintUtils.info(logger, "Update Save Player:" + player);
            }

            @Override
            public int getBatchSize() {
                return playerList.size();
            }
        });
    }

//    @Override
//    public void offlineMatch(PlayerController playerController) {
//
//        playerController.setStatus(E_PlayerStatus.MATCHING);
//
//        int gradeValue = playerController.getPlayer().grade;
//        //找同段位的
//        PlayerModel target = playerMapper.offlineMatch(gradeValue, playerController.getObjectIndex());
//        logger.info("离线匹配-相同段位的玩家:"+target);
//        if(target == null)
//        {
//            //相差1的段位
//            target = offlineMatchByGrade(playerController, 1, true);
//            logger.info("离线匹配-段位差值1的玩家:"+target);
//        }
//        if(target == null)
//        {
//            //不检测匹配过的对手
//            List<Grade> gradeList = DataFactory.getInstance().getDataList(DataFactory.GRADE_KEY);
//            int forCount = gradeList.size() - gradeValue - 1;
//            if(forCount > 0)
//            {
//                for(int i = 0; i < forCount; i++)
//                {
//                    target = offlineMatchByGrade(playerController, 2+i, false);
//                    logger.info("离线匹配-段位差值"+(2+i)+"的玩家:"+target);
//                    if(target != null)
//                        break;
//                }
//            }
//        }
//
//        if(target != null)
//        {
//            Player player = new Player(GameServer.getInstance().getWorldManager().getWorldInfo());
//            player.load(target);
//
//            MsgUtils.startBattle(playerController, player, SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVP_ARENA_AI);
//        }
//
//        playerController.setStatus(E_PlayerStatus.FREE);
//
//        //匹配成功
//        GameServer.getInstance().getWorldManager().removeMatch(playerController);
//    }
//
//    private PlayerModel offlineMatchByGrade(PlayerController playerController, int checkValue, boolean checkLastMatch)
//    {
//        int gradeValue = playerController.getPlayer().grade;
//        Grade grade = DataFactory.getInstance().getGameObject(DataFactory.GRADE_KEY, gradeValue + checkValue);
//        PlayerModel model1 = null, model2 = null;
//        String[] indexs = new String[]{playerController.getObjectIndex()};
//        if(checkLastMatch)
//        {
//            BattleInfo battleInfo = (BattleInfo) playerController.getPlayer().getExtInfo(BattleInfo.class);
//            String lastMatch = battleInfo.getLastTarget(SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVP_ARENA_VALUE, SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVP_ARENA_AI_VALUE);
//            if(lastMatch != null) indexs = new String[]{playerController.getObjectIndex(), lastMatch};
//        }
//        if(grade != null)
//        {
//            model1 = playerMapper.offlineMatch(grade.id, indexs);
//        }
//        grade = DataFactory.getInstance().getGameObject(DataFactory.GRADE_KEY, gradeValue - checkValue);
//        if(grade != null)
//        {
//            model2 = playerMapper.offlineMatch(grade.id, indexs);
//        }
//        PlayerModel playerModel = null;
//        if(model1 == null && model2 != null)
//            playerModel = model2;
//        else if(model1 != null && model2 == null)
//            playerModel = model1;
//        else if(model1 != null && model2 != null)
//        {
//            int r = (int) (Math.random() * 2);
//            if(r == 0)
//                playerModel = model1;
//            else
//                playerModel = model2;
//        }
//        return playerModel;
//    }

    @Override
    public int getAllPlayerCount()
    {
        return playerMapper.findAllCount();
    }


    @Override
    public List<PlayerModel> getPlayerList(Integer page, Integer count)
    {
        if(page <= 0 || count <= 0)
            return null;

        return playerMapper.findList((page-1) * count, count);
    }
}
