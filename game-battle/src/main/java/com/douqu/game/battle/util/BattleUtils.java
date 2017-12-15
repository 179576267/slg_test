package com.douqu.game.battle.util;

import com.douqu.game.battle.entity.aobject.BattleAObject;
import com.douqu.game.core.astar.AbsCoord;
import com.douqu.game.core.astar.Node;
import com.douqu.game.core.config.common.Position;
import com.douqu.game.core.protobuf.SGCommonProto;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-10-21 18:49
 */
public class BattleUtils {

    /**
     * 寻路接口
     */
    public static final int GET_ROUTE_PATH = 101;

    //二维数组地图以左上对齐方式
    //地图类型1
    public static final int[][] BATTLE_MAP_1 = {
           //0  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
    };

    public static final int MAX_X = BATTLE_MAP_1[0].length - 1;
    public static final int MAX_Y = BATTLE_MAP_1.length - 1;

    public static void fixPosition(Position position)
    {
        position.x = position.x < 0 ? 0 : position.x;
        position.y = position.y < 0 ? 0 : position.y;
        position.x = position.x > MAX_X ? MAX_X : position.x;
        position.y = position.y > MAX_Y ? MAX_Y : position.y;
    }

    public static List<Position> changeRoute(List list, float speed)
    {
        int size = list.size();
        List<Position> sourceRoute = new CopyOnWriteArrayList<>();
        for(int i = 0; i < size; i++){
            if(list.get(i) instanceof Position)
                sourceRoute.add((Position) list.get(i));
            else if(list.get(i) instanceof SGCommonProto.Pos)
                sourceRoute.add(new Position((SGCommonProto.Pos) list.get(i)));
            else if(list.get(i) instanceof Node)
                sourceRoute.add(new Position(((Node)list.get(i)).grid.x, ((Node) list.get(i)).grid.y));
            else if(list.get(i) instanceof AbsCoord)
                sourceRoute.add(new Position(((AbsCoord)list.get(i)).x, ((AbsCoord) list.get(i)).y));
            else
            {
                System.out.println("参数错误:" + list);
                return list;
            }
        }

        Map<Integer, Float> distanceMap = new HashMap<>();
        Position p1 = null;
        List<Position> result = new CopyOnWriteArrayList<>();
        if(size <= 2)
        {
            for(int i = 0; i < size; i++){
                result.add(sourceRoute.get(i));
            }
        }
        else
        {
            float totalDistance = 0;
            for(int i = 0; i < size; i++){
                if(i > 0){
                    totalDistance += getDistance(sourceRoute.get(i), sourceRoute.get(i - 1));
                }
                distanceMap.put(i, totalDistance);
            }

            int count = (int)(totalDistance /(speed * 0.5));
            Position lastPosition = sourceRoute.get(size-1);
            Position tempPosition = null;
            boolean isAddLast = false;
            for(int i = 0; i < count; i++){
                //i * speed * 0.5f 表示 这一帆应该走多少距离
                tempPosition = changePoint(sourceRoute, i * speed * 0.5f, distanceMap);
                result.add(tempPosition);

                if(i == count - 1)
                {
                    //是否添加了最后一个点
                    isAddLast = tempPosition.isSame(lastPosition);
                }
            }
            if(!isAddLast)
                result.add(lastPosition);
        }

        return result;
    }





    private static Position changePoint(List<Position> routeList, float distance, Map<Integer, Float> distanceMap)
    {
        int size = routeList.size();
        for(int i = 1; i < size ; i++){
            if(distance < distanceMap.get(i))
            {
                if(i == size - 1){
                    return routeList.get(i);
                }else {
                    Position beforePosition = routeList.get(i - 1);
                    Position tempPosition = routeList.get(i);
                    float offsetNowPoint = distance - distanceMap.get(i-1);
                    float rat = offsetNowPoint / getDistance(tempPosition, beforePosition);
                    return new Position(beforePosition.getX() + rat * (tempPosition.getX() - beforePosition.getX()), beforePosition.getY() + rat * (tempPosition.getY() - beforePosition.getY()));
                }
            }
        }
        return null;
    }


    public static BattleAObject getMinHPUnit(List<BattleAObject> targets)
    {
        return Collections.min(targets, new Comparator<BattleAObject>() {
            @Override
            public int compare(BattleAObject o1, BattleAObject o2) {
                return new Integer(o1.getHp()).compareTo(o2.getHp());
            }
        });
    }

