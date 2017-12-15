package com.douqu.game.core.entity.ext.data.challenge;


import com.bean.core.buffer.ByteBuffer;
import com.bean.core.util.TimeUtils;
import com.douqu.game.core.config.StableData;
import com.douqu.game.core.e.E_StableDataType;
import com.douqu.game.core.entity.ext.data.BaseData;
import com.douqu.game.core.factory.DataFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author wangzhenfei
 *         2017-11-28 16:46
 */
public class HeroTempleData extends BaseData {
    /**
     * 奖励兑换记录
     */
    private List<Integer> hasPassLevel;
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

    public HeroTempleData() {
        hasPassLevel = new CopyOnWriteArrayList<>();
    }

    @Override
    public void init() {
        StableData stableData = DataFactory.getInstance().getGameObject(DataFactory.STABLE_DATA_KEY,
                E_StableDataType.HERO_TEMPLE.getCode());
        todayTotalChallengeTime = stableData.times;
    }

    @Override
    public void checkInit() {
        if(todayTotalChallengeTime == 0){
            init();
        }

    }

    @Override
    public void reset() {
        todayChallengeTimes = 0;
        StableData stableData = DataFactory.getInstance().getGameObject(DataFactory.STABLE_DATA_KEY,
                E_StableDataType.HERO_TEMPLE.getCode());
        todayTotalChallengeTime = stableData.times;
    }

    @Override
    public void checkReset() {
        if(!TimeUtils.isToday(lastChallengeTime))
            reset();
    }

    @Override
    public void writeTo(ByteBuffer buffer) {
        buffer.writeShort(todayChallengeTimes);
        buffer.writeShort(todayTotalChallengeTime);
        buffer.writeLong(lastChallengeTime);
        buffer.writeInt(hasPassLevel.size());
        for(int i = 0; i < hasPassLevel.size(); i++){
            buffer.writeInt(hasPassLevel.get(i));
        }
        buffer.writeInt(0);
    }

    @Override
    public void loadFrom(ByteBuffer buffer) {
        todayChallengeTimes = buffer.readShort();
        todayTotalChallengeTime = buffer.readShort();
        lastChallengeTime = buffer.readLong();
        int size = buffer.readInt();
        for(int i = 0; i < size; i++){
            hasPassLevel.add(buffer.readInt());
        }
        buffer.readInt();
    }


    /**
     * 挑战次数 +1
     */
    public void addChallengeTimes(long time){
        todayChallengeTimes += 1;
        lastChallengeTime = time;
    }

    public List<Integer> getHasPassLevel() {
        return hasPassLevel;
    }

    public int getTodayRemainChallengeTimes() {
        return todayTotalChallengeTime - todayChallengeTimes;
    }

    public int getTodayChallengeTimes() {
        return todayChallengeTimes;
    }

    /**
     * 是否可以挑战
     */
    public boolean isChallengeTimesEnough(){
        return todayChallengeTimes < todayTotalChallengeTime;
    }

    public void addPassLevel(int levelId){
        if(!hasPassLevel.contains(levelId)){
            hasPassLevel.add(levelId);
        }
    }

    /**
     * 获取最大的关卡id
     */
    public int getMaxLevelId(){
        if(hasPassLevel.size() == 0){
            return -1;
        }

        return hasPassLevel.get(hasPassLevel.size() - 1);
    }

    /**
     * 是否有红点消息,登录面调用
     * @return
     */
    public boolean hasRedPointRemind(){
        if(isChallengeTimesEnough()){//可挑战
            return true;
        }
        return false;
    }
}
