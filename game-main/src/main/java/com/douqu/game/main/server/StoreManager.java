package com.douqu.game.main.server;

import com.douqu.game.core.config.store.GoblinStoreConfig;
import com.douqu.game.core.factory.DataFactory;

import java.util.*;

/**
 * @author wangzhenfei
 *         2017-10-28 17:46
 */
public class StoreManager {

    private static StoreManager instance = null;
    /**<商店类型， <商品分类，商品列表数据>**/
    private Map<Integer, Map<Integer, StoreData>> storeDataMap;

    private StoreManager()
    {
        storeDataMap = new HashMap<>();
    }


    /**
     * 获取商品
     * @param storeType 商店类型
     * @param goodsType 物品类型
     * @param count 数量
     */
    public List<GoblinStoreConfig> getStoreGoods(int storeType, int goodsType, int count){
        List<GoblinStoreConfig> goods = null;
        Map<Integer, StoreData> goodsMap = storeDataMap.get(storeType);
        if(goodsMap != null){
            StoreData storeData = goodsMap.get(goodsType);
            if(storeData != null){
                goods = getGoods(storeData.goods, storeData.sumGroupRate, count);
            }
        }
        return goods;
    }


    private List<GoblinStoreConfig> getGoods(List<GoblinStoreConfig> allGoods, int sumGroupCardRate, int num) {
        List<GoblinStoreConfig> storeGoods = new ArrayList<>();
        Random random = new Random();
        while(true){
            int next = random.nextInt(sumGroupCardRate);
            int sumRate = 0;
            for(GoblinStoreConfig goods : allGoods){
                sumRate+=goods.rate;
                if(next < sumRate){
                    if(!storeGoods.contains(goods)){
                        storeGoods.add(goods);
                    }
                    break;
                }

            }
            if(storeGoods.size() == num){
                break;
            }
        }
        return storeGoods;
    }


    public static StoreManager getInstance()
    {
        if(instance == null)
            instance = new StoreManager();

        return instance;
    }



    public void initStore(){
        storeDataMap.clear();
        List<GoblinStoreConfig> allGoods = DataFactory.getInstance().getDataList(DataFactory.GOBLIN_STORE_KEY);
        for(GoblinStoreConfig store : allGoods){
            Map<Integer, StoreData> goodsMap = storeDataMap.get(store.storeType);
            if(goodsMap == null){
                goodsMap = new HashMap<>();
                storeDataMap.put(store.storeType, goodsMap);
            }
            StoreData storeData =  goodsMap.get(store.group);
            if(storeData == null){
                storeData = new StoreData();
                goodsMap.put(store.group, storeData);
            }
            storeData.addGoods(store);

        }
    }

    public static class StoreData{
        private List<GoblinStoreConfig> goods;
        private int sumGroupRate;

        public StoreData() {
            goods = new ArrayList<>();
        }

        public void addGoods(GoblinStoreConfig storeGoods){
            if(storeGoods != null){
                goods.add(storeGoods);
                sumGroupRate+=storeGoods.rate;
            }
        }
    }
}
