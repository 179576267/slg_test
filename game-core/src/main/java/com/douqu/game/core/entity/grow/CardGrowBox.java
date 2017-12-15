//package com.douqu.game.core.entity.grow;
//
//import com.bean.core.buffer.ByteBuffer;
//import com.douqu.game.core.config.goods.AccessoryConfig;
//import com.douqu.game.core.config.goods.EquipConfig;
//import com.douqu.game.core.config.card.CardConfig;
//import com.douqu.game.core.config.card.CardFateConfig;
//import com.douqu.game.core.entity.db.LvDB;
//import com.douqu.game.core.factory.DataFactory;
//
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.CopyOnWriteArrayList;
//
///**
//* 成长盒子
//* 只要跟成长相关的数据全部保存在这里
//* 比如装备,法宝,以及以后的扩展
//* @Author: Bean
//* @Description:
//* @Date: 2017-10-31 16:42
//*/
//public class CardGrowBox extends LvDB {
//
//    /**
//     * 装备列表
//     * key : 装备位置
//     */
//    public Map<Integer, EquipGrowBox> equipMap;
//
//    /**
//     * 饰品列表
//     * key : 饰品类型
//     */
//    public Map<Integer,AccessoryGrowBox> accessoryMap;
//
//    /**
//     * 星级
//     */
//    public int star;
//
//    /**
//     * 已激活的宿命列表
//     */
//    private List<Integer> activeFateList;
//
//
//    public CardGrowBox(CardConfig cardConfig)
//    {
//        super(cardConfig);
//
//        equipMap = new ConcurrentHashMap<>();
//        accessoryMap = new ConcurrentHashMap<>();
//        activeFateList = new CopyOnWriteArrayList<>();
//    }
//
//    @Override
//    public void reset(){
//        super.reset();
//        equipMap.clear();
//        accessoryMap.clear();
//        activeFateList.clear();
//        init();
//        star = 0;
//
//    }
//
//
//
//    public void init()
//    {
//        List<EquipConfig> equipList = DataFactory.getInstance().getDataList(DataFactory.EQUIP_KEY);
//        for(EquipConfig equip : equipList)
//        {
//            if(equip.quality == 1)
//            {
//                equipMap.put(equip.type, new EquipGrowBox(equip));
//            }
//        }
//        CardConfig cardConfig = (CardConfig) getMaster();
//        for(Integer key : cardConfig.soldierConfig.accessory)
//        {
//            AccessoryConfig accessory = DataFactory.getInstance().getGameObject(DataFactory.ACCESSORY_KEY,key);
//            accessoryMap.put(accessory.type, new AccessoryGrowBox(accessory));
//        }
//    }
//
//    public void loadFrom(ByteBuffer buffer)
//    {
//        super.loadFrom(buffer);
//
//        int size = buffer.readByte();
//        EquipGrowBox growBox = null;
//        EquipConfig equip = null;
//        int id = 0;
//        for (int i = 0;i < size; i++){
//            id = buffer.readInt();
//            equip = DataFactory.getInstance().getGameObject(DataFactory.EQUIP_KEY, id);
//            if(equip == null)
//            {
//                try {
//                    throw new Exception("CardGrowBox load error -> equip is null:" + id);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            else
//            {
//                growBox = new EquipGrowBox(equip);
//                growBox.loadFrom(buffer);
//                equipMap.put(buffer.readByte(), growBox);
//            }
//        }
//
//        size = buffer.readByte();
//        AccessoryGrowBox accessoryGrowBox = null;
//        AccessoryConfig accessory = null;
//        for (int i = 0;i < size; i++){
//            id = buffer.readInt();
//            accessory = DataFactory.getInstance().getGameObject(DataFactory.ACCESSORY_KEY, id);
//            if(accessory == null)
//            {
//                try {
//                    throw new Exception("CardGrowBox load error -> accessory is null:" + id);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            else
//            {
//                accessoryGrowBox = new AccessoryGrowBox(accessory);
//                accessoryGrowBox.loadFrom(buffer);
//                accessoryMap.put(buffer.readByte(),accessoryGrowBox);
//            }
//        }
//        size = buffer.readByte();
//        for (int i =0 ; i< size; i++){
//            activeFateList.add(buffer.readInt());
//        }
//        star = buffer.readInt();
//    }
//
//    public void writeTo(ByteBuffer buffer)
//    {
//        super.writeTo(buffer);
//
//        buffer.writeByte(equipMap.size());
//        for(Integer key : equipMap.keySet())
//        {
//            buffer.writeInt(equipMap.get(key).getMaster().id);
//            equipMap.get(key).writeTo(buffer);
//            buffer.writeByte(key);
//        }
//
//        buffer.writeByte(accessoryMap.size());
//        for(Integer key : accessoryMap.keySet())
//        {
//            buffer.writeInt(accessoryMap.get(key).getMaster().id);
//            accessoryMap.get(key).writeTo(buffer);
//            buffer.writeByte(key);
//        }
//
//        buffer.writeByte(activeFateList.size());
//        for (int i = 0; i < activeFateList.size();i++){
//            buffer.writeInt(activeFateList.get(i).intValue());
//        }
//
//        buffer.writeInt(star);
//
//
//    }
//    public int getEquipLvCount(int equipLv){
//        int count = 0;
//        for(Map.Entry<Integer,EquipGrowBox> entry :equipMap.entrySet()){
//            if(entry.getValue().lv >= equipLv)
//                count++;
//        }
//        return count;
//    }
//
//    public void addFate(int fateId)
//    {
//        activeFateList.add(fateId);
//    }
//
//    public boolean isHaveFate(int fateId)
//    {
//        return activeFateList.contains(fateId);
//    }
//
//
//
//    public EquipGrowBox getEquipGrowBox(int type)
//    {
//        return equipMap.get(type);
//    }
//
//    public AccessoryGrowBox getAccessoryGrowBox(int type){
//        return accessoryMap.get(type);
//    }
//
//
//    /**
//     * 获取属性加成
//     * @param attrId
//     * @return
//     */
//    public int getAttribute(int attrId)
//    {
//        CardFateConfig fate = null;
//        int result = 0;
//        for(Integer fateId : activeFateList)
//        {
//            fate = DataFactory.getInstance().getGameObject(DataFactory.CARD_FATE_KEY, fateId);
//            result += fate.getAttribute(attrId);
//        }
//
//        for(Integer key : equipMap.keySet())
//        {
//            result += equipMap.get(key).getAttribute(attrId);
//        }
//
//        for(Integer key : accessoryMap.keySet())
//        {
//            result += accessoryMap.get(key).getAttribute(attrId);
//        }
//
//        return result;
//    }
//
//
//    public List<Integer> getActiveFateList() {
//        return activeFateList;
//    }
//
//
//}
