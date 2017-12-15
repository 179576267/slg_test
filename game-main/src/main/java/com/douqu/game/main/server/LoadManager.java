package com.douqu.game.main.server;

import com.douqu.game.core.config.*;
import com.douqu.game.core.config.battle.BuffConfig;
import com.douqu.game.core.config.battle.MonsterConfig;
import com.douqu.game.core.config.battle.SkillConfig;
import com.douqu.game.core.config.battle.StarCheckConfig;
import com.douqu.game.core.config.card.CardConfig;
import com.douqu.game.core.config.card.CardFateConfig;
import com.douqu.game.core.config.card.CardLvConfig;
import com.douqu.game.core.config.card.CardStarConfig;
import com.douqu.game.core.config.challenge.*;
import com.douqu.game.core.config.drop.DropGroupConfig;
import com.douqu.game.core.config.goods.*;
import com.douqu.game.core.config.sprite.MasterConfig;
import com.douqu.game.core.config.sprite.SoldierConfig;
import com.douqu.game.core.config.common.CommonData;
import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.config.map.RouteConfig;
import com.douqu.game.core.config.map.TownConfig;
import com.douqu.game.core.config.store.GoblinStoreConfig;
import com.douqu.game.core.config.task.ActiveBoxConfig;
import com.douqu.game.core.config.task.GrowUpBoxConfig;
import com.douqu.game.core.config.task.TaskConfig;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.*;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.util.LoadUtils;
import com.douqu.game.main.util.PrintUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bean on 2017/7/27.
 */
public class LoadManager {

    static Logger logger = Logger.getLogger(LoadManager.class);

    public static void load()
    {
        loadData();

        LoadFactory.loadConfig();
    }

