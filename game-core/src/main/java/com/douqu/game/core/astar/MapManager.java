//package com.douqu.game.core.astar;
//
//import java.util.List;
//
///**
// * Created by matrix on 2017/12/1.
// * 所有方法中grid参数坐标均为用户角度坐标参数
// * 因为寻路地图是一二维数组，默认是从左上角开始，我们参与寻路计算全部按照数组坐标来走，和用户数据交互全部按照用户角度坐标来走
// * 目前我们地图坐标以左下角为零点，向右上方增长
// * 玩家移动时定时执行setMapPlayers方法
// */
//
//public class MapManager
//{
//
//    public enum MapSignal
//    {
//        Free,       //可寻路点
//        Obstacle,   //固定障碍
//        Player,     //兵，寻路时会避开
//        Path,       //路线点,寻路和此无关，只起显示路线作用
//    }
//    public static AstarMap flexibleMapInfo;
//
//    private final static int FREE = 0; // 自由通路
//    private final static int OBSTACLE = 1; // 障碍值
//    private final static int PLAYER = 2; // 角色
//    private final static int PATH = 3; // 路径
//
//
//
//    public static void setMapPlayers(List<ObjectBound> allPlayers,MapSignal signal)
//    {
//        for(ObjectBound  obj : allPlayers)
//        {
//            setMap(flexibleMapInfo,obj, signal);
//        }
//    }
//
//    public static boolean isObstacle(AstarMap map,int gridX,int gridY)
//    {
//        if(map.maps[gridY][gridX] == OBSTACLE || map.maps[gridY][gridX] == PLAYER)
//            return true;
//        return false;
//    }
//
//    public static void setMap(AstarMap map,Grid g,MapSignal signal)
//    {
//        setMap(map,g.x, g.y, signal);
//    }
//
//    public static void setMap(AstarMap map,ObjectBound obj,MapSignal signal)
//    {
//        List<Grid> occupyGrids = obj.getOccupyGrids();
//        for(Grid g : occupyGrids)
//        {
//            setMap(map,g,signal);
//        }
//
//        if(obj.lastOccupyGrids != null)
//        {
//            for(Grid g : obj.lastOccupyGrids)
//            {
//                boolean free = false;
//                if(!occupyGrids.contains(g))
//                    free = true;
//                if(free)
//                    setMap(map,g, MapSignal.Free);
//            }
//        }
//        obj.lastOccupyGrids = occupyGrids;
//
//    }
//    public static void setMap(AstarMap map,int gridX,int gridY,MapSignal signal)
//    {
//        if(map.anchor == AstarMap.AnchorPos.BottomLeft)
//        {
//            gridY = map.height - gridY -1;
//        }
//        switch (signal)
//        {
//            case Free:
//                if( map.maps[gridY][gridX] != OBSTACLE )
//                    map.maps[gridY][gridX]= FREE;
//                break;
//            case Obstacle:
//                map.maps[gridY][gridX]= OBSTACLE;
//                break;
//            case Player:
//                if( map.maps[gridY][gridX] != OBSTACLE )
//                    map.maps[gridY][gridX]= PLAYER;
//                break;
//            case Path:
//                map.maps[gridY][gridX]= PATH;
//                break;
//        }
//    }
//
//}
