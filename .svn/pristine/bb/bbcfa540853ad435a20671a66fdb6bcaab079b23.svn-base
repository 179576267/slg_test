package com.douqu.game.core.factory;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.config.*;
import com.douqu.game.core.config.goods.PropConfig;
import com.douqu.game.core.config.task.TaskConfig;
import com.douqu.game.core.entity.GameObject;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Author : Bean
 * 2017-07-06 16:13
 */
public class DataFactory implements Serializable
{
    private static final long serialVersionUID = 1L;

    private static DataFactory instance = null;

    private DataFactory(){}

    public static DataFactory getInstance()
    {
        if (instance == null) {
            synchronized (DataFactory.class) {
                if (instance == null) {
                    instance = new DataFactory();
                }
            }
        }
        return instance;
    }

    /**
     * 分支服务器的时候需要用
     * @param dataFactory
     */
    public void set(DataFactory dataFactory)
    {
        instance = dataFactory;

        List<AttributeConfig> list = instance.getDataList(DataFactory.ATTRIBUTE_KEY);
        for(AttributeConfig attributeConfig : list)
        {
            attributeConfig.check();
        }
    }


    /**
     * 初始化数据
     */
    private InitDataConfig initDataConfig;

    public void setInitDataConfig(InitDataConfig initDataConfig){
        this.initDataConfig = initDataConfig;
    }

    public InitDataConfig getInitDataConfig(){
        return this.initDataConfig;
    }

    /**
     * 最高段位
     */
    public int MAX_GRADE;

    /**
     * 配置文件加载日志
     */
    public static int DATA_VERSION = 1;


    /** 兵种配置 */
    public final static int SOLDIER_KEY = 1;
    /** 兵种动作配置 */
    public final static int SOLDIER_ANIM_KEY = 2;
    /** 等级经验配置 */
    public final static int PLAYER_LV_KEY = 3;
    /** 资源 */
    public final static int ASSET_KEY = 4;
    /** 卡牌配置 */
    public final static int CARD_KEY = 5;
    /** 卡牌升级配置 */
    public final static int CARD_LV_KEY = 6;
    /** 关卡配置 */
    public final static int LEVEL_KEY = 7;
    /** 副本配置 */
    public final static int INSTANCE_KEY = 8;
    /** 道具配置 */
    public final static int PROP_KEY = 9;
    /** 怪物配置 */
    public final static int MONSTER_KEY = 10;
    /** 段位配置 */
    public final static int GRADE_KEY = 11;
    /** 掉落配置 */
    public final static int DROP_GROUP_KEY = 12;
    /** 技能配置 */
    public final static int SKILL_KEY = 13;
    /** 属性配置 */
    public final static int ATTRIBUTE_KEY = 14;
    /** 职业配置 */
    public final static int PROFESSION_KEY = 15;
    /** 十连抽配置 */
    public final static int LOTTERY_KEY = 16;
    /** 评星配置 */
    public final static int STAR_CHECK_KEY = 17;
    /** 竞技场每日奖励配置 */
    public final static int ARENA_DAILY_REWARD_KEY = 18;
    /** 兑换奖励配置 */
    public final static int REWARD_KEY = 19;
    /** BUFF配置 */
    public final static int BUFF_KEY = 20;
    /** 主将配置 */
    public final static int MASTER_KEY = 21;
    /** 祭坛配置*/
    public final static int ALTAR_KEY = 22;
    /** 官阶战配置*/
    public final static int OFFICIAL_RANK_KEY = 23;
    /** 官阶战积分兑换奖励配置*/
    public final static int OFFICIAL_INTEGRAL_REWARD_KEY = 24;
    /** 任务配置*/
    public final static int TASK_KEY = 25;
    /** 活跃宝箱配置*/
    public final static int ACTIVE_BOX_KEY = 26;
    /** 成长宝箱配置*/
    public final static int GROW_UP_BOX_KEY = 27;
    /** 随机名字库*/
    public final static int PLAYER_NAME_KEY = 28;
    /** 地精商店配置*/
    public final static int GOBLIN_STORE_KEY = 29;
    /** 饰品强化配置*/
    public final static int ACCESSORY_INTENSIFY_KEY = 30;
    /** 装备配置*/
    public final static int EQUIP_KEY = 31;
    /** 装备强化配置*/
    public final static int EQUIP_INTENSIFY_KEY = 32;
    /**饰品进阶*/
    public final static int ACCESSORY_UP_KEY = 33;
    /**卡片升星*/
    public final static int CARD_STAR_KEY = 34;
    /**卡片宿命*/
    public final static int CARD_FATE_KEY = 35;
    /**饰品表*/
    public final static int ACCESSORY_KEY = 36;
    /** 固定的配置数据 */
    public final static int STABLE_DATA_KEY = 37;
    /** 分解的配置数据 */
    public final static int RESOLVE_DATA_KEY = 38;
    /** 大地图的路线 */
    public final static int ROUTE_KEY = 39;
    /** 大地图的城 */
    public final static int TOWN_KEY = 40;
    /** 攻城任务 */
    public final static int CAPTURECITY_DATA_KEY = 40;
    /** 英雄圣殿 */
    public final static int HERO_TEMPLE_KEY = 41;

