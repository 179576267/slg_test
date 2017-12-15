package com.douqu.game.core.entity.db;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.config.card.CardConfig;
import com.douqu.game.core.config.card.CardFateConfig;
import com.douqu.game.core.config.goods.AccessoryConfig;
import com.douqu.game.core.config.goods.EquipConfig;
import com.douqu.game.core.e.E_Attribute;
import com.douqu.game.core.factory.DataFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-11-25 17:26
 */
public class CardDB extends DB {

    /**
     * 总战力
     */
    public int fc;

    /**
     * 是否上阵
     */
    public boolean battle;

    /**
     * 装备列表
     * key : 装备位置
     */
    private Map<Integer, EquipDB> equipMap;

    /**
     * 饰品列表
     * key : 饰品类型
     */
    private Map<Integer,AccessoryDB> accessoryMap;

    /**
     * 已激活的宿命列表
     */
    private List<Integer> activeFateList;

    /**
     * 星级
     */
    public int star;

    private LvDB lvDB;

    public CardDB()
    {
        super(DataFactory.CARD_KEY);

        equipMap = new ConcurrentHashMap<>();
        accessoryMap = new ConcurrentHashMap<>();
        activeFateList = new CopyOnWriteArrayList<>();

        lvDB = new LvDB(this);
    }

    public CardDB(int id)
    {
       super(DataFactory.CARD_KEY, id);
        equipMap = new ConcurrentHashMap<>();
        accessoryMap = new ConcurrentHashMap<>();
        activeFateList = new CopyOnWriteArrayList<>();

        lvDB = new LvDB(this);

        init();
    }



    @Override
    public void loadFrom(ByteBuffer buffer)
    {
        super.loadFrom(buffer);

        star = buffer.readShort();

        lvDB.loadFrom(buffer);

        int size = buffer.readByte();
        DB db = null;
        for (int i = 0;i < size; i++)
        {
            db = new EquipDB();
            db.loadFrom(buffer);
            equipMap.put(buffer.readByte(), (EquipDB) db);
        }

        size = buffer.readByte();
        for (int i = 0;i < size; i++)
        {
            db = new AccessoryDB();
            db.loadFrom(buffer);
            accessoryMap.put(buffer.readByte(), (AccessoryDB) db);
        }

        size = buffer.readByte();
        for (int i =0 ; i< size; i++){
            activeFateList.add(buffer.readInt());
        }

        updateFC();
    }

    @Override
    public void writeTo(ByteBuffer buffer)
    {
        super.writeTo(buffer);

        buffer.writeShort(star);

        lvDB.writeTo(buffer);

        buffer.writeByte(equipMap.size());
        for(Integer key : equipMap.keySet())
        {
            equipMap.get(key).writeTo(buffer);
            buffer.writeByte(key);
        }

        buffer.writeByte(accessoryMap.size());
        for(Integer key : accessoryMap.keySet())
        {
            accessoryMap.get(key).writeTo(buffer);
            buffer.writeByte(key);
        }

        buffer.writeByte(activeFateList.size());
        for (int i = 0; i < activeFateList.size();i++){
            buffer.writeInt(activeFateList.get(i).intValue());
        }
    }



    @Override
    public void reset()
    {
        equipMap.clear();
        accessoryMap.clear();
        activeFateList.clear();
        init();
        lvDB.reset();
        star = 0;
        updateFC();
    }


    public void init()
    {
        List<EquipConfig> equipList = DataFactory.getInstance().getDataList(DataFactory.EQUIP_KEY);
        for(EquipConfig equip : equipList)
        {
            if(equip.quality == 1)
            {
                equipMap.put(equip.type, new EquipDB(equip.id));
            }
        }
        CardConfig cardConfig = (CardConfig) getConfig();
        AccessoryConfig accessory = null;
        for(Integer key : cardConfig.soldier.accessory)
        {
            accessory = DataFactory.getInstance().getGameObject(DataFactory.ACCESSORY_KEY, key);
            accessoryMap.put(accessory.type, new AccessoryDB(accessory.id));
        }

        updateFC();
    }



    /**
     * 更新战力
     * @return
     */
    public int updateFC()
    {
        fc = (int) (getAttribute(E_Attribute.HP.getCode()) * 0.1
                + getAttribute(E_Attribute.ATK.getCode())
                + getAttribute(E_Attribute.DEF.getCode())
                +(getAttribute(E_Attribute.CRI.getCode())
                + getAttribute(E_Attribute.REC.getCode())
                + getAttribute(E_Attribute.ADD.getCode())
                + getAttribute(E_Attribute.EXD.getCode())
                + getAttribute(E_Attribute.HIT.getCode())
                + getAttribute(E_Attribute.EVA.getCode())) * 0.5);

        return fc;
    }

