package com.douqu.game.core.astar;

import org.apache.log4j.Logger;

import java.util.*;

public class AStar
{
	Logger logger = Logger.getLogger(AStar.class);

	/**
	 *估值类型
	 */
	public enum Heuristic
	{
		Manhattan,
		Diagonal,
		Euclidean,
	}

	public interface NodeCallback
	{
		public void OnPathComplete(List<Node> pathNodes);
	}

	public interface AbsCoordCallback
	{
		public void OnPathComplete(List<AbsCoord> pathAbsCoords);
	}

	public final static int DIRECT_VALUE = 10; // 横竖移动代价
	public final static int OBLIQUE_VALUE = 14; // 斜移动代价

	public Heuristic heuristic = Heuristic.Euclidean;

	List<Node> priorPath = new ArrayList<Node>(); //优先路径点
	boolean showPath = true;


	private AstarMap mapInfo;

	public AStar(AstarMap mapInfo)
	{
		this.mapInfo = mapInfo;
	}


	/**
	 * 寻路
	 * @param start
	 * @param end
	 * @param callBack
	 */
	public void getPath(Node start, Node end, NodeCallback callBack)
	{
		//转数组坐标
		if(mapInfo.anchor == AstarMap.AnchorPos.BottomLeft)
		{
			start.grid.y = mapInfo.height - start.grid.y -1;
			end.grid.y = mapInfo.height - end.grid.y -1;
		}

		try {
			List<Node> ends = new ArrayList<Node>();
			//ends.add(new Node(5,5));
			ends = GetEndNodes(start, end);
			List<Node> finalPath = new ArrayList<Node>();
			for (int i = 0; i < ends.size(); i++) {
				Node tempEnd = ends.get(i);
				List<Node> tempPath = getPath(start, tempEnd);
				finalPath.addAll(tempPath);
				if (i < ends.size() - 1) {
					finalPath.remove(finalPath.size() - 1);
					tempEnd.reset();
					if (mapInfo.anchor == AstarMap.AnchorPos.BottomLeft) {
						tempEnd.grid.y = mapInfo.height - tempEnd.grid.y - 1;
					}
					start = tempEnd;
				}
			}
			callBack.OnPathComplete(finalPath);
		}catch (Exception e){
			logger.info("寻路错误 -> start:" + start.grid + " end:" + end);
			logger.info(mapInfo);
		}
	}

	public void getPath(AbsCoord startPoint, AbsCoord endPoint, AbsCoordCallback callBack)
	{
		Node start = startPoint.toRoundNode();
		Node end = endPoint.toRoundNode();
		//转数组坐标
		if(mapInfo.anchor == AstarMap.AnchorPos.BottomLeft)
		{
			start.grid.y = mapInfo.height - start.grid.y -1;
			end.grid.y = mapInfo.height - end.grid.y -1;
		}

		List<Node> ends = new ArrayList<Node>();
		//ends.add(new Node(5,5));
		ends = GetEndNodes(start,end);
		List<AbsCoord> finalPath = new ArrayList<AbsCoord>();
		finalPath.add(startPoint);
		for(int i = 0; i< ends.size();i++)
		{
			Node tempEnd = ends.get(i);
			List<Node> tempPath = getPath(start, tempEnd);
			for(int j = 0;j<tempPath.size();j++)
			{
				Node tempNode = tempPath.get(j);
				finalPath.add(tempNode.toAbsCoord());
			}
			if(i < ends.size() -1)
			{
				finalPath.remove(finalPath.size() - 1);
				tempEnd.reset();
				if (mapInfo.anchor == AstarMap.AnchorPos.BottomLeft) {
					tempEnd.grid.y = mapInfo.height - tempEnd.grid.y - 1;
				}
				start = tempEnd;
			}
		}
		finalPath.add(endPoint);
		callBack.OnPathComplete(finalPath);
	}

	/**
	 * 移动当前结点
	 */
	private List<Node> getPath(Node start, Node end)
	{
		Queue<Node> openList = new PriorityQueue<Node>(); // 优先队列(升序)
		List<Node> closeList = new ArrayList<Node>();
		openList.add(start);

		while (!openList.isEmpty())
		{
			if (isGridInClose(end.grid,closeList))
			{
				List<Node> path = getPath(end);
				if(mapInfo.anchor == AstarMap.AnchorPos.BottomLeft)
				{
					for(Node n:path)
					{
						n.grid.y = mapInfo.height - n.grid.y - 1;
					}
				}
				Collections.reverse(path);
				return path;
			}
			Node current = openList.poll();
			closeList.add(current);
			addNeighborNodeInOpen(mapInfo,current,end,openList,closeList);
		}
		return null;
	}


	/**
	 * 在二维数组中绘制路径
	 */

