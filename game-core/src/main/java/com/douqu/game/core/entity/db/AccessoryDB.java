package com.douqu.game.core.entity.db;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.config.goods.AccessoryConfig;
import com.douqu.game.core.config.task.TaskConfig;
import com.douqu.game.core.factory.DataFactory;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-11-25 17:59
 */
public class AccessoryDB extends DB {

    /**
     * 进阶等级
     */
    public int upLv;

    private LvDB lvDB;

    public AccessoryDB()
    {
        super(DataFactory.ACCESSORY_KEY);

        lvDB = new LvDB(this);
    }

    public AccessoryDB(int id)
    {
        super(DataFactory.ACCESSORY_KEY, id);

        lvDB = new LvDB(this);
    }

    @Override
    public void loadFrom(ByteBuffer buffer)
    {
        super.loadFrom(buffer);

        upLv = buffer.readShort();

        lvDB.loadFrom(buffer);
    }

    @Override
    public void writeTo(ByteBuffer buffer)
    {
        super.writeTo(buffer);

        buffer.writeShort(upLv);

        lvDB.writeTo(buffer);
    }

    @Override
    public void reset() {

    }

    public void addExp(int value)
    {
        lvDB.addExp(value);
    }
    /**
     * 获取属性加成
     * @param attrId
     * @return
     */
    public int getAttribute(int attrId)
    {
        AccessoryConfig accessory = getConfig();

        return accessory.getAttribute(attrId, getLv()) + accessory.getUpAttribute(attrId, upLv);
    }

    /**
     * 打NPC的时候设置怪物信息
     * @param monsterLv
     */
    public void setMonsterData(int[] monsterLv)
    {
        lvDB.lv = monsterLv[4];
        upLv = monsterLv[5];
    }

    public int getLv()
    {
        return lvDB.lv;
    }

    public int getExp()
    {
        return lvDB.exp;
    }

    @Override
    public AccessoryConfig getConfig() {
        return (AccessoryConfig) super.getConfig();
    }
}
