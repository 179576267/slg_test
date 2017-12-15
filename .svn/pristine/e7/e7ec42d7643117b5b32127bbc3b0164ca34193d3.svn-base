package com.douqu.game.core.entity.ext.data.card;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.config.*;
import com.douqu.game.core.config.card.CardConfig;
import com.douqu.game.core.config.card.CardFateConfig;
import com.douqu.game.core.config.card.CardLvConfig;
import com.douqu.game.core.config.card.CardStarConfig;
import com.douqu.game.core.config.goods.*;
import com.douqu.game.core.config.task.TaskConfig;
import com.douqu.game.core.controller.PlayerController;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.entity.db.*;
import com.douqu.game.core.config.common.CommonData;
import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.entity.common.TwoTuple;
import com.douqu.game.core.entity.ext.BagInfo;
import com.douqu.game.core.entity.ext.TaskInfo;
import com.douqu.game.core.entity.ext.data.BaseData;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.factory.WordFactory;
import com.douqu.game.core.protobuf.SGBagProto;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.protobuf.SGMainProto;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author: Gavin.
 * Description:
 * Date: 2017/11/22 0022 上午 10:48
 * Huan Yu Copyright (c) 2017 All Rights Reserved.
 */
public class CardData extends BaseData {

    /**玩家所有的卡片map*/
    private Map<Integer,CardDB> cardMap;

    /**玩家未上阵的卡片list */
    private List<CardDB> sleepCardList;

    /**玩家上阵卡组*/
    private List<CardDB> battleCardList;

    protected Player player;

    protected BagInfo bagInfo;


    public CardData(Player player,BagInfo bagInfo){

        this.player = player;
        this.bagInfo = bagInfo;

        sleepCardList = new CopyOnWriteArrayList<>();
        battleCardList = new CopyOnWriteArrayList<>();
        cardMap = new ConcurrentHashMap<>();
    }



    @Override
    public void init()
    {
        CardDB bagCard = null;

        DataFactory dataFactory = DataFactory.getInstance();

        InitDataConfig initDataConfig = dataFactory.getInitDataConfig();
        for(Integer id :  initDataConfig.initCards)
        {
            bagCard = new CardDB(id);
            bagCard.battle = true;

            addBattleCard(bagCard);
        }

        PlayerLvConfig playerLvConfig = dataFactory.getGameObject(DataFactory.PLAYER_LV_KEY, player.getLv());
        if(playerLvConfig != null)
        {
            int size = Math.min(playerLvConfig.battleSoldierCount, battleCardList.size());
            if(battleCardList.size() > size)
                System.out.println("PlayerLv  battleSoldierCount  配置出错!!!");
        }
    }


    @Override
    public void checkInit()
    {
        if(cardMap.size() == 0)
            init();
    }

    @Override
    public void reset() {

    }

    @Override
    public void checkReset() {

    }

    @Override
    public void writeTo(ByteBuffer buffer)
    {
        buffer.writeShort(sleepCardList.size());
        for(CardDB cardDB : sleepCardList)
        {
            cardDB.writeTo(buffer);
        }

        buffer.writeShort(battleCardList.size());
        for(CardDB cardDB : battleCardList)
        {
            cardDB.writeTo(buffer);
        }
    }

