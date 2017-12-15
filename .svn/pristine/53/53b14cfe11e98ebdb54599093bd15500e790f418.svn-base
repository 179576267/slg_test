package com.douqu.game.core.entity.ext.data;

import com.bean.core.buffer.ByteBuffer;

import java.io.Serializable;

/**
 * @author wangzhenfei
 *         2017-10-19 14:11
 *         所有内部加载的积累
 */
public abstract class BaseData implements Serializable{
    protected static final long serialVersionUID = 1L;

    public abstract void init();
    public abstract void checkInit();
    /**
     * 定时(一般是每天凌晨)重置数据
     */
    public abstract void reset();

    /**
     * 定时(一般是每天凌晨)检测重置的数据
     */
    public abstract void checkReset();
    public abstract void writeTo(ByteBuffer buffer);
    public abstract void loadFrom(ByteBuffer buffer);
}
