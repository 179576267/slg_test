package com.douqu.game.main.msg;

import com.douqu.game.core.config.*;
import com.douqu.game.core.config.drop.DropGroupConfig;
import com.douqu.game.core.config.drop.DropResult;
import com.douqu.game.core.config.goods.*;
import com.douqu.game.core.controller.PlayerController;
import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.e.E_StableDataType;
import com.douqu.game.core.entity.db.*;
import com.douqu.game.core.entity.ext.BagInfo;
import com.douqu.game.core.config.card.CardConfig;
import com.douqu.game.core.config.card.CardLvConfig;
import com.douqu.game.core.config.card.CardStarConfig;
import com.douqu.game.core.entity.ext.TaskInfo;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.factory.WordFactory;
import com.douqu.game.core.protobuf.SGBagProto;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.protobuf.SGMainProto;
import com.douqu.game.core.util.Utils;
import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.stereotype.Component;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by bean on 2017/7/21.
 */
@Component
public class BagMsgChannel implements AMsgChannel {

    Logger logger = Logger.getLogger(this.getClass());

    @Override
    public void messageChannel(int code, PlayerController playerController, byte[] data) throws Exception {
        if (playerController == null)
            return;

        switch (code) {
            //兵种升级
            case SGMainProto.E_MSG_ID.MsgID_Bag_CardLevelUp_VALUE:
                SGBagProto.C2S_CardLevelUp c = SGBagProto.C2S_CardLevelUp.parseFrom(data);
//                bagInfo.cardLV(playerController, c == null ? 0 : c.getSid());
                break;
            //我的卡包
//            case SGMainProto.E_MSG_ID.MsgID_Bag_MyCard_VALUE:
//                playerController.getPlayer().getCardData().myCardList(playerController);
//                break;
            //卡组列表
            case SGMainProto.E_MSG_ID.MsgID_Bag_MyCardTeam_VALUE:
                playerController.getPlayer().getCardData().myCardTeam(playerController);
                break;
            //设置出阵列表
//            case SGMainProto.E_MSG_ID.MsgID_Bag_SetBattleCardTeam_VALUE:
//                SGBagProto.C2S_SetBattleCardTeam cardTeam = SGBagProto.C2S_SetBattleCardTeam.parseFrom(data);
//                playerController.getPlayer().getCardData().setBattleCardTeam(playerController, cardTeam.getCardTeamId());
//                break;
            //替换卡片
            case SGMainProto.E_MSG_ID.MsgID_Bag_SetCardReplace_VALUE:
                SGBagProto.C2S_CardReplace replace = SGBagProto.C2S_CardReplace.parseFrom(data);
                playerController.getPlayer().getCardData().cardReplace(playerController, replace.getCardGroupId(), replace.getActiveCardId(), replace.getPassiveCardId());
                break;
            //替换卡片
            case SGMainProto.E_MSG_ID.MsgID_Bag_QuitBattleGroup_VALUE:
                SGBagProto.C2S_QuitBattleGroup quitBattleGroup = SGBagProto.C2S_QuitBattleGroup.parseFrom(data);
                playerController.getPlayer().getCardData().quitBattle(playerController, quitBattleGroup.getCardGroupId(), quitBattleGroup.getActiveCardId());
                break;
            //卡牌详情 508
            case SGMainProto.E_MSG_ID.MsgID_Bag_CardDetail_VALUE:
                SGBagProto.C2S_CardDetail cardDetail = SGBagProto.C2S_CardDetail.parseFrom(data);
                playerController.getPlayer().getCardData().cardDetail(playerController, cardDetail.getCardId());
                break;
            //卡牌升级  509
            case SGMainProto.E_MSG_ID.MsgID_Bag_CardUpLv_VALUE:
                SGBagProto.C2S_CardUpLv cardUpLv = SGBagProto.C2S_CardUpLv.parseFrom(data);
                playerController.getPlayer().getCardData().cardLv(playerController, cardUpLv.getCardId(), cardUpLv.getNeedLv());
                break;
            //卡牌升星 510
            case SGMainProto.E_MSG_ID.MsgID_Bag_CardUpStar_VALUE:
                SGBagProto.C2S_CardUpStar cardUpStar = SGBagProto.C2S_CardUpStar.parseFrom(data);
                playerController.getPlayer().getCardData().cardStar(playerController, cardUpStar.getCardId());
                break;
            //激活宿命 511
            case SGMainProto.E_MSG_ID.MsgID_Bag_CardFate_VALUE:
                SGBagProto.C2S_CardFate cardFate = SGBagProto.C2S_CardFate.parseFrom(data);
                playerController.getPlayer().getCardData().cardFate(playerController, cardFate.getCardId(), cardFate.getFateId());
                break;
            //宿命列表 512
            case SGMainProto.E_MSG_ID.MsgID_Bag_CardFateList_VALUE:
                SGBagProto.C2S_CardFateList cardFateList = SGBagProto.C2S_CardFateList.parseFrom(data);
                playerController.getPlayer().getCardData().cardFateList(playerController, cardFateList.getCardId());
                break;
            //装备强化
            case SGMainProto.E_MSG_ID.MsgID_Bag_EquipIntensify_VALUE:
                SGBagProto.C2S_EquipIntensify equipIntensify = SGBagProto.C2S_EquipIntensify.parseFrom(data);
                playerController.getPlayer().getCardData().equipIntensify(playerController, equipIntensify.getCardId(), equipIntensify.getEquipTypeValue(), equipIntensify.getCount());
                break;
            //装备合成
            case SGMainProto.E_MSG_ID.MsgID_Bag_EquipUp_VALUE:
                SGBagProto.C2S_EquipUp equipUp = SGBagProto.C2S_EquipUp.parseFrom(data);
                playerController.getPlayer().getCardData().equipSyn(playerController, equipUp.getCardId(), equipUp.getEquipTypeValue());
                break;
            //饰品进阶 515
            case SGMainProto.E_MSG_ID.MsgID_Bag_AccessoryUp_VALUE:
                SGBagProto.C2S_AccessoryUp accessoryUp = SGBagProto.C2S_AccessoryUp.parseFrom(data);
                playerController.getPlayer().getCardData().accessoryUp(playerController, accessoryUp.getCardId(), accessoryUp.getAccessoryTypeValue());
                break;

            //饰品强化 516
            case SGMainProto.E_MSG_ID.MsgID_Bag_AccessoryIntensify_VALUE:
                SGBagProto.C2S_AccessoryIntensify accessoryIntensify = SGBagProto.C2S_AccessoryIntensify.parseFrom(data);
                playerController.getPlayer().getCardData().accessoryIntensify(playerController, accessoryIntensify.getCardId(), accessoryIntensify.getAccessoryTypeValue());
                break;

            //卡牌升级添加经验卷轴 517
            case SGMainProto.E_MSG_ID.MsgID_Bag_CardAddExp_VALUE:
                SGBagProto.C2S_CardAddExp cardAddExp = SGBagProto.C2S_CardAddExp.parseFrom(data);
                playerController.getPlayer().getCardData().cardExp(playerController, cardAddExp.getCardId(), cardAddExp.getExpId(), cardAddExp.getCount());
                break;
            //饰品强化经验卷轴升级 518
            case SGMainProto.E_MSG_ID.MsgID_Bag_AccessoryAddExp_VALUE:
                SGBagProto.C2S_AccessoryAddExp accessoryAddExp = SGBagProto.C2S_AccessoryAddExp.parseFrom(data);
                playerController.getPlayer().getCardData().accessoryAddExp(playerController, accessoryAddExp.getCardId(), accessoryAddExp.getExpId(), accessoryAddExp.getCount(), accessoryAddExp.getAccessoryTypeValue());
                break;
            //卡牌合成 519
            case SGMainProto.E_MSG_ID.MsgID_Bag_CardSyn_VALUE:
                SGBagProto.C2S_CardSyn cardSyn = SGBagProto.C2S_CardSyn.parseFrom(data);
                playerController.getPlayer().getCardData().cardSyn(playerController, cardSyn.getSynId());
                break;
            /*******************************重生阁*************************************/
            //520卡牌重生
            case SGMainProto.E_MSG_ID.MsgID_Bag_RebirthCard_VALUE:
                rebirthCard(playerController, data, code);
                break;
            //521 英魂分解可分解卡牌预览
            case SGMainProto.E_MSG_ID.MsgID_Bag_ResolveCardPreview_VALUE:
                resolveCardPreview(playerController, data, code);
                break;
            //522 英魂分解
            case SGMainProto.E_MSG_ID.MsgID_Bag_SoulResolve_VALUE:
                soulResolve(playerController, data, code);
                break;
            //523 装备碎片分解
            case SGMainProto.E_MSG_ID.MsgID_Bag_EquipResolve_VALUE:
                equipResolve(playerController, data, code);
                break;
            /*****************************使用道具******************************/
            case SGMainProto.E_MSG_ID.MsgID_Bag_UseProp_VALUE:
                useProp(playerController, code, data);
                break;
            default:
                break;
        }
    }

