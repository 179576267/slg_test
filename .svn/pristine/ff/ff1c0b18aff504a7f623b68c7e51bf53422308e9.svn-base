package com.douqu.game.main.util;


//import com.douqu.game.core.entity.ext.TestInfo;

import com.alibaba.druid.support.json.JSONParser;
import com.alibaba.fastjson.JSONArray;
import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.astar.Grid;
import com.douqu.game.core.config.AssetConfig;
import com.douqu.game.core.config.map.RouteConfig;
import com.douqu.game.core.config.map.TownConfig;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.entity.db.CardDB;
import com.douqu.game.core.entity.ext.data.card.CardData;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.factory.RouteFactory;
import com.douqu.game.core.util.Utils;
import com.douqu.game.main.server.LoadManager;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

/**
 * Author : Bean
 * 2017-07-06 15:29
 */

public class Test {

    static Logger logger = Logger.getLogger(Test.class);

    public static void main(String[] args) {

List<Grid> list = new ArrayList<>();
        list.add(new Grid(1,2));
        list.add(new Grid(1,3));
        list.add(new Grid(1,4));
        list.add(new Grid(1,5));
        list.add(new Grid(1,6));

        System.out.println(list);

        Collections.max(list, new Comparator<Grid>() {
            @Override
            public int compare(Grid o1, Grid o2) {
                return new Integer(o1.y).compareTo(o2.y);
            }
        });

        System.out.println(list);
    }


    public static void testRoute()
    {
        LoadManager.loadData();

        for(int i = 0; i < 20; i++)
        {
            int start = (int) (Math.random() * 39 + 1);
            int end = (int) (Math.random() * 39 + 1);
            System.out.println("开始寻路 -> start:" + start + "  end:" + end);
            long t = System.currentTimeMillis();
            List<Integer> result = RouteFactory.getInstance().getRouteList(start, end);
            System.out.println("花费时间:" + (System.currentTimeMillis() - t));
            System.out.println("寻路结果:" + result);
        }
    }



    public static void testLoad() {
        InputStream input = null;
        try {
            String filePath = "data/InitData";
            File file = new File(Utils.getBasePath() + filePath);
            if (!file.exists())
                file = new File(Utils.getRootPath() + filePath);

            if (file.exists()) {
                logger.info("用文件目录的方式加载");
                input = new BufferedInputStream(new FileInputStream(file));
                logger.info("Utils.getBasePath():" + Utils.getBasePath());
            } else {
                logger.info("用文件流的方式加载");
                input = Test.class.getClassLoader().getResourceAsStream(filePath);
                logger.info("Utils.getRootPath():" + Utils.getRootPath());
            }

//            String result = FileUtils.readFile(input);

//            logger.info("result:" + result);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage());
                }
            }
        }
    }


}
