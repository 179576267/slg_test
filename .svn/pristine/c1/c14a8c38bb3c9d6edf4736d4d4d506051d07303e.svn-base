package com.douqu.game.core.config.challenge;


import com.douqu.game.core.config.drop.DropGroupConfig;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.util.LoadUtils;

import java.util.Arrays;

/**
* @author wangzhenfei
*         2017-08-08 16:06
*        副本地图
*/
public class InstanceConfig extends GameObject {



    /**
     * 关卡列表
     */
    public int[] levels;
    /**
     * 背景
     */
    public String bg;
    /**
     * 星级宝箱
     * value: 星星数
     * id: 掉落组ID
     */
    public InstanceStarBox[] starBox;
    /**
     * 解锁副本
     */
    public int unLockInstance;



    @Override
    public void check()
    {
        if(levels == null)
        {
            System.out.println("Load Instance levels is null! id:" + id);
        }
        if(starBox == null)
        {
            System.out.println("Load Instance starBox is null! id:" + id);
        }
    }




    public InstanceStarBox getInstanceStarBox(int id)
    {
        for(InstanceStarBox box : starBox)
        {
            if(box.id == id)
                return box;
        }
        return null;
    }





    @Override
    public void setVariable(String key, String value)
    {
        if("starBox".equals(key))
        {
            String[] strs = value.split(ConstantFactory.DIVISION);
            starBox = new InstanceStarBox[strs.length];
            for(int i = 0; i < strs.length; i++)
            {
                //配置文件里配置的是星星数在前，所以这里要先取第二个为ID
                starBox[i] = new InstanceStarBox(strs[i]);
                DropGroupConfig dropGroupConfig = DataFactory.getInstance().getGameObject(DataFactory.DROP_GROUP_KEY, starBox[i].dropId);
                if(dropGroupConfig == null)
                {
                    System.out.println("InstanceMap setVariable starBox error -> DropGroup is null:" + strs[i]);
                }
                if(starBox[i].star <= 0)
                {
                    System.out.println("InstanceMap setVariable starBox count error -> " + strs[i]);
                }
            }
        }
        else if("levels".equals(key))
        {
            levels = LoadUtils.loadIntArray(key, value);
            LevelConfig levelConfig = null;
            for(int levelId : levels)
            {
                levelConfig = DataFactory.getInstance().getGameObject(DataFactory.LEVEL_KEY, levelId);
                if(levelConfig == null)
                {
                    System.out.println("InstanceMap setVariable levels error -> Level is null:" + levelId);
                }
                else
                {
                    levelConfig.setInstanceId(this.id);
                }
            }
        }
        else
            super.setVariable(key, value);
    }

    @Override
    public String toString() {
        return "InstanceConfig{" +
                "levels=" + Arrays.toString(levels) +
                ", bg='" + bg + '\'' +
                ", starBox=" + Arrays.toString(starBox) +
                ", unLockInstance=" + unLockInstance +
                "} " + super.toString();
    }
}
