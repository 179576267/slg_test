package com.douqu.game.core.entity.ext.data.boon;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.entity.ext.data.BaseData;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangzhenfei
 *         2017-10-28 16:06
 *         商品的缓存
 *
 */
public class StoreData extends BaseData {


    /**<商店id，商店缓存数据>**/
    private Map<Integer, StoreCacheData> storeCacheDataMap;

    public StoreData() {
        storeCacheDataMap = new ConcurrentHashMap<>();
    }

    @Override
    public void init() {
        Iterator<Integer> iterator = storeCacheDataMap.keySet().iterator();
        while(iterator.hasNext()){
            storeCacheDataMap.get(iterator.next()).init();
        }
    }

    @Override
    public void checkInit() {
        Iterator<Integer> iterator = storeCacheDataMap.keySet().iterator();
        while(iterator.hasNext()){
            storeCacheDataMap.get(iterator.next()).checkInit();
       }
    }

    public boolean hasRedPointRemind(){
        Iterator<Integer> iterator = storeCacheDataMap.keySet().iterator();
        while(iterator.hasNext()){
            if(storeCacheDataMap.get(iterator.next()).hasRedPointRemind()){
                return true;
            }
        }
        return false;
    }

    @Override
    public void reset()
    {
        Iterator<Integer> iterator = storeCacheDataMap.keySet().iterator();
        while(iterator.hasNext()){
            storeCacheDataMap.get(iterator.next()).reset();
        }
    }

    @Override
    public void checkReset() {
        Iterator<Integer> iterator = storeCacheDataMap.keySet().iterator();
        while(iterator.hasNext()){
            storeCacheDataMap.get(iterator.next()).checkReset();
        }
    }

    @Override
    public void writeTo(ByteBuffer buffer) {
        Iterator<Integer> iterator = storeCacheDataMap.keySet().iterator();
        Integer key;
        StoreCacheData cacheData;
        buffer.writeShort(storeCacheDataMap.size());
        while(iterator.hasNext()){
            key = iterator.next();
            cacheData = storeCacheDataMap.get(key);
            buffer.writeInt(key);
            cacheData.writeTo(buffer);
        }
        buffer.writeInt(0);
    }

    @Override
    public void loadFrom(ByteBuffer buffer) {
        int size = buffer.readShort();
        Integer key;
        StoreCacheData cacheData;
        for(int i = 0; i < size; i++){
            key = buffer.readInt();
            cacheData = new StoreCacheData(key);
            cacheData.loadFrom(buffer);
            storeCacheDataMap.put(key, cacheData);
        }
        buffer.readInt();
    }


    public StoreCacheData getStoreCache(int storeType){
        StoreCacheData cacheData = storeCacheDataMap.get(storeType);
        if(cacheData == null){
            cacheData = new StoreCacheData(storeType);
            storeCacheDataMap.put(storeType, cacheData);
        }
        return cacheData;
    }


    @Override
    public String toString() {
        return "StoreData{" +
                "storeCacheDataMap=" + storeCacheDataMap +
                "} " + super.toString();
    }
}
