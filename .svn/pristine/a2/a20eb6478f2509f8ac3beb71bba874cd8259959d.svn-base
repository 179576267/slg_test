package com.douqu.game.main.msg;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.config.PlayerLvConfig;
import com.douqu.game.core.config.RechargeConfig;
import com.douqu.game.core.config.VipConfig;
import com.douqu.game.core.config.sprite.MasterConfig;
import com.douqu.game.core.controller.PlayerController;
import com.douqu.game.core.database.model.PlayerModel;
import com.douqu.game.core.config.common.CommonData;
import com.douqu.game.core.e.E_ProfileVersion;
import com.douqu.game.core.entity.WorldInfo;
import com.douqu.game.core.entity.common.TwoTuple;
import com.douqu.game.core.entity.db.AccessoryDB;
import com.douqu.game.core.entity.db.CardDB;
import com.douqu.game.core.entity.db.EquipDB;
import com.douqu.game.core.entity.db.PropDB;
import com.douqu.game.core.entity.ext.BagInfo;
import com.douqu.game.core.config.goods.PropConfig;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.CodeFactory;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.netty.NettyConnection;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.util.SendUtils;
import com.douqu.game.core.util.Utils;
import com.douqu.game.main.GameServer;
import com.douqu.game.main.server.SpringContext;
import com.douqu.game.main.server.WorldManager;
import com.douqu.game.main.service.PlayerService;
import io.netty.buffer.ByteBuf;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-11-08 18:18
 */
public class GMChannel {

    static Logger logger = Logger.getLogger(GMChannel.class);

    public static void messageChannel(int code, NettyConnection connection, ByteBuf byteBuf)
    {
        logger.info("收到GM消息:" + code);

        ByteBuffer data = new ByteBuffer(Utils.byteBufToBytes(byteBuf));

        try{
            switch (code)
            {
                case CodeFactory.GM_PLAYER_QUERY:
                    playerQuery(connection, data);
                    break;
                case CodeFactory.GM_PLAYER_OPTION:
                    playerOption(connection, data);
                    break;
                case CodeFactory.GM_PLAYER_ONLINE:
                    playerOnline(connection, data);
                    break;
                case CodeFactory.GM_PLAYER_ALL:
                    playerAll(connection, data);
                    break;
                case CodeFactory.GM_PLAYER_MAIL:
                    sendMail(connection, data);
                    break;
                case CodeFactory.GM_PLAYER_RECHARGE:
                    recharge(connection, data);
                    break;
                case CodeFactory.GM_PLAYER_CLEAR:
                    playerClear(connection, data);
                    break;
                case CodeFactory.GM_SYSTEM_SEND_NOTICE:
                    sendNotice(connection, data);
                    break;
                case CodeFactory.GM_SYSTEM_RECHARGE_LIST:
                    rechargeList(connection, data);
                    break;
            }
        }catch (Exception e) {
            logger.info("GM操作参数错误 -> " + e.getLocalizedMessage());
            e.printStackTrace();
            sendError(connection, "参数错误!");
        }
    }

    private static void playerClear(NettyConnection connection, ByteBuffer data)
    {
        int uid = data.readInt();

        TwoTuple<Player, PlayerController> playerResult = getPlayer(uid);
        if(playerResult.getFirst() == null)
        {
            sendError(connection, "玩家不存在");
            return;
        }

        Player player = playerResult.getFirst();

        player.lock = true;

        player.clearAll();

        if(playerResult.getSecond() != null)
        {
            PlayerMsgChannel playerMsgChannel = SpringContext.getBean(PlayerMsgChannel.class);
            playerMsgChannel.playerBaseInfo(playerResult.getSecond());
        }
        else
        {
            PlayerService playerService = SpringContext.getBean(PlayerService.class);
            playerService.update(player);
        }

        player.lock = false;

        sendPlayerInfo(connection, player);
    }

    private static void rechargeList(NettyConnection connection, ByteBuffer data)
    {
        List<RechargeConfig> list = DataFactory.getInstance().getDataList(DataFactory.RECHARGE_KEY);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;
        for(RechargeConfig recharge : list)
        {
            jsonObject = new JSONObject();
            jsonObject.put("id", recharge.id);
            jsonObject.put("type", recharge.type);
            jsonObject.put("rmb", recharge.rmb);
            jsonObject.put("money", recharge.money);
            jsonObject.put("extra", recharge.extra);
            jsonArray.add(jsonObject);
        }

        ByteBuffer out = new ByteBuffer();
        out.writeUTF(jsonArray.toJSONString());

        connection.sendMsg(CodeFactory.GM_SYSTEM_RECHARGE_LIST, out.getBytes());
    }

