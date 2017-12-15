package com.douqu.game.core.astar;
/**
 * 
 * ClassName: Node 
 * 路径节点
 */
public class Node implements Comparable<Node>
{

	public Grid grid; // 坐标
	public Node parent; // 父结点
	public int G; // G：是个准确的值，是起点到当前结点的代价
	public int H; // H：是个估值，当前结点到目的结点的估计代价

	public Node(int x, int y)
	{
		this.grid = new Grid(x, y);
	}

	public Node(Grid grid)
	{
		this.grid = grid;
	}
	public Node(Grid grid, Node parent, int g, int h)
	{
		this.grid = grid;
		this.parent = parent;
		G = g;
		H = h;
	}

	public void reset()
	{
		this.parent = null;
		G = 0;
		H = 0;
	}

	public String toString()
	{
		return grid.toString();
	}

	public AbsCoord toAbsCoord()
	{
		return new AbsCoord(grid.x,grid.y);
	}
	@Override
	public int compareTo(Node o)
	{
		if (o == null) return -1;
		if (G + H > o.G + o.H)
			return 1;
		else if (G + H < o.G + o.H) return -1;
		return 0;
	}
}
