package com.douqu.game.core.entity.ext.data.challenge;

import com.bean.core.buffer.ByteBuffer;
import com.bean.core.util.TimeUtils;
import com.douqu.game.core.e.E_ExchangeRewardType;
import com.douqu.game.core.e.E_StableDataType;
import com.douqu.game.core.config.StableData;
import com.douqu.game.core.entity.db.ArenaRewardRecordDB;
import com.douqu.game.core.config.ExchangeRewardConfig;
import com.douqu.game.core.config.common.CommonData;
import com.douqu.game.core.entity.ext.BagInfo;
import com.douqu.game.core.entity.ext.data.BaseData;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.factory.DataFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author wangzhenfei
 *         2017-09-27 9:39
 *         竞技场信息
 */
public class ArenaData extends BaseData {

    /**
     * 自己的竞技场名次
     */
    private int mArenaRank;

    /**
     * 上一次领取奖励时间
     */
    private long lastRewardTime;

    /**
     * 当天挑战次数
     */
    private int todayChallengeTimes;

    /**
     * 当天的总挑战次数
     */
    private int todayTotalChallengeTime;

    /**
     * 最近一次挑战时间
     */
    private long lastChallengeTime;

    /**
     * 奖励兑换记录
     */
    private List<ArenaRewardRecordDB> rewardRecords;

    /**
     * 最大排名id
     */
    private int maxRank;

    private List<Integer> alreadyRewardIds;

    /**
     * 挑战胜利次数
     */
    private int totalWinCount;

    private Player player;

    public ArenaData(Player player) {
        this.player = player;

        rewardRecords = new CopyOnWriteArrayList<>();
        alreadyRewardIds = new CopyOnWriteArrayList<>();
    }



    @Override
    public void init() {
        StableData stableData = DataFactory.getInstance().getGameObject(DataFactory.STABLE_DATA_KEY,
                E_StableDataType.ARENA_FREE_TIMES.getCode());
        todayTotalChallengeTime = stableData.times;
    }

    @Override
    public void checkInit() {
        if(todayTotalChallengeTime == 0){
            init();
        }

    }

    @Override
    public void writeTo(ByteBuffer buffer)
    {
        buffer.writeShort(mArenaRank);
        buffer.writeLong(lastRewardTime);
        buffer.writeLong(lastChallengeTime);
        buffer.writeByte(todayChallengeTimes);
        buffer.writeByte(todayTotalChallengeTime);
        buffer.writeInt(rewardRecords.size());
        for(int i = 0; i < rewardRecords.size(); i++){
            rewardRecords.get(i).writeTo(buffer);
        }
        buffer.writeInt(alreadyRewardIds.size());
        for(int i = 0; i < alreadyRewardIds.size(); i++){
            buffer.writeShort(alreadyRewardIds.get(i));
        }
        buffer.writeInt(maxRank);
        buffer.writeInt(totalWinCount);
        buffer.writeInt(0);//备用
    }

    @Override
    public void loadFrom(ByteBuffer buffer)
    {
        mArenaRank = buffer.readShort();
        lastRewardTime = buffer.readLong();
        lastChallengeTime = buffer.readLong();
        todayChallengeTimes = buffer.readByte();
        todayTotalChallengeTime = buffer.readByte();
        int size = buffer.readInt();
        for(int i = 0; i < size; i++){
            rewardRecords.add(new ArenaRewardRecordDB().loadFrom(buffer));
        }

        size = buffer.readInt();
        for(int i = 0; i < size; i++){
            alreadyRewardIds.add(buffer.readShort());
        }
        maxRank = buffer.readInt();
        totalWinCount = buffer.readInt();
        buffer.readInt();
    }

    @Override
    public void reset() {
        todayChallengeTimes = 0;
        StableData stableData = DataFactory.getInstance().getGameObject(DataFactory.STABLE_DATA_KEY,
                E_StableDataType.ARENA_FREE_TIMES.getCode());
        todayTotalChallengeTime = stableData.times;
    }

    /**
     * 重置挑战次数
     */
    @Override
    public void checkReset()
    {
        if(!TimeUtils.isToday(lastChallengeTime))
            reset();
    }