    private static void sendNotice(NettyConnection connection, ByteBuffer data)
    {

    }

    private static void recharge(NettyConnection connection, ByteBuffer data)
    {
        String playerIndex = data.readUTF();
        int rechargeId = data.readInt();
        RechargeConfig rechargeConfig = DataFactory.getInstance().getGameObject(DataFactory.RECHARGE_KEY, rechargeId);
        if(rechargeConfig == null)
        {
            sendError(connection, "没有此充值配置");
            return;
        }

        TwoTuple<Player, PlayerController> result= getPlayer(playerIndex);
        Player player = result.getFirst();
        if(player == null)
        {
            sendError(connection, "玩家不存在");
            return;
        }

        int total = rechargeConfig.money + rechargeConfig.extra;
        player.addMoney(total);
        player.addVipExp(total);

        if(result.getSecond() != null)
        {
            SendUtils.sendChangeData(0, result.getSecond());

            result.getSecond().sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, "GM给你充值了" + total + "钻石!");
        }
        else
        {
            PlayerService playerService = SpringContext.getBean(PlayerService.class);
            playerService.update(player);
        }

        connection.sendMsg(CodeFactory.GM_PLAYER_RECHARGE, null);
    }

    private static void sendMail(NettyConnection connection, ByteBuffer data)
    {

    }


    private static void playerAll(NettyConnection connection, ByteBuffer data)
    {
        PlayerService playerService = SpringContext.getBean(PlayerService.class);
        int page = data.readShort();
        int count = data.readByte();
        if(page <= 0 || count <= 0)
        {
            sendError(connection, "参数错误");
            return;
        }

        List<PlayerModel> playerList = playerService.getPlayerList(page, count);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;
        WorldManager worldManager = GameServer.getInstance().getWorldManager();
        for(PlayerModel player : playerList)
        {
            jsonObject = new JSONObject();
            jsonObject.put("channel", getChannel(player));
            jsonObject.put("objectIndex", player.getObjectIndex());
            jsonObject.put("uid", player.getUid());
            jsonObject.put("name", player.getName());
            jsonObject.put("camp", player.getCamp());
            jsonObject.put("lv", player.getLevel());
            jsonObject.put("fc", player.getFc());
            jsonObject.put("money", player.getMoney());
            jsonObject.put("online", worldManager.getPlayerControllerByUid(player.getUid())!=null);
            jsonObject.put("isDel", player.getIsDel());
            jsonArray.add(jsonObject);
        }

        JSONObject response = new JSONObject();
        response.put("page", page);
        response.put("count", count);
        response.put("totalCount", playerService.getAllPlayerCount());
        response.put("data", jsonArray.toJSONString());

        ByteBuffer out = new ByteBuffer();
        out.writeUTF(response.toJSONString());
        connection.sendMsg(CodeFactory.GM_PLAYER_ALL, out.getBytes());
    }

    private static void playerOnline(NettyConnection connection, ByteBuffer data)
    {
        List<PlayerController> onlinePlayers = GameServer.getInstance().getWorldManager().getPlayerList();
        JSONArray response = new JSONArray();
        JSONObject jsonObject = null;
        for(PlayerController player : onlinePlayers)
        {
            jsonObject = new JSONObject();
            jsonObject.put("channel", getChannel(player));
            jsonObject.put("objectIndex", player.getObjectIndex());
            jsonObject.put("uid", player.getPlayer().getUid());
            jsonObject.put("name", player.getName());
            jsonObject.put("camp", player.getPlayer().camp);
            jsonObject.put("lv", player.getPlayer().getLv());
            jsonObject.put("fc", player.getPlayer().fc);
            jsonObject.put("money", player.getPlayer().money);
            jsonObject.put("ip", player.getIp());
            jsonObject.put("isDel", player.getPlayer().isDel);
            jsonObject.put("online", true);
            jsonObject.put("invincible", player.getPlayer().invincible);
            response.add(jsonObject);
        }

        ByteBuffer out = new ByteBuffer();
        out.writeUTF(response.toJSONString());
        connection.sendMsg(CodeFactory.GM_PLAYER_ONLINE, out.getBytes());
    }



    private static void playerQuery(NettyConnection connection, ByteBuffer data)
    {
        String queryType = data.readUTF();
        String queryValue = data.readUTF();
        PlayerController playerController = null;
        Player player = null;
        WorldManager worldManager = GameServer.getInstance().getWorldManager();
        switch (queryType)
        {
            case CodeFactory.QUERY_TYPE_NICK_NAME:
                playerController = worldManager.getPlayerControllerByName(queryValue);
                break;
            case CodeFactory.QUERY_TYPE_UID:
                playerController = worldManager.getPlayerControllerByUid(Integer.parseInt(queryValue));
                break;
            case CodeFactory.QUERY_TYPE_OBJECT_INDEX:
                playerController = worldManager.getPlayerController(queryValue);
                break;
        }

        if(playerController == null)
        {
            PlayerService playerService = SpringContext.getBean(PlayerService.class);
            PlayerModel playerModel = null;
            switch (queryType)
            {
                case CodeFactory.QUERY_TYPE_NICK_NAME:
                    playerModel = playerService.getPlayerByName(queryValue);
                    break;
                case CodeFactory.QUERY_TYPE_UID:
                    playerModel = playerService.getPlayerByUid(Integer.parseInt(queryValue));
                    break;
                case CodeFactory.QUERY_TYPE_OBJECT_INDEX:
                    playerModel = playerService.getPlayerByIndex(queryValue);
                    break;
            }
            if(playerModel != null)
            {
                player = new Player(playerModel);
            }
        }
        else
        {
            player = playerController.getPlayer();
        }

        if(player == null)
        {
            sendError(connection, "玩家不存在!");
            return;
        }

        sendPlayerInfo(connection, player);
    }

    private static void sendPlayerInfo(NettyConnection connection, Player player)
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("channel", getChannel(player));
        jsonObject.put("name", player.name);
        jsonObject.put("uid", player.getUid());
        jsonObject.put("account", player.getAccount()==null?"":player.getAccount().substring(player.getAccount().indexOf("_")+1));
        jsonObject.put("objectIndex", player.objectIndex);
        jsonObject.put("fc", player.fc);

        PlayerLvConfig playerLv = DataFactory.getInstance().getGameObject(DataFactory.PLAYER_LV_KEY, player.getLv());
        PlayerLvConfig nextLv = DataFactory.getInstance().getGameObject(DataFactory.PLAYER_LV_KEY, player.getLv()+1);
        jsonObject.put("lv", player.getLv() + "(" + (player.getExp() - playerLv.totalExp) + "/" + (nextLv!=null?nextLv.exp:playerLv.exp) + ")");

        VipConfig vipConfig = DataFactory.getInstance().getGameObject(DataFactory.VIP_KEY, player.getVipLevel());
        VipConfig nextVip = DataFactory.getInstance().getGameObject(DataFactory.VIP_KEY, player.getVipLevel()+1);
        jsonObject.put("vipLv", player.getVipLevel() + "(" + (player.getVipExp() - vipConfig.needRecharge) + "/" + (nextVip!=null?nextVip.needRecharge-vipConfig.needRecharge:vipConfig.needRecharge) + ")");

        jsonObject.put("assetInfo", getAssetStr(player));
        jsonObject.put("propInfo", getPropStr(player));
        jsonObject.put("cardInfo", getCardStr(player));

        ByteBuffer out = new ByteBuffer();
        out.writeUTF(jsonObject.toJSONString());

        connection.sendMsg(CodeFactory.GM_PLAYER_QUERY, out.getBytes());
    }

    private static void playerOption(NettyConnection connection, ByteBuffer data)
    {
        String cmd = data.readUTF();
        int uid = data.readInt();
        int id = data.readInt();
        int count = data.readInt();
        if(count == 0)
        {
            sendError(connection, "请输入有效的数量!");
            return;
        }

        logger.info("对玩家进行操作 -> cmd:" + cmd +" uid:" + uid + " id:" + id + " count:" + count);

        TwoTuple<Player, PlayerController> playerResult= getPlayer(uid);
        Player player = playerResult.getFirst();
        if(player == null)
        {
            sendError(connection, "玩家不存在");
            return;
        }

        ByteBuffer out = new ByteBuffer();
        out.writeUTF(cmd);

        BagInfo bagInfo = player.getExtInfo(BagInfo.class);
        switch (cmd)
        {
            case CodeFactory.GM_INVINCIBLE:
                if(!SpringContext.getProfile().equals(E_ProfileVersion.DEV.getKey()))
                {
                    sendError(connection, "非开发版不允许操作此功能");
                    return;
                }
                if(playerResult.getSecond() == null)
                {
                    sendError(connection, "不允许对离线玩家操作此功能");
                    return;
                }
                player.invincible = !player.invincible;

                if(player.invincible)
                {
                    playerResult.getSecond().sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, "你已被GM设置为无敌状态,赶快进入新一轮战斗体验吧!");
                }
                else
                {
                    playerResult.getSecond().sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, "你已被GM恢复为默认状态,将在新的战斗中生效!");
                }
                logger.info("玩家:" + player.getName() + " 是否无敌:" + player.invincible);
                out.writeInt(uid);
                out.writeBoolean(player.invincible);
                break;
            case CodeFactory.GM_ADD_PROP:
                int addResult = bagInfo.addProp(id, count);
                if(addResult != -1)
                {
                    out.writeUTF(getPropStr(player));
                    PropConfig prop = DataFactory.getInstance().getGameObject(DataFactory.PROP_KEY, id);
                    if(prop.type == ConstantFactory.PROP_TYPE_CARD_SYN)
                        out.writeUTF(getCardStr(player));
                }
                else
                {
                    sendError(connection, "参数错误!");
                    return;
                }
                break;
            case CodeFactory.GM_ADD_ASSET:
                boolean assetResult = bagInfo.addAsset(id, count);
                if(assetResult)
                    out.writeUTF(getAssetStr(player));
                else
                {
                    sendError(connection, "参数错误!");
                    return;
                }
                break;
            case CodeFactory.GM_ADD_CARD:
                TwoTuple<CommonData, CommonData> cardResult = bagInfo.addCard(id, count);
                if(cardResult.isNull())
                {
                    sendError(connection, "参数错误!");
                    return;
                }
                else
                {
                    out.writeUTF(getCardStr(player));
                    if(cardResult.getSecond() != null)//分解成碎片
                        out.writeUTF(getPropStr(player));
                }
                break;
            case CodeFactory.GM_ADD_MONEY:
                player.addMoney(count);
                out.writeUTF(getAssetStr(player));
                break;
            case CodeFactory.GM_ADD_EXP:
                if(count <= 0)
                {
                    sendError(connection, "参数错误!");
                    return;
                }
                TwoTuple<Boolean, Boolean> expResult = player.addExp(count);
                out.writeInt(player.getLv());
                if(playerResult.getSecond() != null)
                {
                    //TODO 推送
                    if(expResult.getFirst())//推送升级消息
                    {
                        playerResult.getSecond().updateFC();
                    }
                    if(expResult.getSecond())//推送获得技能消息
                    {
                        SendUtils.sendRedPointRemind(playerResult.getSecond(), SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_MASTER);
                    }
                }
                break;
        }

        if(playerResult.getSecond() == null)
        {
            PlayerService playerService = SpringContext.getBean(PlayerService.class);
            playerService.update(player);
        }
        else
        {
            SendUtils.sendChangeData(0, playerResult.getSecond());
        }

        connection.sendMsg(CodeFactory.GM_PLAYER_OPTION, out.getBytes());
    }



    public static void sendError(NettyConnection connection, String error)
    {
        ByteBuffer out = new ByteBuffer();
        out.writeUTF(error);
        connection.sendMsg(CodeFactory.GM_ERROR, out.getBytes());
    }


    public static String getPropStr(Player player)
    {
        JSONArray jsonArray = new JSONArray();
        BagInfo bagInfo = player.getExtInfo(BagInfo.class);
        List<PropDB> propList = bagInfo.getPropList();
        JSONObject jsonObject = null;
        GameObject gameObject = null;
        DataFactory dataFactory = DataFactory.getInstance();
        for(PropDB prop : propList)
        {
            jsonObject = new JSONObject();
            jsonObject.put("propId", prop.id);
            gameObject = dataFactory.getGameObject(DataFactory.PROP_KEY, prop.id);
            jsonObject.put("propName", gameObject.name);
            jsonObject.put("propEffect", prop.getConfig().effectValue);
            jsonObject.put("propCount", prop.count);
            jsonArray.add(jsonObject);
        }

        return jsonArray.toJSONString();
    }

    public static String getCardStr(Player player)
    {
        JSONArray jsonArray = new JSONArray();
        BagInfo bagInfo = player.getExtInfo(BagInfo.class);
        List<CardDB> cardList = bagInfo.getCardData().getAllCardList();
        StringBuilder temp = null;
        JSONObject jsonObject = null;
        GameObject gameObject = null;
        DataFactory dataFactory = DataFactory.getInstance();
        JSONArray equipList = null;
        JSONObject equipInfo = null;
        EquipDB equipDB = null;
        AccessoryDB accessoryDB = null;
        for(CardDB cardDB : cardList)
        {
            jsonObject = new JSONObject();
            jsonObject.put("cardId", cardDB.id);
            gameObject = dataFactory.getGameObject(DataFactory.CARD_KEY, cardDB.id);
            jsonObject.put("cardName", gameObject.name);
            jsonObject.put("cardLv", cardDB.getLv());
            jsonObject.put("cardStar", cardDB.star);
            temp = new StringBuilder();
            for(Integer fateId : cardDB.getActiveFateList())
            {
                gameObject = dataFactory.getGameObject(DataFactory.CARD_FATE_KEY, fateId);
                temp.append(gameObject.name);
                temp.append(",");
            }
            jsonObject.put("cardFate", temp.length() > 0 ? temp.substring(0, temp.length()-1) : "无");
            jsonObject.put("cardPropCount", bagInfo.getPropCount(cardDB.getConfig().lvUpProp));
            jsonObject.put("fc", cardDB.fc);
            jsonObject.put("battle", bagInfo.isBattleCard(cardDB.id));

            equipList = new JSONArray();
            for(Integer key : cardDB.getEquipMap().keySet())
            {
                equipDB = cardDB.getEquipDB(key);
                equipInfo = new JSONObject();
                equipInfo.put("id", equipDB.id);
                equipInfo.put("name", equipDB.getConfig().name);
                equipInfo.put("lv", equipDB.getLv());
                equipInfo.put("synCount", equipDB.upCount);
                equipList.add(equipInfo);
            }

            jsonObject.put("equipInfo", equipList);

            equipList = new JSONArray();
            for(Integer key : cardDB.getAccessoryMap().keySet())
            {
                accessoryDB = cardDB.getAccessoryDB(key);
                equipInfo = new JSONObject();
                equipInfo.put("id", accessoryDB.id);
                equipInfo.put("name", accessoryDB.getConfig().name);
                equipInfo.put("lv", accessoryDB.getLv());
                equipInfo.put("upLv", accessoryDB.upLv);
                equipInfo.put("fc", 0);
                equipList.add(equipInfo);
            }

            jsonObject.put("accessoryInfo", equipList);

            jsonArray.add(jsonObject);
        }

        return jsonArray.toJSONString();
    }

    public static String getAssetStr(Player player)
    {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;
        DataFactory dataFactory = DataFactory.getInstance();
        GameObject gameObject = null;
        BagInfo bagInfo = player.getExtInfo(BagInfo.class);
        Map<Integer, Integer> assetMap = bagInfo.getAssetData();
        for(Integer key : assetMap.keySet())
        {
            jsonObject = new JSONObject();
            jsonObject.put("assetId", key);
            gameObject = dataFactory.getGameObject(DataFactory.ASSET_KEY, key);
            jsonObject.put("assetName", gameObject.name);
            jsonObject.put("assetCount", assetMap.get(key));
            jsonArray.add(jsonObject);
        }

        return jsonArray.toJSONString();
    }


    public static String getChannel(Object player)
    {
        String account = null;
        if(player instanceof Player)
        {
            account = ((Player) player).getAccount();
        }
        else if(player instanceof PlayerModel)
        {
            account = ((PlayerModel) player).getAccount();
        }
        else if(player instanceof PlayerController)
        {
            account = ((PlayerController) player).getPlayer().getAccount();
        }
        if(StringUtils.isEmpty(account))
            return "";

        return account.indexOf("_") == -1 ? account : account.substring(0, account.indexOf("_"));
    }

    public static TwoTuple<Player, PlayerController> getPlayer(String playerIndex)
    {
        TwoTuple<Player, PlayerController> result = null;
        Player player = null;
        PlayerController playerController = GameServer.getInstance().getWorldManager().getPlayerController(playerIndex);
        if(playerController != null)
        {
            result = new TwoTuple<>(playerController.getPlayer(), playerController);
        }
        else
        {
            PlayerService playerService = SpringContext.getBean(PlayerService.class);
            PlayerModel playerModel = playerService.getPlayerByIndex(playerIndex);
            if(playerModel == null)
            {
                return null;
            }
            result = new TwoTuple<>(new Player(playerModel));
        }

        return result;
    }

    public static TwoTuple<Player, PlayerController> getPlayer(int uid)
    {
        TwoTuple<Player, PlayerController> result = null;
        PlayerController playerController = GameServer.getInstance().getWorldManager().getPlayerControllerByUid(uid);
        if(playerController != null)
        {
            result = new TwoTuple<>(playerController.getPlayer(), playerController);
        }
        else
        {
            PlayerService playerService = SpringContext.getBean(PlayerService.class);
            PlayerModel playerModel = playerService.getPlayerByUid(uid);
            if(playerModel == null)
            {
                return null;
            }
            result = new TwoTuple<>(new Player(playerModel));
        }

        return result;
    }

}