	private List<Node> getPath(Node end)
	{
		if(end==null||mapInfo.maps==null) return null;
//		System.out.println("Node:"+end.toString()+"总代价：" + end.G);
		List<Node> pathNodes =  new ArrayList<Node>();
		while (end != null)
		{
			Grid g = end.grid;
			if(showPath)
			{
				if (mapInfo.anchor == AstarMap.AnchorPos.BottomLeft)
					mapInfo.setMap(g.x, mapInfo.height - g.y - 1, AstarMap.MapSignal.Path);
				else
					mapInfo.setMap(g, AstarMap.MapSignal.Path);
			}
			pathNodes.add(end);
			end = end.parent;
		}
		return pathNodes;
	}

	//获取节点
	//横屏地图
	private List<Node> GetEndNodes(Node start,Node end)
	{
		List<Node> endNodes =  new ArrayList<Node>();
		if((start.grid.x < mapInfo.width*0.5&&end.grid.x < mapInfo.width*0.5)||
				(start.grid.x > mapInfo.width*0.5&&end.grid.x > mapInfo.width*0.5))
		{
			endNodes.add(end);
		}
		else
		{
			Vector2 absDirect = new Vector2(end.grid.x - start.grid.x, start.grid.y -end.grid.y); //y方向向下增长
			float temp = mapInfo.width*0.5f-end.grid.x;
			Vector2 endFaceDirect = new Vector2(Math.signum(temp),0);
			boolean angleBelow180 = DirectionHelper.angleBelow180(absDirect,endFaceDirect);
			DirectionHelper.Direction playerFaceTrendDirect = DirectionHelper.GetTrendDirect(mapInfo,new Vector2(start.grid),new Vector2(end.grid));
			DirectionHelper.Direction middleNodeDirect = DirectionHelper.GetPriorPathDirect(angleBelow180,playerFaceTrendDirect);
		 	endNodes = GetPriorPathNode(mapInfo,start,end,middleNodeDirect,angleBelow180);
			endNodes.add(end);
		}
		return endNodes;
	}

	private List<Node> GetPriorPathNode(AstarMap mapInfo,Node pos,Node targetPos,DirectionHelper.Direction direct,boolean angleBelow180)
	{
		List<Node> targetNode = new ArrayList<Node>();
		//假定优先路径都在第三条线
		switch (direct)
		{
			case LeftDown:
				if(angleBelow180)
				{
					int delta = mapInfo.height -3 - pos.grid.y;
					Node node = GetActiveNode(mapInfo,pos.grid.x-delta,mapInfo.height - 3,DirectionHelper.Direction.Left);
					targetNode.add(node);
					targetNode.add(new Node(targetPos.grid.x,mapInfo.height - 3));
				}
				else
				{
					int delta = pos.grid.x - 2 ;
					Node node = GetActiveNode(mapInfo,2,pos.grid.y+delta,DirectionHelper.Direction.Down);
					targetNode.add(node);
				}
				break;
			case RightDown:
				if(angleBelow180)
				{
					int delta = mapInfo.width -3 - pos.grid.x;
					Node node = GetActiveNode(mapInfo,mapInfo.width -3,pos.grid.y+delta,DirectionHelper.Direction.Down);
					targetNode.add(node);
				}
				else
				{
					int delta = mapInfo.height -3 - pos.grid.y;
					Node node = GetActiveNode(mapInfo,pos.grid.x+delta,mapInfo.height - 3,DirectionHelper.Direction.Right);
					targetNode.add(node);
					targetNode.add(new Node(targetPos.grid.x,mapInfo.height - 3));
				}
				break;
			case RightUp:
				if(angleBelow180)
				{
					int delta = pos.grid.y - 2;
					Node node = GetActiveNode(mapInfo,pos.grid.x+delta,2,DirectionHelper.Direction.Right);
					targetNode.add(node);
					targetNode.add(new Node(targetPos.grid.x,2));
				}
				else
				{
					int delta = mapInfo.width -3 - pos.grid.y;
					Node node = GetActiveNode(mapInfo,pos.grid.x+delta, mapInfo.width - 3,DirectionHelper.Direction.Up);
					targetNode.add(node);
				}
				break;
			case LeftUp:
				if(angleBelow180)
				{
					int delta = pos.grid.x - 2;
					Node node = GetActiveNode(mapInfo,2, pos.grid.y-delta,DirectionHelper.Direction.Up);
					targetNode.add(node);
				}
				else
				{
					int delta = pos.grid.y - 2;
					Node node = GetActiveNode(mapInfo,pos.grid.x-delta, 2,DirectionHelper.Direction.Left);
					targetNode.add(node);
					targetNode.add(new Node(targetPos.grid.x,2));
				}
				break;
		}
		//验证有效性
		return  targetNode;
	}