    @Override
    public void loadFrom(ByteBuffer buffer)
    {
        //读取卡牌数据

        int size = buffer.readShort();
        int i = 0;
        CardDB cardDB = null;
        for(i = 0; i < size; i++)
        {
            cardDB = new CardDB();
            cardDB.loadFrom(buffer);
            if(cardDB.getConfig() == null)
            {
                try {
                    throw new Exception("BagInfo load error -> Sleep Card is null:" + cardDB.id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            {
                sleepCardList.add(cardDB);
                cardMap.put(cardDB.id, cardDB);
            }
        }
        size = buffer.readShort();
        for(i = 0; i < size; i++)
        {
            cardDB = new CardDB();
            cardDB.loadFrom(buffer);
            if(cardDB.getConfig() == null)
            {
                try {
                    throw new Exception("BagInfo load error -> Battle Card is null:" + cardDB.id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            {
                cardDB.battle = true;
                battleCardList.add(cardDB);
                cardMap.put(cardDB.id, cardDB);
            }
        }
    }


    /***
     * 上阵卡组列表
     */
    public void myCardTeam(PlayerController playerController){

        SGBagProto.S2C_MyCardTeam.Builder b = SGBagProto.S2C_MyCardTeam.newBuilder();
        //取出上证卡组id
        SGCommonProto.CardGroup.Builder c = SGCommonProto.CardGroup.newBuilder();
        c.setGroupId(SGCommonProto.E_CARDGROUP_TYPE.CARDGROUP_TYPE_BATTLE_VALUE);
        for(CardDB cardDB : battleCardList){
            SGCommonProto.Card.Builder v = SGCommonProto.Card.newBuilder();
            v.setLv(cardDB.getLv());
            //已拥有的碎片v
            v.setStar(cardDB.star);
            v.setId(cardDB.id);
            v.setCurExp(cardDB.getExp());
            v.setFc(cardDB.fc);
            c.addCards(v);
        }
        b.addCardGroups(c);
        //返回给客户端
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Bag_MyCardTeam_VALUE, b.build().toByteArray());

    }


    /***
     * 设置上阵卡组
     * @param playerController
     * @param cardTeamId
     */
/*    public void setBattleCardTeam(PlayerController playerController,int cardTeamId) {

        if(cardTeamMap.get(cardTeamId) == null) {
            //参数错误
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        battleCardIndex = cardTeamId;
        SGBagProto.S2S_SetBattleCardTeam.Builder b = SGBagProto.S2S_SetBattleCardTeam.newBuilder();
        b.setCardTeamId(cardTeamId);
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Bag_SetBattleCardTeam_VALUE, b.build().toByteArray());
    }*/

    /***
     * 卡片替换使用
     * @param playerController
     * @param activeCardId 要上阵的卡牌
     * @param passiveCardId 要替换下来的卡牌
     */
    public void cardReplace(PlayerController playerController,int cardTeamId,int activeCardId,int passiveCardId){

        CardDB toBattleCard = cardMap.get(activeCardId);
        if (toBattleCard == null) {
            //卡牌不存在
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_NOTHING_ERROR));
            return;
        }
        if (toBattleCard.battle) {
            //已经是上阵的了
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_IN_CARD_BATTLE_GROUP));
            return;
        }
        //新增一个卡牌到上阵列表中
        if (passiveCardId == 0) {
            PlayerLvConfig playerLvConfig = DataFactory.getInstance().getGameObject(DataFactory.PLAYER_LV_KEY, playerController.getPlayer().getLv());
            if (battleCardList.size() + 1 > playerLvConfig.battleSoldierCount) {
                //上阵卡组中的卡牌不能大于当前能携带进战斗的兵种数量
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CANNOT_BE_GREATER_THAN_THE_NUMBER_OF_CARRY));
                return;
            }
            toBattleCard.battle = true;
            battleCardList.add(toBattleCard);
            sleepCardList.remove(toBattleCard);
        } else {
            CardDB sleepCard = cardMap.get(passiveCardId);
            //此卡牌不在未上阵卡组
            if (sleepCard == null) {
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_NOTHING_ERROR,sleepCard.id));
                return;
            }
            if (!sleepCard.battle) {
                //卡牌本来就不再上阵中
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_NOT_IN_CARD_BATTLE_GROUP));
                return;
            }
            //设置上阵
            toBattleCard.battle = true;
            battleCardList.add(toBattleCard);


            sleepCardList.remove(toBattleCard);

            //设置待上阵
            sleepCard.battle = false;
            battleCardList.remove(sleepCard);
            sleepCardList.add(sleepCard);
        }
        playerController.updateFC();

        SGBagProto.S2C_CardReplace.Builder b = SGBagProto.S2C_CardReplace.newBuilder();
        b.setCardGroupId(cardTeamId);
        b.setActiveCardId(activeCardId);
        b.setPassiveCardId(passiveCardId);
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Bag_SetCardReplace_VALUE, b.build().toByteArray());
    }

    /***
     * 在战斗卡组中下阵
     * @param playerController
     * @param cardTeamId
     * @param activeCardId
     */
    public void quitBattle(PlayerController playerController,int cardTeamId,int activeCardId){
        CardDB toBattleCard = cardMap.get(activeCardId);
        if (toBattleCard == null) {
            //卡牌不存在
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_NOTHING_ERROR));
            return;
        }
        if (!toBattleCard.battle) {
            //不在上阵卡组中
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_NOT_IN_CARD_BATTLE_GROUP));
            return;
        }

        //设置待上阵
        toBattleCard.battle = false;
        battleCardList.remove(toBattleCard);
        sleepCardList.add(toBattleCard);

        SGBagProto.S2C_QuitBattleGroup.Builder b = SGBagProto.S2C_QuitBattleGroup.newBuilder();
        b.setCardGroupId(cardTeamId);
        b.setActiveCardId(activeCardId);
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Bag_QuitBattleGroup_VALUE, b.build().toByteArray());
    }

    /**
     * 卡牌id
     * @param playerController
     * @param cardId
     */
    public void cardDetail(PlayerController playerController, int cardId){
        CardDB cardDB = cardMap.get(cardId);
        if(cardDB == null){
            //卡牌不存在
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_NOTHING_ERROR));
            return;
        }
        SGBagProto.S2C_CardDetail.Builder b = SGBagProto.S2C_CardDetail.newBuilder();
        SGCommonProto.Card.Builder cardInfo  = SGCommonProto.Card.newBuilder();
        cardInfo.setId(cardId);
        cardInfo.setCurExp(cardDB.getExp());
        cardInfo.setFc(playerController.getPlayer().fc);
        cardInfo.setLv(cardDB.getLv());
        cardInfo.setStar(cardDB.star);

        SGCommonProto.Accessory.Builder accessory = null;
        SGCommonProto.Equip.Builder equip = null;
        //饰品
        for(Map.Entry<Integer,AccessoryDB> entry : cardDB.getAccessoryMap().entrySet()){
            accessory = SGCommonProto.Accessory.newBuilder();
            accessory.setId(entry.getValue().id);
            accessory.setIntensifyLv(entry.getValue().getLv());
            accessory.setCurrentExp(entry.getValue().getExp());
            accessory.setUpLv(entry.getValue().upLv);
            cardInfo.addAccessory(accessory);
        }
        //装备
        for(Map.Entry<Integer,EquipDB> entry : cardDB.getEquipMap().entrySet()){
            equip = SGCommonProto.Equip.newBuilder();
            equip.setId(entry.getValue().id);
            equip.setLv(entry.getValue().getLv());
            cardInfo.addEquip(equip);
        }
        //宿命列表
        cardInfo.addAllActiveFateList(cardDB.getActiveFateList());
        b.setCardInfo(cardInfo);
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Bag_CardDetail_VALUE, b.build().toByteArray());

    }

    /**
     * 装备强化
     * @param playerController
     * @param cardId
     * @param type
     * @param count
     */
    public void equipIntensify(PlayerController playerController, int cardId, int type, int count) {
        CardDB cardDB = cardMap.get(cardId);
        if(cardDB == null){
            //卡牌不存在
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_NOTHING_ERROR));
            return;
        }
        //获取当前身上某位置的装备
        EquipDB equipDB =  cardDB.getEquipDB(type);
        if(equipDB == null) {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_NOTHING_ERROR));
            return;
        }
        EquipIntensifyConfig equipIntensifyConfig = DataFactory.getInstance().getGameObject(DataFactory.EQUIP_INTENSIFY_KEY, equipDB.getLv() + count);


        if(equipIntensifyConfig.id > playerController.getPlayer().getLv() * ConstantFactory.MAX_PLAYER_LV){
            //装备等级不能大于主将等级的两倍
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.EQUIP_NOT_GREATER_THAN_PLAYER_LV));
            return;
        }

        //强化超过限制
        if(equipIntensifyConfig == null) {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.EQUIPMENT_STRENGTHENING_OVERRUN));
            return;
        }

        EquipConfig equip =  equipDB.getConfig();
        if(!equipIntensifyConfig.checkEnough(bagInfo,equip.type)) {
            //资源不够,不能升级
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ASSET_ARE_NOT_ENOUGH));
            return;
        }
        //扣除资源
        for(GoodsData goodsData : equipIntensifyConfig.needAssets) {
            if(goodsData.type == type) {
                bagInfo.addAsset(goodsData.id, -goodsData.value);
                break;
            }
        }

        //强化升级
        equipDB.lvUp(count);

        cardDB.updateFC();

        //修改战斗力
        if(cardDB.battle)
            playerController.updateFC();

        SGBagProto.S2C_EquipIntensify.Builder b = SGBagProto.S2C_EquipIntensify.newBuilder();
        b.setCardId(cardId);
        b.setEquipType(SGCommonProto.E_EQUIP_TYPE.forNumber(type));
        b.setEquipLv(equipIntensifyConfig.id);
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Bag_EquipIntensify_VALUE, b.build().toByteArray());

        //任务
        TaskInfo taskInfo = playerController.getPlayer().getExtInfo(TaskInfo.class);
        List<TaskDB> mainList = taskInfo.getDoingTask(ConstantFactory.TASK_TARAGET_MAIN_EQUIPINTENSIFY);

        TaskConfig taskConfig = null;
        //检查任务主线支线任务
        for(TaskDB taskDB : mainList) {
            taskConfig = DataFactory.getInstance().getGameObject(DataFactory.TASK_KEY, taskDB.id);
            int  equipLv = getEquipIntensifyLv(taskConfig.demand[0]);
            if(equipLv >= taskDB.currentDemand) {
                taskDB.check(ConstantFactory.TASK_TARAGET_MAIN_EQUIPINTENSIFY, equipLv - taskDB.currentDemand);
            }
        }

        //检查支线任务
        List<TaskDB> branchList = null;
        if(branchList == null || branchList.size() == 0){
            branchList = taskInfo.getDoingTask(ConstantFactory.TASK_TARAGET_EQUIPINTENSIFY);
        }
        for(TaskDB taskDB : branchList) {
            taskConfig = DataFactory.getInstance().getGameObject(DataFactory.TASK_KEY, taskDB.id);
            int  equipCount = bagInfo.getCardData().getEquipLvCount(taskConfig.demand[1]);
            if(equipCount >= taskDB.currentDemand) {
                taskDB.check(ConstantFactory.TASK_TARAGET_EQUIPINTENSIFY, equipCount);
            }
        }

        List<TaskDB> everyList = null;
        //检查日常任务
        everyList =  taskInfo.getDoingEveryTask(ConstantFactory.TASK_TARAGET_MAIN_EQUIPINTENSIFY);
        for(TaskDB taskDB : everyList) {
            if(taskDB.status == SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY_VALUE){
                taskDB.check(ConstantFactory.TASK_TARAGET_MAIN_EQUIPINTENSIFY, 1);
            }
        }

    }

    /**
     * 装备合成
     * @param playerController
     * @param cardId
     * @param type
     */
    public void equipSyn(PlayerController playerController, int cardId, int type){

        CardDB cardDB = cardMap.get(cardId);
        if(cardDB == null){
            //卡牌不存在
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_NOTHING_ERROR));
            return;
        }

        //获取当前身上某位置的装备
        EquipDB equipDB =  cardDB.getEquipDB(type);
        if(equipDB == null) {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_NOTHING_ERROR));
            return;
        }
        //获取装备配置
        EquipConfig equip = equipDB.getConfig();

        EquipConfig nextEquip = DataFactory.getInstance().getGameObject(DataFactory.EQUIP_KEY, equip.nextEquip);
        if(nextEquip == null){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.EQUIP_UPGRADED_HIGHEST_LEVEL));
            return;
        }
        if(!nextEquip.checkEnoughProp(bagInfo)){
            //检查道具是否足够
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PROPS_ARE_NOT_ENOUGH));
            return;
        }
        if(!nextEquip.checkEnoughAsset(bagInfo)){
            //检查资源是否足够
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ASSET_ARE_NOT_ENOUGH));
            return;
        }
        if(!nextEquip.checkEnoughCard(bagInfo)) {
            //检查卡片是否足够
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_FRAGMENT_ARE_NOT_ENOUGH));
            return;
        }

        //扣除资源
        TwoTuple<CommonData, CommonData> cardResult = null;
        for(GoodsData goodsData : nextEquip.synNeed) {
            if(goodsData.type == SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_PROPS_VALUE) {
                bagInfo.addProp(goodsData.id, -goodsData.value);
            }else if(goodsData.type == SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_ASSETS_VALUE){
                bagInfo.addAsset(goodsData.id, -goodsData.value);
            }
            //目前我们的卡片是不能删除的
//            else if(goodsData.type == SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_CARDS_VALUE){
//                cardResult = addCard(goodsData.id, -goodsData.value);
//                if(cardResult != null)
//                {
//                    if(cardResult.getFirst() != null)
//                    {
//                        //卡片
//                        flushDataList.add(SendUtils.createFlushData(SGCommonProto.E_DATA_TYPE.DATA_TYPE_CARD,
//                                goodsData.id, -goodsData.value, 0));
//                    }
//                }
//            }
        }


        //装备合成后记录合成的次数
        equipDB.upCount +=1;

        //合成之后新的装备赋值
        equipDB.setId(nextEquip.id);

        cardDB.updateFC();
        //修改战斗力
        if(cardDB.battle)
            playerController.updateFC();


        //任务检测
        TaskInfo taskInfo = playerController.getPlayer().getExtInfo(TaskInfo.class);

        List<TaskDB> doingList = taskInfo.getDoingTask(ConstantFactory.TASK_TARAGET_MAIN_EQUIPSYN);
        for(TaskDB taskDB : doingList) {
            taskDB.check(ConstantFactory.TASK_TARAGET_MAIN_EQUIPSYN, 1);
        }

        SGBagProto.S2C_EquipUp.Builder b = SGBagProto.S2C_EquipUp.newBuilder();
        b.setCardId(cardId);
        b.setEquipType(SGCommonProto.E_EQUIP_TYPE.forNumber(type));
        b.setNextEquipId(nextEquip.id);

        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Bag_EquipUp_VALUE, b.build().toByteArray());
    }

    /**
     * 饰品强化
     * @param playerController
     * @param cardId
     * @param type
     */
    public void accessoryIntensify(PlayerController playerController, int cardId, int type){

        CardDB cardDB = cardMap.get(cardId);
        if(cardDB == null){
            //卡牌不存在
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_NOTHING_ERROR));
            return;
        }
        //获取当前身上某位置的饰品
        AccessoryDB accessoryDB =  cardDB.getAccessoryDB(type);
        if(accessoryDB == null) {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_NOTHING_ERROR));
            return;
        }

        //饰品强化
        AccessoryIntensifyConfig accessoryIntensifyConfig = DataFactory.getInstance().getGameObject(DataFactory.ACCESSORY_INTENSIFY_KEY, accessoryDB.getLv() + 1);
        if (accessoryIntensifyConfig == null) {
            //已到最高级别无法在升级
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.EQUIP_INTENSIFY_HIGHEST_LEVEL));
            return;
        }

        //计算后的所需经验和升级所加等级
        int array[] = getUpLv(null,null, accessoryIntensifyConfig, accessoryDB);

        //检测背包里的经验道具是否足够
        List<PropDB> expProps = bagInfo.getPropTypeMap().get(ConstantFactory.PROP_TYPE_ACCESSORY_EXP);
        int tempExp = getExpValue(array,expProps);
        if (tempExp < array[0]) {
            //材料不够，轻先获取材料
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PROPS_ARE_NOT_ENOUGH));
            return;
        }

        //根据经验值，由小到大排序
        Collections.sort(expProps, new Comparator<PropDB>() {
            @Override
            public int compare(PropDB o1, PropDB o2) {
                return new Integer(o1.getConfig().effectValue).compareTo(o2.getConfig().effectValue);
            }
        });

        //扣除大于needExp的道具
        Map<Integer,Integer> tempMap = tempMap(array[0], expProps);

        //扣除经验卷轴,并获取要添加的总经验值
        int addTotalExp = 0;
        for (Integer key : tempMap.keySet()) {
            if(tempMap.keySet().size() > 1){
                addTotalExp += bagInfo.getPropById(key).getConfig().effectValue * tempMap.get(key);
            }else {
                addTotalExp = bagInfo.getPropById(key).getConfig().effectValue * tempMap.get(key);
            }
            bagInfo.addProp(key, -tempMap.get(key));
        }
        accessoryDB.addExp(addTotalExp);

        cardDB.updateFC();

        //修改战斗力
        if(cardDB.battle)
            playerController.updateFC();


        //任务检测
        TaskInfo taskInfo = playerController.getPlayer().getExtInfo(TaskInfo.class);

        List<TaskDB> doingList = taskInfo.getDoingTask(ConstantFactory.TASK_TARAGET_MAIN_ACCESSORY_INTENSIFY);
        for(TaskDB taskDB : doingList) {
            taskDB.check(ConstantFactory.TASK_TARAGET_MAIN_ACCESSORY_INTENSIFY, 1);
        }
        List<TaskDB> everyList = taskInfo.getDoingEveryTask(ConstantFactory.TASK_TARAGET_MAIN_ACCESSORY_INTENSIFY);
        for(TaskDB taskDB : everyList) {
            if(taskDB.status == SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY_VALUE){
                taskDB.check(ConstantFactory.TASK_TARAGET_MAIN_ACCESSORY_INTENSIFY, 1);
            }
        }


        SGBagProto.S2C_AccessoryIntensify.Builder b =  SGBagProto.S2C_AccessoryIntensify.newBuilder();
        b.setCardId(cardId);
        b.setAccessoryType(SGCommonProto.E_ACCESSORY_TYPE.forNumber(type));
        b.setAccessoryLv(accessoryDB.getLv());
        //当前经验
        b.setCurrentExp(accessoryDB.getExp());

        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Bag_AccessoryIntensify_VALUE, b.build().toByteArray());

    }

    public int getExpValue(int array[],List<PropDB> expProps){

        int tempExp = 0;
        if (expProps != null) {
            if(array[0] > 0){
                for (PropDB prop : expProps) {
                    tempExp += prop.getConfig().effectValue * prop.count;
                }
            }
        }
        return tempExp;
    }



    /**
     *饰品进阶
     */
    public void accessoryUp(PlayerController playerController, int cardId, int type){

        CardDB cardDB = cardMap.get(cardId);
        if(cardDB == null){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_NOTHING_ERROR));
            return;
        }

        //获取当前身上某位置的饰品
        AccessoryDB accessoryDB =  cardDB.getAccessoryDB(type);
        AccessoryConfig accessory = accessoryDB.getConfig();

        //饰品进阶
        AccessoryUpConfig nextAccessory = DataFactory.getInstance().getGameObject(DataFactory.ACCESSORY_UP_KEY, accessoryDB.upLv+1);
        if(nextAccessory == null){
            //已经到最高级别无法升级
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ACCESSORY_UPGRADED_HIGHEST_LEVEL));
            return;
        }
        //检测背包的资源是否足够
        if(!nextAccessory.checkEnoughProp(bagInfo,accessory.quality)){
            //需要进阶的材料不够，轻先获取材料
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PROPS_ARE_NOT_ENOUGH));
            return;
        }

        //扣除背包中的资源
        for (int i =0 ; i  < nextAccessory.needProps.length; i++){
            //品质
            if(accessory.quality == nextAccessory.needProps[i].type){
                bagInfo.addProp(nextAccessory.needProps[i].id, -nextAccessory.needProps[i].value);
                break;
            }
        }

        //下一级
        accessoryDB.upLv++;

        cardDB.updateFC();

        //修改战斗力
        if(cardDB.battle)
            playerController.updateFC();



        SGBagProto.S2C_AccessoryUp.Builder b = SGBagProto.S2C_AccessoryUp.newBuilder();
        b.setCardId(cardId);
        b.setAccessoryType(SGCommonProto.E_ACCESSORY_TYPE.forNumber(type));
        b.setAccessoryLvId(accessoryDB.upLv);

        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Bag_AccessoryUp_VALUE, b.build().toByteArray());


    }


    /**
     * 根据经验卷轴id添加经验
     * @param playerController
     * @param expId
     * @param count
     */
    public void accessoryAddExp(PlayerController playerController ,int cardId,int expId, int count,int type){

        CardDB cardDB = cardMap.get(cardId);
        if(cardDB == null){
            //卡牌不存在
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_NOTHING_ERROR));
            return;
        }

        //获取当前身上某位置的饰品
        AccessoryDB accessoryDB =  cardDB.getAccessoryDB(type);
        int beforeLv = accessoryDB.getLv();

        //获取配置文件中的经验卷轴
        PropConfig prop = DataFactory.getInstance().getGameObject(DataFactory.PROP_KEY, expId);
        if(prop == null){
            //获取经验卷轴配置错误
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.EXPERIENCE_PROPS_IS_NULL));
            return;
        }
        //所需总经验
        int sumExp = prop.effectValue * count;
        int bagExp = 0;
        //检测背包里的经验道具是否足够
        List<PropDB> expProps = bagInfo.getPropTypeMap().get(ConstantFactory.PROP_TYPE_ACCESSORY_EXP);
        for (PropDB propConfig : expProps){
            if(propConfig.id == expId ){
                bagExp += propConfig.getConfig().effectValue;
                break;
            }
        }

