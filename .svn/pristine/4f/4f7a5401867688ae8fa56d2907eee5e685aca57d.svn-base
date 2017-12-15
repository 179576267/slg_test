package com.douqu.game.core.config.challenge;

import com.douqu.game.core.config.BattleInitConfig;
import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.util.LoadUtils;

import java.util.Arrays;


/**
* @author wangzhenfei
*         2017-08-08 16:09
*         副本关卡
*/
public class LevelConfig extends BattleInitConfig {


    /** 图标 */
    public String icon;
    /** 地图 */
    public String map;
    /** 头像是否显示 */
    public int showAvatar;

    /** 奖励组 */
    public GoodsData[] passReward;
    /** 解锁关卡 */
    public int unlockLevel;
    /** 宝箱奖励(掉落组ID) */
    public int boxReward;

    /**
     * 评星条件
     */
    public int[] starChecks;

    public String pos;

    /**
     * 副本ID
     */
    private int instanceId;

    @Override
    public void check()
    {
        super.check();
        if(passReward == null)
        {
            System.out.println("Level check reward is null -> id:" + id + " name:" + name);
        }
        if(starChecks == null)
        {
            System.out.println("Load Level starChecks is null! id:" + id);
        }
    }




    @Override
    public void setVariable(String key, String value)
    {
        if("starChecks".equals(key))
        {
            starChecks = LoadUtils.loadIntArray(key, value);
            for(int sc : starChecks)
            {
                if(DataFactory.getInstance().getGameObject(DataFactory.STAR_CHECK_KEY, sc) == null)
                {
                    System.out.println("Level setVariable Error -> starCheck : " + value + " id:" + id);
                }
            }
        }
        else if("passReward".equals(key))
        {
            passReward = LoadUtils.loadGoodDataToArray(key, value);
            for(GoodsData data : passReward)
            {
                int type = 0;
                if(data.type == SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_PROPS_VALUE){
                    type = DataFactory.PROP_KEY;
                }else if(data.type == SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_ASSETS_VALUE){
                    type = DataFactory.ASSET_KEY;
                }else if(data.type == SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_CARDS_VALUE){
                    type = DataFactory.CARD_KEY;
                }

                if(DataFactory.getInstance().getGameObject(type, data.id) == null)
                {
                    System.out.println("Level setVariable Error -> type : " + data.type + " id:" + data.id + " num:" + data.value);
                }
            }
        }
        else if("boxReward".equals(key))
        {
            this.boxReward = Integer.parseInt(value);
            if(boxReward != 0 && DataFactory.getInstance().getGameObject(DataFactory.DROP_GROUP_KEY, boxReward) == null)
            {
                System.out.println("Level setVariable Error -> boxReward : " + value + " id:" + id);
            }
        }

        else
            super.setVariable(key, value);
    }


    public int getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(int instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    public String toString() {
        return "LevelConfig{" +
                "icon='" + icon + '\'' +
                ", map='" + map + '\'' +
                ", showAvatar=" + showAvatar +
                ", passReward=" + Arrays.toString(passReward) +
                ", unlockLevel=" + unlockLevel +
                ", boxReward=" + boxReward +
                ", starChecks=" + Arrays.toString(starChecks) +
                ", pos='" + pos + '\'' +
                "} " + super.toString();
    }
}
