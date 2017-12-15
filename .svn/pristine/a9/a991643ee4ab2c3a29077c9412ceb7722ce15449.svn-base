package com.douqu.game.core.config.map;

import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.DataFactory;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-11-24 10:43
 */
public class RouteConfig extends GameObject {

    public int townA;

    public int townB;

    public float distance;

    /**
     * 路线类型
     * 1.默认
     * 2.隐藏
     */
    public int type;

    public RouteConfig() {
    }

    public RouteConfig(int townA, int townB, int distance, int type)
    {
        this.townA = townA;
        this.townB = townB;
        this.distance = distance;
        this.type = type;
    }

    @Override
    public void check()
    {
        if(townA == townB)
        {
            System.out.println("Load Route Error -> " + this);
        }

        if(distance == 0)
        {
            System.out.println("Load Route distance Error -> " + this);
        }

        if(DataFactory.getInstance().getGameObject(DataFactory.TOWN_KEY, townA) == null)
        {
            System.out.println("Load Route TownA Error -> " + this);
        }

        if(DataFactory.getInstance().getGameObject(DataFactory.TOWN_KEY, townB) == null)
        {
            System.out.println("Load Route TownB Error -> " + this);
        }
    }

    @Override
    public String toString() {
        return "Route{" +
                "townA=" + townA +
                ", townB=" + townB +
                ", distance=" + distance +
                ", type=" + type +
                "} " + super.toString();
    }
}
