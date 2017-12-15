package com.douqu.game.core.entity.db;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.config.challenge.InstanceConfig;
import com.douqu.game.core.config.challenge.InstanceStarBox;
import com.douqu.game.core.factory.DataFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author wangzhenfei
 *         2017-11-27 14:45
 */
public class InstanceDB extends DB{

    /**
     * 副本关卡
     */
    private List<LevelDB> finishLevelConfigs;

    private Map<Integer, LevelDB> levelMap;

    /**
     * 已经获取的地图奖励
     * Integer 记录所领取的ID
     */
    private List<Integer> hasReward;

    public InstanceDB() {
        super(DataFactory.INSTANCE_KEY);
        finishLevelConfigs = new CopyOnWriteArrayList<LevelDB>();
        levelMap =  new ConcurrentHashMap();
        hasReward = new CopyOnWriteArrayList<>();
    }

    public InstanceDB(int id) {
        super(DataFactory.INSTANCE_KEY, id);
        finishLevelConfigs = new CopyOnWriteArrayList<LevelDB>();
        levelMap =  new ConcurrentHashMap();
        hasReward = new CopyOnWriteArrayList<>();
    }


    @Override
    public void reset() {

    }


    @Override
    public void loadFrom(ByteBuffer buffer) {
        super.loadFrom(buffer);
        LevelDB levelDB;
        int levelSize = buffer.readByte();
        for(int j = 0 ; j < levelSize; j++) {
//            int maxStar = buffer.readInt();
//            int id = buffer.readInt();
//            boolean receive = buffer.readBoolean();
            levelDB = new LevelDB();
            levelDB.loadFrom(buffer);
//            levelDB.setMaxStars(maxStar);
//            levelDB.setReceive(receive);
            addLevel(levelDB);
        }

        int rewardSize = buffer.readByte();
        for(int j = 0; j < rewardSize; j ++){
            getHasReward().add(buffer.readInt());
        }
    }

    @Override
    public void writeTo(ByteBuffer buffer) {
        super.writeTo(buffer);
        buffer.writeByte(getFinishLevelConfigs().size());
        for(LevelDB l : getFinishLevelConfigs()){
            l.writeTo(buffer);
//            buffer.writeInt(l.getMaxStars());
//            buffer.writeInt(l.id);
//            buffer.writeBoolean(l.isReceive());

        }
        buffer.writeByte(getHasReward().size());
        for(Integer rewardId : getHasReward()){
            buffer.writeInt(rewardId);
        }
    }

    /**
     * 是否有红点提示
     * @return
     */
    public boolean hasRedPointRemind(){
        for(LevelDB levelDB : finishLevelConfigs){
            if(!levelDB.isReceive()){
                System.out.println("有关卡宝箱未领取 level id：" + levelDB.getConfig().id);
                return true;
            }
        }

        int sumMax = 0;
        for(LevelDB levelDB : finishLevelConfigs){
            sumMax += levelDB.getMaxStars();
        }

        InstanceConfig instanceConfig = getConfig();
        for(InstanceStarBox box : instanceConfig.starBox){
            if(sumMax >= box.getStar() && !hasReward.contains(Integer.valueOf(box.getId()))){//满足领取条件，但是没有领取
                System.out.println("有副本宝箱未领取 mapId ：" + id + ", 宝箱id：" + box.getId());
                return true;
            }
        }
        return false;
    }



    public Map<Integer, LevelDB> getLevelMap() {
        return levelMap;
    }

    public void setLevelMap(Map<Integer, LevelDB> levelMap) {
        this.levelMap = levelMap;
    }

    public List<LevelDB> getFinishLevelConfigs() {
        return finishLevelConfigs;
    }

    public void addLevel(LevelDB levelDB){
        if(levelDB != null && !levelMap.containsKey(levelDB.id)){
            levelMap.put(levelDB.id, levelDB);
            finishLevelConfigs.add(levelDB);
        }

    }

    public void setFinishLevelConfigs(List<LevelDB> finishLevelConfigs) {
        this.finishLevelConfigs = finishLevelConfigs;
    }

    public List<Integer> getHasReward() {
        return hasReward;
    }

    public void setHasReward(List<Integer> hasReward) {
        this.hasReward = hasReward;
    }

    @Override
    public InstanceConfig getConfig()
    {
        return (InstanceConfig) super.getConfig();
    }
}
