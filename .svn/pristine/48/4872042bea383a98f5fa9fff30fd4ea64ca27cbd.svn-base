package com.douqu.game.core.config;

import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.util.LoadUtils;

import java.util.Arrays;

/**
 * @author wangzhenfei
 *         2017-11-28 15:28
 *         战斗初始化配置
 */
public class BattleInitConfig extends GameObject{
    /** 主将 */
    public int master;
    /** 主将等级 */
    public int masterLv;
    /** 对应怪物ID */
    public int monsterId;
    /**
     *  怪物等级
     *   1.等级
         2.突破等级
         3.装备强化等级
         4.装备进阶等级
         5.法宝强化等级
         6.法宝升阶等级
     填为0代表不计算该部分属性
     * */
    public int[] monsterLv;

    /**战斗力**/
    public int fc;

    public int mapId;

    @Override
    public void check(){
        if(monsterLv == null)
        {
            System.out.println(getClass().getSimpleName() + " Load BattleInitConfig monsterLv is null! id:" + id);
        }
        if(monsterLv.length != 6)
        {
            System.out.println(getClass().getSimpleName() + " Load BattleInitConfig monsterLv Error -> id:" + id);
        }

        if(DataFactory.getInstance().getGameObject(DataFactory.MAP_KEY, mapId) == null){
            System.out.println(getClass().getSimpleName() + " Load BattleInitConfig mapId Error -> id:" + id);
        }
    }

    @Override
    public void setVariable(String key, String value)
    {
        if("monsterId".equals(key))
        {
            monsterId = Integer.parseInt(value);
            if(DataFactory.getInstance().getGameObject(DataFactory.MONSTER_KEY, monsterId) == null)
            {
                System.out.println(getClass().getSimpleName() + " BattleInitConfig setVariable Error -> monsterId : " + value + " id:" + id);
            }
        }
        else if("monsterLv".equals(key))
        {
            monsterLv = LoadUtils.loadIntArray(key, value);

        }
        else if("master".equals(key))
        {
            master = Integer.parseInt(value);
            if(DataFactory.getInstance().getGameObject(DataFactory.MASTER_KEY, master) == null)
            {
                System.out.println(getClass().getSimpleName() + " BattleInitConfig setVariable Error -> master : " + value + " id:" + id);
            }
        }else
            super.setVariable(key, value);
    }

    @Override
    public String toString() {
        return "BattleInitConfig{" +
                ", master=" + master +
                ", masterLv=" + masterLv +
                ", monsterId=" + monsterId +
                ", monsterLv=" + Arrays.toString(monsterLv) +
                "} " + super.toString();
    }
}
