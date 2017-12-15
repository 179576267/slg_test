package com.douqu.game.core.entity.ext.data.boon;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.entity.ext.data.BaseData;

/**
 * @author wangzhenfei
 *         2017-10-28 16:14
 *         地精商店记录上一次领取的记录实体
 */
public class GoblinStoreRecordData extends BaseData {

    /**此字段作为唯一标识，修改hashcode基于此字段**/
    public int goodsId;
    public boolean isBuy;

    public GoblinStoreRecordData() {
    }

    public GoblinStoreRecordData(int goodsId) {
        this.goodsId = goodsId;
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
    public void writeTo(ByteBuffer buffer) {
        buffer.writeShort(goodsId);
        buffer.writeBoolean(isBuy);
        buffer.writeInt(0);
    }

    @Override
    public void loadFrom(ByteBuffer buffer) {
        goodsId = buffer.readShort();
        isBuy = buffer.readBoolean();
        buffer.readInt();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GoblinStoreRecordData)) return false;

        GoblinStoreRecordData that = (GoblinStoreRecordData) o;

        if (goodsId != that.goodsId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return goodsId;
    }

    @Override
    public String toString() {
        return "GoblinStoreRecordData{" +
                "goodsId=" + goodsId +
                ", isBuy=" + isBuy +
                "} " + super.toString();
    }
}