//        if(bagExp < sumExp){
//            //经验道具不足够，请获取经验道具
//            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.EXPERIENCE_PROPS_ARE_NOT_ENOUGH));
//            return;
//        }

        //扣除经验道具
        bagInfo.addProp(expId, -count);

        //卡牌升级添加经验值
        accessoryDB.addExp(sumExp);

        cardDB.updateFC();

        //修改战斗力
        if(cardDB.battle)
            playerController.updateFC();

        int afterLv = accessoryDB.getLv();

        if(afterLv - beforeLv >=1){
            //任务检测
            TaskInfo taskInfo = playerController.getPlayer().getExtInfo(TaskInfo.class);

            List<TaskDB> doingList = taskInfo.getDoingTask(ConstantFactory.TASK_TARAGET_MAIN_ACCESSORY_INTENSIFY);
            for(TaskDB taskDB : doingList) {
                taskDB.check(ConstantFactory.TASK_TARAGET_MAIN_ACCESSORY_INTENSIFY, 1);
            }
            List<TaskDB> everyList = taskInfo.getDoingEveryTask(ConstantFactory.TASK_TARAGET_MAIN_ACCESSORY_INTENSIFY);
            for(TaskDB taskDB : everyList) {
                if(taskDB.status == SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY_VALUE){
                    taskDB.check(ConstantFactory.TASK_TARAGET_MAIN_ACCESSORY_INTENSIFY, 1);
                }
            }
        }

        SGBagProto.S2C_AccessoryAddExp.Builder b = SGBagProto.S2C_AccessoryAddExp.newBuilder();
        b.setCardId(cardId);
        b.setCurrentExp(accessoryDB.getExp());
        b.setAccessoryLv(accessoryDB.getLv());
        b.setAccessoryType(SGCommonProto.E_ACCESSORY_TYPE.forNumber(type));
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Bag_AccessoryAddExp_VALUE, b.build().toByteArray());


    }


    /**
     * 宿命列表
     * @param playerController
     */
    public void cardFateList(PlayerController playerController, int cardId){
        CardDB cardDB = cardMap.get(cardId);
        if(cardDB == null){
            //卡牌不存在
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_NOTHING_ERROR));
            return;
        }
        //返回宿命列表
        SGBagProto.S2C_CardFateList.Builder b = SGBagProto.S2C_CardFateList.newBuilder();
        b.addAllFateList(cardDB.getActiveFateList());
        b.setCardId(cardId);

        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Bag_CardFateList_VALUE, b.build().toByteArray());

    }


    /**
     * 激活宿命
     * @param playerController
     * @param cardId
     * @param fateId
     */
    public void cardFate(PlayerController playerController , int cardId, int fateId ){
        CardDB cardDB = cardMap.get(cardId);
        if(cardDB == null){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_NOTHING_ERROR));
            return;
        }

        CardFateConfig cardFateConfig = DataFactory.getInstance().getGameObject(DataFactory.CARD_FATE_KEY,fateId);
        if(cardFateConfig == null){
            //此宿命配置不存在
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_FATE_NOT_EXIST));
            return;
        }

        //检测是否已激活此宿命
        if(cardDB.isHaveFate(fateId)) {
            //已经激活过了
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_FATE_HAS_BEEN_ACTIVATED));
            return;
        }

        //这里是检测我是否拥有此宿命所需要的卡
        if (!cardFateConfig.checkNeedCards(bagInfo)) {
            //此卡牌不在此宿命列表中
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_NOT_FATE_LIST));
            return;
        }

        //激活宿命
        cardDB.addFate(fateId);
        //修改战斗力
        if(cardDB.battle)
            playerController.updateFC();

        SGBagProto.S2C_CardFate.Builder c = SGBagProto.S2C_CardFate.newBuilder();
        c.setCardId(cardId);
        c.addAllFateList(cardDB.getActiveFateList());
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Bag_CardFate_VALUE, c.build().toByteArray());


    }


    /**
     * 卡牌升星
     * @param playerController
     * @param cardId
     */
    public void cardStar(PlayerController playerController ,int cardId){

        CardDB cardDB = cardMap.get(cardId);
        if(cardDB == null){
            //卡牌不存在
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_NOTHING_ERROR));
            return;
        }

        //获取下一个等级的卡牌
        CardStarConfig nextCardStarConfig = DataFactory.getInstance().getGameObject(DataFactory.CARD_STAR_KEY, cardDB.star +1);
        if(nextCardStarConfig == null){
            //已经升级到最高级无法在升级
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_UPGRADED_HIGHEST_LEVEL));
            return;
        }

        CardConfig cardConfig =  cardDB.getConfig();

        //检查背包理所需要的资源是否足够
        if(!nextCardStarConfig.checkAsset(bagInfo, cardConfig.quality)){
            //背包中所需资源不够
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ASSET_ARE_NOT_ENOUGH));
            return;
        }
        //检查背包中所需的兵种碎片
        if(!nextCardStarConfig.checkDebris(bagInfo, cardConfig.quality, cardConfig.lvUpProp)){
            //背包中碎片不足
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.BAG_LVUPPROP_ARE_NOT_ENOUGH));
            return;
        }

        //检测所需要的突破令是否足够
        if(!nextCardStarConfig.checkProps(bagInfo, cardConfig.quality)){
            //突破令不够
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PROPS_ARE_NOT_ENOUGH));
            return;
        }

        //扣除背包中的资源
        for(Map.Entry<Integer,CommonData> entry : nextCardStarConfig.needAssets.entrySet()){
            if(entry.getKey() == cardConfig.quality){
                bagInfo.addAsset(entry.getValue().id, -entry.getValue().value);
                break;
            }
        }
        //扣除碎片
        for (int i =0 ; i < nextCardStarConfig.needDebris.length; i++){
            if(cardConfig.quality == nextCardStarConfig.needDebris[i].type){
                bagInfo.addAsset(nextCardStarConfig.needDebris[i].id, -nextCardStarConfig.needDebris[i].value);
                break;
            }
        }

        //扣除背包中的道具突破令
        for(Map.Entry<Integer,CommonData> entry : nextCardStarConfig.needProps.entrySet()){
            if(entry.getKey() == cardConfig.quality){
                bagInfo.addProp(entry.getValue().id, -entry.getValue().value);
                break;
            }
        }
        //星级加1
        cardDB.addStar(1);

        if(cardDB.battle)
            playerController.updateFC();

        //任务检测
        TaskInfo taskInfo = playerController.getPlayer().getExtInfo(TaskInfo.class);

        List<TaskDB> doingList = taskInfo.getDoingTask(ConstantFactory.TASK_TARAGET_MAIN_CARD_STAR);
        for(TaskDB taskDB : doingList) {
            taskDB.check(ConstantFactory.TASK_TARAGET_MAIN_CARD_STAR, 1);
        }

        List<TaskDB> everyList = taskInfo.getDoingEveryTask(ConstantFactory.TASK_TARAGET_MAIN_CARD_STAR);
        for(TaskDB taskDB : everyList) {
            if(taskDB.status == SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY_VALUE){
                taskDB.check(ConstantFactory.TASK_TARAGET_MAIN_CARD_STAR, 1);
            }
        }

        SGBagProto.S2C_CardUpStar.Builder b = SGBagProto.S2C_CardUpStar.newBuilder();
        b.setCardId(cardId);
        b.setCardStarId(cardDB.star);
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Bag_CardUpStar_VALUE, b.build().toByteArray());

    }


    /***
     * 卡牌合成
     * @param playerController
     * @param synId
     */
    public void  cardSyn(PlayerController playerController,int synId){

        CardDB cardDB = getCard(synId);
        if(cardDB != null){
            //卡片已存在
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARDS_ALREADY_EXIST));
            return;
        }
        CardConfig cardConfig = DataFactory.getInstance().getGameObject(DataFactory.CARD_KEY,synId);
        if(cardConfig == null){
            //获取配置数据错误
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_NOTHING_ERROR));
            return;
        }
        //检测背包碎片道具是否足够
        if(bagInfo.getPropCount(synId) < cardConfig.transformation){
            //道具不够
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PROPS_ARE_NOT_ENOUGH));
            return;
        }

        addSleepCard(new CardDB(cardConfig.id));
        bagInfo.addProp(synId, -cardConfig.transformation);

        cardDB.updateFC();

        if(cardDB.battle)
            playerController.updateFC();

        //任务检测
        TaskInfo taskInfo = playerController.getPlayer().getExtInfo(TaskInfo.class);

        List<TaskDB> doingList = taskInfo.getDoingTask(ConstantFactory.TASK_TARAGET_CARD);
        for(TaskDB taskDB : doingList) {
            taskDB.check(ConstantFactory.TASK_TARAGET_CARD, 1);
        }

        SGBagProto.S2C_CardSyn.Builder b = SGBagProto.S2C_CardSyn.newBuilder();
        b.setSynId(synId);
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Bag_CardSyn_VALUE, b.build().toByteArray());

    }

    /***
     * 卡牌升级
     * @param playerController
     * @param cardId
     */
    public void cardLv(PlayerController playerController, int cardId,int needLv){
        CardDB cardDB = cardMap.get(cardId);
        if(cardDB == null){
            //卡牌不存在
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_NOTHING_ERROR));
            return;
        }

        CardLvConfig nextCardLvConfig = DataFactory.getInstance().getGameObject(DataFactory.CARD_LV_KEY, cardDB.getLv() + needLv);
        if(nextCardLvConfig == null){
            //已经到最高等级不能在升级
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_UPGRADED_HIGHEST_LEVEL));
            return;
        }

        if(cardDB.getLv() >= playerController.getPlayer().getLv()){
            //卡牌等级不能大于等于主将等级
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_NOT_GREATER_THAN_PLAYER_LV));
            return;
        }

        int array[] = getUpLv(nextCardLvConfig, cardDB, null, null);

        //检测背包里的经验道具是否足够
        List<PropDB> expProps = bagInfo.getPropTypeMap().get(ConstantFactory.PROP_TYPE_CARD_EXP);
        int tempExp = getExpValue(array,expProps);

        if (tempExp < array[0]) {
            //经验道具不足够，请获取经验道具
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.EXPERIENCE_PROPS_ARE_NOT_ENOUGH));
            return;
        }

        //根据经验值，由小到大排序
        Collections.sort(expProps, new Comparator<PropDB>() {
            @Override
            public int compare(PropDB o1, PropDB o2) {
                return new Integer(o1.getConfig().effectValue).compareTo(o2.getConfig().effectValue);
            }
        });

        //扣除大于needExp的道具
        Map<Integer,Integer> tempMap = tempMap(array[0], expProps);

        //扣除经验卷轴,并获取要添加的总经验值
        int addTotalExp = 0;
        for (Integer key : tempMap.keySet()) {
            if(tempMap.keySet().size() > 1){
                addTotalExp += bagInfo.getPropById(key).getConfig().effectValue * tempMap.get(key);
            }else {
                addTotalExp = bagInfo.getPropById(key).getConfig().effectValue * tempMap.get(key);
            }
            bagInfo.addProp(key, -tempMap.get(key));
        }

        //添加经验值
        cardDB.addExp(addTotalExp);

        if(cardDB.battle)
            playerController.updateFC();

        //任务检测
        TaskInfo taskInfo = playerController.getPlayer().getExtInfo(TaskInfo.class);

        List<TaskDB> doingList = taskInfo.getDoingTask(ConstantFactory.TASK_TARAGET_MAIN_CARD_LV);
        for(TaskDB taskDB : doingList) {
            taskDB.check(ConstantFactory.TASK_TARAGET_MAIN_CARD_LV, 1);
        }

        List<TaskDB> everyList = taskInfo.getDoingEveryTask(ConstantFactory.TASK_TARAGET_EVERYDAY_CARD_LV);
        for(TaskDB taskDB : everyList) {
            if(taskDB.status == SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY_VALUE){
                taskDB.check(ConstantFactory.TASK_TARAGET_EVERYDAY_CARD_LV, 1);
            }
        }

        SGBagProto.S2C_CardUpLv.Builder b = SGBagProto.S2C_CardUpLv.newBuilder();
        b.setCardId(cardId);
        b.setCardLv(cardDB.getLv());
        b.setCurrentExp(cardDB.getExp());
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Bag_CardUpLv_VALUE, b.build().toByteArray());
    }



    /**
     * 根据经验卷轴id添加经验
     * @param playerController
     * @param expId
     * @param count
     */
    public void cardExp(PlayerController playerController ,int cardId,int expId, int count){

        CardDB cardDB = cardMap.get(cardId);

        if(cardDB == null){
            //卡牌不存在
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_NOTHING_ERROR));
            return;
        }
        int beforeLv = cardDB.getLv();

        if(cardDB.getLv() >= playerController.getPlayer().getLv()){

            //卡牌等级不能大于等于主将等级
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_NOT_GREATER_THAN_PLAYER_LV));
            return;
        }

        //获取配置文件中的经验卷轴
        PropConfig prop = DataFactory.getInstance().getGameObject(DataFactory.PROP_KEY,expId);
        if(prop == null){
            //获取经验卷轴配置错误
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.EXPERIENCE_PROPS_IS_NULL));
            return;
        }

        //所需总经验
        int sumExp = prop.effectValue * count;
        int bagExp = 0;
        //检测背包里的经验道具是否足够
        List<PropDB> expProps = bagInfo.getPropTypeMap().get(ConstantFactory.PROP_TYPE_CARD_EXP);
        for (PropDB propConfig : expProps){
            if(propConfig.id == expId ){
                bagExp += propConfig.getConfig().effectValue;
                break;
            }
        }

