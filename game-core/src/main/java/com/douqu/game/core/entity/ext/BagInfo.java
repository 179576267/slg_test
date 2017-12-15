package com.douqu.game.core.entity.ext;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.config.AssetConfig;
import com.douqu.game.core.config.card.CardConfig;
import com.douqu.game.core.config.card.CardFateConfig;
import com.douqu.game.core.config.goods.PropConfig;
import com.douqu.game.core.entity.*;
import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.entity.db.CardDB;
import com.douqu.game.core.entity.db.PropDB;
import com.douqu.game.core.entity.ext.data.card.CardData;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.entity.common.TwoTuple;
import com.douqu.game.core.config.common.CommonData;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.factory.ConfigFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.protobuf.SGCommonProto;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 背包信息
 * Created by bean on 2017/7/17.
 */
public class BagInfo extends ExtInfo {

//    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * 资源数据
     * key:资源ID
     * value:资源数量
     */
    private Map<Integer,Integer> assetData;

    /**
     * 所有道具列表
     */
    private List<PropDB> propList;

    /**
     * 所有道具
     * key:道具ID
     * value:道具或装备
     */
    private Map<Integer, PropDB> propMap;
    /**
     * 根据物品分类存储一下，方便取分类数据
     */
    private Map<Integer, List<PropDB>> propTypeMap;

    /**卡牌*/
    private CardData cardData;


    public BagInfo(Player player, WorldInfo worldInfo)
    {
        super(player, worldInfo);

        assetData = new ConcurrentHashMap<>();
        propList = new ArrayList<>();
        propMap = new ConcurrentHashMap<>();
        propTypeMap = new ConcurrentHashMap<>();
        cardData = new CardData(player,this);
    }



    @Override
    public void writeTo(ByteBuffer buffer)
    {
        buffer.writeByte(assetData.size());
        for(Integer key : assetData.keySet())
        {
            buffer.writeInt(key);
            buffer.writeInt(assetData.get(key));
        }

        buffer.writeShort(propList.size());
        for(PropDB prop : propList)
        {
            prop.writeTo(buffer);
        }

        cardData.writeTo(buffer);
    }

