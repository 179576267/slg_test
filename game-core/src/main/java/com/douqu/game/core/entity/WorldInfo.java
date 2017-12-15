package com.douqu.game.core.entity;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.database.model.ServerModel;
import com.douqu.game.core.entity.ext.data.world.WorldArenaRankData;
import com.douqu.game.core.entity.ext.data.world.WorldOfficialRankData;
import com.douqu.game.core.util.Utils;


/**
 * Created by bean on 2017/7/28.
 */
public class WorldInfo extends EntityObject {

    private int id;

    private int goodsIndex;

    private int playerIndex;

    private int battleId;

    private WorldOfficialRankData officialRankData;

    private WorldArenaRankData worldArenaRankData;

    public WorldInfo()
    {
        clear();
    }

    public void load(ServerModel serverModel)
    {
        if(serverModel == null)
            return;

        id = serverModel.getId();
        goodsIndex += serverModel.getGoodsIndex();
        playerIndex += serverModel.getPlayerIndex();
        battleId = 10000;
    }


    public void clear() {
        officialRankData = new WorldOfficialRankData();
        worldArenaRankData = new WorldArenaRankData();
    }


    public void init()
    {
        officialRankData.init();
        worldArenaRankData.init();
    }


    public void checkInit()
    {
        officialRankData.checkInit();
        worldArenaRankData.checkInit();
    }

    public void writeTo(ByteBuffer buffer)
    {
        officialRankData.writeTo(buffer);
        worldArenaRankData.writeTo(buffer);
        buffer.writeInt(0);
    }


    public void loadFrom(ByteBuffer buffer)
    {
        clear();

        officialRankData.loadFrom(buffer);
        worldArenaRankData.loadFrom(buffer);
        buffer.readInt();
    }


    public ServerModel createModel()
    {
        ServerModel serverModel = new ServerModel();
        serverModel.setId(id);
        serverModel.setPlayerIndex(goodsIndex);
        serverModel.setGoodsIndex(playerIndex);

        ByteBuffer buffer = new ByteBuffer();
        writeTo(buffer);
        serverModel.setWorldInfo(buffer.getBytes());

        return serverModel;
    }


//    /**
//     * 获取新的物品流水号
//     * @return
//     */
//    public String createGoodsIndex()
//    {
//        return id + "-" + Utils.createUUID(5) + "-" + (int) (Math.random() * 9000 + 1000) + "-" + (++goodsIndex);
//    }

    /**
     * 获取新的玩家流水号
     * @return
     */
    public String createPlayerIndex(int userId)
    {
        return id + "-" + (++playerIndex) + "-" + Utils.createUUID(5) + "-" + userId;
    }

    public int getServerId()
    {
        return id;
    }

    public String createBattleId()
    {
        return id + "-" + (++battleId) + "-" + Utils.createUUID(4);
    }

    public int getGoodsIndex() {
        return goodsIndex;
    }

    public void setGoodsIndex(int goodsIndex) {
        this.goodsIndex = goodsIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public WorldOfficialRankData getOfficialRankInfo() {
        return officialRankData;
    }

    public WorldArenaRankData getArenaRankInfo() {
        return worldArenaRankData;
    }
    @Override
    public String toString() {
        return "WorldRankInfo{" +
                "officialRankData=" + officialRankData +
                '}';
    }




}
