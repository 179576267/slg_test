package com.douqu.game.core.entity.ext.data.boon;

import com.bean.core.buffer.ByteBuffer;
import com.bean.core.util.TimeUtils;
import com.douqu.game.core.e.E_StableDataType;
import com.douqu.game.core.config.StableData;
import com.douqu.game.core.entity.ext.data.BaseData;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.protobuf.SGCommonProto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author wangzhenfei
 *         2017-10-28 16:06
 *         商品的缓存
 *         
 */
public class StoreCacheData extends BaseData {
    /**最近一次的刷新时间**/
    private long lastFreshTime;
    /**已经刷新的次数**/
    private int hasRefreshTimes;
    /**总的刷新次数**/
    private int totalRefreshTimes;
    /**商品记录**/
    private List<GoblinStoreRecordData> records;

    /**缓存商店类型**/
    private int storeType;

    public StoreCacheData(int storeType) {
        lastFreshTime = 0;
        hasRefreshTimes = 0;
        records = new CopyOnWriteArrayList<>();
        this.storeType = storeType;
        init();
    }


    @Override
    public void init() {
        if(storeType == SGCommonProto.E_STORE_TYPE.STORE_TYPE_GOBLIN_VALUE){
            StableData stableData = DataFactory.getInstance().getGameObject(DataFactory.STABLE_DATA_KEY, E_StableDataType.GOBLIN_FRESH_TIMES.getCode());
            totalRefreshTimes = stableData.times;
        }
    }

    @Override
    public void checkInit() {
        if(totalRefreshTimes == 0){
            init();
        }
    }

    public boolean hasRedPointRemind(){
        return isFreeTimesEnough();
    }

    @Override
    public void reset()
    {
        hasRefreshTimes = 0;
        if(storeType == SGCommonProto.E_STORE_TYPE.STORE_TYPE_GOBLIN_VALUE){
            StableData stableData = DataFactory.getInstance().getGameObject(DataFactory.STABLE_DATA_KEY, E_StableDataType.GOBLIN_FRESH_TIMES.getCode());
            totalRefreshTimes = stableData.times;
        }
    }

    @Override
    public void checkReset() {
        if(!TimeUtils.isToday(hasRefreshTimes))
            reset();
    }

    @Override
    public void writeTo(ByteBuffer buffer) {
        buffer.writeLong(lastFreshTime);
        buffer.writeShort(hasRefreshTimes);
        buffer.writeShort(totalRefreshTimes);
        buffer.writeByte(records.size());
        for(GoblinStoreRecordData data : records){
            data.writeTo(buffer);
        }
        buffer.writeInt(0);
    }

    @Override
    public void loadFrom(ByteBuffer buffer) {
        lastFreshTime = buffer.readLong();
        hasRefreshTimes = buffer.readShort();
        totalRefreshTimes = buffer.readShort();
        int size = buffer.readByte();
        GoblinStoreRecordData recordData;
        for(int i = 0; i < size; i++){
            recordData = new GoblinStoreRecordData();
            recordData.loadFrom(buffer);
            getRecords().add(recordData);
        }
        buffer.readInt();
    }



    /**
     * 刷新次数 +1
     */
    public void addRefreshTimes(long time){
        hasRefreshTimes += 1;
        lastFreshTime = time;
    }




    /**
     * 是否可以免费刷新
     */
    public boolean isFreeTimesEnough(){
        return hasRefreshTimes < totalRefreshTimes;
    }


    public long getLastFreshTime() {
        return lastFreshTime;
    }

    public int getHasRefreshTimes() {
        return hasRefreshTimes;
    }

    /**
     * 获取当日剩余的刷新次数
     * @return
     */
    public int getRemindRefreshTimes(){
        return totalRefreshTimes - hasRefreshTimes;
    }

    public List<GoblinStoreRecordData> getRecords() {
        return records == null ? records = new CopyOnWriteArrayList<>() : records;
    }

    @Override
    public String toString() {
        return "GoblinStoreData{" +
                "lastFreshTime=" + lastFreshTime +
                ", totalRefreshTimes=" + totalRefreshTimes +
                ", hasRefreshTimes=" + hasRefreshTimes +
                ", records=" + records +
                "} " + super.toString();
    }
}
