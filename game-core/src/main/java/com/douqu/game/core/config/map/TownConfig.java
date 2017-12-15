package com.douqu.game.core.config.map;

import com.douqu.game.core.config.common.Position;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.util.LoadUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-11-07 16:25
 */
public class TownConfig extends GameObject {

    public String model;
    /**
     * 所在位置
     */
    public Position position;

    /**
     * 初始阵营
     */
    public int camp;

    /**
     * 类型
     */
    public int type;

    private Map<Integer, Float> routeMap = new HashMap<>();

    /*****************************以上是配置文件属性,以下是动态属性******************************************/

    /**
     * 当前状态
     */
    public int status;

    /**
     * 所属阵营
     */
    public int masterCamp;

    public TownConfig() {
    }

    public TownConfig(int id, String name, int type, int camp, Position position)
    {
        this.id = id;
        this.name = name;
        this.type = type;
        this.camp = camp;
        this.position = position;
    }


    @Override
    public void copyTo(GameObject obj) {
        super.copyTo(obj);

        TownConfig townConfig = (TownConfig) obj;
        townConfig.model = this.model;
        townConfig.position = this.position;
        townConfig.camp = this.camp;
        townConfig.type = this.type;
        townConfig.status = this.status;
        townConfig.masterCamp = this.masterCamp;
        townConfig.routeMap = this.routeMap;
    }


    /**
     * 加载完路线后设置
     */
    public void initRouteInfo()
    {
        List<RouteConfig> list = DataFactory.getInstance().getDataList(DataFactory.ROUTE_KEY);
        for(RouteConfig routeConfig : list)
        {
            if(routeConfig.townA == id)
            {
                routeMap.put(routeConfig.townB, routeConfig.distance);
            }
            else if(routeConfig.townB == id)
            {
                routeMap.put(routeConfig.townA, routeConfig.distance);
            }
        }
    }

    /**
     * 获取到这个城的距离
     * @param target
     * @return
     */
    public float getDistance(int target)
    {
        return routeMap.get(target);
    }






    public static void calculateNext(TownConfig start, TownConfig end, List<Integer> passList, Map<Integer, TownConfig> townMap, List<List<Integer>> result){
        for(Integer id : start.getRouteMap().keySet()){
            if(passList.contains(id)){
                continue;
            }
            List<Integer> temp = new ArrayList<>();
            temp.addAll(passList);
            temp.add(id);
            if(id == end.id){//找到了
                System.out.println("找到路径了:" + temp);
                result.add(temp);
            } else {
//                System.out.println(temp);
                calculateNext(townMap.get(id), end, temp, townMap, result);
            }
        }
    }

    public Map<Integer, Float> getRouteMap() {
        return routeMap;
    }



    @Override
    public void setVariable(String key, String value)
    {
        if("position".equals(key))
        {
            int[] xy = LoadUtils.loadIntArray(key, value);
            position = new Position(xy[0], xy[1]);
        }
        else
            super.setVariable(key, value);
    }


    public static void main(String[] args)
    {
        Map<Integer, TownConfig> dataMap = new HashMap<>();
        dataMap.put(1, new TownConfig(1, "北京1", 1, 1, new Position(1,2)));
        dataMap.put(2, new TownConfig(2, "北京2", 2, 2, new Position(2,3)));
        dataMap.put(3, new TownConfig(3, "北京3", 1, 3, new Position(4,2)));
        dataMap.put(4, new TownConfig(4, "北京4", 2, 1, new Position(4,1)));
        dataMap.put(5, new TownConfig(5, "北京5", 3, 2, new Position(5,2)));
        dataMap.put(6, new TownConfig(6, "北京6", 4, 3, new Position(4,4)));
        dataMap.put(7, new TownConfig(7, "北京7", 1, 1, new Position(1,4)));
        dataMap.put(8, new TownConfig(8, "北京8", 2, 2, new Position(0,3)));
        dataMap.put(9, new TownConfig(9, "北京9", 2, 3, new Position(2,0)));
        dataMap.put(0, new TownConfig(0, "北京0", 2, 1, new Position(6,4)));

        List<RouteConfig> routeConfigList = new ArrayList<>();
        routeConfigList.add(new RouteConfig(1,2,5,1));
        routeConfigList.add(new RouteConfig(1,7,4,1));
        routeConfigList.add(new RouteConfig(1,8,3,1));
        routeConfigList.add(new RouteConfig(2,9,8,1));
        routeConfigList.add(new RouteConfig(2,3,7,1));
        routeConfigList.add(new RouteConfig(2,6,10,1));
        routeConfigList.add(new RouteConfig(3,4,15,1));
        routeConfigList.add(new RouteConfig(3,5,13,1));
        routeConfigList.add(new RouteConfig(3,6,17,1));
        routeConfigList.add(new RouteConfig(4,9,27,1));
        routeConfigList.add(new RouteConfig(5,0,1,1));
        routeConfigList.add(new RouteConfig(7,8,2,1));

        for(Integer key : dataMap.keySet())
        {
            TownConfig townConfig = dataMap.get(key);
            for(RouteConfig routeConfig : routeConfigList)
            {
                if(routeConfig.townA == townConfig.id)
                {
                    townConfig.getRouteMap().put(routeConfig.townB, routeConfig.distance);
                }
                else if(routeConfig.townB == townConfig.id)
                {
                    townConfig.getRouteMap().put(routeConfig.townA, routeConfig.distance);
                }
            }

//            if(key == 1)
//            System.out.println("town -> " + town.getRouteMap());
        }

        List<Integer> passList = new ArrayList<>();

        List<List<Integer>> result = new ArrayList<>();

        TownConfig start = dataMap.get(1);
        TownConfig end = dataMap.get(0);
        System.out.println("起点 -> " + start);
        System.out.println("终点 -> " + end);
        passList.add(start.id);

        TownConfig temp = start;
        long startTime = System.currentTimeMillis();

        System.out.println("result:" + result);
        System.out.println("passList:" + passList);
//        calculateNext(start, end, passList, dataMap, result);

//        System.out.println("结果:");
//        System.out.println(result);

//        for(List<Integer> list : result)
//        {
//            int total = 0;
//            for(int i = 0; i < list.size(); i++)
//            {
//                if(i > 0)
//                {
//                    total += dataMap.get(list.get(i-1)).getDistance(list.get(i));
//                }
//            }
//            System.out.println("总距离:" + total);
//        }
    }


}
