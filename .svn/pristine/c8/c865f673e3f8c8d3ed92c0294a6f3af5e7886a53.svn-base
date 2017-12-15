package com.douqu.game.core.entity.ext;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.entity.WorldInfo;
import com.douqu.game.core.entity.ext.data.challenge.ArenaData;
import com.douqu.game.core.entity.ext.data.challenge.HeroTempleData;
import com.douqu.game.core.entity.ext.data.challenge.InstanceData;
import com.douqu.game.core.entity.ext.data.challenge.OfficialRankData;
import com.douqu.game.core.entity.ext.data.world.WorldOfficialRankBean;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.protobuf.SGCommonProto;

import java.util.ArrayList;
import java.util.List;


/**
 * @author wangzhenfei
 *         2017-10-18 18:12
 *         挑战的信息{1.副本， 2.竞技场， 3.官阶站}
 */
public class ChallengeInfo extends ExtInfo {

    private OfficialRankData officialData;

    private ArenaData arenaData;

    private InstanceData instanceData;

    private HeroTempleData heroTempleData;

    public ChallengeInfo(Player player, WorldInfo worldInfo)
    {
        super(player, worldInfo);

        officialData = new OfficialRankData(player);
        arenaData = new ArenaData(player);
        instanceData = new InstanceData();
        heroTempleData = new HeroTempleData();
    }

    @Override
    public void checkRedPointRemindAddGoods(SGCommonProto.E_GOODS_TYPE type, int id) {
        if(type != SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_ASSETS){ //官阶战和竞技场的兑换只会是消耗assets
            return;
        }
        WorldOfficialRankBean rankBean = worldInfo.getOfficialRankInfo().getRankInfoByObjectIndex(player);
        int myRank = rankBean== null ? 15 : rankBean.getRankId();
        if(officialData.checkHasExchangeReward(myRank)){
            player.cacheRedPoint(SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_OFFICIAL_RANK);
        }

        if(arenaData.checkHasExchangeReward(worldInfo.getArenaRankInfo().getArenaRankByObjectIndex(player.getObjectIndex()))){
            player.cacheRedPoint(SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_ARENA);
        }
    }

    @Override
    public List<SGCommonProto.E_RED_POINT_TYPE> checkRedPointRemindAllCondition(long currentTime) {
        WorldOfficialRankBean rankBean = worldInfo.getOfficialRankInfo().getRankInfoByObjectIndex(player);
        int myRank = rankBean== null ? 15 : rankBean.getRankId();
        List<SGCommonProto.E_RED_POINT_TYPE> list = null;
        if(officialData.hasRedPointRemind(myRank)){
            if(list == null){
                list = new ArrayList<>();
            }
            list.add(SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_OFFICIAL_RANK);
        }

        if(arenaData.hasRedPointRemind( worldInfo.getArenaRankInfo().getArenaRankByObjectIndex(player.getObjectIndex()))){
            if(list == null){
                list = new ArrayList<>();
            }
            list.add(SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_ARENA);
        }

        if(instanceData.hasRedPointRemind()){
            if(list == null){
                list = new ArrayList<>();
            }
            list.add(SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_INSTANCE);
        }

        if(heroTempleData.hasRedPointRemind()){
            if(list == null){
                list = new ArrayList<>();
            }
            list.add(SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_HERO_TEMPLE);
        }

        return list;
    }



    @Override
    public void checkInit()
    {
        officialData.checkInit();
        arenaData.checkInit();
        instanceData.checkInit();
        heroTempleData.checkInit();
    }

    @Override
    public void init()
    {
        officialData.init();
        arenaData.init();
        instanceData.init();
        heroTempleData.init();
    }

    @Override
    public void reset(){
        arenaData.reset();
        officialData.reset();
        instanceData.reset();
        heroTempleData.reset();
    }
    @Override
    public void checkReset()
    {
        arenaData.checkReset();
        officialData.checkReset();
        instanceData.checkReset();
        heroTempleData.checkReset();
    }

    @Override
    public void writeTo(ByteBuffer buffer)
    {
        officialData.writeTo(buffer);

        arenaData.writeTo(buffer);

        instanceData.writeTo(buffer);

        heroTempleData.writeTo(buffer);
    }

    @Override
    public void loadFrom(ByteBuffer buffer)
    {
        officialData.loadFrom(buffer);

        arenaData.loadFrom(buffer);

        instanceData.loadFrom(buffer);

        heroTempleData.loadFrom(buffer);
    }

    public OfficialRankData getOfficialData() {
        return officialData;
    }


    public ArenaData getArenaData() {
        return arenaData;
    }

    public InstanceData getInstanceData() {
        return instanceData;
    }

    public HeroTempleData getHeroTempleData() {
        return heroTempleData;
    }
}
