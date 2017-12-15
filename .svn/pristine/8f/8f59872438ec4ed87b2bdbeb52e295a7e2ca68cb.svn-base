package com.douqu.game.core.astar;

/**
 * Created by matrix on 2017/12/2.
 * 绝对坐标值
 */

public class AbsCoord
{
    public float x;
    public float y;

    public AbsCoord(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public Node toRoundNode()
    {
        return new Node(Math.round(x),Math.round(y));
    }
    public String toString()
    {
        return "x:"+this.x+ "   y:"+this.y;
    }
}