    public static BattleAObject getMaxAtkUnit(List<BattleAObject> targets)
    {
        return Collections.max(targets, new Comparator<BattleAObject>() {
            @Override
            public int compare(BattleAObject o1, BattleAObject o2) {
                return new Integer(o1.atk.getAtt()).compareTo(o2.atk.getAtt());
            }
        });
    }

    public static BattleAObject getMaxDefUnit(List<BattleAObject> targets)
    {
        return Collections.max(targets, new Comparator<BattleAObject>() {
            @Override
            public int compare(BattleAObject o1, BattleAObject o2) {
                return new Integer(o1.def.getAtt()).compareTo(o2.def.getAtt());
            }
        });
    }

    /**
     * 获取两点的距离
     * @param source
     * @param target
     * @return
     */
    public static float getDistance(Position source, Position target)
    {
        return (float) Math.abs(Math.sqrt(Math.pow((source.getX() - target.getX()), 2) + Math.pow((source.getY() - target.getY()), 2)));
    }

    /**
     * 获取两点的距离
     * @param source
     * @param target
     * @return
     */
    public static float getDistance(SGCommonProto.Pos source, SGCommonProto.Pos target)
    {
        return (float) Math.abs(Math.sqrt(Math.pow((source.getX() - target.getX()), 2) + Math.pow((source.getY() - target.getY()), 2)));
    }

    public static boolean checkPoint(Position leftTop, Position rightBottom, Position target){
        boolean in = false;
        if(target.getX() <= rightBottom.getX() && target.getX() >= leftTop.getX()
                && target.getY() <= leftTop.getY() && target.getY() >= rightBottom.getY()){
            in = true;
        }
        return in;
    }


    /**
     * 坐标转换成标准XY轴的
     * @param A
     * @param sinVale
     * @return
     */
    public static Position switchPoint(Position A, float sinVale){
        return new Position(A.y * sinVale, -A.x * sinVale);
    }
    /**
     * 坐标转换成标准XY轴的
     * @param A
     * @param sinVale
     * @param cosVale
     * @param tanVale
     * @return
     */
    public static Position switchPoint(Position A, float sinVale, float cosVale,float tanVale){
        float x = A.getX() / cosVale  + (A.getY() - A.getX() * tanVale) * sinVale;
        float y = A.getY() * cosVale - A.getX() * sinVale;
        return new Position(x, y);
    }


    /**
     * 检测目标是否在某个坐标的圆形攻击范围内
     * @param source 检测对象
     * @param target 要检测的目标
     * @param range 范围
     * @return
     */
    public static boolean isInCircleRange(Position source, Position target, int range)
    {
        if(range == 0)
            return true;

        // 两点间距离公式
        float result = getDistance(source, target);

        int temp = (int) result;
        return temp <= range;
    }


    /**
     * 以参考点在中心判断矩形
     * @param sourcePoint
     * @param directionPoint
     * @param checkPoint
     * @param width
     * @param height
     * @return
     */
    public static boolean isInRectangleByCenter(Position sourcePoint, Position directionPoint, Position checkPoint, int width, int height)
    {
        float r = (float) Math.sqrt(Math.pow(sourcePoint.getX() - directionPoint.getX(), 2) + Math.pow(sourcePoint.getY() - directionPoint.getY(), 2));
        float cosVale = (directionPoint.getX() - sourcePoint.getX()) / r;
        float sinVale = (directionPoint.getY() - sourcePoint.getY()) / r;
        //把起点坐标转换
        Position sA = null;
        //把要检测的点转换一个坐标
        Position sP = null;
        if(directionPoint.x == sourcePoint.x)
        {
            sA = switchPoint(sourcePoint, sinVale);
            sP = switchPoint(checkPoint, sinVale);
        }
        else
        {
            float tanVale = (directionPoint.getY() - sourcePoint.getY()) / (directionPoint.getX() - sourcePoint.getX());
            sA = switchPoint(sourcePoint, sinVale, cosVale, tanVale);
            sP = switchPoint(checkPoint, sinVale, cosVale, tanVale);
        }

        Position leftTop = new Position(sA.getX() - (width>>1), sA.getY() + (width>>2));
        Position rightBottom = new Position(sA.getX() + (height>>1), sA.getY() - (width>>2));

        return checkPoint(leftTop, rightBottom, sP);
    }