    public static void loadData()
    {
        DataFactory.getInstance().clearData();

        LoadManager.loadGameObject("data/Attribute",          AttributeConfig.class,          DataFactory.ATTRIBUTE_KEY);
        LoadManager.loadGameObject("data/Asset",              AssetConfig.class,              DataFactory.ASSET_KEY);
        LoadManager.loadGameObject("data/Profession",         ProfessionConfig.class,         DataFactory.PROFESSION_KEY);
        LoadManager.loadGameObject("data/Buff",               BuffConfig.class,               DataFactory.BUFF_KEY);
        LoadManager.loadGameObject("data/Skill",              SkillConfig.class,              DataFactory.SKILL_KEY);
        LoadManager.loadGameObject("data/Map",                MapConfig.class,              DataFactory.MAP_KEY);
        LoadManager.loadGameObject("data/Prop",               PropConfig.class,          DataFactory.PROP_KEY);
        LoadManager.loadGameObject("data/Equip",              EquipConfig.class,         DataFactory.EQUIP_KEY);
        LoadManager.loadGameObject("data/Accessory",          AccessoryConfig.class,     DataFactory.ACCESSORY_KEY);
        LoadManager.loadGameObject("data/Soldier",            SoldierConfig.class,            DataFactory.SOLDIER_KEY);
        LoadManager.loadGameObject("data/Master",             MasterConfig.class,             DataFactory.MASTER_KEY);
        LoadManager.loadGameObject("data/Card",               CardConfig.class,               DataFactory.CARD_KEY);
        LoadManager.loadGameObject("data/CardLv",             CardLvConfig.class,             DataFactory.CARD_LV_KEY);
        LoadManager.loadGameObject("data/CardStar",           CardStarConfig.class,           DataFactory.CARD_STAR_KEY);
        LoadManager.loadGameObject("data/CardFate",           CardFateConfig.class,           DataFactory.CARD_FATE_KEY);
        LoadManager.loadGameObject("data/AccessoryUp",        AccessoryUpConfig.class,        DataFactory.ACCESSORY_UP_KEY);
        LoadManager.loadGameObject("data/AccessoryIntensify", AccessoryIntensifyConfig.class, DataFactory.ACCESSORY_INTENSIFY_KEY);
        LoadManager.loadGameObject("data/Monster",            MonsterConfig.class,            DataFactory.MONSTER_KEY);
        LoadManager.loadGameObject("data/DropGroup",          DropGroupConfig.class,          DataFactory.DROP_GROUP_KEY);
        LoadManager.loadGameObject("data/PlayerLv",           PlayerLvConfig.class,           DataFactory.PLAYER_LV_KEY);
        LoadManager.loadGameObject("data/StarCheck",          StarCheckConfig.class,          DataFactory.STAR_CHECK_KEY);
        LoadManager.loadGameObject("data/Level",              LevelConfig.class,              DataFactory.LEVEL_KEY);
        LoadManager.loadGameObject("data/Instance",           InstanceConfig.class,           DataFactory.INSTANCE_KEY);
        LoadManager.loadGameObject("data/ArenaDaily",         ArenaDailyRewardConfig.class,   DataFactory.ARENA_DAILY_REWARD_KEY);
        LoadManager.loadGameObject("data/ExchangeReward",     ExchangeRewardConfig.class,     DataFactory.REWARD_KEY);
        LoadManager.loadGameObject("data/Lottery",            LotteryConfig.class,            DataFactory.LOTTERY_KEY);
        LoadManager.loadGameObject("data/OfficialRank",       OfficialRankConfig.class,       DataFactory.OFFICIAL_RANK_KEY);
        LoadManager.loadGameObject("data/OfficialIntegral",   OfficialIntegralConfig.class,   DataFactory.OFFICIAL_INTEGRAL_REWARD_KEY);
        LoadManager.loadGameObject("data/Altar",              AltarConfig.class,              DataFactory.ALTAR_KEY);
        LoadManager.loadGameObject("data/PlayerName",         PlayerNameConfig.class,         DataFactory.PLAYER_NAME_KEY);
        LoadManager.loadGameObject("data/GoblinStore",        GoblinStoreConfig.class,        DataFactory.GOBLIN_STORE_KEY);
        LoadManager.loadGameObject("data/Task",               TaskConfig.class,               DataFactory.TASK_KEY);
        LoadManager.loadGameObject("data/GrowUpBox",          GrowUpBoxConfig.class,          DataFactory.GROW_UP_BOX_KEY);
        LoadManager.loadGameObject("data/ActiveBox",          ActiveBoxConfig.class,          DataFactory.ACTIVE_BOX_KEY);
//        LoadManager.loadGameObject("data/CaptureCityBox",     CaptureCityBox.class,          DataFactory.CAPTURECITY_DATA_KEY);
        LoadManager.loadGameObject("data/EquipIntensify",     EquipIntensifyConfig.class,     DataFactory.EQUIP_INTENSIFY_KEY);
        LoadManager.loadGameObject("data/StableData",         StableData.class,         DataFactory.STABLE_DATA_KEY);
        LoadManager.loadGameObject("data/Resolve",            ResolveConfig.class,        DataFactory.RESOLVE_DATA_KEY);
        LoadManager.loadGameObject("data/Town",               TownConfig.class,               DataFactory.TOWN_KEY);
        LoadManager.loadGameObject("data/Route",              RouteConfig.class,              DataFactory.ROUTE_KEY);
        LoadManager.loadGameObject("data/HeroTemple",         HeroTempleConfig.class,              DataFactory.HERO_TEMPLE_KEY);
        LoadManager.loadGameObject("data/Function",           FunctionConfig.class,              DataFactory.FUNCTION_KEY);
        LoadManager.loadGameObject("data/Vip",                VipConfig.class,              DataFactory.VIP_KEY);
        LoadManager.loadGameObject("data/Recharge",           RechargeConfig.class,              DataFactory.RECHARGE_KEY);
        LoadManager.loadGameObject("data/PurchaseTimes",      PurchaseTimesConfig.class,              DataFactory.PURCHASE_KEY);
        LoadManager.loadGameObject("data/DailySignReward",    DailySignConfig.class,              DataFactory.DAILY_SIGN_KEY);
        LoadManager.loadGameObject("data/FundReward",    FundRewardConfig.class,              DataFactory.FUND_REWARD_KEY);
        LoadManager.loadGameObject("data/LoginTimesBonus",    LoginTimesBonusConfig.class,              DataFactory.LOGIN_TIMES_REWARD);


       DataFactory.getInstance().checkConfig();

        LoadManager.loadInitData();

        LoadManager.loadWord();

        StoreManager.getInstance().initStore();

        initTownRoute();

        initCardFate();

        RouteFactory.getInstance().init();
    }



    /**
     * 给卡初始化宿命列表
     */
    private static void initCardFate()
    {
        List<CardConfig> cardConfigList = DataFactory.getInstance().getDataList(DataFactory.CARD_KEY);
        for(CardConfig cardConfig : cardConfigList)
        {
            cardConfig.initFateList();
        }
    }
    /**
     * 给城镇设置路线
     */
    private static void initTownRoute()
    {
        List<TownConfig> townConfigList = DataFactory.getInstance().getDataList(DataFactory.TOWN_KEY);
        for(TownConfig townConfig : townConfigList)
        {
            townConfig.initRouteInfo();
        }
    }



