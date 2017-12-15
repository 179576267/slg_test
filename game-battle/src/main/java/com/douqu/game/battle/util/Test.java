package com.douqu.game.battle.util;

import com.douqu.game.core.astar.*;
import com.douqu.game.core.config.common.Position;
import com.douqu.game.core.factory.ConstantFactory;

import java.util.List;

import java.util.Date;

import java.text.SimpleDateFormat;

public class Test
{
    public static void main(String[] args)
    {
        //二维数组地图以左上对齐方式
        AstarMap info = new AstarMap(AstarMap.AnchorPos.BottomLeft, BattleUtils.BATTLE_MAP_1, BattleUtils.BATTLE_MAP_1[0].length, BattleUtils.BATTLE_MAP_1.length);

        AStar astar = new AStar(info);

//        System.out.println(info);

        Position target = new Position(3,12);
        int targetArea = 3;
        ObjectBound targetBound = new ObjectBound(targetArea);
        AbsCoord coord = new AbsCoord((float)(target.x+Math.random()),(float)(target.y+Math.random()));
        targetBound.setCenter(coord);
        astar.getAstarMap().setMap(targetBound, AstarMap.MapSignal.Player);

        Position source = new Position(40,13);
        int sourceArea = 2;
        ObjectBound sourceBound = new ObjectBound(sourceArea);
        coord = new AbsCoord((float)(source.x+Math.random()),(float)(source.y+Math.random()));
        sourceBound.setCenter(coord);
        astar.getAstarMap().setMap(sourceBound, AstarMap.MapSignal.Player);

        System.out.println("------------------------------------------------------------");
        System.out.println(info);


        int[][] maps = BattleUtils.BATTLE_MAP_1;
        int startX = 0, endX = 0, startY = 0, endY = 0;
        //ax表示x++,ay表示y++

        System.out.println("左上:" + targetBound.getVertex(DirectionHelper.Direction.LeftUp));
        System.out.println("左下:" + targetBound.getVertex(DirectionHelper.Direction.LeftDown));
        System.out.println("右上:" + targetBound.getVertex(DirectionHelper.Direction.RightUp));
        System.out.println("右下:" + targetBound.getVertex(DirectionHelper.Direction.RightDown));
        if(source.x <= target.x)
        {
            Grid leftUpGrid = targetBound.getVertex(DirectionHelper.Direction.LeftUp);
            Grid leftDownGrid = targetBound.getVertex(DirectionHelper.Direction.LeftDown);
            startX = leftDownGrid.x - 1;
            endX = leftDownGrid.x - sourceArea;
            if(source.y <= target.y)
            {
                System.out.println("左下");
                //x--,y++
                startY = leftDownGrid.y - sourceArea;
                endY = leftUpGrid.y + sourceArea;
            }
            else
            {
                System.out.println("左上");
                //x--,y--
                startY = leftUpGrid.y + sourceArea;
                endY = leftDownGrid.y - sourceArea;
            }
        }
        else
        {
            Grid rightUpGrid = targetBound.getVertex(DirectionHelper.Direction.RightUp);
            Grid rightDownGrid = targetBound.getVertex(DirectionHelper.Direction.RightDown);
            startX = rightDownGrid.x + 1;
            endX = rightDownGrid.x + sourceArea;
            if(source.y <= target.y)
            {
                System.out.println("右下");
                //x++,y++
                startY = rightDownGrid.y - sourceArea;
                endY = rightUpGrid.y + sourceArea;
            }
            else
            {
                System.out.println("右上");
                //x++,y--
                startY = rightUpGrid.y + sourceArea;
                endY = rightDownGrid.y - sourceArea;
            }
        }

        System.out.println("source:" + source + " target:" + target);
        System.out.println("startX:" + startX + " endX:" + endX + " startY:" + startY + " endY:" + endY);
        if(startX > endX)
        {
            choose:
            for (int i = startX; i >= endX; i--)
            {
                if(startY > endY)
                {
                    for (int j = startY; j >= endY; j--)
                    {
                        if(!astar.getAstarMap().isObstacleToApplication(i, j))
                        {
                            System.out.println("选定目标点 -> {x=" + i + ",y=" + j + "}");
                            break choose;
                        }
                    }
                }
                else
                {
                    for (int j = startY; j <= endY; j++)
                    {
                        if(!astar.getAstarMap().isObstacleToApplication(i, j))
                        {
                            System.out.println("选定目标点 -> {x=" + i + ",y=" + j + "}");
                            break choose;
                        }
                    }
                }
            }
        }
        else
        {
            choose:
            for (int i = startX; i <= endX; i++)
            {
                if(startY > endY)
                {
                    for (int j = startY; j >= endY; j--)
                    {
                        if(!astar.getAstarMap().isObstacleToApplication(i, j))
                        {
                            System.out.println("选定目标点 -> {x=" + i + ",y=" + j + "}");
                            break choose;
                        }
                    }
                }
                else
                {
                    for (int j = startY; j <= endY; j++)
                    {
                        if(!astar.getAstarMap().isObstacleToApplication(i, j))
                        {
                            System.out.println("选定目标点 -> {x=" + i + ",y=" + j + "}");
                            break choose;
                        }
                    }
                }
            }
        }




//        System.out.println(info);

//        //Node(1,1) Node(10, 1)为用户坐标，即相对左下角为零点，向上增长坐标系坐标
//        int startMinX = 1;
//        int startMaxX = 20;
//        int startMinY = 1;
//        int startMaxY = 23;
//        int endMinX = 29;
//        int endMaxX = 48;
//        int endMinY = 1;
//        int endMaxY = 23;

//        for(int i = 0; i < 1; i++)
//        {
//            int startX = (int) (Math.random() * (startMaxX - startMinX + 1) + startMinX);
//            int startY = (int) (Math.random() * (startMaxY - startMinY + 1) + startMinY);
//            int endX = (int) (Math.random() * (endMaxX - endMinX + 1) + endMinX);
//            int endY = (int) (Math.random() * (endMaxY - endMinY + 1) + endMinY);
//            int startX = 25, startY = 1, endX = 5, endY = 10;

//            search(astar, new Node(startX, startY), new Node(endX, endY));
//        }
    }


