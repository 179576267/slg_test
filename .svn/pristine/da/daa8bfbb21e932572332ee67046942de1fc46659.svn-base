package com.douqu.game.core.entity.ext.data.challenge;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.config.challenge.InstanceConfig;
import com.douqu.game.core.entity.db.InstanceDB;
import com.douqu.game.core.entity.db.LevelDB;
import com.douqu.game.core.entity.ext.data.BaseData;
import com.douqu.game.core.factory.DataFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-10-24 16:21
 */
public class InstanceData extends BaseData {

    /**
     * 已经完成的地图列表
     */
    private List<InstanceDB> instanceList;

    private Map<Integer, InstanceDB> instanceMap;

    /**
     * 可以进行的地图列表
     */
    private List<Integer> canPassMap;

    /**
     * 可以进行的地关卡列表
     */
    private List<Integer> nextPassLevel;

    /**
     * 缓存副本挑战结算信息，等待客户端调用
     */
    public PassLevelCache responseTemp;



    public InstanceData()
    {
        canPassMap = new CopyOnWriteArrayList<>();
        instanceList = new CopyOnWriteArrayList<>();
        instanceMap =  new ConcurrentHashMap();
        nextPassLevel = new CopyOnWriteArrayList<>();
    }


    @Override
    public void init()
    {
        changeNext();
    }

    @Override
    public void checkInit() {
        if(canPassMap.size() == 0)
            init();
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
        buffer.writeByte(instanceList.size());
        for(InstanceDB map : instanceList)
        {
            map.writeTo(buffer);
//            buffer.writeInt(map.id);
//            buffer.writeByte(map.getFinishLevelConfigs().size());
//            for(LevelDB l : map.getFinishLevelConfigs()){
//                buffer.writeInt(l.getMaxStars());
//                buffer.writeInt(l.id);
//                buffer.writeBoolean(l.isReceive());
//
//            }
//            buffer.writeByte(map.getHasReward().size());
//            for(Integer rewardId : map.getHasReward()){
//                buffer.writeInt(rewardId);
//            }
        }
        buffer.writeByte(canPassMap.size());
        for(Integer id : canPassMap){
            buffer.writeInt(id);
        }

        buffer.writeByte(nextPassLevel.size());
        for(Integer nextId : nextPassLevel){
            buffer.writeInt(nextId);
        }

        buffer.writeInt(0);//备用
    }

    @Override
    public void loadFrom(ByteBuffer buffer)
    {
        int instanceMapSize = buffer.readByte();
        InstanceDB insMap;
//        LevelDB levelDB;
        for(int i = 0 ; i < instanceMapSize; i++){
//            int mapId = buffer.readInt();
            insMap = new InstanceDB();
            insMap.loadFrom(buffer);
//            int levelSize = buffer.readByte();
//            for(int j = 0 ; j < levelSize; j++) {
//                int maxStar = buffer.readInt();
//                int id = buffer.readInt();
//                boolean receive = buffer.readBoolean();
//                levelDB = new LevelDB(id);
//                levelDB.setMaxStars(maxStar);
//                levelDB.setReceive(receive);
//                insMap.addLevel(levelDB);
//            }
//
//            int rewardSize = buffer.readByte();
//            for(int j = 0; j < rewardSize; j ++){
//                insMap.getHasReward().add(buffer.readInt());
//            }

            addInstanceMap(insMap);
        }
        int canPassMapSize = buffer.readByte();
        for(int i = 0; i < canPassMapSize; i++){
            canPassMap.add(buffer.readInt());
        }

        int nextLevelSize = buffer.readByte();
        for(int k = 0 ; k < nextLevelSize; k++){
            nextPassLevel.add(buffer.readInt());
        }

        if(canPassMap.size() == 0){
            changeNext();
        }
        buffer.readInt();
    }

    public InstanceDB getInstanceMap(int mapId)
    {
        return instanceMap.get(mapId);
    }

    public void addInstanceMap(InstanceDB map){
        if(map != null && !instanceMap.containsKey(map.id)){
            instanceList.add(map);
            instanceMap.put(map.id, map);

        }
    }

    /**
     * 是否有红点消息
     * @return
     */
    public boolean hasRedPointRemind(){
        for(InstanceDB instanceDB : instanceList){
            if(instanceDB.hasRedPointRemind()){
                return true;
            }
        }
        return false;
    }

    public void changeNext(){
        List<InstanceConfig> list = DataFactory.getInstance().getDataList(DataFactory.INSTANCE_KEY);
        Integer canPassMapId = canPassMap.size() == 0? 0 : canPassMap.get(canPassMap.size() - 1);
        if(canPassMapId == 0){ // 第一次
            //初始化第一张图的第一个关卡
            if(list.size() > 0){
                InstanceConfig map = list.get(0);
                canPassMap.add(map.getId());
                InstanceDB instanceDB = new InstanceDB(map.getId());
                addInstanceMap(instanceDB);
                if(map.levels.length > 0){
                    nextPassLevel.add(map.levels[0]);
                }

            }
        }else {
            InstanceConfig currentMap = null;
            for(InstanceConfig map : list){
                if(map.getId() == canPassMapId){
                    currentMap = map;
                    break;
                }
            }

            if(currentMap != null){
                //当前地图是否都过关了
                if(instanceMap.get(currentMap.getId()).getFinishLevelConfigs().size() == currentMap.levels.length){
                    //开启下张地图
                    canPassMapId = currentMap.unLockInstance;
                    canPassMap.add(currentMap.unLockInstance);
                    if(currentMap.unLockInstance != 0 ){

                        for(InstanceConfig map : list){
                            if(map.getId() == canPassMapId){
                                currentMap = map;
                                break;
                            }
                        }
                        if(currentMap != null){
                            nextPassLevel.clear();
                            nextPassLevel.add(currentMap.levels[0]);
                        }

                    }
                }else{
                    //只增加关卡
                    int index = instanceMap.get(currentMap.getId()).getFinishLevelConfigs().size();
                    nextPassLevel.clear();
                    nextPassLevel.add(currentMap.levels[index]);
                }
            }
        }

//        System.out.println("\n改变可通过关卡信息：\ncanPassMap : " + canPassMap + "\nnextLevel : " + nextPassLevel);

    }


    /**
     * 判断某一关卡是否通过了
     * @param levelId
     * @return
     */
    public boolean levelHasPass(int levelId){
        for(InstanceDB db :instanceList){
            List<LevelDB> finishLevelConfigs = db.getFinishLevelConfigs();
            for(LevelDB levelDB : finishLevelConfigs){
                if(levelDB.id == levelId){
                    return true;
                }
            }
        }

        return false;

    }

    public List<Integer> getCanPassMap() {
        return canPassMap;
    }

    public List<Integer> getNextPassLevel() {
        return nextPassLevel;
    }

    public List<InstanceDB> getInstanceList() {
        return instanceList;
    }

    public static class PassLevelCache implements Serializable {
        public int mapId;
        public int levelId;
        public int stars;

        public PassLevelCache(int mapId, int levelId, int stars) {
            this.mapId = mapId;
            this.levelId = levelId;
            this.stars = stars;
        }
    }

    public Map<Integer, InstanceDB> getInstanceMap() {
        return instanceMap;
    }

    public void setInstanceMap(Map<Integer, InstanceDB> instanceMap) {
        this.instanceMap = instanceMap;
    }
}