    /**
     * 检测矩形范围,以矩形的一条边的中间点为起点
     * @param sourcePoint 起点
     * @param directionPoint 目标方位点
     * @param checkPoint 要检测的点
     * @param width 长
     * @param height 宽
     * @param height 宽
     * @param sourceStart 是否是以source为起点
     */
    public static boolean isInRectangleByBorder(Position sourcePoint, Position directionPoint, Position checkPoint, int width, int height, boolean sourceStart)
    {
        float r = (float) Math.sqrt(Math.pow(sourcePoint.getX() - directionPoint.getX(), 2) + Math.pow(sourcePoint.getY() - directionPoint.getY(), 2));
        float cosVale = (directionPoint.getX() - sourcePoint.getX()) / r;
        cosVale = sourceStart ? cosVale : -cosVale;
        float sinVale = (directionPoint.getY() - sourcePoint.getY()) / r;
        sinVale = sourceStart ? sinVale : -sinVale;
        //把起点坐标转换
        Position sA = null;
        //把要检测的点转换一个坐标
        Position sP = null;
        if(directionPoint.x == sourcePoint.x)
        {
            sA = switchPoint(sourcePoint, sinVale);
            sP = switchPoint(checkPoint, sinVale);
        }
        else
        {
            float tanVale = (directionPoint.getY() - sourcePoint.getY()) / (directionPoint.getX() - sourcePoint.getX());
            sA = switchPoint(sourcePoint, sinVale, cosVale, tanVale);
            sP = switchPoint(checkPoint, sinVale, cosVale, tanVale);
        }

        Position leftTop = new Position(sA.getX(), sA.getY() + (width>>1));
        Position rightBottom = new Position(sA.getX() + height, sA.getY() - (width>>1));

        return checkPoint(leftTop, rightBottom, sP);
    }

    /**
     * 是否在扇形内
     * @param sourcePoint
     * @param directionPoint
     * @param checkPoint
     * @param radius
     * @param angle 角度必须在90度以内 ,大于0度
     * @param sourceStart 是否是以source为起点
     * @return
     */
    public static boolean isInSector(Position sourcePoint, Position directionPoint, Position checkPoint, int radius, int angle, boolean sourceStart)
    {
        //检测距离
        if(getDistance(sourcePoint, checkPoint) > radius)
            return false;

        if(checkPoint.getX() == (sourceStart ? sourcePoint.getX() : directionPoint.getX()))
            return true;

        //到检测点的距离
        float distance = getDistance(sourcePoint, directionPoint);
        float sinVale = (directionPoint.getY() - sourcePoint.getY()) / distance;
        float cosVale = (directionPoint.getX() - sourcePoint.getX()) / distance;
        float tanVale = 0;
        //检测点转换坐标
        Position checkSwitchPoint = null;
        Position startSwitchPoint = null;
        if(directionPoint.x == sourcePoint.x)
        {
            checkSwitchPoint = switchPoint(checkPoint, sinVale);
            if(sourceStart)
            {
                //起点转换坐标,转换成中间线重合X轴
                startSwitchPoint = switchPoint(sourcePoint, sinVale);
            }
            else
            {
                startSwitchPoint = switchPoint(directionPoint, sinVale);
            }
        }
        else
        {
            tanVale = (directionPoint.getY() - sourcePoint.getY()) / (directionPoint.getX() - sourcePoint.getX());
            checkSwitchPoint = switchPoint(checkPoint, sinVale, cosVale, tanVale);
            if(sourceStart)
            {
                //起点转换坐标,转换成中间线重合X轴
                startSwitchPoint = switchPoint(sourcePoint, sinVale, cosVale, tanVale);
            }
            else
            {
                startSwitchPoint = switchPoint(directionPoint, sinVale, cosVale, tanVale);
            }
        }


        //角度转弧度
        float radian = (float) ((angle>>1)*Math.PI/180);
        float max = (float) Math.tan(radian);
        //斜率
        float K = (checkSwitchPoint.getY() - startSwitchPoint.getY()) / (checkSwitchPoint.getX() - startSwitchPoint.getX());
        return K >= -max && K <= max;
    }

    public static void main(String[] args)
    {

        System.out.println(BattleUtils.isInCircleRange(new Position(49, 15), new Position(46, 12), 3));
    }



}
