package com.douqu.game.main.server.netty;


import com.alibaba.fastjson.JSONObject;
import com.bean.core.util.HttpJsonUtils;
import com.douqu.game.core.config.FunctionConfig;
import com.douqu.game.core.controller.PlayerController;
import com.douqu.game.core.database.model.PlayerModel;
import com.douqu.game.core.database.model.UserModel;
import com.douqu.game.core.e.E_PlayerStatus;
import com.douqu.game.core.e.E_ProfileVersion;
import com.douqu.game.core.e.ReturnMessage;
import com.douqu.game.core.entity.battle.PlayerBattleData;
import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.entity.ext.BagInfo;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.entity.ext.data.challenge.InstanceData;
import com.douqu.game.core.factory.*;
import com.douqu.game.core.netty.NettyConnection;
import com.douqu.game.core.protobuf.*;
import com.douqu.game.core.protobuf.SGMainProto.E_MSG_ID;
import com.douqu.game.core.protobuf.SGMainProto.E_MSG_TYPE;
import com.douqu.game.core.util.BufferUtils;
import com.douqu.game.core.util.SendUtils;
import com.douqu.game.core.util.Utils;
import com.douqu.game.main.database.model.LoginLogModel;
import com.douqu.game.main.msg.*;
import com.douqu.game.main.platform.QQImpl;
import com.douqu.game.main.GameServer;
import com.douqu.game.main.server.ServerManager;
import com.douqu.game.main.server.SpringContext;
import com.douqu.game.main.server.thread.SavePlayerThread;
import com.douqu.game.main.service.PlayerService;
import com.douqu.game.main.util.MsgUtils;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mysql.jdbc.StringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;


/**
 * Created by bean on 2017/3/9.
 */
public class NettyTCPServerHandler extends SimpleChannelInboundHandler
{
    Logger logger = Logger.getLogger(NettyTCPServerHandler.class);

    private NettyConnection connection;

    private void close()
    {
        Object object = connection.getObject();
        if(object != null)
        {
            PlayerController playerController = (PlayerController) object;
            if(playerController != null)
            {
                //保存玩家信息
                GameServer.getInstance().startThread(new SavePlayerThread(playerController.getPlayer(), true));

                if (playerController.isBattling()) {
                    //在战斗中，只断开连接，不删除数据
                    logger.debug("去战斗了,断开连接......................." + connection);
                    playerController.serverDestroy();
                } else {
                    logger.debug("普通模式断开连接......................." + connection);
                    GameServer.getInstance().getWorldManager().playerExitGame(playerController);
                }

            }
        }

        ServerManager.getInstance().removeServerInfo(connection);
        GameServer.getInstance().getWorldManager().removeConnection(connection);

        connection.destroy();
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE)
            {
                logger.info("Reader Timeout!"+connection);
                close();
            }
        }
    }

