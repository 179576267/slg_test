package com.douqu.game.core.entity.db;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.entity.EntityObject;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.DataFactory;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-11-25 17:26
 */
public abstract class DB extends EntityObject {

    protected int dataKey;

    public int id;

    /**
     * loadFrom之前初始化，子类不带参数
     * @param dataKey
     */
    public DB(int dataKey)
    {
        this.dataKey = dataKey;
    }

    /**
     * 第一次初始化
     * @param dataKey
     * @param id
     */
    public DB(int dataKey, int id)
    {
        this.dataKey = dataKey;
        this.id = id;
        if(getConfig() == null)
        {
            try {
                throw new Exception(this.getClass().getSimpleName() + " Load config error -> config is null id:" + id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


//    public <T> T getGameObject()
//    {
//        return DataFactory.getInstance().getGameObject(dataKey, id);
//    }

    public GameObject getConfig()
    {
        return DataFactory.getInstance().getGameObject(dataKey, id);
    }

    public void loadFrom(ByteBuffer buffer)
    {
        id = buffer.readInt();

        if(getConfig() == null)
        {
            try {
                throw new Exception(this.getClass().getSimpleName() + " Load config error -> config is null id:" + id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void writeTo(ByteBuffer buffer)
    {
        buffer.writeInt(id);
    }

    public abstract void reset();

    public void setId(int id)
    {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DB{" +
                "id=" + id +
                "} ";
    }
}
