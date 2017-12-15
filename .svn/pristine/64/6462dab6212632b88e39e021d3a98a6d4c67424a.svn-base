package com.douqu.game.core.entity.db;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.config.goods.PropConfig;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.DataFactory;

/**
 * @author: Gavin.
 * Description:
 * Date: 2017/12/8 0008 下午 5:57
 * Huan Yu Copyright (c) 2017 All Rights Reserved.
 */
public class PropDB extends DB {
    /***
     * 当前已存放数量
     */
    public int count;


    public PropDB() {
        super(DataFactory.PROP_KEY);
    }

    public PropDB(int id) {
        super(DataFactory.PROP_KEY, id);
    }

    @Override
    public void reset() {

    }

    @Override
    public void writeTo(ByteBuffer buffer)
    {
        super.writeTo(buffer);
        buffer.writeInt(count);
    }

    @Override
    public void loadFrom(ByteBuffer buffer)
    {
        super.loadFrom(buffer);
        count = buffer.readInt();
    }

    @Override
    public PropConfig getConfig() {
        return (PropConfig) super.getConfig();
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count < 0 ? 0 : count;
    }

    @Override
    public String toString() {
        return "PropDB{" +
                "id=" + id +
                ", count=" + count +
                '}';
    }
}
