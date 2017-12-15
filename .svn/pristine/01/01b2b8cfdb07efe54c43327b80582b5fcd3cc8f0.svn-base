package com.douqu.game.core.factory;

import com.douqu.game.core.config.map.RouteConfig;
import com.douqu.game.core.config.map.TownConfig;

import java.util.*;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-11-29 17:47
 */
public class RouteFactory {

    private static int INF = Integer.MAX_VALUE;

    private static RouteFactory instance = null;

    private VNode[] towns;  // 顶点数组


    public static RouteFactory getInstance()
    {
        if(instance == null)
            instance = new RouteFactory();

        return instance;
    }

    /*
   * 创建图(用已提供的矩阵)
   *
   * 参数说明：
   *     vexs  -- 顶点数组
   *     edges -- 边
   */
    public void init()
    {
        List<TownConfig> townList = DataFactory.getInstance().getDataList(DataFactory.TOWN_KEY);
        // 初始化"顶点"
        towns = new VNode[townList.size()];
        for (int i = 0; i < towns.length; i++) {
            towns[i] = new VNode();
            towns[i].id = townList.get(i).id;
            towns[i].firstEdge = null;
        }

        List<RouteConfig> routeList = DataFactory.getInstance().getDataList(DataFactory.ROUTE_KEY);
        EData[] routes = new EData[routeList.size()];
        RouteConfig routeConfig = null;
        for(int i = 0; i < routes.length; i++)
        {
            routeConfig = routeList.get(i);
            routes[i] = new EData(routeConfig.townA, routeConfig.townB, routeConfig.distance, getPosition(routeConfig.townA), getPosition(routeConfig.townB));
        }

        // 初始化"边"
        ENode node = null;
        for (EData data : routes)
        {
            // 初始化node1
            node = new ENode();
            node.id = data.end;
            node.index = data.endIndex;
            node.weight = data.weight;
            // 将node1链接到"p1所在链表的末尾"
            if(towns[data.startIndex].firstEdge == null)
                towns[data.startIndex].firstEdge = node;
            else
                linkLast(towns[data.startIndex].firstEdge, node);

            // 初始化node2
            node = new ENode();
            node.id = data.start;
            node.index = data.startIndex;
            node.weight = data.weight;
            // 将node2链接到"p2所在链表的末尾"
            if(towns[data.endIndex].firstEdge == null)
                towns[data.endIndex].firstEdge = node;
            else
                linkLast(towns[data.endIndex].firstEdge, node);
        }
    }


    /*
    * 获取边<start, end>的权值；若start和end不是连通的，则返回无穷大。
    */
    private float getWeight(int start, int end) {

        if (start==end)
            return 0;

        ENode node = towns[start].firstEdge;
        while (node != null) {
            if (end == node.index)
                return node.weight;
            node = node.nextEdge;
        }

        return INF;
    }

    /*
    * Dijkstra最短路径。
    * 即，统计图中"顶点vs"到其它各个顶点的最短路径。
    *
    * 参数说明：
    *       vs -- 起始顶点(start vertex)。即计算"顶点vs"到其它顶点的最短路径。
    *     prev -- 前驱顶点数组。即，prev[i]的值是"顶点vs"到"顶点i"的最短路径所经历的全部顶点中，位于"顶点i"之前的那个顶点。
    *     dist -- 长度数组。即，dist[i]是"顶点vs"到"顶点i"的最短路径的长度。
    */
    public List<Integer> getRouteList(int startId, int endId)
    {
        if(startId == endId)
            return null;

        int startIndex = getPosition(startId);
        if(startIndex == -1)
            return null;

        int endIndex = getPosition(endId);
        if(endIndex == -1)
            return null;

        // flag[i]=true表示"顶点vs"到"顶点i"的最短路径已成功获取。
        boolean[] flag = new boolean[towns.length];

        int[] prev = new int[towns.length];
        float[] dist = new float[towns.length];

        // 初始化
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < towns.length; i++) {
            flag[i] = false;            // 顶点i的最短路径还没获取到。
            prev[i] = 0;                // 顶点i的前驱顶点为0。
            dist[i] = getWeight(startIndex, i); // 顶点i的最短路径为"顶点vs"到"顶点i"的权。

            map.put(towns[i].id, startId);
        }

        // 对"顶点vs"自身进行初始化
        flag[startIndex] = true;
        dist[startIndex] = 0;


        // 遍历towns.length-1次；每次找出一个顶点的最短路径。
        int k = 0;
        for (int i = 1; i < towns.length; i++)
        {
            // 寻找当前最小的路径；
            // 即，在未获取最短路径的顶点中，start(k)。
            float min = INF;
            for (int j = 0; j < towns.length; j++) {
                if (!flag[j] && dist[j] < min) {
                    min = dist[j];
                    k = j;
                }
            }
            // 标记"顶点k"为已经获取到最短路径
            flag[k] = true;

            // 修正当前最短路径和前驱顶点
            // 即，当已经"顶点k的最短路径"之后，更新"未获取最短路径的顶点的最短路径和前驱顶点"。
            for (int j = 0; j < towns.length; j++) {
                float tmp = getWeight(k, j);
                tmp = (tmp==INF ? INF : (min + tmp)); // 防止溢出
                if (!flag[j] && tmp < dist[j])
                {
                    dist[j] = tmp;
                    prev[j] = k;

                    map.put(towns[j].id, towns[k].id);
                }
            }
        }

        // 打印dijkstra最短路径的结果
//        System.out.printf("dijkstra(%d): \n", towns[start].id);
//        for (int i = 0; i < towns.length; i++)
//            System.out.printf("  shortest(%d, %d)=%f\n", towns[start].id, towns[i].id, dist[i]);

        List<Integer> result = new ArrayList<>();
        int temp = endId;
        result.add(temp);
        while(temp != startId)
        {
            result.add(0, temp = map.get(temp));
        }

        return result;
    }

    /*
     * 将node节点链接到list的最后
     */
    private void linkLast(ENode list, ENode node) {
        ENode p = list;

        while(p.nextEdge!=null)
            p = p.nextEdge;
        p.nextEdge = node;
    }

    /*
     * 返回ch位置
     */
    private int getPosition(int ch) {
        for(int i=0; i< towns.length; i++)
            if(towns[i].id == ch)
                return i;
        return -1;
    }



    // 边的结构体
    private class EData {
        int start; // 边的起点
        int end;   // 边的终点
        float weight; // 边的权重
        int startIndex;   //起点的索引
        int endIndex;  //终点的索引

        public EData(int start, int end, float weight, int startIndex, int endIndex) {
            this.start = start;
            this.end = end;
            this.weight = weight;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }
    }


    // 邻接表中表对应的链表的顶点
    private class ENode {
        int id;
        int index;       // 该边所指向的顶点的位置
        float weight;     // 该边的权
        ENode nextEdge; // 指向下一条弧的指针

        @Override
        public String toString() {
            return "{" +
                    "id=" + id +
                    ", index=" + index +
                    ", weight=" + weight +
                    ", nextEdge=" + nextEdge +
                    '}';
        }
    }

    // 邻接表中表的顶点
    private class VNode {
        int id;          // 顶点信息
        ENode firstEdge;    // 指向第一条依附该顶点的弧

        @Override
        public String toString() {
            return "{" +
                    "id=" + id +
                    ", firstEdge=" + firstEdge +
                    '}';
        }
    }
}