    public static void search(AStar astar, Node start, Node end)
    {
        final long t1 = System.currentTimeMillis();
        System.out.println("起点 -> " + start.grid + "  终点 -> " + end.grid);
        System.out.println("重点是否有障碍:" + astar.getAstarMap().isObstacleToApplication(end.grid.x, end.grid.y));
        astar.getPath(start, end, pathNodes -> {
//				for (Node n : pathNodes)
//				{
//					System.out.print("node:"+n.grid.x+ " 	"+n.grid.y+"\n");
//				}
//				List<ObjectBound> players = new  ArrayList<ObjectBound>();
//				ObjectBound playerOne = new ObjectBound(2);
//				players.add(playerOne);
//				Tick(players,pathNodes);
				System.out.print("===============================================\n");
//				printMap(astar.getAstarMap().maps);
            final long t2 = System.currentTimeMillis();
            System.out.println("获取路线花费时间:" + (t2 - t1));
            System.out.println(astar.getAstarMap());
            System.out.println("获取到路线:" + pathNodes);
        });
    }

    public static void Tick(final List<ObjectBound> players,final List<Node> pathNodes)
    {
        final long timeInterval = 500;
        Runnable runnable = new Runnable()
        {
            public void run()
            {
                int index = 0;
                while (index < pathNodes.size())
                {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                    System.out.println(df.format(new Date()));
                    try
                    {
                        Node curNode = pathNodes.get(index);
                        AbsCoord coord = new AbsCoord((float)(curNode.grid.x+Math.random()),(float)(curNode.grid.y+Math.random()));
                        players.get(0).setCenter(coord);
//                        MapManager.setMapPlayers(players, MapManager.MapSignal.Player);
                        System.out.print("player center pos:"+players.get(0).center.toString()+" \n");
//						printMap(MapManager.flexibleMapInfo.maps);
                        index++;
                        Thread.sleep(timeInterval);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    /**
     * 打印地图
     */
    public static void printMap(int[][] maps)
    {
        for (int i = 0; i < maps.length; i++)
        {
            for (int j = 0; j < maps[i].length; j++)
            {
                System.out.print(maps[i][j] + " ");
            }
            System.out.println();
        }
    }
}
