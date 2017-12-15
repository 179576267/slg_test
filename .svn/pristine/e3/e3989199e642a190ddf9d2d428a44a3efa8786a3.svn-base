package com.douqu.game.core.entity.db;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.config.challenge.LevelConfig;
import com.douqu.game.core.config.goods.EquipConfig;
import com.douqu.game.core.factory.DataFactory;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-11-25 17:59
 */
public class EquipDB extends DB {

    /**装备合成次数*/
    public int upCount;

    private LvDB lvDB;

    public EquipDB()
    {
        super(DataFactory.EQUIP_KEY);

        lvDB = new LvDB(this);
    }

    public EquipDB(int id)
    {
       super(DataFactory.EQUIP_KEY, id);
        lvDB = new LvDB(this);
    }

    @Override
    public void loadFrom(ByteBuffer buffer)
    {
        super.loadFrom(buffer);

        upCount = buffer.readShort();

        lvDB.loadFrom(buffer);
    }

    @Override
    public void writeTo(ByteBuffer buffer)
    {
        super.writeTo(buffer);
 
        buffer.writeShort(upCount);

        lvDB.writeTo(buffer);
    }

    @Override
    public void reset()
    {
        lvDB.reset();

        upCount = 0;
    }

    public void lvUp(int value)
    {
        lvDB.lvUp(value);
    }


    /**
     * 获取属性加成
     * @param attrId
     * @return
     */
    public int getAttribute(int attrId)
    {
        EquipConfig equip = getConfig();

        return equip.getAttribute(attrId, getLv());
    }

    /**
     * 打NPC的时候设置怪物信息
     * @param monsterLv
     */
    public void setMonsterData(int[] monsterLv)
    {
        lvDB.lv = monsterLv[2];
        upCount = monsterLv[3];
    }


    public int getLv()
    {
        return lvDB.lv;
    }


    @Override
    public EquipConfig getConfig() {
        return (EquipConfig) super.getConfig();
    }
}
