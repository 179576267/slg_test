package com.douqu.game.core.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.douqu.game.core.config.PlayerNameConfig;
import com.douqu.game.core.config.common.CommonData;
import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import org.yaml.snakeyaml.reader.UnicodeReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Author : Bean
 * 2017-05-03 19:08
 */
public class LoadUtils {

    /**
     * 循环向上转型, 获取对象的 DeclaredField
     * @param object : 子类对象
     * @param fieldName : 父类中的属性名
     * @return 父类中的属性对象
     */

    public static Field getDeclaredField(Object object, String fieldName){
        Field field = null ;

        Class<?> clazz = object.getClass() ;

        for(; clazz != Object.class ; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName) ;
                return field ;
            } catch (Exception e) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了

            }
        }

        return null;
    }

    public static <T> List<T> loadJsonVariables(InputStream inputStream, Class<T> cls)
    {
        List<T> list = new CopyOnWriteArrayList<T>();
        BufferedReader br = null;
        try{
            br = new BufferedReader(new UnicodeReader(inputStream));
            String line = null;
            StringBuilder data = new StringBuilder();
            while((line = br.readLine()) != null) {
                data.append(line);
            }

            JSONObject jsonObject = JSONObject.parseObject(data.toString());
            JSONObject infoObj = null;
            for(String key : jsonObject.keySet())
            {
                infoObj = jsonObject.getJSONObject(key);

                T info = cls.newInstance();

                for(String fieldKey : infoObj.keySet())
                {
                    Field field = getDeclaredField(info, fieldKey);
                    if(field == null)
                    {
                        System.out.println("Field is null:" + fieldKey + " cls:" + cls);
                        continue;
                    }

                    ((GameObject)info).setVariable(fieldKey, infoObj.get(fieldKey).toString());
                }

                list.add(info);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(br != null){
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    public static <T> List<T> loadFileVariables(InputStream inputStream, Class<T> cls)
    {
        List<T> list = new CopyOnWriteArrayList<T>();
        BufferedReader br = null;
        try{
            br = new BufferedReader(new UnicodeReader(inputStream));
            String line = null;
            String[] fieldStr = br.readLine().split(ConstantFactory.TABLE);
//            String[] typeStr = br.readLine().split(ConstantFactory.TABLE);
            br.readLine();//属性类型，忽略
            br.readLine();//中文注释行，忽略
            while((line = br.readLine()) != null) {
                if(!line.equals(""))
                {
                    String[] values = line.split("\t");
                    T info = cls.newInstance();

                    for(int i = 0; i < fieldStr.length; i++)
                    {
                        Field field = getDeclaredField(info, fieldStr[i]);
                        if(field == null)
                        {
                            System.out.println("Field is null:" + fieldStr[i] + " cls:" + cls);
                            continue;
                        }

                        if(i >= values.length)
                            ((GameObject)info).setVariable(fieldStr[i].trim(), "");
                        else
                            ((GameObject)info).setVariable(fieldStr[i].trim(), values[i].trim());
                    }
                    list.add(info);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(br != null){
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    public static int[] loadIntArray(String key, String value)
    {
        int[] result = null;
        try{
            String[] strs = value.split(ConstantFactory.SEMICOLON);
            result = new int[strs.length];
            int i = 0;
            for(String v : strs)
            {
                result[i++] = Integer.parseInt(v);
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Load Config Error key：" + key + " value：" + value);
        }

        return result;
    }



    public static CommonData[] loadDataToArray(String key, String value)
    {
        CommonData[] result = null;
        try{
            String[] strs = value.split(ConstantFactory.DIVISION);
            result = new CommonData[strs.length];
            int i = 0;
            for(String v : strs)
            {
                result[i++] = new CommonData(v);
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Load Config Error key：" + key + " value：" + value);
        }

        return result;
    }


    public static GoodsData[] loadGoodDataToArray(String key, String value)
    {
        if(Utils.isErrorValue(value))
            return new GoodsData[0];

        GoodsData[] result = null;
        try{
            String[] strs = value.split(ConstantFactory.DIVISION);
            result = new GoodsData[strs.length];
            int i = 0;
            for(String v : strs)
            {
                result[i++] = new GoodsData(v);
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Load Config Error key：" + key + " value：" + value);
        }

        return result;
    }

    public static List<CommonData> loadResourceToList(String value)
    {
        List<CommonData> result = null;
        try{
            String[] strs = value.split(ConstantFactory.DIVISION);
            result = new CopyOnWriteArrayList<>();
            for(String v : strs)
            {
                result.add(new CommonData(v));
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Load Config Error:" + value);
        }

        return result;
    }


    public static int[] loadIntArray(String value)
    {
        int[] result = null;
        try{
            String[] strs = value.split(ConstantFactory.SEMICOLON);
            result = new int[strs.length];
            int i = 0;
            for(String v : strs)
            {
                result[i++] = Integer.parseInt(v);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 加载一个指定大小的随机名字表
     * @param num
     * @return
     */

    public static List<String> getRandomPlayerNameList(int num){
        List<String> list = new ArrayList<>();
        if(DataFactory.getInstance().getDataList(DataFactory.PLAYER_NAME_KEY).size() < num){
            return list;
        }

        while (true){
            String name = getRandomPlayerName();
            if(list.contains(name)){
                continue;
            }
            list.add(name);
            if(list.size() == num){
                break;
            }
        }
        return list;
    }

    /**
     * 加载一个随机名字
     * @return
     */
    public static String getRandomPlayerName(){
        List<PlayerNameConfig> dataList = DataFactory.getInstance().getDataList(DataFactory.PLAYER_NAME_KEY);
        Random random = new Random();
        int first = random.nextInt(dataList.size());
        int second = random.nextInt(dataList.size());
        int third = random.nextInt(dataList.size());
        return (dataList.get(first).name1 == null ? "" :  dataList.get(first).name1) +
                (dataList.get(second).name2 == null ? "" : dataList.get(second).name2 ) +
                (dataList.get(third).name3 == null ? "" : dataList.get(third).name3);
    }


}