    /**
     * 卡牌是否培养过
     * @return
     */
    public boolean hasTrain(){
        //卡片升级
        if(getLv() > 1){
            return true;
        }

        //卡片升星
        if(star > 0){
            return true;
        }

        //装备升级和升阶
        Iterator<Map.Entry<Integer, EquipDB>> iterator = equipMap.entrySet().iterator();
        int key;
        EquipDB equipDB;
        while (iterator.hasNext()){
            key = iterator.next().getKey();
            equipDB = equipMap.get(key);
            //装备强化
            if(equipDB.getLv() > 0){
                return true;
            }

            //装备升阶
            if(equipDB.upCount > 0){
                return true;
            }

        }

        //饰品升级和升阶
        Iterator<Map.Entry<Integer, AccessoryDB>> iteratorAccessory = accessoryMap.entrySet().iterator();
        AccessoryDB accessoryDB;
        while (iteratorAccessory.hasNext()){
            key = iteratorAccessory.next().getKey();
            accessoryDB = accessoryMap.get(key);
            //饰品强化
            if(accessoryDB.getLv() > 0){
                return true;
            }

            if(accessoryDB.upLv > 0){
                return true;
            }
        }
        return false;
    }

    public void addExp(int value)
    {
        lvDB.addExp(value);

        updateFC();
    }

    public void addStar(int value)
    {
        star += value;
        star = star < 0 ? 0 : star;

        updateFC();
    }


    /**
     * 获取属性加成
     * @param attrId
     * @return
     */
    public int getAttribute(int attrId)
    {
        CardConfig cardConfig = getConfig();

        int result = cardConfig.soldier.getAttribute(attrId, lvDB.lv) + cardConfig.soldier.getStarAttribute(attrId, star);

        CardFateConfig fate = null;
        for(Integer fateId : activeFateList)
        {
            fate = DataFactory.getInstance().getGameObject(DataFactory.CARD_FATE_KEY, fateId);
            result += fate.getAttribute(attrId);
        }
//        System.out.println("name:"+getConfig().name + " attrId:"+attrId + "  result:"+result);
        for(Integer key : equipMap.keySet())
        {
            result += equipMap.get(key).getAttribute(attrId);
        }
//        System.out.println("name:"+getConfig().name + " attrId:"+attrId + "  result:"+result);
        //TODO 检测饰品是否解锁,解锁后才取饰品的属性
        boolean unlock = true;
        if(unlock)
        {
            for(Integer key : accessoryMap.keySet())
            {
                result += accessoryMap.get(key).getAttribute(attrId);
//                System.out.println("accessory result:"+result+"  lv"+accessoryMap.get(key).getLv() + "  upLv"+accessoryMap.get(key).upLv );
            }
        }
//        System.out.println("name:"+getConfig().name + " attrId:"+attrId + "  result:"+result);
        return result;
    }

    public int getEquipLvCount(int equipLv){
        int count = 0;
        for(Map.Entry<Integer,EquipDB> entry :equipMap.entrySet()){
            if(entry.getValue().getLv() >= equipLv)
                count++;
        }
        return count;
    }

    public void addFate(int fateId)
    {
        activeFateList.add(fateId);

        updateFC();
    }

    public boolean isHaveFate(int fateId) {
        return activeFateList.contains(fateId);
    }

    /**
     * 打NPC的时候设置怪物信息
     * @param monsterLv
     */
    public void setMonsterData(int[] monsterLv)
    {
        lvDB.lv = monsterLv[0];
        star = monsterLv[1];

        for(Integer key : equipMap.keySet())
        {
            equipMap.get(key).setMonsterData(monsterLv);
        }

        for(Integer key : accessoryMap.keySet())
        {
            accessoryMap.get(key).setMonsterData(monsterLv);
        }
    }

    public int getLv()
    {
        return lvDB.lv;
    }

    public int getExp()
    {
        return lvDB.exp;
    }

    public EquipDB getEquipDB(int type)
    {
        return equipMap.get(type);
    }

    public AccessoryDB getAccessoryDB(int type){
        return accessoryMap.get(type);
    }

    public Map<Integer, EquipDB> getEquipMap() {
        return equipMap;
    }

    public Map<Integer, AccessoryDB> getAccessoryMap() {
        return accessoryMap;
    }


    public List<Integer> getActiveFateList() {
        return activeFateList;
    }

    @Override
    public CardConfig getConfig()
    {
        return (CardConfig) super.getConfig();
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name=" + getConfig().name +
                ", lv=" + lvDB.lv +
                "} ";
    }
}
