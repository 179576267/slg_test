package com.douqu.game.core.astar;


/**
 * Created by matrix on 2017/12/7.
 */
public class DirectionHelper
{
    public enum Direction
    {
        Left,
        Right,
        Up,
        Down,
        RightUp,
        LeftUp,
        LeftDown,
        RightDown
    }

    //顺时针旋转角度是否大于180°
    public static boolean angleBelow180(Vector2 from,Vector2 to)
    {
        float z = from.x*to.y - from.y*to.x;
        if(z<0) return true;
        else return false;
    }

    //获取趋势朝向
    //总共四个方向(1,0)(-1,0)(0,1)(0-1)
    public static Direction  GetTrendDirect(AstarMap mapInfo,Vector2 cur,Vector2 target)
    {
        //横版地图，左右是主流 只要过河趋势就只有左右
        float valueCur = mapInfo.width*0.5f - cur.x;
        float valueTarget = mapInfo.width*0.5f - target.x;
        if(valueCur>=0 && valueTarget <= 0)
        {
            return Direction.Right;
        }
        else if(valueCur<0 && valueTarget > 0)
        {
            return Direction.Left;
        }

        Vector2 absDirection = new Vector2(target.x - cur.x,target.y - cur.y);
        if(Math.abs(absDirection.x)>Math.abs(absDirection.y))
        {
            if(Math.signum(absDirection.x)>0)
            {
                return Direction.Right;
            }
            else
            {
                return Direction.Left;
            }
        }
        else
        {
            if(Math.signum(absDirection.y)>0)
            {
                return Direction.Down;
            }
            else
            {
                return Direction.Up;
            }
        }
    }

    public static Direction  GetPriorPathDirect(boolean angleBelow180,Direction playerFaceTrendDirect)
    {
        if(angleBelow180)
        {
            if(playerFaceTrendDirect == Direction.Right)
            {
                return Direction.RightUp;
            }
            else if(playerFaceTrendDirect == Direction.Left)
            {
                return Direction.LeftDown;
            }
            else if(playerFaceTrendDirect == Direction.Up)
            {
                return Direction.LeftUp;
            }
            else if(playerFaceTrendDirect == Direction.Down)
            {
                return Direction.RightDown;
            }
        }
        else
        {
            if(playerFaceTrendDirect == Direction.Right)
            {
                return Direction.RightDown;
            }
            else if(playerFaceTrendDirect == Direction.Left)
            {
                return Direction.LeftUp;
            }
            else if(playerFaceTrendDirect == Direction.Up)
            {
                return Direction.RightUp;
            }
            else if(playerFaceTrendDirect == Direction.Down)
            {
                return Direction.LeftDown;
            }
        }
        return null;
    }
}