    /**
     * 道具的使用
     * @param playerController
     * @param code
     * @param data
     */
    private void useProp(PlayerController playerController, int code, byte[] data) {
        SGBagProto.C2S_UseProp request = null;
        try {
            request = SGBagProto.C2S_UseProp.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if (request == null) {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        int propId = request.getPropId();
        int useCount = request.getCount();
        logger.debug("道具使用参数:" + request.toString());
        PropConfig propConfig = DataFactory.getInstance().getGameObject(DataFactory.PROP_KEY, propId);
        if(useCount <= 0  ||  propConfig == null || propConfig.type != ConstantFactory.PROP_TYPE_BAG){
            logger.debug("找不到道具，或者数量错误,或者物品不可使用");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
        if(bagInfo.getPropCount(propId) < useCount){
            logger.debug("物品数量不足");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.GOODS_NOT_ENOUGH, propId));
            return;
        }

        for(int i=0; i < useCount; i++){
            DropGroupConfig dropGroupConfig = DataFactory.getInstance().getGameObject(DataFactory.DROP_GROUP_KEY, propConfig.effectValue);
            dropGroupConfig.reward(playerController);
        }

        bagInfo.addProp(propId, -useCount);

        SGBagProto.S2C_UseProp.Builder response =  SGBagProto.S2C_UseProp.newBuilder();
        response.setPropId(propId);
        response.setCount(useCount);
        playerController.sendMsg(code, response.build().toByteArray());
    }

    /**
     * 卡片重生
     *
     * @param playerController
     * @param data
     * @param code
     */
    private void rebirthCard(PlayerController playerController, byte[] data, int code) {
        SGBagProto.C2S_RebirthCard request = null;
        try {
            request = SGBagProto.C2S_RebirthCard.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if (request == null) {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }
        int cardId = request.getCardId();
        logger.debug("卡片重生返回参数:" + cardId);
        BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
        StableData stableData = DataFactory.getInstance().getGameObject(DataFactory.STABLE_DATA_KEY,
                E_StableDataType.REBIRTH_CARD_CONSUME.getCode());
        if(bagInfo.getAsset(stableData.goods[0].id) < stableData.goods[0].value){
            //钻石资源不足
            logger.debug("钻石资源不足" + stableData.goods[0].value);
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ASSET_NOT_ENOUGH, stableData.goods[0].id));
            return;
        }

        CardDB cardDB = bagInfo.getCardData().getCard(cardId);
        if (cardDB == null) {
            //卡片不存在
            logger.debug("卡片不存在" + cardId);
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_NOTHING_ERROR));
            return;
        }
        //上阵卡组