//    @Override
//    public void handlerRemoved(ChannelHandlerContext ctx)
//    {
//        logger.debug("Client handlerRemoved Disconnect:"+playerController);
//
//        close();
//    }


    /*
      * channelAction
      *
      * 当客户端主动链接服务端的链接后，这个通道就是活跃的了。也就是客户端与服务端建立了通信通道并且可以传输数据
      *
      */
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        logger.info("Client Connect Success:"+ctx.channel().remoteAddress());

        connection = new NettyConnection(ctx.channel());

        GameServer.getInstance().getWorldManager().addConnection(connection);
    }



    /*
     * channelInactive
     *
     *
     * 当客户端主动断开服务端的 链接后，这个通道就是不活跃的。也就是说客户端与服务端的关闭了通信通道并且不可以传输数据
     *
     */
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        logger.info("Client Disconnect:" + connection);

        close();
    }


    /**
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg)
    {
        connection.receiveMsg();

        ByteBuf byteBuf = (ByteBuf) msg;
        int code = BufferUtils.readShort(byteBuf);

        if(code != E_MSG_ID.MsgID_System_Ping_VALUE)
        {
            logger.info("code: " + E_MSG_ID.forNumber(code) + "->" + code + "->data length: " + byteBuf.readableBytes());
        }

        if(CodeFactory.isServer(code))
        {
            ServerMsgChannel.messageChannel(code, connection, byteBuf);
        }
        else
        {
            if(E_MSG_ID.MsgID_System_Login_VALUE == code)
            {
                //登录
                login(byteBuf, ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress());
            }
            else if(E_MSG_ID.MsgID_System_Ping_VALUE == code)
            {
                //ping 心跳
            }
            else if(E_MSG_ID.MsgID_System_Regist_VALUE == code)
            {
                register(byteBuf, ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress());
            }
            else if(E_MSG_ID.MsgID_System_SuperLogin_VALUE == code)
            {
                //超级登录
                superLogin();
            }
            else
            {
                if(connection.getObject() == null)
                    return;

                if(!(connection.getObject() instanceof PlayerController))
                    return;

                PlayerController playerController = (PlayerController) connection.getObject();
                if(playerController == null)
                    return;

                if(playerController.getPlayer().isDel)
                {
                    //被封号了
                    playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.LOCK_ACCOUNT));
                    return;
                }
                if(playerController.isBattling())
                {
                    playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.BATTLING_NOT_ALLOW));
                    return;
                }

                if(E_MSG_ID.MsgID_System_GMCmd_VALUE == code)
                {
                    //cmd命令处理
                    cmd(playerController, Utils.byteBufToBytes(byteBuf));
                }
                else
                {
                    try {
                        int msgType = code - code%100;
                        byte[] data = Utils.byteBufToBytes(byteBuf);

                        //检测功能是否解锁
//                        if(isForbid(playerController.getPlayer().getLv(),code,data)){
//                            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.UNLOCKED_CONFIGURATION_FILE_DOES_NOT_EXIST));
//                            return;
//                        }

                        if(playerController.getPlayer().lock)
                            return;

                        switch (msgType)
                        {
                            case E_MSG_TYPE.MsgType_Player_VALUE:
                                SpringContext.getMessageChannel(PlayerMsgChannel.class).messageChannel(code, playerController, data);
                                break;
                            case E_MSG_TYPE.MsgType_Bag_VALUE:
                                SpringContext.getMessageChannel(BagMsgChannel.class).messageChannel(code, playerController, data);
                                break;
                            case E_MSG_TYPE.MsgType_Task_VALUE:
                                SpringContext.getMessageChannel(TaskMsgChannel.class).messageChannel(code, playerController, data);
                                break;
                            case E_MSG_TYPE.MsgType_Instance_VALUE:
                            case E_MSG_TYPE.MsgType_Arena_VALUE:
                            case E_MSG_TYPE.MsgType_OfficialWar_VALUE:
                            case E_MSG_TYPE.MsgType_HeroTemple_VALUE:
                                SpringContext.getMessageChannel(ChallengeMsgChannel.class).messageChannel(code, playerController, data);
                                break;
                            case E_MSG_TYPE.MsgType_Altar_VALUE:
                            case E_MSG_TYPE.MsgType_Lottery_VALUE:
                            case E_MSG_TYPE.MsgType_Store_VALUE:
                            case E_MSG_TYPE.MsgType_Bonus_VALUE:
                                SpringContext.getMessageChannel(BoonMsgChannel.class).messageChannel(code, playerController, data);
                                break;
                        }

                        SendUtils.sendChangeData(code, playerController);

                        //TODO 红点提醒 > 0
                        playerController.getPlayer().sendReadPoint(playerController);

                        playerController.setLastUpdateTime(GameServer.currentTime);

                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error(e);
                        e.printStackTrace();
                        playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
                    }
                }
            }
        }

    }

    /**
     * gm命令
     * @param playerController
     * @param data
     */
    private void cmd(PlayerController playerController, byte[] data) {
        if(!CodeFactory.TEST){
            return;
        }
        SGSystemProto.C2S_GMCmd request = null;
        try {
            request = SGSystemProto.C2S_GMCmd.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if(request == null){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        String cmd = request.getCmd();
//        String datas[] = cmd.split(ConstantFactory.DIVISION);
        if("hero".equals(cmd))
        {
            MsgUtils.startBattle(playerController, SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVE_HERO_TEMPLE, "1");
        }
        else if("reset".equals(cmd)){
            if(!playerController.getPlayer().getHeroTempleData().isChallengeTimesEnough()){
                playerController.getPlayer().getHeroTempleData().reset();
            }

            if(!playerController.getPlayer().getArenaData().isChallengeTimesEnough()){
                playerController.getPlayer().getArenaData().reset();
            }

            if(!playerController.getPlayer().getOfficialData().isChallengeTimesEnough()){
                playerController.getPlayer().getOfficialData().reset();
             }
        }else if("passLevel".equals(cmd)){
            InstanceData instanceData = playerController.getPlayer().getInstanceData();
            int mapId = instanceData.getCanPassMap().get(instanceData.getCanPassMap().size() - 1);
            int levelId =  instanceData.getNextPassLevel().get(instanceData.getNextPassLevel().size() - 1);
            byte[] data1 = SGInstanceProto.C2S_PassLevel.newBuilder()
                    .setChapterId(mapId)
                    .setLevelId(levelId)
                    .setStarts(3)
                    .build().toByteArray();
            try {
                SpringContext.getMessageChannel(ChallengeMsgChannel.class).messageChannel(
                        E_MSG_ID.MsgID_Instance_PassLevel_VALUE, playerController, data1);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else
        {
            GoodsData goodsData = new GoodsData(cmd);
            BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
            bagInfo.addGoods(new GoodsData[]{goodsData},null);
        }

    }


    private void superLogin()
    {
        PlayerService playerService = SpringContext.getBean(PlayerService.class);
        Player player = playerService.regist(Utils.createUUID(6), Utils.createUUID(6), null, "", SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_QUICK, "127.0.0.1");
        if(player != null)
            enterGame(null, player, SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_QUICK);
        logger.info("超级登录玩家:" + player.getId() + "," + player.getObjectIndex() + "," + player.getName());
    }

    /**
     * 登录
     * @param byteBuf
     */
    private void login(ByteBuf byteBuf, String ip)
    {
        if(connection.getObject() != null)
        {
            //重复登录
            return;
        }

        byte[] data = Utils.byteBufToBytes(byteBuf);
        SGSystemProto.S2C_Login.Builder response = SGSystemProto.S2C_Login.newBuilder();
        SGSystemProto.C2S_Login request = null;
        try {
            request =  SGSystemProto.C2S_Login.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        if(request == null){
            response.setResult(SGCommonProto.E_LOGIN_RESULT.LOGIN_RESULT_FAIL);
            connection.sendMsg(E_MSG_ID.MsgID_System_Login_VALUE, response.build().toByteArray());
            return;
        }
        logger.info("登录参数:" + request.toString());

        String account = request.getAccount().trim();
        if(StringUtils.isNullOrEmpty(account))
        {
            logger.debug("账号为空!");
            response.setResult(SGCommonProto.E_LOGIN_RESULT.LOGIN_RESULT_FAIL);
            connection.sendMsg(E_MSG_ID.MsgID_System_Login_VALUE, response.build().toByteArray());
            return;
        }
        String password = request.getPassword().trim();
        SGCommonProto.E_CHANNEL_TYPE channel = request.getChannel();
        PlayerService playerService = SpringContext.getBean(PlayerService.class);

        UserModel user = getUser(channel, account, password, playerService);
        if(user == null){
            logger.info("用户不存在,告诉客户端要注册");
            response.setResult(SGCommonProto.E_LOGIN_RESULT.LOGIN_RESULT_ENREGIST);
            response.setChannel(channel);
            connection.sendMsg(E_MSG_ID.MsgID_System_Login_VALUE, response.build().toByteArray());
            return;
        }else if(user.getId() == 0){ //认证失败
            logger.info("认证失败，或者密码错误");
            response.setResult(SGCommonProto.E_LOGIN_RESULT.LOGIN_RESULT_FAIL);
            response.setChannel(channel);
            connection.sendMsg(E_MSG_ID.MsgID_System_Login_VALUE, response.build().toByteArray());
            return;
        }

        PlayerModel playerModel = playerService.getPlayerByUid(user.getId());
//        logger.info("playerModel:" + playerModel);
        if(playerModel == null)
        {
            //数据异常，有账号没角色
            logger.info("数据异常，有账号没角色");
            return;
        }

        Player player = null;

        PlayerController online = GameServer.getInstance().getWorldManager().getPlayerController(playerModel.getObjectIndex());
        if(online != null)
        {
            if(online.isBattling())
            {
                //战斗中不允许再登陆
                logger.info("战斗中不允许再登陆：" + online.getStatus());
//                response.setResult(SGCommonProto.E_LOGIN_RESULT.LOGIN_RESULT_FAIL);
//                response.setChannel(channel);
//                connection.sendMsg(E_MSG_ID.MsgID_System_Login_VALUE, response.build().toByteArray());
                return;
            }

            if(online.getStatus() == E_PlayerStatus.BATTLE_OFFLINE)
            {
                logger.info("战斗中离线重新连接,要告诉客户端战斗服务器的地址!");
                PlayerBattleData playerBattleData = online.getBattleData();
                if(playerBattleData != null)
                {
                    response.setResult(SGCommonProto.E_LOGIN_RESULT.LOGIN_RESULT_BATTLE);
                    response.setChannel(channel);
                    SGCommonProto.ServerInfo.Builder serverInfoData = SGCommonProto.ServerInfo.newBuilder();
                    serverInfoData.setHost(playerBattleData.getIp());
                    serverInfoData.setPort(playerBattleData.getPort());

                    SGCommonProto.BattleData.Builder battleData = SGCommonProto.BattleData.newBuilder();
                    battleData.setBattleId(playerBattleData.getBattleId());
                    battleData.setPlayerIndex(online.getObjectIndex());
                    battleData.setBattleServer(serverInfoData);

                    response.setBattleData(battleData);

                    connection.sendMsg(E_MSG_ID.MsgID_System_Login_VALUE, response.build().toByteArray());
                }
                return;
            }

            if(online.getTcpConnection() != null)
            {
                logger.info("相同账号已经在游戏里，把之前已经登陆的T下线!状态:" + online.getStatus());
                online.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.OTHER_LOGIN));
                GameServer.getInstance().getWorldManager().playerExitGame(online);
                return;
            }

            //从战斗服务器退出重新连接主服务器的玩家
            logger.info("直接赋值在线玩家:" + online.getPlayer());
            player = online.getPlayer();

            GameServer.getInstance().getWorldManager().playerExitGame(online);
        }

        if(player == null)
        {
            player = new Player(GameServer.getInstance().getWorldManager().getWorldInfo(), playerModel);
        }


        if(player == null || player.getId() == 0)
        {
            logger.info("登录异常");
            response.setResult(SGCommonProto.E_LOGIN_RESULT.LOGIN_RESULT_FAIL);
            response.setChannel(channel);
            connection.sendMsg(E_MSG_ID.MsgID_System_Login_VALUE, response.build().toByteArray());
            return;
        }

        if(player.isDel)
        {
            response.setResult(SGCommonProto.E_LOGIN_RESULT.LOGIN_RESULT_LOCK);
            response.setChannel(channel);
            connection.sendMsg(E_MSG_ID.MsgID_System_Login_VALUE, response.build().toByteArray());
            return;
        }

        //正常登录才判断
        if(request.getNormal())
        {
//            int lockTime = playerService.getAccountLockTime(player.getId());
//            if(lockTime > 0)
//            {//还有这么多时间才解锁
//                response.setResult(E_Login_RESULT.Login_RESULT_Lock);
//                response.setChannel(channel);
//                response.setLockTime(lockTime/1000);//毫秒转为秒
//                connection.sendMsg(E_MSG_ID.MsgID_Login_VALUE, response.build().toByteArray());
//                return;
//            }
        }

        if(online == null)//online不为空表示是从战斗服务器回来登录的
        {
            //写入登录日志
            LoginLogModel loginLogModel = new LoginLogModel();
            loginLogModel.setChannel(channel.getNumber());
            loginLogModel.setIp(ip);
            loginLogModel.setPlayerId(player.getId());
            loginLogModel.setNormal(request.getNormal() ? 1 : 0);
            playerService.login(loginLogModel);
            //告知登录服务器，本次登录成功，方便登录服务器返回最近一次的登录服务器
            notifyAuthServerLoginSuccess(request.getAccount(), channel);
        }

        enterGame(online, player, channel);

    }

    private UserModel getUser(SGCommonProto.E_CHANNEL_TYPE channel, String account, String password,PlayerService playerService) {
        UserModel user = null;
        switch (channel.getNumber()){
            case SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_QUICK_VALUE:
                user = playerService.getUserByDeviceId(Utils.createAccount(account, channel));
                break;
            case SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_PHONE_VALUE:
                user = playerService.getUserByAccount(Utils.createAccount(account, channel));
                break;
            case SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_QQ_VALUE:
                user = SpringContext.getPlatformSDK(QQImpl.class).login(account, password);
                break;
        }
        return user;
    }

    private void enterGame(PlayerController playerController, Player player, SGCommonProto.E_CHANNEL_TYPE channel)
    {
        PlayerService playerService = SpringContext.getBean(PlayerService.class);
        try{
            playerService.enterGame(playerController, player, channel, connection);
        }catch (Exception e){
            e.printStackTrace();
            connection.setObject(null);
        }
    }

    private void notifyAuthServerLoginSuccess(String account, SGCommonProto.E_CHANNEL_TYPE channel) {
        //注册
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("serverId", GameServer.getInstance().getServerId());
        jsonObject.put("channel", channel.getNumber());
        jsonObject.put("account", account);

        JSONObject result = HttpJsonUtils.httpPost(ConfigFactory.AUTH_SERVER_HTTP_BASE + "player/loginSuccess", jsonObject);
//        System.out.println(result.toString());
    }

    /**
     * 注册
     * @param byteBuf
     */

    private void register(ByteBuf byteBuf, String ip) {
        byte[] data = Utils.byteBufToBytes(byteBuf);
        SGSystemProto.C2S_Regist request = null;
        try {
            request = SGSystemProto.C2S_Regist.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if(request == null){
//            SGSystemProto.S2C_Regist s2CRegist =  SGSystemProto.S2C_Regist.newBuilder().
//                    setResult(SGCommonProto.E_REGIST_RESULT.REGIST_RESULT_FormatError).build();
//
//            connection.sendMsg(E_MSG_ID.MsgID_System_Regist_VALUE, s2CRegist.toByteArray());
            connection.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }
        logger.info("注册参数:\n" + request.toString());
        if(StringUtils.isNullOrEmpty(request.getNickName())){
            logger.info("昵称为空了");
//            String content = WordFactory.getWord(WordFactory.NAME_NOT_EMPTY);
//            connection.sendMsg(SGMainProto.E_MSG_ID.MsgID_System_NotifyAlert_VALUE, SGSystemProto.S2C_NotifyAlert.newBuilder()
//                    .setType(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN)
//                    .setContent(content == null ? "" : content).build().toByteArray());

            connection.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.NAME_NOT_EMPTY));
            return;
        }
        if(request.getNickName().length() > ConstantFactory.NAME_MAX_LENGTH){
            logger.info("昵称过长: " + request.getNickName());
//            String content = WordFactory.getWord(WordFactory.NAME_TOO_LONG);
//            connection.sendMsg(SGMainProto.E_MSG_ID.MsgID_System_NotifyAlert_VALUE, SGSystemProto.S2C_NotifyAlert.newBuilder()
//                    .setType(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN)
//                    .setContent(content == null ? "" : content).build().toByteArray());

            connection.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.NAME_TOO_LONG));
            return;
        }
        //提交到认证服务器去记录角色
        String result = createRoleRecord(ip, request, true);
        if(StringUtils.isNullOrEmpty(result)){
            //注册信息认证成功，进行注册
            PlayerService playerService = SpringContext.getBean(PlayerService.class);
            UserModel user;
            //验证本服务器上账户是否存在
            if(request.getChannel() == SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_QUICK){
                user = playerService.getUserByDeviceId(Utils.createAccount(request.getAccount(), request.getChannel()));
            }else {
                user = playerService.getUserByAccount(Utils.createAccount(request.getAccount(), request.getChannel()));
            }

            if(user != null)
            {
                logger.info("账户已存在");
//                response.setResult(SGCommonProto.E_REGIST_RESULT.REGIST_RESULT_Fail);
//                connection.sendMsg(E_MSG_ID.MsgID_System_Regist_VALUE, response.build().toByteArray());
                connection.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ACCOUNT_EXIST));
                return;
            }
            Player player = playerService.regist(request.getAccount(), request.getNickName(), request.getCaptcha(), request.getAvatarUrl(), request.getChannel(), ip);
            createRoleRecord(ip, request, false);
            notifyAuthServerLoginSuccess(request.getAccount(), request.getChannel());
            enterGame(null, player, request.getChannel());
            return;
        }
        else
        {
            connection.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(result));
            return;
        }
