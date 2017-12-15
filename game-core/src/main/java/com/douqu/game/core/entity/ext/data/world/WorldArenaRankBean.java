package com.douqu.game.core.entity.ext.data.world;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.entity.ext.data.BaseData;

/**
 *
 * Created by data on 2017/9/27.
 */
public class WorldArenaRankBean extends BaseData {

    private int rank;
    private  String objectIndex;
    private boolean isBattle;

    public WorldArenaRankBean() {
    }

    public WorldArenaRankBean(String objectIndex) {
        this.objectIndex = objectIndex;
    }



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
        buffer.writeShort(rank);
        buffer.writeShortString(objectIndex);
        buffer.writeInt(0);
    }


    @Override
    public void loadFrom(ByteBuffer buffer)
    {
        rank = buffer.readShort();
        objectIndex = buffer.readShortString();
        buffer.readInt();
    }





    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getObjectIndex() {
        return objectIndex;
    }

    public void setObjectIndex(String objectIndex) {
        this.objectIndex = objectIndex;
    }

    public boolean isBattle() {
        return isBattle;
    }

    public void setBattle(boolean isBattle) {
        this.isBattle = isBattle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorldArenaRankBean)) return false;

        WorldArenaRankBean rank = (WorldArenaRankBean) o;

        if (objectIndex != null ? !objectIndex.equals(rank.objectIndex) : rank.objectIndex != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return objectIndex != null ? objectIndex.hashCode() : 0;
    }

    /**
     * objectIndex相同认为时同一个
     */


    @Override
    public String toString() {
        return "ArenaRank{" +
                "rank=" + rank +
                ", objectIndex='" + objectIndex + '\'' +
                '}';
    }
}
