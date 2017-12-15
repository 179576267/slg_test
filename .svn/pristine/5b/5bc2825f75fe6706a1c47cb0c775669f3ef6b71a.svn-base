package com.douqu.game.main.util; /**
 * Java: Dijkstra算法获取最短路径(邻接表)
 *
 * @author skywang
 * @date 2014/04/24
 */

import com.douqu.game.core.config.map.RouteConfig;
import com.douqu.game.core.config.map.TownConfig;
import com.douqu.game.core.factory.DataFactory;

import java.util.*;

public class DijkstraUtils {

    private static int INF = Integer.MAX_VALUE;

    private static DijkstraUtils instance = null;

    private VNode[] towns;  // 顶点数组


    public static DijkstraUtils getInstance()
    {
        if(instance == null)
            instance = new DijkstraUtils();

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
            result.add(temp = map.get(temp));
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


//    /*
//     * 深度优先搜索遍历图的递归实现
//     */
//    private void DFS(int i, boolean[] visited) {
//        ENode node;
//
//        visited[i] = true;
//        System.out.printf("%d ", towns[i].id);
//        node = towns[i].firstEdge;
//        while (node != null) {
//            if (!visited[node.index])
//                DFS(node.index, visited);
//            node = node.nextEdge;
//        }
//    }
//
//    /*
//     * 深度优先搜索遍历图
//     */
//    public void DFS() {
//        boolean[] visited = new boolean[towns.length];       // 顶点访问标记
//
//        // 初始化所有顶点都没有被访问
//        for (int i = 0; i < towns.length; i++)
//            visited[i] = false;
//
//        System.out.printf("DFS: ");
//        for (int i = 0; i < towns.length; i++) {
//            if (!visited[i])
//                DFS(i, visited);
//        }
//        System.out.printf("\n");
//    }
//
//    /*
//     * 广度优先搜索（类似于树的层次遍历）
//     */
//    public void BFS() {
//        int head = 0;
//        int rear = 0;
//        int[] queue = new int[towns.length];            // 辅组队列
//        boolean[] visited = new boolean[towns.length];  // 顶点访问标记
//
//        for (int i = 0; i < towns.length; i++)
//            visited[i] = false;
//
//        System.out.printf("BFS: ");
//        for (int i = 0; i < towns.length; i++) {
//            if (!visited[i]) {
//                visited[i] = true;
//                System.out.printf("%d ", towns[i].id);
//                queue[rear++] = i;  // 入队列
//            }
//
//            while (head != rear) {
//                int j = queue[head++];  // 出队列
//                ENode node = towns[j].firstEdge;
//                while (node != null) {
//                    int k = node.index;
//                    if (!visited[k])
//                    {
//                        visited[k] = true;
//                        System.out.printf("%d ", towns[k].id);
//                        queue[rear++] = k;
//                    }
//                    node = node.nextEdge;
//                }
//            }
//        }
//        System.out.printf("\n");
//    }
//
//    /*
//     * 打印矩阵队列图
//     */
//    public void print() {
//        System.out.printf("List Graph:\n");
//        for (int i = 0; i < towns.length; i++) {
//            System.out.printf("%d(%d): ", i, towns[i].id);
//            ENode node = towns[i].firstEdge;
//            while (node != null) {
//                System.out.printf("%d(%d) ", node.index, towns[node.index].id);
//                node = node.nextEdge;
//            }
//            System.out.printf("\n");
//        }
//    }
//    /*
//     * prim最小生成树
//     *
//     * 参数说明：
//     *   start -- 从图中的第start个元素开始，生成最小树
//     */
//    public void prim(int start) {
//        int i,j,k,m,n,sum;
//        float min,tmp;
//        int num = towns.length;
//        int index=0;                   // prim最小树的索引，即prims数组的索引
//        int[] prims = new int[num];  // prim最小树的结果数组
//        float[] weights = new float[num];  // 顶点间边的权值
//
//        // prim最小生成树中第一个数是"图中第start个顶点"，因为是从start开始的。
//        prims[index++] = towns[start].id;
//
//        // 初始化"顶点的权值数组"，
//        // 将每个顶点的权值初始化为"第start个顶点"到"该顶点"的权值。
//        for (i = 0; i < num; i++ )
//            weights[i] = getWeight(start, i);
//
//        for (i = 0; i < num; i++) {
//            // 由于从start开始的，因此不需要再对第start个顶点进行处理。
//            if(start == i)
//                continue;
//
//            j = 0;
//            k = 0;
//            min = INF;
//            // 在未被加入到最小生成树的顶点中，找出权值最小的顶点。
//            while (j < num) {
//                // 若weights[j]=0，意味着"第j个节点已经被排序过"(或者说已经加入了最小生成树中)。
//                if (weights[j] != 0 && weights[j] < min) {
//                    min = weights[j];
//                    k = j;
//                }
//                j++;
//            }
//
//            // 经过上面的处理后，在未被加入到最小生成树的顶点中，权值最小的顶点是第k个顶点。
//            // 将第k个顶点加入到最小生成树的结果数组中
//            prims[index++] = towns[k].id;
//            // 将"第k个顶点的权值"标记为0，意味着第k个顶点已经排序过了(或者说已经加入了最小树结果中)。
//            weights[k] = 0;
//            // 当第k个顶点被加入到最小生成树的结果数组中之后，更新其它顶点的权值。
//            for (j = 0 ; j < num; j++) {
//                // 获取第k个顶点到第j个顶点的权值
//                tmp = getWeight(k, j);
//                // 当第j个节点没有被处理，并且需要更新时才被更新。
//                if (weights[j] != 0 && tmp < weights[j])
//                    weights[j] = tmp;
//            }
//        }
//
//        // 计算最小生成树的权值
//        sum = 0;
//        for (i = 1; i < index; i++) {
//            min = INF;
//            // 获取prims[i]在矩阵表中的位置
//            n = getPosition(prims[i]);
//            // 在vexs[0...i]中，找出到j的权值最小的顶点。
//            for (j = 0; j < i; j++) {
//                m = getPosition(prims[j]);
//                tmp = getWeight(m, n);
//                if (tmp < min)
//                    min = tmp;
//            }
//            sum += min;
//        }
//        // 打印最小生成树
//        System.out.printf("PRIM(%c)=%d: ", towns[start].id, sum);
//        for (i = 0; i < index; i++)
//            System.out.printf("%c ", prims[i]);
//        System.out.printf("\n");
//    }
//
//    /*
//     * 克鲁斯卡尔（Kruskal)最小生成树
//     */
//    public void kruskal() {
//        int index = 0;                      // rets数组的索引
//        int[] vends = new int[mEdgNum];     // 用于保存"已有最小生成树"中每个顶点在该最小树中的终点。
//        EData[] rets = new EData[mEdgNum];  // 结果数组，保存kruskal最小生成树的边
//        EData[] edges;                      // 图对应的所有边
//
//        // 获取"图中所有的边"
//        edges = getEdges();
//        // 将边按照"权"的大小进行排序(从小到大)
//        sortEdges(edges, mEdgNum);
//
//        for (int i=0; i<mEdgNum; i++) {
//            int p1 = getPosition(edges[i].start);      // 获取第i条边的"起点"的序号
//            int p2 = getPosition(edges[i].end);        // 获取第i条边的"终点"的序号
//
//            int m = getEnd(vends, p1);                 // 获取p1在"已有的最小生成树"中的终点
//            int n = getEnd(vends, p2);                 // 获取p2在"已有的最小生成树"中的终点
//            // 如果m!=n，意味着"边i"与"已经添加到最小生成树中的顶点"没有形成环路
//            if (m != n) {
//                vends[m] = n;                       // 设置m在"已有的最小生成树"中的终点为n
//                rets[index++] = edges[i];           // 保存结果
//            }
//        }
//
//        // 统计并打印"kruskal最小生成树"的信息
//        int length = 0;
//        for (int i = 0; i < index; i++)
//            length += rets[i].weight;
//        System.out.printf("Kruskal=%d: ", length);
//        for (int i = 0; i < index; i++)
//            System.out.printf("(%c,%c) ", rets[i].start, rets[i].end);
//        System.out.printf("\n");
//    }
//
//    /*
//     * 获取图中的边
//     */
//    private EData[] getEdges() {
//        int index=0;
//        EData[] edges;
//
//        edges = new EData[mEdgNum];
//        for (int i=0; i < towns.length; i++) {
//
//            ENode node = towns[i].firstEdge;
//            while (node != null) {
//                if (node.index > i) {
//                    edges[index++] = new EData(towns[i].id, towns[node.index].id, node.weight);
//                }
//                node = node.nextEdge;
//            }
//        }
//
//        return edges;
//    }
//
//    /*
//     * 对边按照权值大小进行排序(由小到大)
//     */
//    private void sortEdges(EData[] edges, int elen) {
//
//        for (int i=0; i<elen; i++) {
//            for (int j=i+1; j<elen; j++) {
//
//                if (edges[i].weight > edges[j].weight) {
//                    // 交换"边i"和"边j"
//                    EData tmp = edges[i];
//                    edges[i] = edges[j];
//                    edges[j] = tmp;
//                }
//            }
//        }
//    }
//
//    /*
//     * 获取i的终点
//     */
//    private int getEnd(int[] vends, int i) {
//        while (vends[i] != 0)
//            i = vends[i];
//        return i;
//    }
//
//    /*
//     * Dijkstra最短路径。
//     * 即，统计图中"顶点vs"到其它各个顶点的最短路径。
//     *
//     * 参数说明：
//     *       vs -- 起始顶点(start vertex)。即计算"顶点vs"到其它顶点的最短路径。
//     *     prev -- 前驱顶点数组。即，prev[i]的值是"顶点vs"到"顶点i"的最短路径所经历的全部顶点中，位于"顶点i"之前的那个顶点。
//     *     dist -- 长度数组。即，dist[i]是"顶点vs"到"顶点i"的最短路径的长度。
//     */
//    public void dijkstra(int vs, int[] prev, float[] dist) {
//        // flag[i]=true表示"顶点vs"到"顶点i"的最短路径已成功获取。
//        boolean[] flag = new boolean[towns.length];
//
//        // 初始化
//        for (int i = 0; i < towns.length; i++) {
//            flag[i] = false;            // 顶点i的最短路径还没获取到。
//            prev[i] = 0;                // 顶点i的前驱顶点为0。
//            dist[i] = getWeight(vs, i); // 顶点i的最短路径为"顶点vs"到"顶点i"的权。
//        }
//
//        // 对"顶点vs"自身进行初始化
//        flag[vs] = true;
//        dist[vs] = 0;
//
//        // 遍历mVexs.length-1次；每次找出一个顶点的最短路径。
//        int k = 0;
//        for (int i = 1; i < towns.length; i++) {
//            // 寻找当前最小的路径；
//            // 即，在未获取最短路径的顶点中，找到离vs最近的顶点(k)。
//            float min = INF;
//            for (int j = 0; j < towns.length; j++) {
//                if (flag[j]==false && dist[j]<min) {
//                    min = dist[j];
//                    k = j;
//                }
//            }
//            // 标记"顶点k"为已经获取到最短路径
//            flag[k] = true;
//
//            // 修正当前最短路径和前驱顶点
//            // 即，当已经"顶点k的最短路径"之后，更新"未获取最短路径的顶点的最短路径和前驱顶点"。
//            for (int j = 0; j < towns.length; j++) {
//                float tmp = getWeight(k, j);
//                tmp = (tmp==INF ? INF : (min + tmp)); // 防止溢出
//                if (flag[j]==false && (tmp<dist[j]) )
//                {
//                    dist[j] = tmp;
//                    prev[j] = k;
//                }
//            }
//        }
//
//        // 打印dijkstra最短路径的结果
//        System.out.printf("dijkstra(%d): \n", towns[vs].id);
//        for (int i = 0; i < towns.length; i++)
//            System.out.printf("  shortest(%d, %d)=%f\n", towns[vs].id, towns[i].id, dist[i]);
//    }



    public static void main(String[] args) {

        DijkstraUtils pG;

        // 自定义"图"(输入矩阵队列)
        // 采用已有的"图"
        pG = new DijkstraUtils();

//        pG.print();   // 打印图
//        pG.DFS();     // 深度优先遍历
//        pG.BFS();     // 广度优先遍历
//        pG.prim(0);   // prim算法生成最小生成树
//        pG.kruskal(); // Kruskal算法生成最小生成树

//        int[] prev = new int[pG.towns.length];
//        float[] dist = new float[pG.towns.length];
        // dijkstra算法获取"第4个顶点"到其它各个顶点的最短距离
//        pG.dijkstra(0, prev, dist);
//        pG.getRouteList(0, 38);
    }
}