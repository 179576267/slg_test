package com.douqu.game.core.entity.db;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.config.challenge.LevelConfig;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.DataFactory;

/**
 * @author wangzhenfei
 *         2017-11-27 14:06
 */
public class LevelDB extends DB{

    /**
     * 已获取的最高星星数
     */
    private int maxStars;

    /**
     * 是否已经通关过
     */
    private boolean isReceive;



    public LevelDB() {
        super(DataFactory.LEVEL_KEY);
    }

    public LevelDB(int id) {
        super(DataFactory.LEVEL_KEY, id);
    }

    @Override
    public LevelConfig getConfig() {
        return (LevelConfig) super.getConfig();
    }

    @Override
    public void reset() {

    }

    @Override
    public void loadFrom(ByteBuffer buffer) {
        super.loadFrom(buffer);
        maxStars = buffer.readInt();
        isReceive = buffer.readBoolean();
    }

    @Override
    public void writeTo(ByteBuffer buffer) {
        super.writeTo(buffer);
        buffer.writeInt(maxStars);
        buffer.writeBoolean(isReceive);
    }

    public int getMaxStars() {
        return maxStars;
    }

    public void setMaxStars(int maxStars) {
        this.maxStars = maxStars;
    }

    public boolean isReceive() {
        return isReceive;
    }

    public void setReceive(boolean isReceive) {
        this.isReceive = isReceive;
    }


}
