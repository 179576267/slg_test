package com.douqu.game.core.entity.ext.data.world;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.entity.ext.data.BaseData;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-10-24 17:02
 */
public class WorldArenaRankData extends BaseData {

    /**
     * 竞技场排行榜对象
     */
    private List<WorldArenaRankBean> arenaRankList = new CopyOnWriteArrayList<>();


    @Override
    public void init() {

    }

    @Override
    public void checkInit() {

    }



    @Override
    public void reset()
    {

    }

    @Override
    public void checkReset() {

    }

    @Override
    public void writeTo(ByteBuffer buffer)
    {
        int size = arenaRankList.size();
        buffer.writeInt(size);
        for(int i = 0; i < size; i++)
        {
            arenaRankList.get(i).writeTo(buffer);
        }
    }

    @Override
    public void loadFrom(ByteBuffer buffer)
    {
        int size = buffer.readInt();
        WorldArenaRankBean arenaRank = null;
        for(int i = 0; i < size; i++)
        {
            arenaRank = new WorldArenaRankBean();
            arenaRank.loadFrom(buffer);
            arenaRankList.add(arenaRank);
        }
    }



    public int getArenaRankByObjectIndex(String objectIndex){
        int rank = arenaRankList.indexOf(new WorldArenaRankBean(objectIndex)) + 1;
        if(rank == 0){ // 排行榜还没有自己
            addArenaRankInfo(objectIndex);
            return arenaRankList.size();
        }

        return rank;
    }

    /**
     * 添加一个排名
     * @param objectIndex
     * @return
     */
    private WorldArenaRankBean addArenaRankInfo(String objectIndex){
        WorldArenaRankBean rankInfo= new WorldArenaRankBean();
        rankInfo.setRank(arenaRankList.size() + 1);
        rankInfo.setObjectIndex(objectIndex);
        arenaRankList.add(rankInfo);
        return rankInfo;
    }


    /**
     * 根据排名获取排行榜信息
     * @param rank
     * @return
     */
    public WorldArenaRankBean getArenaRankInfo(int rank){
        int index = rank - 1;
        WorldArenaRankBean rankInfo = index < arenaRankList.size()? arenaRankList.get(index) : null ;
        return rankInfo;
    }

    /**
     * 根据指定排名下的所有玩家
     * @return
     */
    public List<WorldArenaRankBean> getUnderTargetRankList(int maxRank){
        maxRank = Math.min(maxRank, arenaRankList.size());
        return arenaRankList.subList(0, maxRank);
    }



    /**
     * 根据两个排行榜名次，交换排行榜信息
     * @param rank1
     * @param rank2
     * @return
     */
    public boolean swapArenaRank(int rank1, int rank2){
        WorldArenaRankBean rankInfo1 = rank1 - 1 < arenaRankList.size()? arenaRankList.get(rank1 - 1) : null ;
        WorldArenaRankBean rankInfo2 = rank2 - 1 < arenaRankList.size()? arenaRankList.get(rank2 - 1) : null ;
        if(rankInfo1 != null && rankInfo2 != null){
            rankInfo1.setRank(rank2);
            rankInfo2.setRank(rank1);
            Collections.swap(arenaRankList, rank1 - 1, rank2 - 1);
        }else {
            return false;
        }
        return true;
    }

    public List<WorldArenaRankBean> getArenaRankList() {
        return arenaRankList;
    }
}
