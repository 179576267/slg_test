package com.douqu.game.core.entity.ext.data.boon;

import com.bean.core.buffer.ByteBuffer;
import com.bean.core.util.TimeUtils;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.entity.ext.data.BaseData;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author wangzhenfei
 *         2017-10-28 16:06
 *         vip福利的缓存
 *
 */
public class RechargeRecordData extends BaseData {


    /**<充值id，结束时间>**/
    private Map<Integer, MouthCardRecord> rechargeRecordMap;

    /**vip特权礼包购买记录 存入vip等级**/
    private List<Integer> vipGiftBagRecordList;



    private Player player;
    public RechargeRecordData(Player player) {
        this.player = player;
        rechargeRecordMap = new ConcurrentHashMap<>();
        vipGiftBagRecordList = new CopyOnWriteArrayList<>();
    }

    @Override
    public void init() {

    }

    @Override
    public void checkInit() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void checkReset() {

    }

    public boolean hasRedPointRemind(){

        return false;
    }

    @Override
    public void writeTo(ByteBuffer buffer) {
        Iterator<Integer> iterator = rechargeRecordMap.keySet().iterator();
        Integer key;
        MouthCardRecord cardRecord;
        buffer.writeShort(rechargeRecordMap.size());
        while(iterator.hasNext()){
            key = iterator.next();
            cardRecord = rechargeRecordMap.get(key);
            buffer.writeInt(key);
            cardRecord.writeTo(buffer);
//            buffer.writeLong(endTime == null? 0 : endTime.longValue());
        }

        buffer.writeShort(vipGiftBagRecordList.size());
        for(Integer id : vipGiftBagRecordList){
            buffer.writeInt(id);
        }

        buffer.writeInt(0);
    }

    @Override
    public void loadFrom(ByteBuffer buffer) {
        int size = buffer.readShort();
        int key;
        MouthCardRecord cardRecord;
        for(int i = 0; i < size; i++){
            key = buffer.readInt();
            cardRecord = new MouthCardRecord();
            cardRecord.loadFrom(buffer);
            rechargeRecordMap.put(key, cardRecord);
        }
        size = buffer.readShort();
        for(int i = 0; i < size ; i++){
            vipGiftBagRecordList.add(buffer.readInt());
        }

        buffer.readInt();
    }

    public boolean hasRechargeRecord(int id){
        return rechargeRecordMap.get(id) != null;
    }

    public void addRecord(int id, long endTime){
        MouthCardRecord cardRecord = new MouthCardRecord();
        cardRecord.setEndTime(endTime);
        rechargeRecordMap.put(id, cardRecord);
    }

    public Map<Integer, MouthCardRecord> getRechargeRecordMap() {
        return rechargeRecordMap;
    }

    public void  addGiftBagVipId(int id){
        if(!vipGiftBagRecordList.contains(id)){
            vipGiftBagRecordList.add(id);
        }
    }

    public boolean hasGiftBagForVipId(int id){
        return vipGiftBagRecordList.contains(id);
    }

    public List<Integer> getGiftBagRecordList() {
        return vipGiftBagRecordList;
    }

    /**
     * 今天是否领取了月卡奖励
     * @return
     */
    public boolean isMouthCardRewardToday(int rechargeId){
        return TimeUtils.isToday(rechargeRecordMap.get(rechargeId).getLastRewardTime());
    }

    /**
     * 记录当天月卡领取记录
     * @param rechargeId
     * @param currentTime
     */
    public void recordMouthCardRewardTime(int rechargeId, long currentTime){
        rechargeRecordMap.get(rechargeId).setLastRewardTime(currentTime);
    }


    public boolean isFirstRechargeComplete(){
        return rechargeRecordMap.size() > 0;
    }

    @Override
    public String toString() {
        return "RechargeRecordData{" +
                "rechargeRecordMap=" + rechargeRecordMap +
                ", giftBagRecordList=" + vipGiftBagRecordList +
                ", player=" + player +
                "} " + super.toString();
    }


    public class MouthCardRecord{
        /**月卡结束时间**/
        private long endTime;
        /**上传领取月卡奖励的时间**/
        private long lastRewardTime;

        public void writeTo(ByteBuffer buffer) {
            buffer.writeLong(endTime);
            buffer.writeLong(lastRewardTime);
        }

        public void loadFrom(ByteBuffer buffer) {
            endTime = buffer.readLong();
            lastRewardTime = buffer.readLong();
        }


        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public long getLastRewardTime() {
            return lastRewardTime;
        }

        public void setLastRewardTime(long lastRewardTime) {
            this.lastRewardTime = lastRewardTime;
        }
    }
}
