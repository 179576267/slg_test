package com.douqu.game.core.astar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.douqu.game.core.astar.DirectionHelper.Direction;
import com.douqu.game.core.factory.DataFactory;

/**
 * Created by matrix on 2017/12/2.
 */
public class ObjectBound
{
    public AbsCoord center;
    //全部都是方形,1*1  2*2  3*3
    public int size;

    public  List<Grid> lastOccupyGrids;
    public ObjectBound(int size)
    {
        this.size = size;
    }

    public void setCenter(AbsCoord center)
    {
        this.center = center;
    }
    public List<Grid> getOccupyGrids()
    {
        List<Grid> grids =  new ArrayList<Grid>();
        float topY = center.y + 0.5f*size;
        float bottomY = center.y - 0.5f*size;
        float leftX = center.x - 0.5f*size;
        float rightX = center.x + 0.5f*size;

        int topGridY = Math.round(topY);
        int bottomGridY = Math.round(bottomY);
        int leftGridX = Math.round(leftX);
        int rightGridX = Math.round(rightX);
        for(int i = leftGridX; i<rightGridX;i++)
        {
            for(int j = bottomGridY;j<topGridY;j++)
            {
                Grid grid = new Grid(i,j);
                grids.add(grid);
            }
        }
        return  grids;
    }

    public Grid getVertex(Direction direction)
    {
        List<Grid> gridList = getOccupyGrids();

        if(Direction.LeftUp == direction)
        {
            Grid tempGrid = getMinXGrid(gridList);
            List<Grid> tempList = new ArrayList<>();
            for(Grid grid : gridList)
            {
                if(grid.x == tempGrid.x)
                    tempList.add(grid);
            }

            return getMaxYGrid(tempList);
        }
        else if(Direction.LeftDown == direction)
        {
            Grid tempGrid = getMinXGrid(gridList);
            List<Grid> tempList = new ArrayList<>();
            for(Grid grid : gridList)
            {
                if(grid.x == tempGrid.x)
                    tempList.add(grid);
            }

            return getMinYGrid(tempList);
        }
        else if(Direction.RightUp == direction)
        {
            Grid tempGrid = getMaxXGrid(gridList);
            List<Grid> tempList = new ArrayList<>();
            for(Grid grid : gridList)
            {
                if(grid.x == tempGrid.x)
                    tempList.add(grid);
            }

            return getMaxYGrid(tempList);
        }
        else if(Direction.RightDown == direction)
        {
            Grid tempGrid = getMaxXGrid(gridList);
            List<Grid> tempList = new ArrayList<>();
            for(Grid grid : gridList)
            {
                if(grid.x == tempGrid.x)
                    tempList.add(grid);
            }

            return getMinYGrid(tempList);
        }

        return null;
    }

    public Grid getMinXGrid(List<Grid> gridList)
    {
        return Collections.min(gridList, new Comparator<Grid>() {
            @Override
            public int compare(Grid o1, Grid o2) {
                return new Integer(o1.x).compareTo(o2.x);
            }
        });
    }

    public Grid getMaxXGrid(List<Grid> gridList)
    {
        return Collections.max(gridList, new Comparator<Grid>() {
            @Override
            public int compare(Grid o1, Grid o2) {
                return new Integer(o1.x).compareTo(o2.x);
            }
        });
    }

    public Grid getMinYGrid(List<Grid> gridList)
    {
        return Collections.min(gridList, new Comparator<Grid>() {
            @Override
            public int compare(Grid o1, Grid o2) {
                return new Integer(o1.y).compareTo(o2.y);
            }
        });
    }

    public Grid getMaxYGrid(List<Grid> gridList)
    {
        return Collections.max(gridList, new Comparator<Grid>() {
            @Override
            public int compare(Grid o1, Grid o2) {
                return new Integer(o1.y).compareTo(o2.y);
            }
        });
    }

}