    /** 解锁配置 */
    public final static int FUNCTION_KEY = 42;

    /**VIP配置**/
    public final static int VIP_KEY = 43;

    /**充值配置**/
    public final static int RECHARGE_KEY = 44;

    /**购买次数对应价格配置**/
    public final static int PURCHASE_KEY = 45;

    /**每日签到配置**/
    public final static int DAILY_SIGN_KEY = 46;

    /**地图**/
    public final static int MAP_KEY = 47;

    /**基金返回**/
    public final static int FUND_REWARD_KEY = 48;

    /**累计登陆奖励**/
    public final static int LOGIN_TIMES_REWARD = 49;

    private Map<Integer,List> dataList = new HashMap<Integer,List>();

    private Map<Integer,Map> dataMap = new HashMap<Integer,Map>();

    public synchronized void clearData()
    {
        dataList.clear();
        dataMap.clear();
    }


    public <T> void addData(int key, List<T> list, Map<Integer,T> map)
    {
        dataList.put(key, list);
        dataMap.put(key, map);
    }

    public <T> List<T> getDataList(int key)
    {
        return dataList.get(key);
    }

    public <T> Map<Integer, T> getDataMap(int key)
    {
        return dataMap.get(key);
    }

    public <T> T getGameObject(int key, int id)
    {
        Map map = getDataMap(key);
        if(map != null)
            return (T) map.get(id);

        return null;
    }



    public TaskConfig getTaskByBefoTask(int beforeTask){
        List<TaskConfig> list =  getDataList(TASK_KEY);
        for(TaskConfig taskConfig : list ){
            if(taskConfig.beforeTask == beforeTask ){
                return taskConfig;
            }
        }
        return null;
    }

    /**
     *
     * @param lotteryConfig
     * @return
     */
    public boolean isMaxLevelLottery(LotteryConfig lotteryConfig)
    {
        List<LotteryConfig> list = getDataList(LOTTERY_KEY);
        int max = 0;
        for(LotteryConfig data : list)
        {
            if(data.type == lotteryConfig.type && data.level > max)
            {
                max = data.level;
            }
        }
        return lotteryConfig.level == max;
    }
    /**
     * 获取一级的奖池
     * @param type
     * @return
     */
    public LotteryConfig getInitLevelLottery(int type)
    {
        List<LotteryConfig> list = getDataList(LOTTERY_KEY);
        for(LotteryConfig data : list)
        {
            if(data.type == type && data.level == 1)
                return data;
        }
        return null;
    }

    /**
     * 获取下一级的奖池
     * @param type
     * @return
     */
    public LotteryConfig getNextLevelLottery(int type, int level)
    {
        List<LotteryConfig> list = getDataList(LOTTERY_KEY);
        for(LotteryConfig data : list)
        {
            if(data.type == type && data.level == level + 1)
                return data;
        }
        return null;
    }



    private Map<String, String> wordMap = new ConcurrentHashMap<>();

    public void addWord(String key, String value)
    {
        wordMap.put(key, value);
    }

    public String getWord(String key, Object... params)
    {
        String result = wordMap.get(key);
        if(result == null)
            return key;

        if(key.equals(WordFactory.ASSET_NOT_ENOUGH))
        {
            if(params != null && params.length > 0)
            {
                AssetConfig assetConfig = getGameObject(ASSET_KEY, Integer.parseInt(params[0].toString()));
                if(assetConfig != null)
                {
                    result = MessageFormat.format(result, assetConfig.name);
                }
            }
        }
        else if(key.equals(WordFactory.GOODS_NOT_ENOUGH))
        {
            if(params != null && params.length > 0)
            {
                PropConfig goods = getGameObject(PROP_KEY, Integer.parseInt(params[0].toString()));
                if(goods != null)
                {
                    result = MessageFormat.format(result, goods.name);
                }
            }
        }

        return result == null || result.length() == 0 ? key : result;
    }



    public JSONArray getDataToJson(int key)
    {
        List<GameObject> list = DataFactory.getInstance().getDataList(key);
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = null;
        String value = null;
        for(GameObject gameObject : list)
        {
            jsonObject = new JSONObject();
            Field[] fields = gameObject.getClass().getFields();
            for(Field f : fields)
            {
                value = gameObject.getVariable(f.getName());
                if(StringUtils.isEmpty(value))
                    continue;

                jsonObject.put(f.getName(), value);
            }
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }


    public  void  checkConfig(){
        Iterator<Integer> iterator = dataList.keySet().iterator();
        int  key;
        List<GameObject> list;
        while(iterator.hasNext()){
            key = iterator.next();
            list = dataList.get(key);
            for(GameObject gameObject : list){
                gameObject.check();
            }
        }
    }

}
