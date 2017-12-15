package com.douqu.game.core.entity.ext;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.entity.WorldInfo;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.protobuf.SGCommonProto;

import java.io.Serializable;
import java.util.List;
//import java.util.logging.Logger;

/**
 * Created by bean on 2017/7/17.
 */
public abstract class ExtInfo implements Serializable
{

//    Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    protected static final long serialVersionUID = 1L;

    protected Player player;

    protected WorldInfo worldInfo;

    public ExtInfo(Player player, WorldInfo worldInfo){
        this.player = player;
        this.worldInfo = worldInfo;
    }

    public String getName()
    {
        return this.getClass().getSimpleName();
    }


    /**
     * 初始化数据,只在创建角色的时候用
     */
    public abstract void init();

    /**
     * 检测是否要初始化, 当有新系统需要初始化一些数据的时候用来检测
     */
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

    /**
     * 红点检测添加物品和资源
     */
    public void checkRedPointRemindAddGoods(SGCommonProto.E_GOODS_TYPE type, int id){
    };

    /**
     * 红点检测所有条件
     */
    public List<SGCommonProto.E_RED_POINT_TYPE> checkRedPointRemindAllCondition(long currentTime){
        return null;
    };

    public byte[] getData()
    {
        ByteBuffer buffer = new ByteBuffer();
        writeTo(buffer);

        return buffer.getBytes();
    }

}