    /**
     * 挑战次数 +1
     */
    public void addChallengeTimes(long time){
        todayChallengeTimes += 1;
        lastChallengeTime = time;
    }

    /**
     * 是否可以挑战
     */
    public boolean isChallengeTimesEnough(){
        return todayChallengeTimes < todayTotalChallengeTime;
    }

    /**
     * 是否有红点消息,登录面调用
     * @return
     */
    public boolean hasRedPointRemind(int myRank){
        if(isChallengeTimesEnough()){//可挑战
            return true;
        }
        if(!TimeUtils.isToday(lastRewardTime)){//每日排名奖励未领取
            return true;
        }
        //有可兑换的排名福利未兑换
        if(checkHasExchangeReward(myRank)){
            return true;
        }
        return false;
    }

    /**
     * 检测是否有可以兑换的奖励
     * @return
     */
    public boolean checkHasExchangeReward(int myRank) {
        List<ExchangeRewardConfig> rewardConfigs = DataFactory.getInstance().getDataList(DataFactory.REWARD_KEY);
        for(ExchangeRewardConfig rewardConfig : rewardConfigs){
            if(rewardConfig.type != E_ExchangeRewardType.ARENA.getCode()){
                continue;
            }

            if(alreadyRewardIds.contains(Integer.valueOf(rewardConfig.getId()))){
                continue;
            }

            if(myRank > rewardConfig.minRank){// 自己的排名大于要求的最低排名，不符合领取条件
                continue;
            }

            //循环配置表检测自己的资源是否足够
            BagInfo bagInfo = player.getExtInfo(BagInfo.class);
            boolean hasRedPoint = true;
            for(CommonData assets : rewardConfig.needAssets){
                if(bagInfo.getAsset(assets.id) < assets.value){//自己拥有的小于配置的
                    //TODO 资源不足
                    hasRedPoint = false;
                   break;
                }
            }
            if(hasRedPoint){
                return true;
            }

        }
        return false;
    }

    public int getRemainTodayChallengeTimes() {
        return todayTotalChallengeTime - todayChallengeTimes;
    }

    public int getTodayTotalChallengeTime() {
        return todayTotalChallengeTime;
    }

    /**
     *  购买完增加总挑战次数
     * @param times
     * @return
     */
    public int addTodayTotalChallengeTime(int times) {
        if(times > 0){
            todayTotalChallengeTime +=   times;
        }
        return todayTotalChallengeTime;
    }

    /**
     * 获取已经购买的次数
     * @return
     */
    public int getAlreadyBuyTimes(){
        StableData stableData = DataFactory.getInstance().getGameObject(DataFactory.STABLE_DATA_KEY,
                E_StableDataType.ARENA_FREE_TIMES.getCode());
        return todayTotalChallengeTime - stableData.times;
    }

    public long getLastRewardTime() {
        return lastRewardTime;
    }

    public void setLastRewardTime(long lastRewardTime) {
        this.lastRewardTime = lastRewardTime;
    }

    public long getLastChallengeTime() {
        return lastChallengeTime;
    }

    public List<ArenaRewardRecordDB> getRewardRecords() {
        return rewardRecords == null ? rewardRecords = new CopyOnWriteArrayList<>() : rewardRecords;
    }

    public int getTotalWinCount() {
        return totalWinCount;
    }

    public void winBattle(){
        totalWinCount++;
    }

    public List<Integer> getAlreadyRewardIds() {
        return alreadyRewardIds == null ? alreadyRewardIds = new CopyOnWriteArrayList<>() : alreadyRewardIds;
    }

    public int getMaxRank() {
        return maxRank;
    }

    public int changeMaxRank(int max) {
        maxRank = maxRank == 0? max :  Math.min(max, maxRank);
        return maxRank;
    }

    public int getTodayChallengeTimes() {
        return todayChallengeTimes;
    }

    public void setTodayChallengeTimes(int todayChallengeTimes) {
        this.todayChallengeTimes = todayChallengeTimes;
    }
}
