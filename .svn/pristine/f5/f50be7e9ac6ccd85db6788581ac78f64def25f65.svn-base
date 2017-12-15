package com.douqu.game.core.astar;
/**
 * 
 * ClassName: Coord
 * 寻路格子坐标
 */
public class Grid
{

	public int x;
	public int y;

	public Grid(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) return false;
		if (obj instanceof Grid)
		{
			Grid c = (Grid) obj;
			return x == c.x && y == c.y;
		}
		return false;
	}

	@Override
	public String toString() {
		return "{" +
				"x=" + x +
				", y=" + y +
				'}';
	}
}