//        logger.info("response:" + response);
//        connection.sendMsg(E_MSG_ID.MsgID_System_Regist_VALUE, response.build().toByteArray());
    }

    private String createRoleRecord(String ip, SGSystemProto.C2S_Regist c2S_regist, boolean justCheck) {
        //注册
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("serverId", GameServer.getInstance().getServerId());
        jsonObject.put("channel", c2S_regist.getChannel().getNumber());
        if(c2S_regist.getChannel() == SGCommonProto.E_CHANNEL_TYPE.CHANNEL_TYPE_QUICK){
            jsonObject.put("deviceId", c2S_regist.getAccount());
        }else {
            jsonObject.put("account", c2S_regist.getAccount());
        }

        jsonObject.put("password", c2S_regist.getCaptcha());
        jsonObject.put("ip", ip);
        jsonObject.put("playName", c2S_regist.getNickName());
        jsonObject.put("avatar", c2S_regist.getAvatarUrl());
        jsonObject.put("level", 1);
        jsonObject.put("justCheck", justCheck);

        JSONObject result = HttpJsonUtils.httpPost(ConfigFactory.AUTH_SERVER_HTTP_BASE + "player/register", jsonObject);
        logger.info(result.toString());
        String resultCode = result.getString("code");
//        SGSystemProto.S2C_Regist.Builder s2CRegist =  SGSystemProto.S2C_Regist.newBuilder();
        if(Integer.parseInt(resultCode) == 0){//成功
//            s2CRegist.setResult(SGCommonProto.E_REGIST_RESULT.REGIST_RESULT_Success);
            return null;
        }else {
            if(resultCode.equals(ReturnMessage.NICKNAME_EXITS.getCode())){
                return WordFactory.NAME_EXIST;
//                s2CRegist.setResult(SGCommonProto.E_REGIST_RESULT.REGIST_RESULT_NameExists);
            } else if(resultCode.equals(ReturnMessage.THIS_SERVER_PLAYER_EXITS.getCode())){
                return WordFactory.ACCOUNT_EXIST;
//                s2CRegist.setResult(SGCommonProto.E_REGIST_RESULT.REGIST_RESULT_PhoneIsExist);
            }else {
//                s2CRegist.setResult(SGCommonProto.E_REGIST_RESULT.REGIST_RESULT_Fail);
                return WordFactory.PARAM_ERROR;
            }
        }
//        return s2CRegist;
    }


    /*
     * channelReadComplete
     *
     * channel  通道
     * Read     读取
     * Complete 完成
     *
     * 在通道读取完成后会在这个方法里通知，对应可以做刷新操作
     * ctx.flush()
     *
     */
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    /*
     * exceptionCaught
     *
     * exception	异常
     * Caught		抓住
     *
     * 抓住异常，当发生异常的时候，可以做一些相应的处理，比如打印日志、关闭链接
     *
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        try{
            logger.info(cause.getLocalizedMessage());
            if(E_ProfileVersion.DEV.getKey().equals(SpringContext.getProfile())){
                cause.printStackTrace();
            }
            ctx.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //注册
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("channel", 7);
        jsonObject.put("serverId", 1);
        jsonObject.put("deviceId", "wzngzhenfe");
        jsonObject.put("ip", "192.168.23.54");
        jsonObject.put("serverId", 1);
        JSONObject result = HttpJsonUtils.httpPost("http://192.168.2.202:8888/player/login", jsonObject);
        System.out.println(result.toString());
        String resultCode = result.getString("code");
    }


    public boolean isForbid(int lv,int msgId,byte[] data) {

        FunctionConfig functionConfig = null;
        switch (msgId) {

            case E_MSG_ID.MsgID_Pub_LotteryInit_VALUE:
            case E_MSG_ID.MsgID_Pub_LotteryClick_VALUE:
                functionConfig = DataFactory.getInstance().getGameObject(DataFactory.FUNCTION_KEY, FunctionFactory.UNLOCK_LOTTERY);
                if (lv < functionConfig.lv)
                    return true;
                break;
            case E_MSG_ID.MsgID_Bag_CardUpLv_VALUE:
                functionConfig = DataFactory.getInstance().getGameObject(DataFactory.FUNCTION_KEY, FunctionFactory.UNLOCK_CARD_UP_LV);
                if (lv < functionConfig.lv)
                    return true;
                break;
            case E_MSG_ID.MsgID_Bag_CardUpStar_VALUE:
                functionConfig = DataFactory.getInstance().getGameObject(DataFactory.FUNCTION_KEY, FunctionFactory.UNLOCK_CARD_UP_STAR);
                if (lv < functionConfig.lv)
                    return true;
                break;
            case E_MSG_ID.MsgID_Bag_EquipIntensify_VALUE:
                functionConfig = DataFactory.getInstance().getGameObject(DataFactory.FUNCTION_KEY, FunctionFactory.UNLOCK_EQUIP_INTENSIFY);
                if (lv < functionConfig.lv)
                    return true;
                break;
            case E_MSG_ID.MsgID_Bag_AccessoryIntensify_VALUE:
                functionConfig = DataFactory.getInstance().getGameObject(DataFactory.FUNCTION_KEY, FunctionFactory.UNLOCK_ACCESSORY_ACTIVATION_UP_LV);
                if (lv < functionConfig.lv)
                    return true;
                break;
            case E_MSG_ID.MsgID_Bag_AccessoryUp_VALUE:
                functionConfig = DataFactory.getInstance().getGameObject(DataFactory.FUNCTION_KEY, FunctionFactory.UNLOCK_ACCESSORY_UP_LV);
                if (lv < functionConfig.lv)
                    return true;
                break;
            case E_MSG_ID.MsgID_Bag_CardFate_VALUE:
            case E_MSG_ID.MsgID_Bag_CardFateList_VALUE:
                functionConfig = DataFactory.getInstance().getGameObject(DataFactory.FUNCTION_KEY, FunctionFactory.UNLOCK_CARD_FATE);
                if (lv < functionConfig.lv)
                    return true;
                break;

            case E_MSG_ID.MsgID_Instance_GetInstanceInfo_VALUE:
            case E_MSG_ID.MsgID_Instance_PassLevel_VALUE:
            case E_MSG_ID.MsgID_Instance_ReceiveAward_VALUE:
            case E_MSG_ID.MsgID_Instance_RequestLevelBattle_VALUE:
            case E_MSG_ID.MsgID_Instance_ReceiveLevelBoxReward_VALUE:
            case E_MSG_ID.MsgID_Instance_GetLastPassLevel_VALUE:
                functionConfig = DataFactory.getInstance().getGameObject(DataFactory.FUNCTION_KEY, FunctionFactory.UNLOCK_REPLICA_SYSTEM);
                if (lv < functionConfig.lv)
                    return true;
                break;

            case E_MSG_ID.MsgID_Arena_GetArenaInfo_VALUE:
            case E_MSG_ID.MsgID_Arena_Challenge_VALUE:
            case E_MSG_ID.MsgID_Arena_GetDailyReward_VALUE:
            case E_MSG_ID.MsgID_Arena_PreviewRank_VALUE:
            case E_MSG_ID.MsgID_Arena_ExchangeReward_VALUE:
            case E_MSG_ID.MsgID_Arena_RewardRecord_VALUE:
            case E_MSG_ID.MsgID_Arena_Sweep_VALUE:
                functionConfig = DataFactory.getInstance().getGameObject(DataFactory.FUNCTION_KEY, FunctionFactory.UNLOCK_COMPETITIVE_SYSTEM);
                if (lv < functionConfig.lv)
                    return true;
                break;

            case E_MSG_ID.MsgID_OfficialWar_InitInfo_VALUE:
            case E_MSG_ID.MsgID_OfficialWar_PreviewRank_VALUE:
            case E_MSG_ID.MsgID_OfficialWar_ChallengeRank_VALUE:
            case E_MSG_ID.MsgID_OfficialWar_SweepRank_VALUE:
            case E_MSG_ID.MsgID_OfficialWar_GetDailyReward_VALUE:
            case E_MSG_ID.MsgID_OfficialWar_ExchangeReward_VALUE:
            case E_MSG_ID.MsgID_OfficialWar_RewardRecord_VALUE:
            case E_MSG_ID.MsgID_OfficialWar_IntegralReward_VALUE:
            case E_MSG_ID.MsgID_OfficialWar_IntegralRewardRecord_VALUE:
            case E_MSG_ID.MsgID_OfficialWar_RequestBattle_VALUE:

                functionConfig = DataFactory.getInstance().getGameObject(DataFactory.FUNCTION_KEY, FunctionFactory.UNLOCK_Military_SYSTEM);
                if (lv < functionConfig.lv)
                    return true;
                break;

            case 100:
                functionConfig = DataFactory.getInstance().getGameObject(DataFactory.FUNCTION_KEY, FunctionFactory.UNLOCK_HEROIC_SANCTUARY);
                if (lv < functionConfig.lv)
                    return true;
                break;

            case E_MSG_ID.MsgID_Altar_Init_VALUE:
            case E_MSG_ID.MsgID_Altar_Sacrifice_VALUE:
                functionConfig = DataFactory.getInstance().getGameObject(DataFactory.FUNCTION_KEY, FunctionFactory.UNLOCK_ALTAR_SYSTEM);
                if (lv < functionConfig.lv)
                    return true;
                break;

            case E_MSG_ID.MsgID_Bag_RebirthCard_VALUE:
            case E_MSG_ID.MsgID_Bag_ResolveCardPreview_VALUE:
            case E_MSG_ID.MsgID_Bag_SoulResolve_VALUE:
            case E_MSG_ID.MsgID_Bag_EquipResolve_VALUE:
                functionConfig = DataFactory.getInstance().getGameObject(DataFactory.FUNCTION_KEY, FunctionFactory.UNLOCK_RENASCENCE_SANCTUARY);
                if (lv < functionConfig.lv)
                    return true;
                break;


            case E_MSG_ID.MsgID_Store_InitInfo_VALUE:
            case E_MSG_ID.MsgID_Store_BuyGoods_VALUE:
                try {
                    SGPlayerProto.C2S_StoreInitInfo store = SGPlayerProto.C2S_StoreInitInfo.parseFrom(data);

                    if (store.getType().getNumber() == SGCommonProto.E_STORE_TYPE.STORE_TYPE_GOBLIN_VALUE) {
                        functionConfig = DataFactory.getInstance().getGameObject(DataFactory.FUNCTION_KEY, FunctionFactory.UNLOCK_SHOP_SYSTEM);
                        if (lv < functionConfig.lv)
                            return true;
                        break;
                    }
                    SGPlayerProto.C2S_StoreBuyGoods storeBuyGoods = SGPlayerProto.C2S_StoreBuyGoods.parseFrom(data);
                    if (storeBuyGoods.getType().getNumber() == SGCommonProto.E_STORE_TYPE.STORE_TYPE_GOBLIN_VALUE) {
                        functionConfig = DataFactory.getInstance().getGameObject(DataFactory.FUNCTION_KEY, FunctionFactory.UNLOCK_SHOP_SYSTEM);
                        if (lv < functionConfig.lv)
                            return true;
                        break;
                    }

                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }

            case E_MSG_ID.MsgID_Task_TaskList_VALUE:
            case E_MSG_ID.MsgID_Task_TaskReward_VALUE:
            case E_MSG_ID.MsgID_Task_TreasureReward_VALUE:
                functionConfig = DataFactory.getInstance().getGameObject(DataFactory.FUNCTION_KEY, FunctionFactory.UNLOCK_TASK_SYSTEM);
                if (lv < functionConfig.lv)
                    return true;
                break;

        }

        return false;
    }

}
