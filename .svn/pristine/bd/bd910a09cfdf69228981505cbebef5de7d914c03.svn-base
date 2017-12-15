package com.douqu.game.core.entity.ext.data.challenge;

import com.bean.core.buffer.ByteBuffer;
import com.bean.core.util.TimeUtils;
import com.douqu.game.core.e.E_ExchangeRewardType;
import com.douqu.game.core.e.E_StableDataType;
import com.douqu.game.core.config.challenge.OfficialIntegralConfig;
import com.douqu.game.core.config.StableData;
import com.douqu.game.core.entity.db.ArenaRewardRecordDB;
import com.douqu.game.core.config.ExchangeRewardConfig;
import com.douqu.game.core.config.common.CommonData;
import com.douqu.game.core.entity.ext.BagInfo;
import com.douqu.game.core.entity.ext.data.BaseData;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.factory.ConfigFactory;
import com.douqu.game.core.factory.DataFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author wangzhenfei
 *         2017-10-18 18:38
 *         个人的官阶信息
 */
public class OfficialRankData extends BaseData {
    /**
     * 官阶id
     */
    private int rankId;

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
     * 最大官阶id
     */
    private int maxOfficialRankId;
    /**
     * 上一次领取奖励时间
     */
    private long lastRewardTime;

    /**
     * 已经兑换的奖励Id
     */
    private List<Integer> alreadyRewardIds;

    /**
     * 已经兑换的积分奖励Id
     */
    private List<Integer> integralRewardIds;
    /**
     * 奖励兑换记录
     */
    private List<ArenaRewardRecordDB> rewardRecords;

    private Player player;
    public OfficialRankData(Player player)
    {
        this.player = player;

        rewardRecords = new CopyOnWriteArrayList<>();
        alreadyRewardIds = new CopyOnWriteArrayList<>();
        integralRewardIds = new CopyOnWriteArrayList<>();
    }

    @Override
    public void init()
    {
        StableData stableData = DataFactory.getInstance().getGameObject(DataFactory.STABLE_DATA_KEY, E_StableDataType.OFFICIAL_RANK_FREE_TIMES.getCode());
        todayTotalChallengeTime = stableData.times;
    }

    @Override
    public void checkInit() {
        if(todayTotalChallengeTime == 0){
            init();
        }

    }


    @Override
    public void reset()
    {
        //清空积分
        BagInfo  bagInfo = player.getExtInfo(BagInfo.class);
        bagInfo.addAsset(ConfigFactory.ASSET_INTEGRAL_KEY, -bagInfo.getAsset(ConfigFactory.ASSET_INTEGRAL_KEY));

        todayChallengeTimes = 0;
        integralRewardIds.clear();
        StableData stableData = DataFactory.getInstance().getGameObject(DataFactory.STABLE_DATA_KEY, E_StableDataType.OFFICIAL_RANK_FREE_TIMES.getCode());
        todayTotalChallengeTime = stableData.times;
    }

    @Override
    public void checkReset() {
        if(!TimeUtils.isToday(lastChallengeTime)){
            reset();
        }
    }


    @Override
    public void writeTo(ByteBuffer buffer)
    {
        buffer.writeShort(rankId);
        buffer.writeShort(todayChallengeTimes);
        buffer.writeShort(todayTotalChallengeTime);
        buffer.writeShort(maxOfficialRankId);
        buffer.writeLong(lastChallengeTime);
        buffer.writeLong(lastRewardTime);
        buffer.writeInt(rewardRecords.size());
        for(int i = 0; i < rewardRecords.size(); i++){
            rewardRecords.get(i).writeTo(buffer);
        }
        buffer.writeInt(alreadyRewardIds.size());
        for(int i = 0; i < alreadyRewardIds.size(); i++){
            buffer.writeShort(alreadyRewardIds.get(i));
        }
        buffer.writeInt(integralRewardIds.size());
        for(int i = 0; i < integralRewardIds.size(); i++){
            buffer.writeShort(integralRewardIds.get(i));
        }

        buffer.writeInt(0);

    }

    @Override
    public void loadFrom(ByteBuffer buffer)
    {
        rankId = buffer.readShort();
        todayChallengeTimes = buffer.readShort();
        todayTotalChallengeTime = buffer.readShort();
        maxOfficialRankId = buffer.readShort();
        lastChallengeTime = buffer.readLong();
        lastRewardTime = buffer.readLong();
        int size = buffer.readInt();
        for(int i = 0; i < size; i++){
            rewardRecords.add(new ArenaRewardRecordDB().loadFrom(buffer));
        }

        size = buffer.readInt();
        for(int i = 0; i < size; i++){
            alreadyRewardIds.add(buffer.readShort());
        }

        size = buffer.readInt();
        for(int i = 0; i < size; i++){
            integralRewardIds.add(buffer.readShort());
        }
        buffer.readInt();
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
        if(checkHasIntegralExchangeReward()){
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
            if(rewardConfig.type != E_ExchangeRewardType.OFFICIAL_RANK.getCode()){
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

    /**
     * 检测是否有可以兑换的积分奖励
     * @return
     */
    public boolean checkHasIntegralExchangeReward() {
        boolean hasRedPoint = false;
        List<OfficialIntegralConfig> integralConfigs = DataFactory.getInstance().getDataList(DataFactory.OFFICIAL_INTEGRAL_REWARD_KEY);
        BagInfo bagInfo = player.getExtInfo(BagInfo.class);
        int integral = bagInfo.getAsset(ConfigFactory.ASSET_INTEGRAL_KEY);
        for(OfficialIntegralConfig config : integralConfigs){
            if(integral > config.integral && !integralRewardIds.contains(Integer.valueOf(config.getId()))){
                hasRedPoint = true;
                break;
            }
        }
        return hasRedPoint;
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
     * 获取已经购买的次数
     * @return
     */
    public int getAlreadyBuyTimes(){
        StableData stableData = DataFactory.getInstance().getGameObject(DataFactory.STABLE_DATA_KEY,
                E_StableDataType.OFFICIAL_RANK_FREE_TIMES.getCode());
        return todayTotalChallengeTime - stableData.times;
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


    public int getRemainTodayChallengeTimes() {
        return todayTotalChallengeTime - todayChallengeTimes;
    }

    public int getTodayRemainChallengeTimes() {
        return todayTotalChallengeTime - todayChallengeTimes;
    }

    public int getTodayChallengeTimes() {
        return todayChallengeTimes;
    }

    public int getMaxOfficialRankId() {
        return maxOfficialRankId;
    }

    public int changeMaxOfficialRankId(int max) {
        maxOfficialRankId = maxOfficialRankId == 0? max :  Math.min(max, maxOfficialRankId);
        return maxOfficialRankId;
    }

    public void setMaxOfficialRankId(int maxOfficialRankId) {
        this.maxOfficialRankId = maxOfficialRankId;
    }

    public long getLastRewardTime() {
        return lastRewardTime;
    }

    public void setLastRewardTime(long lastRewardTime) {
        this.lastRewardTime = lastRewardTime;
    }

    public List<Integer> getAlreadyRewardIds() {
        return alreadyRewardIds == null ? alreadyRewardIds = new CopyOnWriteArrayList<>() : alreadyRewardIds;
    }
    public List<ArenaRewardRecordDB> getRewardRecords() {
        return rewardRecords == null ? rewardRecords = new CopyOnWriteArrayList<>() : rewardRecords;
    }

    public List<Integer> getIntegralRewardIds() {
        return integralRewardIds == null ? integralRewardIds = new CopyOnWriteArrayList<>() : integralRewardIds;
    }

}
