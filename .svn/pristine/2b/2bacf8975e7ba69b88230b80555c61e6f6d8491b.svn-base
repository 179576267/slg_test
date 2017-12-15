package com.douqu.game.core.astar;

import com.douqu.game.core.config.common.Position;
import com.douqu.game.core.factory.ConstantFactory;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * ClassName: MapInfo 
 * 地图数据
 */
public class AstarMap implements Cloneable
{
	public enum AnchorPos
	{
		BottomLeft,
		TopLeft,
	}
	public int[][] maps; // 二维数组的地图

	public int width; // 地图的宽

	public int height; // 地图的高

	public int maxX;

	public int maxY;

	public int minX;

	public int minY;

	/**
	 * 原点位置
	 */
	public AnchorPos anchor = AnchorPos.BottomLeft;

	public AstarMap(AnchorPos anchor, int[][] maps, int width, int height)
	{
		this.anchor = anchor;
		this.width = width;
		this.height = height;
		this.maps = new int[maps.length][maps[0].length];
		for(int i = 0; i < maps.length; i++)
		{
			System.arraycopy(maps[i], 0, this.maps[i], 0, maps[i].length);
		}

		this.maxX = width - 2;
		this.maxY = height - 2;
		this.minX = 2;
		this.minY = 2;
	}


	public enum MapSignal
	{
		Free,       //可寻路点
		Obstacle,   //固定障碍
		Player,     //兵，寻路时会避开
		Path,       //路线点,寻路和此无关，只起显示路线作用
	}

	private final static int FREE = 0; // 自由通路
	private final static int OBSTACLE = 1; // 障碍值
	private final static int PLAYER = 2; // 角色
	private final static int PATH = 3; // 路径

	public Position getAtkRouteTarget(Position source, Position target, int gridArea)
	{
		int intX = target.getIntX();
		int intY = target.getIntY();
		if(source.getX() < target.getX())//自己在目标左边
		{
			for(int i = intX - gridArea; i <= intX + gridArea; i++)
			{
				if(source.getY() > target.getY())//自己在目标上面
				{
					for(int j = intY + gridArea; j >= intY - gridArea; j--)
					{
						if(i == intX && j == intY)
							continue;

						if(!isObstacleToApplication(i, j))
							return new Position(i, j);
					}
				}
				else if(source.getY() < target.getY())
				{
					for(int j = intY - gridArea; j <= intY + gridArea; j++)
					{
						if(i == intX && j == intY)
							continue;

						if(!isObstacleToApplication(i, j))
							return new Position(i, j);
					}
				}
			}
		}
		else if(source.getX() > target.getX())//自己在目标右边
		{
			for(int i = intX + gridArea; i >= intX + gridArea; i--)
			{
				if(source.getY() > target.getY())//自己在目标上面
				{
					for(int j = intY + gridArea; j >= intY - gridArea; j--)
					{
						if(i == intX && j == intY)
							continue;

						if(!isObstacleToApplication(i, j))
							return new Position(i, j);
					}
				}
				else if(source.getY() < target.getY())
				{
					for(int j = intY - gridArea; j <= intY + gridArea; j++)
					{
						if(i == intX && j == intY)
							continue;

						if(!isObstacleToApplication(i, j))
							return new Position(i, j);
					}
				}
			}
		}

		return null;
	}


	/**
	 * 取一个默认的士兵出生位置
	 * @return
	 */
	public Position getSoldierPos(int teamNo, int route, int gridArea)
	{
		if(teamNo == ConstantFactory.BATTLE_TEAM_1)
		{
			for(int i = gridArea; i < width; i++)
			{
				if(route == ConstantFactory.ROUTE_TOP)
				{
					for(int j = height-gridArea; j > 0; j--)
					{
						if(!isObstacleToApplication(i, j))
							return new Position(i, j);
					}
				}
				else
				{
					for(int j = gridArea; j < height; j++)
					{
						if(!isObstacleToApplication(i, j))
							return new Position(i, j);
					}
				}
			}
		}
		else if(teamNo == ConstantFactory.BATTLE_TEAM_2)
		{
			for(int i = width-gridArea; i > 0; i--)
			{
				if(route == ConstantFactory.ROUTE_TOP)
				{
					for(int j = height-gridArea; j > 0; j--)
					{
						if(!isObstacleToApplication(i, j))
							return new Position(i, j);
					}
				}
				else
				{
					for(int j = gridArea; j < height; j++)
					{
						if(!isObstacleToApplication(i, j))
							return new Position(i, j);
					}
				}
			}
		}

		return null;
	}


	public void setMapPlayers(List<ObjectBound> allPlayers,MapSignal signal)
	{
		for(ObjectBound  obj : allPlayers)
		{
			setMap(obj, signal);
		}
	}

	/**
	 * 是否是障碍物
	 * @param gridX
	 * @param gridY
	 * @return
	 */
	public boolean isObstacle(int gridX, int gridY)
	{
		if(maps[gridY][gridX] == OBSTACLE || maps[gridY][gridX] == PLAYER)
			return true;
		return false;
	}

	//是否是障碍物
	public boolean isObstacleToApplication(int gridXInApplication,int gridYInApplication)
	{
		if(maps[height - gridYInApplication -1][gridXInApplication] == OBSTACLE || maps[height - gridYInApplication -1][gridXInApplication] == PLAYER)
			return true;
		return false;
	}

	public void setMap(Grid g,MapSignal signal)
	{
		setMap(g.x, g.y, signal);
	}

	public void setMap(ObjectBound obj,MapSignal signal)
	{
		List<Grid> occupyGrids = obj.getOccupyGrids();
		for(Grid g : occupyGrids)
		{
			setMap(g,signal);
		}

		if(obj.lastOccupyGrids != null)
		{
			for(Grid g : obj.lastOccupyGrids)
			{
				boolean free = false;
				if(!occupyGrids.contains(g))
					free = true;
				if(free)
					setMap(g, MapSignal.Free);
			}
		}
		obj.lastOccupyGrids = occupyGrids;

	}
	public void setMap(int gridX,int gridY,MapSignal signal)
	{
		if(anchor == AstarMap.AnchorPos.BottomLeft)
		{
			gridY = height - gridY -1;
		}
		switch (signal)
		{
			case Free:
				if( maps[gridY][gridX] != OBSTACLE )
					maps[gridY][gridX]= FREE;
				break;
			case Obstacle:
				maps[gridY][gridX]= OBSTACLE;
				break;
			case Player:
				if( maps[gridY][gridX] != OBSTACLE )
					maps[gridY][gridX]= PLAYER;
				break;
			case Path:
				maps[gridY][gridX]= PATH;
				break;
		}
	}


	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		StringBuilder zhang = new StringBuilder();
		for (int i = 0; i < maps.length; i++)
		{
			for (int j = 0; j < maps[i].length; j++)
			{
				result.append(maps[i][j]);
				result.append(" ");
			}
			result.append("\n");
		}
		return result.substring(0, result.length()-1);
	}
}