//        List<Integer> cardList = bagInfo.getCardData().getCardTeamMap().get(bagInfo.getCardData().getBattleCardIndex());
        List<CardDB> cardList = bagInfo.getCardData().getBattleCardList();
        if (cardDB.battle) {
            //上阵卡组的卡片不能重生
            logger.debug("上阵卡组的卡片不能重生" + cardId);
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.BATTLE_CARD_NOT_REBIRTH));
            return;
        }

        if (!cardDB.hasTrain()) {
            //卡片还未培养
            logger.debug("卡片还未培养" + cardId);
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_NOT_TRAIN));
            return;
        }

        //卡片升级返回
        if (cardDB.getLv() > 1) {
            CardLvConfig cardLvConfig = DataFactory.getInstance().getGameObject(DataFactory.CARD_LV_KEY, cardDB.getLv());
            if (cardLvConfig != null) {
                bagInfo.addGoods(cardLvConfig.restitution, null);
            }
        }

        //卡片升星
        CardStarConfig cardStarConfig = DataFactory.getInstance().getGameObject(DataFactory.CARD_STAR_KEY, cardDB.star);
        if (cardStarConfig != null) {
            bagInfo.addGoods(cardStarConfig.restitution, null);
            //返回碎片
            bagInfo.addGoods(new GoodsData[]{new GoodsData(SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_PROPS_VALUE, cardId, cardStarConfig.backDebris)}, null);
        }

        //装备升级和升阶
        Iterator<Map.Entry<Integer, EquipDB>> iterator = cardDB.getEquipMap().entrySet().iterator();
        int key;
        EquipDB equipDB;
        EquipIntensifyConfig equipIntensifyConfig;
        while (iterator.hasNext()) {
            key = iterator.next().getKey();
            equipDB = cardDB.getEquipDB(key);
            //装备升级
            equipIntensifyConfig = DataFactory.getInstance().getGameObject(DataFactory.EQUIP_INTENSIFY_KEY, equipDB.getLv());
            if (equipIntensifyConfig != null) {
                for (GoodsData goodData : equipIntensifyConfig.restitution) {
                    if (key == goodData.type) {
                        GoodsData goodsData[] = new GoodsData[]{new GoodsData(SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_ASSETS_VALUE, goodData.id, goodData.value)};
                        bagInfo.addGoods(goodsData, null);
                    }
                }

            }
            //装备升阶
            bagInfo.addGoods( equipDB.getConfig().restitution, null);

        }

        //饰品升级和升阶
        Iterator<Map.Entry<Integer, AccessoryDB>> iteratorAccessory = cardDB.getAccessoryMap().entrySet().iterator();
        AccessoryDB accessoryDB;
        AccessoryIntensifyConfig accessoryIntensifyConfig;
        while (iteratorAccessory.hasNext()) {
            key = iteratorAccessory.next().getKey();
            accessoryDB = cardDB.getAccessoryDB(key);
            //饰品强化
            accessoryIntensifyConfig = DataFactory.getInstance().getGameObject(DataFactory.ACCESSORY_INTENSIFY_KEY, accessoryDB.getLv());
            if (accessoryIntensifyConfig != null) {
                bagInfo.addGoods(accessoryIntensifyConfig.restitution, null);
            }

            //饰品升阶
            AccessoryUpConfig accessoryUpConfig = DataFactory.getInstance().getGameObject(DataFactory.ACCESSORY_UP_KEY, accessoryDB.upLv);
            if (accessoryUpConfig != null) {
                bagInfo.addGoods(accessoryUpConfig.restitution, null);
            }
        }

        //卡牌重置
        cardDB.reset();
        //资源扣除
        bagInfo.addGoods(new GoodsData[] {}, stableData.goods);

        TaskInfo taskInfo = playerController.getPlayer().getExtInfo(TaskInfo.class);
        List<TaskDB> doingList = taskInfo.getDoingTask(ConstantFactory.TASK_TARAGET_REBORN);
        for(TaskDB taskDB : doingList) {
            taskDB.check(ConstantFactory.TASK_TARAGET_REBORN, 1);
        }

        //返回数据给客户端
        SGBagProto.S2C_RebirthCard.Builder response = SGBagProto.S2C_RebirthCard.newBuilder();
        response.setCardId(cardId);
        response.setCardLv(cardDB.getLv());
        response.setCardStar(cardDB.star);
        playerController.sendMsg(code, response.build().toByteArray());
    }

    /**
     * 英魂分解可分解卡牌预览
     *
     * @param playerController
     * @param data
     * @param code
     */
    private void resolveCardPreview(PlayerController playerController, byte[] data, int code)
    {
        BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);

        List<CardDB> cardList = bagInfo.getCardData().getSleepCardList();

        SGBagProto.S2C_ResolveCardPreview.Builder response = SGBagProto.S2C_ResolveCardPreview.newBuilder();

        for (CardDB cardDB : cardList) {
            if (cardDB.hasTrain()) { //已经培养的剔除
                continue;
            }

            SGCommonProto.ResolveCardInfo.Builder info = SGCommonProto.ResolveCardInfo.newBuilder();
            info.setCardId(cardDB.id);
            info.setCardLevel(cardDB.getLv());
            info.setIsFate(bagInfo.hasFateLabel(cardDB.id));
            response.addInfo(info);
        }
        playerController.sendMsg(code, response.build().toByteArray());
    }

    /**
     * 英魂分解
     *
     * @param playerController
     * @param data
     * @param code
     */
    private void soulResolve(PlayerController playerController, byte[] data, int code) {
        SGBagProto.C2S_SoulResolve request = null;
        try {
            request = SGBagProto.C2S_SoulResolve.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if (request == null) {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }
        BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
        Map<String, GoodsData> cacheMap = new HashMap<>();
        //碎片分解
        List<Integer> debrisIds = request.getDebrisIdsList();
        PropDB prop;
        for (Integer debrisId : debrisIds) {
            prop = bagInfo.getPropById(debrisId);
            if (prop != null && prop.getConfig().type == ConstantFactory.PROP_TYPE_CARD_SYN) {
                GoodsData[] resolveData = getResolveData(ConstantFactory.RESOLVE_TYPE_FRAGMENT, prop.getConfig().quality);
                if (resolveData != null) {
                    //将配置数量装换为实际数量
                    GoodsData[] result = new GoodsData[resolveData.length];
                    for (int i = 0; i < resolveData.length; i++) {
                        result[i] = new GoodsData(resolveData[i].type, resolveData[i].id, resolveData[i].value * prop.count);
                    }
//                    bagInfo.addGoods(result, null);
                    GoodsData goodsData;
                    for(GoodsData g : result){
                        goodsData = cacheMap.get(g.type + "_" + g.id);
                        if(goodsData == null){
                            goodsData = g;
                            cacheMap.put(g.type + "_" + g.id, goodsData);
                        }else {
                            goodsData.value += g.value;
                        }
                    }
                    bagInfo.addProp(debrisId, -prop.count);
                }
            } else{
                logger.debug("卡片碎片分解错误，背包物品找不到或者物品不是卡片碎片：" + debrisId);
            }

        }

        //卡片分解
        List<Integer> cardIds = request.getCardIdsList();
        CardDB cardDB;
        for (Integer cardId : cardIds) {
            cardDB = bagInfo.getCardData().getCard(cardId);
            if (cardDB != null && !cardDB.battle) {
                GoodsData[] resolveData = getResolveData(ConstantFactory.RESOLVE_TYPE_CARD, cardDB.getConfig().quality);
//                bagInfo.addGoods(resolveData, null);
                GoodsData goodsData;
                for(GoodsData g : resolveData){
                    goodsData = cacheMap.get(g.type + "_" + g.id);
                    if(goodsData == null){
                        goodsData = g;
                        cacheMap.put(g.type + "_" + g.id, goodsData);
                    }else {
                        goodsData.value += g.value;
                    }
                }
                bagInfo.removeSleepCard(cardId);
            } else {
                logger.error("卡片找不到或卡片：" + cardId);
            }

        }

        GoodsData[] result = new GoodsData[cacheMap.size()];
        Iterator<String> iterator = cacheMap.keySet().iterator();
        int i = 0;
        while(iterator.hasNext()){
            result[i++] = cacheMap.get(iterator.next());
        }
        bagInfo.addGoods(result,null);
        playerController.sendMsg(code, SGBagProto.S2C_SoulResolve.newBuilder().addAllCardIds(cardIds).
                addAllDebrisIds(debrisIds).build().toByteArray());

    }

    /**
     * 装备碎片分解
     *
     * @param playerController
     * @param data
     * @param code
     */
    private void equipResolve(PlayerController playerController, byte[] data, int code) {

        SGBagProto.C2S_EquipResolve request = null;
        try {
            request = SGBagProto.C2S_EquipResolve.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if (request == null) {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
        Map<String, GoodsData> cacheMap = new HashMap<>();
        List<Integer> propIds = request.getPropIdsList();
        PropDB prop;
        for(Integer id : propIds){
            prop = bagInfo.getPropById(id);
            if(prop != null && prop.getConfig().type == ConstantFactory.PROP_TYPE_EQUIP_SYN_EXP){
                //将配置数量装换为实际数量
                GoodsData[] result = new GoodsData[prop.getConfig().soulData.length];
                for (int i = 0; i < prop.getConfig().soulData.length; i++) {
                    result[i] = new GoodsData(prop.getConfig().soulData[i].type, prop.getConfig().soulData[i].id, prop.getConfig().soulData[i].value * prop.count);
                }
//                bagInfo.addGoods(result, null);
                GoodsData goodsData;
                for(GoodsData g : result){
                    goodsData = cacheMap.get(g.type + "_" + g.id);
                    if(goodsData == null){
                        goodsData = g;
                        cacheMap.put(g.type + "_" + g.id, goodsData);
                    }else {
                        goodsData.value += g.value;
                    }
                }
                bagInfo.addProp(id, -prop.count);
            }else{
                logger.debug("装备碎片分解错误，背包物品找不到或者物品不是装备碎片：" + id);
            }

        }
        GoodsData[] result = new GoodsData[cacheMap.size()];
        Iterator<String> iterator = cacheMap.keySet().iterator();
        int i = 0;
        while(iterator.hasNext()){
            result[i++] = cacheMap.get(iterator.next());
        }
        bagInfo.addGoods(result,null);
        playerController.sendMsg(code, SGBagProto.S2C_EquipResolve.newBuilder().addAllPropIds(propIds).build().toByteArray());
    }


    /**
     * 获取分解数据
     *
     * @param type
     * @param quality
     * @return
     */
    public GoodsData[] getResolveData(int type, int quality) {
        List<ResolveConfig> resolveConfig = DataFactory.getInstance().getDataList(DataFactory.RESOLVE_DATA_KEY);
        for (ResolveConfig data : resolveConfig) {
            if (type == data.type && quality == data.quality) {
                return data.goods;
            }
        }
        return null;
    }


}