    private final static String INIT_DATA = "data/InitData";
    /**
     * 加载初始数据配置
     */
    public static void loadInitData()
    {
        InputStream input = null;
        try {
            input = LoadFactory.getInputStream(INIT_DATA);
            List<InitDataConfig> datas = LoadUtils.loadFileVariables(input, InitDataConfig.class);
            DataFactory.getInstance().setInitDataConfig(datas.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(input != null){
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
    }


    public static void loadGameObject(String path, Class cls, int key)
    {
        if(DataFactory.getInstance().getDataList(key) != null)
        {
            PrintUtils.info(logger, "Load " + cls.getSimpleName() + " Key Repeated Error -> key:" + key);
        }

        InputStream input = null;
        try {
            input = LoadFactory.getInputStream(path);
            List<GameObject> datas = LoadUtils.loadFileVariables(input, cls);
//            List<GameObject> datas = LoadUtils.loadJsonVariables(input, cls);
            Map<Integer,GameObject> map = new HashMap<>();
            for(GameObject gameObject : datas)
            {
                if(map.get(gameObject.id) != null)
                {
                    PrintUtils.info(logger, "Load " + cls.getSimpleName() + " Repeated Error -> id:" + gameObject.id);
                }

//                gameObject.check();
                map.put(gameObject.id, gameObject);
            }

            DataFactory.getInstance().addData(key, datas, map);

            if(key == DataFactory.GRADE_KEY)
            {
                DataFactory.getInstance().MAX_GRADE = datas.get(datas.size()-1).getId();
            }

            if(key == DataFactory.LOTTERY_KEY)
            {
                for(GameObject gameObject : datas)
                {
                    LotteryConfig lotteryConfig = (LotteryConfig) gameObject;
                    if(DataFactory.getInstance().getGameObject(DataFactory.LOTTERY_KEY, lotteryConfig.nextLevelPond) == null)
                    {
                        PrintUtils.info(logger, "Load LotteryObject Error -> id:" + lotteryConfig.id + " nextLevelPond:" + lotteryConfig.nextLevelPond);
                    }
                }
            }

            if(key == DataFactory.PROFESSION_KEY)
            {
                List<ProfessionConfig> list = DataFactory.getInstance().getDataList(DataFactory.PROFESSION_KEY);
                for(ProfessionConfig professionConfig : list)
                {
                    if(DataFactory.getInstance().getGameObject(DataFactory.PROFESSION_KEY, professionConfig.restrictType) == null)
                    {
                        PrintUtils.info(logger, "LoadFactory Load Profession Error -> restrictType Error,profession.restrictType = " + professionConfig.restrictType + " id = " + professionConfig.id);
                    }

                    for(CommonData commonData : professionConfig.damagePlus)
                    {
                        if(DataFactory.getInstance().getGameObject(DataFactory.PROFESSION_KEY, commonData.id) == null)
                        {
                            PrintUtils.info(logger, "LoadFactory Load Profession Error -> damagePlus is null,commonData.id = " + commonData.id + " id = " + professionConfig.id);
                        }
                    }

                    for(CommonData commonData : professionConfig.exdamagePlus)
                    {
                        if(DataFactory.getInstance().getGameObject(DataFactory.PROFESSION_KEY, commonData.id) == null)
                        {
                            PrintUtils.info(logger, "LoadFactory Load Profession Error -> exdamagePlus is null,commonData.id = " + commonData.id + " id = " + professionConfig.id);
                        }
                    }
                }
            }

            if(key == DataFactory.EQUIP_KEY)
            {
                List<EquipConfig> list = DataFactory.getInstance().getDataList(DataFactory.EQUIP_KEY);
                for(EquipConfig equip : list)
                {
                    for(GoodsData goodsData : equip.synNeed)
                    {
                        if(goodsData.type == SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_EQUIP_VALUE){
                            if(DataFactory.getInstance().getGameObject(DataFactory.EQUIP_KEY, goodsData.id) == null)
                            {
                                PrintUtils.info(logger, "Load GoodsEquip synNeed Equip Error ->  id:" + goodsData.id + " equipId:" + equip.id);
                            }
                        }
                    }
                }
            }

            PrintUtils.info(logger, "Load " + cls.getSimpleName() + " Success!");

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(input != null){
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }

    }

    private final static String WORD_CONFIG = "data/Localization";

    public static void loadWord()
    {
        InputStream input = null;
        try {
            logger.info("【Load Word Start FileName=" + WORD_CONFIG + "】");

//            input = LoadFactory.class.getClassLoader().getResourceAsStream(WORD_CONFIG);
            input = LoadFactory.getInputStream(WORD_CONFIG);
            List<LocalizationConfig> datas = LoadUtils.loadFileVariables(input, LocalizationConfig.class);
            Map<Integer,LocalizationConfig> map = new HashMap<>();
            for(LocalizationConfig gameObject : datas)
            {
                if(ConfigFactory.LANGUAGE.equals(ConstantFactory.LANGUAGE_CHINESE))
                {
                    DataFactory.getInstance().addWord(gameObject.key, gameObject.Chinese);
                }
                else if(ConfigFactory.LANGUAGE.equals(ConstantFactory.LANGUAGE_CHINESETRADITIONAL))
                {
                    DataFactory.getInstance().addWord(gameObject.key, gameObject.ChineseTraditional);
                }
                else if(ConfigFactory.LANGUAGE.equals(ConstantFactory.LANGUAGE_ENGLISH))
                {
                    DataFactory.getInstance().addWord(gameObject.key, gameObject.English);
                }
            }
            logger.info("Load Word Success=================================");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(input != null){
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
    }
}