//        if(bagExp < sumExp){
//            //经验道具不足够，请获取经验道具
//            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.EXPERIENCE_PROPS_ARE_NOT_ENOUGH));
//            return;
//        }

        //扣除经验道具
        bagInfo.addProp(expId, -count);
        //卡牌升级添加经验值
        cardDB.addExp(sumExp);
        //修改战斗力
        if(cardDB.battle)
            playerController.updateFC();



        int afterLv = cardDB.getLv();
        //根据经验值来计算升级次数
        if(afterLv - beforeLv >=1 ){

            //主线任务检测
            TaskInfo taskInfo = playerController.getPlayer().getExtInfo(TaskInfo.class);
            List<TaskDB> doingList = taskInfo.getDoingTask(ConstantFactory.TASK_TARAGET_MAIN_CARD_LV);
            for(TaskDB taskDB : doingList) {
                taskDB.check(ConstantFactory.TASK_TARAGET_MAIN_CARD_LV, 1);
            }

            //日常任务检测
            List<TaskDB> everyList = taskInfo.getDoingEveryTask(ConstantFactory.TASK_TARAGET_EVERYDAY_CARD_LV);
            for(TaskDB taskDB : everyList) {
                if(taskDB.status == SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY_VALUE){
                    taskDB.check(ConstantFactory.TASK_TARAGET_EVERYDAY_CARD_LV, 1);
                }
            }
        }




        SGBagProto.S2C_CardAddExp.Builder b = SGBagProto.S2C_CardAddExp.newBuilder();
        b.setCardId(cardId);
        b.setCardLv(cardDB.getLv());
        b.setCurrentExp(cardDB.getExp());
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Bag_CardAddExp_VALUE, b.build().toByteArray());


    }


    /***
     * 卡牌升级，饰品强化返回所需的经验值和等级 0:needExp ,1:tempLv
     * @param nextCardLvConfig
     * @return
     */
    public int[] getUpLv(CardLvConfig nextCardLvConfig, CardDB cardDB, AccessoryIntensifyConfig accessoryIntensifyConfig, AccessoryDB accessoryDB){

        int needExp = 0;
        int tempLv = 0;
        if(nextCardLvConfig != null && cardDB != null){
            if(nextCardLvConfig.totalExp > cardDB.getExp()){
                needExp = nextCardLvConfig.totalExp - cardDB.getExp();
            }else{
                int nextLvId = nextCardLvConfig.id;
                while(true){
                    CardLvConfig nextLv = DataFactory.getInstance().getGameObject(DataFactory.CARD_LV_KEY,nextLvId+1);
                    if(nextLv.totalExp > cardDB.getExp()){
                        break;
                    }else{
                        nextLvId ++;
                        tempLv++;
                    }

                }
            }
        }
        if (accessoryIntensifyConfig != null  && accessoryDB != null){
            if(accessoryIntensifyConfig.totalExp > accessoryDB.getExp()){
                needExp = accessoryIntensifyConfig.totalExp - accessoryDB.getExp();
            }else{
                int nextLvId = accessoryIntensifyConfig.id;
                while(true){
                    AccessoryIntensifyConfig nextLv = DataFactory.getInstance().getGameObject(DataFactory.ACCESSORY_INTENSIFY_KEY,nextLvId+1);
                    if(nextLv.totalExp > accessoryDB.getExp()){
                        break;
                    }else{
                        nextLvId ++;
                        tempLv++;
                    }

                }
            }
        }
        int array[] = new int[2];
        array[0] = needExp;
        array[1] = tempLv;
        return array;
    }

    /**
     * 返回需要扣除的经验道具
     * @param needExp
     * @param expProps
     * @return
     */
    public Map<Integer,Integer> tempMap(int needExp,List<PropDB> expProps){

        //扣除大于needExp的道具
        int sumExp = 0;
        //要扣除的经验道具 key : id  value: count
        Map<Integer, Integer> tempMap = new ConcurrentHashMap<>();
        if(needExp > 0){
            if(expProps.size() > 0 && expProps.get(0).getConfig().effectValue >= needExp){
                //要扣除的map已存在 那么个数加1
                tempMap.put(expProps.get(0).id, 1);
            }else{
                choose:
                for (PropDB prop : expProps) {
                    //根据经验卷轴id 计算总经验
                    for (int i = 0; i < prop.count; i++) {
                        sumExp += prop.getConfig().effectValue;
                        //总经验值小于需要的经验值
                        if (needExp >= sumExp ) {
                            //要扣除的map已存在 那么个数加1
                            if (tempMap.get(prop.id) != null) {
                                tempMap.put(prop.id, tempMap.get(prop.id) + 1);
                            } else {
                                //存入
                                tempMap.put(prop.id, 1);
                            }
                        } else {
                            if(sumExp -  needExp < prop.getConfig().effectValue){
                                if (tempMap.get(prop.id) != null) {
                                    tempMap.put(prop.id, tempMap.get(prop.id) + 1);
                                }else {
                                    tempMap.put(prop.id,  1);
                                }
                            }
                            break choose;

                        }


                    }
                }
            }

        }
        return tempMap;
    }

    /**
     * 卡牌升级
     * @return
     */
    public boolean checkCardUpLv(){

        CardLvConfig nextCardLvConfig = null;

        for(CardDB cardDB : battleCardList){
            nextCardLvConfig = DataFactory.getInstance().getGameObject(DataFactory.CARD_LV_KEY, cardDB.getLv() + 1);
            if(checkSingleCardUpLv(cardDB, nextCardLvConfig))
                return true;
        }
        return false;
    }

    /***
     * 检查单张卡牌升级
     * @return
     */
    public boolean checkSingleCardUpLv(CardDB cardDB, CardLvConfig nextCardLvConfig){
        if(nextCardLvConfig == null) {
            nextCardLvConfig = DataFactory.getInstance().getGameObject(DataFactory.CARD_LV_KEY, cardDB.getLv() + 1);
        }
        if(nextCardLvConfig.id >= player.getLv() * ConstantFactory.MAX_PLAYER_LV){
            return false;
        }
        List<PropDB> expProps = null;
        if(nextCardLvConfig != null){
            int needExp = nextCardLvConfig.totalExp - cardDB.getExp();
            //检测背包里的经验道具是否足够
            expProps = bagInfo.getPropTypeMap().get(ConstantFactory.PROP_TYPE_CARD_EXP);
            int tempExp = 0;
            if (expProps != null) {
                if(needExp > 0){
                    for (PropDB prop : expProps) {
                        tempExp += prop.getConfig().effectValue * prop.count;
                    }
                    if (tempExp > needExp) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    /**
     * 英雄升星
     * @return
     */
    public boolean checkCardUpStar() {
        CardStarConfig nextCardStarConfig = null;
        for(CardDB cardDB : battleCardList){
            //获取下一个等级的卡牌
            nextCardStarConfig = DataFactory.getInstance().getGameObject(DataFactory.CARD_STAR_KEY, cardDB.star + 1);
            if (nextCardStarConfig != null) {
                //检查背包中所需的资源,兵种碎片,突破令
                if(checkSingleCardUpStar(cardDB, nextCardStarConfig))
                    return true;

            }
        }

        return false;
    }


    /**
     * 单个英雄升星
     * @return
     */
    public boolean checkSingleCardUpStar(CardDB cardDB,  CardStarConfig nextCardStarConfig) {
        //获取下一个等级的卡牌
        if (nextCardStarConfig == null) {
            nextCardStarConfig = DataFactory.getInstance().getGameObject(DataFactory.CARD_STAR_KEY, cardDB.star + 1);
        }
        if(nextCardStarConfig != null){
            //检查背包中所需的资源,兵种碎片,突破令
            CardConfig cardConfig =  cardDB.getConfig();
            if (nextCardStarConfig.checkAsset(bagInfo, cardConfig.quality) &&
                    nextCardStarConfig.checkDebris(bagInfo, cardConfig.quality, cardConfig.lvUpProp) &&
                    nextCardStarConfig.checkProps(bagInfo, cardConfig.quality)) {
                return true;
            }
        }
        return false;
    }

    /***
     * 英雄宿命(检测是否有可激活的宿命)
     * @return
     */
    public boolean checkCardFate(){

        for(CardDB cardDB : battleCardList){
            //每一张卡牌的宿命列表
            //检测单张卡是否有可激活的宿命
            if(checkSingleCardFate(cardDB))
                return true;
        }
        return false;
    }

    /***
     * 英雄宿命
     * @return
     */
    public boolean checkSingleCardFate(CardDB cardDB){
        List<Integer> tempList = new CopyOnWriteArrayList<>();
        tempList.addAll(( cardDB.getConfig()).getFateList());
        //得出所有未激活的宿命
        tempList.removeAll(cardDB.getActiveFateList());
        CardFateConfig cardFateConfig = null;
        for(Integer fateId : tempList)
        {
            cardFateConfig = DataFactory.getInstance().getGameObject(DataFactory.CARD_FATE_KEY, fateId);
            if(cardFateConfig.checkNeedCards(bagInfo))
                return true;
        }
        return false;
    }

    /**
     * 装备强化
     *
     * @return
     */
    public boolean checkEquipIntensify() {
        for(CardDB cardDB : battleCardList){
            //获取当前所有得装备列表
            if(checkSingleEquipIntensify(cardDB))
                return true;

        }
        return false;
    }


    /**
     * 装备强化
     *
     * @return
     */
    public boolean checkSingleEquipIntensify(CardDB cardDB) {
        EquipDB equipDB = null;
        EquipIntensifyConfig equipIntensifyConfig = null;
        EquipConfig equipConfig = null;
        for (Integer key : cardDB.getEquipMap().keySet()) {
            //根据位置获取某一件装备
            equipDB = cardDB.getEquipDB(key);
            //获取配置
            equipIntensifyConfig = DataFactory.getInstance().getGameObject(DataFactory.EQUIP_INTENSIFY_KEY, equipDB.getLv() + 1);
            if(equipDB.getLv() >= player.getLv() * ConstantFactory.MAX_PLAYER_LV){
                return false;
            }

            if (equipIntensifyConfig != null) {

                equipConfig =  equipDB.getConfig();
                if (equipIntensifyConfig.checkEnough(bagInfo, equipConfig.type)) {
                    return true;
                }
            }
        }
        return false;
    }

    /***
     * 装备合成
     * @return
     */
    public boolean checkEquipSynthesis(){
        for(CardDB cardDB : battleCardList){
            //获取当前身上某位置的装备
            if(checkSingleEquipSynthesis(cardDB))
                return true;
        }
        return false;
    }


    /***
     * 装备合成
     * @return
     */
    public boolean checkSingleEquipSynthesis(CardDB cardDB){
        EquipConfig equip = null;
        EquipConfig nextEquip = null;
        EquipDB equipDB = null;

        for(Integer key : cardDB.getEquipMap().keySet()){
            equipDB = cardDB.getEquipDB(key);
            //获取装备配置
            equip = equipDB.getConfig();
            nextEquip = DataFactory.getInstance().getGameObject(DataFactory.EQUIP_KEY, equip.nextEquip);
            if(nextEquip != null){
                if(nextEquip.checkEnoughProp(bagInfo) &&
                        nextEquip.checkEnoughAsset(bagInfo) &&
                        nextEquip.checkEnoughCard(bagInfo) ){
                    return true;
                }
            }
        }
        return false;
    }



    /***
     * 饰品强化
     * @return
     */
    public boolean checkAccessoryIntensify(){
        for(CardDB cardDB : battleCardList){
            if(checkSingleAccessoryIntensify(cardDB))
                return true;
        }
        return false;
    }


    /***
     * 饰品强化
     * @return
     */
    public boolean checkSingleAccessoryIntensify(CardDB cardDB){
        AccessoryDB accessoryDB = null;
        AccessoryIntensifyConfig accessoryIntensifyConfig = null;
        for (Integer key : cardDB.getAccessoryMap().keySet()){
            //获取当前身上某位置的饰品
            accessoryDB = cardDB.getAccessoryDB(key);
            //饰品强化
            accessoryIntensifyConfig = DataFactory.getInstance().getGameObject(DataFactory.ACCESSORY_INTENSIFY_KEY, accessoryDB.getLv() + 1);
            if (accessoryIntensifyConfig != null) {
                int needExp = accessoryIntensifyConfig.totalExp - accessoryDB.getExp();
                //检测背包里的经验道具是否足够
//                    bagInfo = player.getExtInfo(BagInfo.class);

                List<PropDB> expProps = bagInfo.getPropTypeMap().get(ConstantFactory.PROP_TYPE_ACCESSORY_EXP);
                int tempExp = 0;
                if (expProps != null) {
                    if(needExp > 0){
                        for (PropDB prop : expProps) {
                            tempExp += prop.getConfig().effectValue * prop.count;
                        }
                    }
                    if (tempExp > needExp) {
                        return true;
                    }
                }

            }
        }
        return false;
    }



    /***
     * 饰品进阶
     * @return
     */
    public boolean checkAccessoryUp(){

        for(CardDB cardDB : battleCardList){
            if(checkSingleAccessoryUp(cardDB))
                return true;
        }
        return false;
    }


    /***
     * 饰品进阶
     * @return
     */
    public boolean checkSingleAccessoryUp(CardDB cardDB){

        AccessoryDB accessoryDB = null;
        AccessoryConfig accessory = null;
        AccessoryUpConfig nextAccessory = null;
        for (Map.Entry<Integer,AccessoryDB> entry1 : cardDB.getAccessoryMap().entrySet()){
            //获取当前身上某位置的饰品
            accessoryDB =  cardDB.getAccessoryDB(entry1.getKey().intValue());
            accessory =  accessoryDB.getConfig();
            //饰品进阶
            nextAccessory = DataFactory.getInstance().getGameObject(DataFactory.ACCESSORY_UP_KEY, accessoryDB.upLv+1);
            if(nextAccessory != null){
                //检测背包的资源是否足够
                if(nextAccessory.checkEnoughProp(bagInfo,accessory.quality)){
                    return true;
                }
            }
        }
        return false;
    }


    /***
     * 获取装备强化后等级
     * @param equipLv
     * @return
     */
    public int getEquipIntensifyLv(int equipLv){
        int count = 0;
        for (CardDB cardDB : battleCardList){
            count += cardDB.getEquipLvCount(equipLv);
        }
        return count;
    }


    /**
     * 红点系统总入口检测
     * @return
     */
    public boolean checkCorps(){
        if(checkCardUpLv()){
            return true;
        }else if (checkCardUpStar()){
            return true;
        }else if (checkCardFate()){
            return true;
        }else if (checkEquipIntensify()){
            return true;
        }else if (checkEquipSynthesis()){
            return true;
        }else if (checkAccessoryIntensify()){
            return true;
        }else if (checkAccessoryUp()){
            return true;
        }else {
            return false;
        }
    }

    /***
     * 军团列表没张卡牌红点检测
     * @param cardDB
     * @return
     */
    public boolean checkSingleCorps(CardDB cardDB){
        if(checkSingleCardUpLv(cardDB, null)){
            return true;
        }else if(checkSingleCardUpStar(cardDB,null)){
            return true;
        }else if(checkSingleCardFate(cardDB)){
            return true;
        }else if(checkSingleEquipIntensify(cardDB)){
            return true;
        }else if(checkSingleEquipSynthesis(cardDB)){
            return true;
        }else if(checkSingleAccessoryIntensify(cardDB)){
            return true;
        }else if(checkSingleAccessoryUp(cardDB)){
            return true;
        }else {
            return false;
        }
    }



    /***
     * 获取单个卡片信息
     * @param id
     * @return
     */
    public CardDB getCard(int id){
        return cardMap.get(id);
    }

    public void removeSleepCard(int cardId)
    {
        CardDB cardDB = getCard(cardId);
        if(cardDB == null)
            return;

        if(cardDB.battle)
            return;

        sleepCardList.remove(cardDB);
        cardMap.remove(cardId);
        player.addChangeGoods(SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_CARDS, cardId, -1, 0);
    }

    public boolean isBattle(int cardId)
    {
        return cardMap.get(cardId) == null ? false : cardMap.get(cardId).battle;
    }


    public void addBattleCard(CardDB cardDB)
    {
        if(cardMap.get(cardDB.id) != null)
            return;

        cardDB.battle = true;
        battleCardList.add(cardDB);
        cardMap.put(cardDB.id, cardDB);
    }

    public void addSleepCard(CardDB cardDB)
    {
        if(cardMap.get(cardDB.id) != null)
            return;

        cardDB.battle = false;
        sleepCardList.add(cardDB);
        cardMap.put(cardDB.id, cardDB);
    }



    public List<CardDB> getBattleCardList() {
        return battleCardList;
    }


    public List<CardDB> getAllCardList() {
        List<CardDB> tempList = new CopyOnWriteArrayList<>();
        tempList.addAll(battleCardList);
        tempList.addAll(sleepCardList);
        return tempList;
    }

    public List<CardDB> getSleepCardList()
    {
        return sleepCardList;
    }

    public Map<Integer, CardDB> getCardMap() {
        return cardMap;
    }

    public int getEquipLvCount (int targetLv){
        CardDB cardDB = null;
        EquipDB equipDB = null;
        int count = 0 ;
        for(Map.Entry<Integer,CardDB> entry :cardMap.entrySet()){
            cardDB = entry.getValue();
            for (Map.Entry<Integer,EquipDB> entry1 : cardDB.getEquipMap().entrySet()){
                equipDB  = cardDB.getEquipDB(entry1.getKey());
                if(equipDB.getLv() >= targetLv){
                    count+=1;
                }

            }
        }
        return count;
    }
}
