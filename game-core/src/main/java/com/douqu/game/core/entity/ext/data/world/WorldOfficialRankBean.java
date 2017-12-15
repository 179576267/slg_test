package com.douqu.game.core.entity.ext.data.world;

import com.alibaba.druid.util.StringUtils;
import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.entity.ext.data.BaseData;


/**
 * @author wangzhenfei
 *         2017-10-19 14:13
 */
public class WorldOfficialRankBean extends BaseData
{
    private String objectIndex;
    private String name;
    private boolean isBattle;
    private int rankId;
    private int position;

    public WorldOfficialRankBean() {
    }

    public WorldOfficialRankBean(String objectIndex) {
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
        buffer.writeUTF(objectIndex);
        buffer.writeUTF(name);
        buffer.writeInt(0);
    }

    @Override
    public void loadFrom(ByteBuffer buffer)
    {
        objectIndex = buffer.readUTF();
        name = buffer.readUTF();
        buffer.readInt();
    }

    public boolean isPlayer()
    {
        return !StringUtils.isEmpty(objectIndex);
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

    public int getRankId() {
        return rankId;
    }

    public void setRankId(int rankId) {
        this.rankId = rankId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorldOfficialRankBean)) return false;

        WorldOfficialRankBean that = (WorldOfficialRankBean) o;

        if (objectIndex != null ? !objectIndex.equals(that.objectIndex) : that.objectIndex != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return objectIndex != null ? objectIndex.hashCode() : 0;
    }

    @Override
    public String toString() {
        return StringUtils.isEmpty(objectIndex) ? "{npc:" + name + "}": "{"+ objectIndex +"}";
    }
}