	/**
	 * 添加所有邻结点到open表
	 */
	private void addNeighborNodeInOpen(AstarMap mapInfo,Node current,Node end,Queue<Node> openList,List<Node> closeList)
	{
		int x = current.grid.x;
		int y = current.grid.y;
		// 左
		addNeighborNodeInOpen(mapInfo,current,end, x - 1, y, DIRECT_VALUE,openList,closeList);
		// 上
		addNeighborNodeInOpen(mapInfo,current,end, x, y - 1, DIRECT_VALUE,openList,closeList);
		// 右
		addNeighborNodeInOpen(mapInfo,current,end, x + 1, y, DIRECT_VALUE,openList,closeList);
		// 下
		addNeighborNodeInOpen(mapInfo,current,end, x, y + 1, DIRECT_VALUE,openList,closeList);
		// 左上
		addNeighborNodeInOpen(mapInfo,current,end, x - 1, y - 1, OBLIQUE_VALUE,openList,closeList);
		// 右上
		addNeighborNodeInOpen(mapInfo,current,end, x + 1, y - 1, OBLIQUE_VALUE,openList,closeList);
		// 右下
		addNeighborNodeInOpen(mapInfo,current,end, x + 1, y + 1, OBLIQUE_VALUE,openList,closeList);
		// 左下
		addNeighborNodeInOpen(mapInfo,current,end, x - 1, y + 1, OBLIQUE_VALUE,openList,closeList);
	}

	/**
	 * 添加一个邻结点到open表
	 */
	private void addNeighborNodeInOpen(AstarMap mapInfo,Node current, Node end,int x, int y, int value,Queue<Node> openList,List<Node> closeList)
	{
		if (canAddNodeToOpen(mapInfo,x, y,closeList))
		{
			Grid grid = new Grid(x, y);
			int G = current.G + value; // 计算邻结点的G值
			Node child = findNodeInOpen(grid,openList);
			if (child == null)
			{
				int H=calcH(end.grid,grid); // 计算H值
				if(isEndNode(end.grid,grid))
				{
					child=end;
					child.parent=current;
					child.G=G;
					child.H=H;
				}
				else
				{
					child = new Node(grid, current, G, H);
				}
				openList.add(child);
			}
			else if (child.G > G)
			{
				child.G = G;
				child.parent = current;
				openList.add(child);
			}
		}
	}

	/**
	 * 从Open列表中查找结点
	 */
	private Node findNodeInOpen(Grid grid,Queue<Node> openList)
	{
		if (grid == null || openList.isEmpty()) return null;
		for (Node node : openList)
		{
			if (node.grid.equals(grid))
			{
				return node;
			}
		}
		return null;
	}


	/**
	 * 计算H的估值
	 */
	private int calcH(Grid end,Grid grid)
	{
		int h;
		int dx =  Math.abs(end.x - grid.x);
		int dy = Math.abs(end.y - grid.y);
		switch(heuristic)
		{
			case Diagonal:
				h = Math.max(dx,dy);
				break;
			case Euclidean:
				h = (int)(Math.sqrt((double)(dx*dx+dy*dy)));
				break;
			case Manhattan:
				h = dx+dy;
				break;
			default:
				h = Math.abs(end.x - grid.x) + Math.abs(end.y - grid.y);
				System.out.println("run here");
				break;
		}
		return h;
	}
	
	/**
	 * 判断结点是否是最终结点
	 */
	private boolean isEndNode(Grid end,Grid grid)
	{
		return grid != null && end.equals(grid);
	}

	/**
	 * 判断结点能否放入Open列表
	 */
	private boolean canAddNodeToOpen(AstarMap mapInfo,int x, int y,List<Node> closeList)
	{
		// 是否在地图中
		if (x < 0 || x >= mapInfo.width || y < 0 || y >= mapInfo.height) return false;
		// 判断是否是不可通过的结点
		if(mapInfo.isObstacle(x, y)) return false;
//		if (mapInfo.maps[y][x] == MapManager.OBSTACLE) return false;
		// 判断结点是否存在close表
		if (isGridInClose(x, y,closeList)) return false;

		return true;
	}

	/**
	 * 判断坐标是否在close表中
	 */
	private boolean isGridInClose(Grid grid,List<Node> closeList)
	{
		return grid!=null&&isGridInClose(grid.x, grid.y,closeList);
	}

	/**
	 * 判断坐标是否在close表中
	 */
	private boolean isGridInClose(int x, int y,List<Node> closeList)
	{
		if (closeList.isEmpty()) return false;
		for (Node node : closeList)
		{
			if (node.grid.x == x && node.grid.y == y)
			{
				return true;
			}
		}
		return false;
	}

	private Node GetActiveNode(AstarMap mapInfo,int x,int y,DirectionHelper.Direction direction)
	{
		if(!mapInfo.isObstacle(x, y)) return new Node(x,y);
		switch(direction)
		{
			case Right:
				x++;
				break;
			case Left:
				x--;
				break;
			case Up:
				y--;
				break;
			case Down:
				y++;
				break;
		}
		return GetActiveNode(mapInfo,x,y,direction);
	}

	public AstarMap getAstarMap()
	{
		return mapInfo;
	}
}
