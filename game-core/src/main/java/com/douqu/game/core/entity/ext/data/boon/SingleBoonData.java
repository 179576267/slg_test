package com.douqu.game.core.entity.ext.data.boon;

import com.bean.core.buffer.ByteBuffer;
import com.bean.core.util.TimeUtils;
import com.douqu.game.core.config.AltarConfig;
import com.douqu.game.core.config.LotteryConfig;
import com.douqu.game.core.config.VipConfig;
import com.douqu.game.core.entity.EntityObject;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.factory.DataFactory;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-10-30 19:29
 */
public class SingleBoonData extends EntityObject {

    public int id;

    public int type;

    /**
     * 当前已经抽了的次数
     */
    public int currentNum;

    public long lastUpdateTime;
    /**
     * 免费次数
     * */
    public int freeCount;

    private Player player;

    public SingleBoonData(Player player, int type) {
        this.type = type;
        this.player = player;
    }

    public SingleBoonData(Player player, int type, int id){
        this.type = type;
        this.id = id;
        this.player = player;
        clear();
    }


    public void clear()
    {
        currentNum = 0;
        lastUpdateTime = 0;
        if(type == DataFactory.ALTAR_KEY)
        {
//            AltarConfig altarConfig = DataFactory.getInstance().getGameObject(DataFactory.ALTAR_KEY, id);
//            freeCount = altarConfig.everydayFreeCount;
            //根据vip等级获取免费次数
            VipConfig vipConfig = DataFactory.getInstance().getGameObject(DataFactory.VIP_KEY, player.getVipLevel());
            freeCount = vipConfig.altarFreeTimesMap.get(id);

        }
        else if(type == DataFactory.LOTTERY_KEY)
        {
            LotteryConfig lotteryConfig = DataFactory.getInstance().getGameObject(DataFactory.LOTTERY_KEY, id);
            freeCount = lotteryConfig.freeCount;
        }
    }

    /**
     * vip升级后补上免费差
     * @param beforeVipLv
     */
    public void vipLevelUp(int beforeVipLv){
        if(type == DataFactory.ALTAR_KEY){
            VipConfig beforeVipConfig = DataFactory.getInstance().getGameObject(DataFactory.VIP_KEY, beforeVipLv);
            VipConfig vipConfig = DataFactory.getInstance().getGameObject(DataFactory.VIP_KEY, player.getVipLevel());
            freeCount += (vipConfig.altarFreeTimesMap.get(id) - beforeVipConfig.altarFreeTimesMap.get(id));
        }

    }

    public void writeTo(ByteBuffer byteBuffer){
        byteBuffer.writeInt(id);
        byteBuffer.writeShort(currentNum);
        byteBuffer.writeShort(freeCount);
        byteBuffer.writeLong(lastUpdateTime);
    }

    public void loadFrom(ByteBuffer byteBuffer){
        id = byteBuffer.readInt();
        currentNum = byteBuffer.readShort();
        freeCount= byteBuffer.readShort();
        lastUpdateTime = byteBuffer.readLong();
    }

    /**
     * 获取已经购买的次数
     * @return
     */
    public int getBuyTimes(){
        if(type == DataFactory.ALTAR_KEY){
            VipConfig vipConfig = DataFactory.getInstance().getGameObject(DataFactory.VIP_KEY, player.getVipLevel());
            return currentNum - vipConfig.altarFreeTimesMap.get(id);
        }

        return 0;

    }

    public int getRemainTimes(){
        if(type == DataFactory.ALTAR_KEY){
            VipConfig vipConfig = DataFactory.getInstance().getGameObject(DataFactory.VIP_KEY, player.getVipLevel());
            return vipConfig.altarFreeTimesMap.get(id) + vipConfig.altarPurchaseMaxMap.get(id) - currentNum;
        }
        return 0;

    }

    public boolean isToday()
    {
        return TimeUtils.isToday(lastUpdateTime);
    }


    @Override
    public String toString() {
        return "SingleBoonData{" +
                "id=" + id +
                ", currentNum=" + currentNum +
                ", lastUpdateTime=" + lastUpdateTime +
                ", freeCount=" + freeCount +
                "} " + super.toString();
    }
}