    @Override
    public void loadFrom(ByteBuffer buffer)
    {
        //读取资源数据
        int size = buffer.readByte();
        int i = 0;
        for(i = 0; i < size; i++)
        {
            assetData.put(buffer.readInt(), buffer.readInt());
        }

        GameObject dbObj = null, dataObj = null;
        DataFactory dataFactory = DataFactory.getInstance();
        List<PropDB> list = null;
        int id = 0;
        //读取道具
        size = buffer.readShort();

        PropDB propDB = null;
        for(i = 0; i < size; i++)
        {
            propDB = new PropDB();
            propDB.loadFrom(buffer);
            if(propDB.getConfig() == null)
            {
                try {
                    throw new Exception("BagInfo load error -> Sleep Card is null:" + propDB.id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            {
                propList.add(propDB);
                propMap.put(propDB.id, propDB);
                list = propTypeMap.get(propDB.getConfig().type);
                if(list == null)
                {
                    list = new ArrayList<>();
                    propTypeMap.put(propDB.getConfig().type, list);
                }
                list.add(propDB);
            }
        }


        cardData.loadFrom(buffer);
    }

    /**
     * 创建角色的时候初始化
     */
    @Override
    public void init()
    {
        initAsset();
        initProp();
        cardData.init();
    }

    private void initAsset()
    {
        DataFactory dataFactory = DataFactory.getInstance();
        AssetConfig assetConfig = null;
        for(CommonData data : dataFactory.getInitDataConfig().initAssets)
        {
            assetData.put(data.id, data.value);
            assetConfig = dataFactory.getGameObject(DataFactory.ASSET_KEY, data.id);
            if(assetConfig.isMoney())
                player.money = data.value;
        }
    }

    private void initProp() {
        PropDB bagProp = null;
        List<PropDB> list = null;
        DataFactory dataFactory = DataFactory.getInstance();
        for (CommonData data : dataFactory.getInitDataConfig().initProps) {
            bagProp = new PropDB(data.id);
            bagProp.count = data.value;

            propList.add(bagProp);
            propMap.put(bagProp.id, bagProp);
            list = propTypeMap.get(bagProp.getConfig().type);
            if (list == null) {
                list = new ArrayList<>();
                propTypeMap.put(bagProp.getConfig().type, list);
            }
            list.add(bagProp);
        }
    }


    @Override
    public void checkInit()
    {
        if(assetData.size() == 0)
            initAsset();

        if(propList.size() == 0)
            initProp();

        cardData.checkInit();
    }

    @Override
    public void reset() {
        cardData.reset();
    }

    @Override
    public void checkReset() {
        cardData.checkReset();
    }

    @Override
    public void checkRedPointRemindAddGoods(SGCommonProto.E_GOODS_TYPE type, int id) {
        if(cardData.checkCorps()){
            player.cacheRedPoint(SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_LEGION);
        }
    }

    @Override
    public List<SGCommonProto.E_RED_POINT_TYPE> checkRedPointRemindAllCondition(long currentTime) {
        List<SGCommonProto.E_RED_POINT_TYPE> list = null;
        if(cardData.checkCorps()){
            list = new ArrayList<>();
//            player.cacheRedPoint(SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_LEGION);
            list.add(SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_LEGION);
        }
        return list;
    }


    /**
     * 添加资源
     * @param key
     * @param value
     * @Param obj 这个参数不为空则表示要展示
     */
    public boolean addAsset(int key, int value, Object... hide)
    {
        if(value == 0)
            return false;

        AssetConfig assetConfig = DataFactory.getInstance().getGameObject(DataFactory.ASSET_KEY, key);
        if(assetConfig == null)
            return false;

        if(key == ConfigFactory.ASSET_EXP_KEY && value > 0)
        {
            int beforeLv = player.getLv();

            player.addExp(value);

            player.addChangeGoods(SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_ASSETS, key, value, player.getExp(), hide);

            if(beforeLv != player.getLv())
            {
                player.addChangeGoods(SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_OTHER, SGCommonProto.E_GOODS_OTHER_ID.GOODS_OTHER_ID_LV_VALUE, player.getLv() - beforeLv, player.getLv(), true);
            }
        }
        else
        {
            int newValue = value;

            Integer resValue = assetData.get(key);
            if(resValue != null)
                newValue += resValue;

            newValue = newValue < 0 ? 0 : newValue;
            newValue = newValue > Integer.MAX_VALUE ? Integer.MAX_VALUE : newValue;
            assetData.put(key, newValue);

            if(assetConfig.isMoney())
                player.money = newValue;

            player.addChangeGoods(SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_ASSETS, key, resValue != null ? newValue - resValue : newValue, newValue, hide);

            checkRedPoint(SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_ASSETS, key);
        }

        return true;
    }

    private void checkRedPoint(SGCommonProto.E_GOODS_TYPE type, int id) {
        player.checkRedPointRemind(type, id);
    }


    /**
     * 获取资源数量
     * @param key
     * @return
     */
    public int getAsset(int key)
    {
        Integer integer = assetData.get(key);
        return integer == null ? 0 : integer.intValue();
    }

    public PropDB getPropById(int propId)
    {
        return propMap.get(propId);
    }

    /***
     * 根据id获取所拥有的数量
     * @return
     */
    public int getPropCount(int propId)
    {
        PropDB prop = getPropById(propId);

        return prop == null ? 0 : prop.count;
    }



    /**
     * 添加物品
     * @param goodsId
     * @param count 返回添加后的数量,如果为-1表示数据错误
     * @return
     */
    public int addProp(int goodsId, int count, Object... hide)
    {
//        System.out.println("addProp添加物品ID:" + goodsId + "  添加数量:" + count);
        if(count == 0)
        {
            System.out.println("添加物品数量错误");
            return -1;
        }

        count = count > Integer.MAX_VALUE ? Integer.MAX_VALUE : count;

        PropConfig goods = DataFactory.getInstance().getGameObject(DataFactory.PROP_KEY, goodsId);
        if(goods == null)
        {
            System.out.println("物品不存在");
            //物品不存在
            return -1;
        }

        List<PropDB> typePropList = propTypeMap.get(goods.type);
        if(count > 0)
        {
            if(typePropList == null)
            {
                typePropList = new CopyOnWriteArrayList<>();
                propTypeMap.put(goods.type, typePropList);
            }
        }

        int result = 0;

        PropDB bagProp = getPropById(goodsId);
        if(count > 0)
        {
            //添加物品
            if(bagProp == null)
            {
                bagProp = new PropDB(goodsId);
                bagProp.count = count;
                typePropList.add(bagProp);
                propList.add(bagProp);
                propMap.put(bagProp.id, bagProp);
            }
            else
            {
                bagProp.count += count;
            }

            checkRedPoint(SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_PROPS, goodsId);
            result = bagProp.count;
        }
        else
        {
            //删除物品
            if(bagProp == null || bagProp.count < Math.abs(count))
            {
                //物品数量错误
                System.out.println("删除物品数量错误");
                return -1;
            }

            bagProp.count += count;

            if(bagProp.count == 0)
            {
                typePropList.remove(bagProp);
                propList.remove(bagProp);
                propMap.remove(bagProp.id);
            }

            result = bagProp.count;
        }

        player.addChangeGoods(SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_PROPS, goodsId, count, bagProp.count, hide);
        return result;
    }

    /**
     * 删除卡,只有在分解的时候才会用到
     * @param id
     */
    public void removeSleepCard(int id)
    {
        cardData.removeSleepCard(id);
    }

    /***
     * 添加卡片
     * result : TwoTuple ,first表示是卡牌 second表示是道具
     */
    public TwoTuple<CommonData,CommonData> addCard(int id, int count, Object... hide)
    {
        TwoTuple<CommonData, CommonData> result = new TwoTuple<>();
        if(count <= 0)
            return result;

        //在配置文件中根据id 来获取卡牌对象
        CardConfig cardConfig =  DataFactory.getInstance().getGameObject(DataFactory.CARD_KEY, id);
        if(cardConfig == null)
            return result;

        if(cardData.getCard(id) != null)
        {
            //分解成碎片
            addProp(cardConfig.lvUpProp, count * cardConfig.transformation);

            result.setSecond(new CommonData(cardConfig.lvUpProp,count * cardConfig.transformation));
        }
        else
        {
            //卡片添加到list、map
            cardData.addSleepCard(new CardDB(cardConfig.id));

            //TODO 检测招募勇士的任务

            player.addChangeGoods(SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_CARDS, id, 1, 1, hide);

            result.setFirst(new CommonData(id,count));

            if(count > 1)
            {
                addProp(cardConfig.lvUpProp, (count-1) * cardConfig.transformation);

                result.setSecond(new CommonData(cardConfig.lvUpProp,(count-1) * cardConfig.transformation));
            }
        }

        return result;
    }


    /**
     * 获取物品数量
     * @return
     */
    public int getGoodsCount(int type, int id){

        if(type == SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_PROPS_VALUE){
           return getPropCount(id);
        }else if(type == SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_ASSETS_VALUE){
            return getAsset(id);
        }

        return 0;
    }


    /**
     * 添加物品
     * @param addData
     * @return
     */
    public void addGoods(GoodsData addData, Object... hide)
    {
//        System.out.println("addData: " + addData);
        if(addData == null || addData.id == 0)
            return;

        if(addData.type == SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_PROPS_VALUE){
            if(addData.value != 0){
                addProp(addData.id, addData.value, hide);
            }
        }else if(addData.type == SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_ASSETS_VALUE){
            addAsset(addData.id, addData.value, hide);
        }else if(addData.type == SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_CARDS_VALUE) {
            addCard(addData.id, addData.value, hide);
        }
    }
    /**
     * 添加物品
     * @param addData
     * @return
     */
    public void addGoods(GoodsData[] addData, GoodsData[] reduceData)
    {
        if(addData != null){
            for(GoodsData data : addData){
                addGoods(data);
            }
        }
        if(reduceData != null){
            for(GoodsData data : reduceData){
                if(data.type == SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_PROPS_VALUE){
                    if(data.value != 0){
                        addProp(data.id, -data.value);
                    }
                }else if(data.type == SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_ASSETS_VALUE){
                    addAsset(data.id, -data.value);
                }else if(data.type == SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_CARDS_VALUE){
                    addCard(data.id, -data.value);
                }
            }
        }
        return ;
    }

    /***
     *
     * 此卡牌是否在上阵卡组中
     * @param cardId
     * @return
     */
    public boolean isBattleCard(int cardId) {
        return cardData.isBattle(cardId);
    }



    //获取碎片或者卡片id在上阵卡组是否有宿命标签
    public boolean hasFateLabel(int id) {
        List<CardDB> cardBattleList = cardData.getBattleCardList();
        List<CardFateConfig> fateList = DataFactory.getInstance().getDataList(DataFactory.CARD_FATE_KEY);
         for(CardDB cardDB : cardBattleList){//id 上阵卡组的id
                for(CardFateConfig fate : fateList){
                    if(fate.isCardHasFate(id, cardDB.id)){
                        return true;
                    }
                }
        }
        return false;
    }

    //获取碎片或者卡片id在上阵卡组是否已经合成
    public boolean hasCompoundLabel(int id) {
        return  cardData.getCard(id) != null;
    }

    //-------------------------------Get Set---------------------------------------------------------------

    public List<PropDB> getTypePropList(int type)
    {
        return propTypeMap.get(type);
    }
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }



    public Map<Integer, Integer> getAssetData() {
        return assetData;
    }

    public void setAssetbattleCardIndexData(Map<Integer, Integer> assetData) {
        this.assetData = assetData;
    }

    public List<PropDB> getPropList() {
        return propList;
    }

    public void setPropList(List<PropDB> propList) {
        this.propList = propList;
    }

    public CardData getCardData() {
        return cardData;
    }

    public void setCardData(CardData cardData) {
        this.cardData = cardData;
    }

    public Map<Integer, List<PropDB>> getPropTypeMap() {
        return propTypeMap;
    }

    public void setPropTypeMap(Map<Integer, List<PropDB>> propTypeMap) {
        this.propTypeMap = propTypeMap;
    }
}
