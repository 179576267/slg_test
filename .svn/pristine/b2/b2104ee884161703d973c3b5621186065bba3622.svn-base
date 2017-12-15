package com.douqu.game.core.entity.ext;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.entity.WorldInfo;
import com.douqu.game.core.config.common.CommonData;
import com.douqu.game.core.config.sprite.MasterConfig;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.protobuf.SGCommonProto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author wangzhenfei
 *         2017-11-03 17:10
 *         玩家的设置信息
 */
public class SettingInfo extends ExtInfo {
    /**消费不提醒 存储的是不需要提醒的id**/
    private List<Integer> consumeNotRemindList;

    /**用户姓名更改次数**/
    private int playerNameChangeTimes;

    /**用户上阵技能**/
    private List<Integer> equipSkillIds;

    /**新解锁的技能id**/
    private int newUnlockSkillId;

    public SettingInfo(Player player, WorldInfo worldInfo) {
        super(player, worldInfo);

        consumeNotRemindList = new CopyOnWriteArrayList<>();
        equipSkillIds = new CopyOnWriteArrayList<>();
    }

    @Override
    public void writeTo(ByteBuffer buffer) {
        buffer.writeByte(playerNameChangeTimes);
        int size = consumeNotRemindList.size();
        buffer.writeByte(size);
        for(int i = 0 ; i < size; i++){
            buffer.writeShort(consumeNotRemindList.get(i));
        }
        size = equipSkillIds.size();
        if(size > 2){
            try {
                throw new Exception("SettingInfo writeTo error equipSkillIds size > 2");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        buffer.writeByte(size);
        for(int i = 0 ; i < size; i++){
            buffer.writeInt(equipSkillIds.get(i));
        }
        buffer.writeShort(newUnlockSkillId);
        buffer.writeInt(0);

    }


    @Override
    public void loadFrom(ByteBuffer buffer) {

        playerNameChangeTimes = buffer.readByte();
        int size = buffer.readByte();
        for(int i = 0 ; i < size; i++){
            consumeNotRemindList.add(buffer.readShort());
        }
        size = buffer.readByte();
        if(size > 2){
            try {
                throw new Exception("SettingInfo loadFrom error equipSkillIds size > 2");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i = 0 ; i < size; i++){
            equipSkillIds.add(buffer.readInt());
        }
        newUnlockSkillId = buffer.readShort();
        buffer.readInt();
    }


    @Override
    public List<SGCommonProto.E_RED_POINT_TYPE> checkRedPointRemindAllCondition(long currentTime) {
        if(hasNewUnlockSkill()){
            List<SGCommonProto.E_RED_POINT_TYPE> list = new ArrayList<>();
            list.add(SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_MASTER);
            return list;
        }
        return super.checkRedPointRemindAllCondition(currentTime);

    }

    public void addPlayerNameChangeTimes(){
        playerNameChangeTimes++;
        if(playerNameChangeTimes > Byte.MAX_VALUE){
            playerNameChangeTimes = 10;
        }
    }

    /**
     * 改变上阵技能
     * @param addSkillId
     * @param removeSkillId
     */
    public void changeEquipSkill(int addSkillId, int removeSkillId){
        int index = equipSkillIds.indexOf(Integer.valueOf(removeSkillId));
        equipSkillIds.remove(Integer.valueOf(removeSkillId));
        if(equipSkillIds.size() >= 2){
            return;
        }
        equipSkillIds.add(index, addSkillId);
    }

    /**
     * 升级的时候添加默认上阵技能
     */
    public void levelUpToAddDefaultSkill() {
        int equipSkillSize = equipSkillIds.size();
        if(equipSkillSize < 2){ //只有0和1技能
            MasterConfig masterConfig = DataFactory.getInstance().getGameObject(DataFactory.MASTER_KEY, player.master);
            if(masterConfig == null){
                System.out.println("SettingInfo checkInit() master 找不到 master：" + player.master);
                try {
                    throw new Exception("sdfasdfasdf");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            for(CommonData data : masterConfig.skills){
                if(equipSkillIds.size() >= 2 || player.getLv() < data.id){//自己的等级小于配置的要求的最低等级
                    break;
                }
                if(!equipSkillIds.contains(Integer.valueOf(data.value))){
                    equipSkillIds.add(data.value);
                }
            }
        }
    }


    /**
     *
     * @param beforeLevel
     */
    public boolean levelUpToNewUnlockSkill(int beforeLevel){
        levelUpToAddDefaultSkill();
        int nowLevel = player.getLv();
        MasterConfig masterConfig = DataFactory.getInstance().getGameObject(DataFactory.MASTER_KEY, player.master);
        if(masterConfig == null){
            System.out.println("SettingInfo checkInit() master 找不到 master：" + player.master);
            return false;
        }
        for(CommonData data : masterConfig.skills){
           if(data.id > beforeLevel && data.id <= nowLevel){//有新的技能解锁了
               newUnlockSkillId = data.value;
               System.out.println("SettingInfo 有新的技能解锁了id为：" + newUnlockSkillId);
               return true;
           }
        }

        return false;
    }

    public boolean isFirstChangeName(){
        return playerNameChangeTimes == 0;
    }


    public List<Integer> getConsumeNotRemindList() {
        return consumeNotRemindList == null ? consumeNotRemindList = new CopyOnWriteArrayList<>() : consumeNotRemindList;
    }


    public List<Integer> getEquipSkillIds() {
        return equipSkillIds == null ? equipSkillIds = new CopyOnWriteArrayList<>() : equipSkillIds;
    }

    public int getPlayerNameChangeTimes() {
        return playerNameChangeTimes;
    }

    public void removeNewUnlockSkill(){
        newUnlockSkillId = 0;
    }

    public boolean hasNewUnlockSkill(){
        return newUnlockSkillId != 0;
    }

    public int getNewUnlockSkillId() {
        return newUnlockSkillId;
    }

    @Override
    public void init() {
        levelUpToAddDefaultSkill();
    }

    @Override
    public void checkInit() {
        levelUpToAddDefaultSkill();
    }



    @Override
    public void reset() {

    }

    @Override
    public void checkReset() {

    }
}
