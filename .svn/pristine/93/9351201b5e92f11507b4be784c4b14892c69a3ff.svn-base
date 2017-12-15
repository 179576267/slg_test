package com.douqu.game.main.msg;

import com.bean.core.util.TimeUtils;
import com.douqu.game.core.config.*;
import com.douqu.game.core.config.drop.DropGroupConfig;
import com.douqu.game.core.controller.PlayerController;
import com.douqu.game.core.e.E_RechargeType;
import com.douqu.game.core.e.E_StableDataType;
import com.douqu.game.core.e.E_StoreGoodGroup;
import com.douqu.game.core.config.task.TaskConfig;
import com.douqu.game.core.entity.db.CardDB;
import com.douqu.game.core.entity.db.TaskDB;
import com.douqu.game.core.entity.ext.TaskInfo;
import com.douqu.game.core.config.card.CardConfig;
import com.douqu.game.core.config.card.CardStarConfig;
import com.douqu.game.core.config.store.GoblinStoreConfig;
import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.entity.ext.BagInfo;
import com.douqu.game.core.entity.ext.BoonInfo;
import com.douqu.game.core.entity.ext.data.boon.BonusData;
import com.douqu.game.core.entity.ext.data.boon.RechargeRecordData;
import com.douqu.game.core.entity.ext.data.boon.StoreCacheData;
import com.douqu.game.core.entity.ext.data.boon.GoblinStoreRecordData;
import com.douqu.game.core.entity.ext.data.challenge.ArenaData;
import com.douqu.game.core.entity.ext.data.challenge.OfficialRankData;
import com.douqu.game.core.factory.ConfigFactory;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.factory.WordFactory;
import com.douqu.game.core.protobuf.*;
import com.douqu.game.core.util.Utils;
import com.douqu.game.main.GameServer;
import com.douqu.game.main.server.StoreManager;
import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author wangzhenfei
 *         2017-08-21 10:06
 *         福利消息通道（商店，祭坛，酒馆）
 */
@Component
public class BoonMsgChannel implements AMsgChannel {
    Logger logger = Logger.getLogger(BoonMsgChannel.class);


    @Override
    public void messageChannel(int code, PlayerController playerController, byte[] data) throws Exception{
        //获取相应的数据
        if (playerController == null)
            return;
        switch (code) {
            /*****************************充值和福利***********************/
            case SGMainProto.E_MSG_ID.MsgID_Bonus_RechargeInit_VALUE:
                rechargeInit(playerController, code);
                break;

            case SGMainProto.E_MSG_ID.MsgID_Bonus_Recharge_VALUE:
                recharge(playerController, code, data);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Bonus_BuyVipGiftBag_VALUE:
                buyVipGiftBag(playerController, code, data);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Bonus_ReceiveMouthCardReward_VALUE:
                receiveMouthCardReward(playerController, code, data);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Bonus_ReceiveFirstRechargeReward_VALUE:
                receiveFirstRechargeReward(playerController, code, data);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Bonus_BonusBoardInit_VALUE:
                bonusBoardInit(playerController, code, data);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Bonus_DailySignReward_VALUE:
                dailySignReward(playerController, code, data);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Bonus_BuyFund_VALUE:
                buyFund(playerController, code, data);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Bonus_ReceiveFundReward_VALUE:
                receiveFundReward(playerController, code, data);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Bonus_LoginTimesRewardInit_VALUE:
                loginTimesRewardInit(playerController, code, data);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Bonus_ReceiveLoginTimesReward_VALUE:
                receiveLoginTimesReward(playerController, code, data);
                break;
            /*****************************地精商店***********************/
            case SGMainProto.E_MSG_ID.MsgID_Store_InitInfo_VALUE:
                storeRefresh(playerController, data, code);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Store_BuyGoods_VALUE:
                buyGoods(playerController, data, code);
                break;
            /******************************次数购买**********************/
            case SGMainProto.E_MSG_ID.MsgID_Store_BuyTimes_VALUE:
                buyTimes(playerController, data, code);
                break;

            /*****************************祭坛***********************/
            case SGMainProto.E_MSG_ID.MsgID_Altar_Init_VALUE:
                //祭坛信息
                playerController.getPlayer().getAltarData().altarInit(playerController);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Altar_Sacrifice_VALUE:

                SGPlayerProto.C2S_Sacrifice r = SGPlayerProto.C2S_Sacrifice.parseFrom(data);
                if (r == null) {
                    playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CURRENT_ALTAR));
                    return;
                }
                playerController.getPlayer().getAltarData().getAltarReward(playerController, r.getId());

                break;
            /*****************************酒馆***********************/
            case SGMainProto.E_MSG_ID.MsgID_Pub_LotteryClick_VALUE:
                try {
                    SGPlayerProto.C2S_LotteryClick c = SGPlayerProto.C2S_LotteryClick.parseFrom(data);
                    if (c == null){
                        playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CARD_NOTHING_ERROR));
                        return;
                    }
                    playerController.getPlayer().getLotteryData().getCommonLottery(playerController, c.getType(), c.getBuyType());
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                    logger.error(e);
                }
                break;
            case SGMainProto.E_MSG_ID.MsgID_Pub_LotteryInit_VALUE:
                playerController.getPlayer().getLotteryData().initLotteryResponse(playerController,true);
                break;
            default:
                break;
        }

    }

    /**
     *  领取首冲礼包奖励
     * @param playerController
     * @param code
     * @param data
     */
    private void receiveFirstRechargeReward(PlayerController playerController, int code, byte[] data) {
        //TODO 首冲礼包 读取配置（目前还没有）发放奖励

        if(!playerController.getPlayer().getRechargeData().isFirstRechargeComplete()){
            logger.debug("没有完成首冲，不能领取首冲奖励");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.FIRST_RECHARGE_NOT_COMPLETE));
            return;
        }
        BonusData bonusData = playerController.getPlayer().getBonusData();
        bonusData.setFistRechargeReceive(true);
        playerController.sendMsg(code, SGPlayerProto.S2C_ReceiveFirstRechargeReward.newBuilder().build().toByteArray());
    }

    /**
     * 福利面板的初始化
     * @param playerController
     * @param code
     * @param data
     */
    private void bonusBoardInit(PlayerController playerController, int code, byte[] data) {
        RechargeRecordData recordData = playerController.getPlayer().getRechargeRecordData();
        BonusData bonusData = playerController.getPlayer().getBonusData();
        SGPlayerProto.S2C_BonusBoardInit.Builder response = SGPlayerProto.S2C_BonusBoardInit.newBuilder();
        int key = 0;
        RechargeConfig rechargeConfig = null;
        Iterator<Integer> iterator = recordData.getRechargeRecordMap().keySet().iterator();
        while (iterator.hasNext()){
            key = iterator.next();
            rechargeConfig = DataFactory.getInstance().getGameObject(DataFactory.RECHARGE_KEY, key);
            if(rechargeConfig.type == E_RechargeType.MOUTH_CARD.getCode()){
                //获取月卡剩余天数
                long endTime = recordData.getRechargeRecordMap().get(key).getEndTime();
                if(GameServer.currentTime <= endTime){
                    //获取剩余天数
                    int day = Utils.getBetweenDay(GameServer.currentTime, endTime);
                    if(day != 0){
                        response.addMouthCards(SGCommonProto.MouthCard.newBuilder().
                                setId(key).
                                setEndDays(day).
                                setHasRewardToday(recordData.isMouthCardRewardToday(key)).
                                build());
                    }
                }
            }
        }

        response.addAllHasSignDay(bonusData.getSignRecordList());
        String dayNowString = TimeUtils.getFormatTime(GameServer.currentTime, TimeUtils.DD);
        response.setDay(Integer.valueOf(dayNowString));

        response.setIsOpenFundBuy(bonusData.isBuyOpenFund());
        if(bonusData.isBuyOpenFund()){
            response.addAllOpenFundRewardRecord(bonusData.getOpenfundReciverRecord());
        }

        response.setIsFirstRechargeComplete(recordData.isFirstRechargeComplete());
        if(recordData.isFirstRechargeComplete()){
            response.setIsFirstRechargeGiftReward(bonusData.isFistRechargeReceive());
        }

        response.setDailySignRewardIndex(bonusData.getSignRewardIndex());


        logger.debug("返回给客户端福利面板信息"+ response.toString());
        playerController.sendMsg(code, response.build().toByteArray());

    }


    /**
     * 累计登陆奖励领取
     * @param playerController
     * @param code
     * @param data
     */
    private void receiveLoginTimesReward(PlayerController playerController, int code, byte[] data) {
        SGPlayerProto.C2S_ReceiveLoginTimesReward request = null;
        try {
            request = SGPlayerProto.C2S_ReceiveLoginTimesReward.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if (request == null) {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        int rewardId = request.getRewardId();
        logger.debug("领取累计登录奖励参数:" +  request.toString());
        BonusData bonusData = playerController.getPlayer().getBonusData();
        LoginTimesBonusConfig bonusConfig = DataFactory.getInstance().getGameObject(DataFactory.LOGIN_TIMES_REWARD, rewardId);
        if(bonusConfig == null || bonusData.getLoginTimesByDay() < bonusConfig.id){
            logger.debug("配置找不到，或者累计登录天数未达到");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        if(bonusData.isLoginTimesRewardReceive(rewardId)){
            logger.debug("奖励已经领取过了");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.GET_REWARD_ED));
            return;
        }

        bonusData.addLoginTimesRewardReceive(rewardId);
        BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
        bagInfo.addGoods(bonusConfig.reward, null);
        playerController.sendMsg(code, SGPlayerProto.S2C_ReceiveLoginTimesReward.newBuilder().setRewardId(rewardId).build().toByteArray());
    }

    /**
     * 登录次数奖励初始化
     * @param playerController
     * @param code
     * @param data
     */
    private void loginTimesRewardInit(PlayerController playerController, int code, byte[] data) {
        BonusData bonusData = playerController.getPlayer().getBonusData();
        SGPlayerProto.S2C_LoginTimesRewardInit.Builder response = SGPlayerProto.S2C_LoginTimesRewardInit.newBuilder();
        response.setTotalLoginDays(bonusData.getLoginTimesByDay());
        response.addAllHasReceiveId(bonusData.getLoginRewardRecord());
        playerController.sendMsg(code, response.build().toByteArray());
    }

    /**
     * 领取基金奖励
     * @param playerController
     * @param code
     * @param data
     */
    private void receiveFundReward(PlayerController playerController, int code, byte[] data) {
        SGPlayerProto.C2S_ReceiveFundReward request = null;
        try {
            request = SGPlayerProto.C2S_ReceiveFundReward.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if (request == null) {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        logger.debug("领取基金奖励参数:" +  request.toString());
        if(request.getFundType() == SGCommonProto.E_FUND_TYPE.FUND_TYPE_OPEN){//开服基金
            int rewardId = request.getRewardId();
            FundRewardConfig rewardConfig = DataFactory.getInstance().getGameObject(DataFactory.FUND_REWARD_KEY, rewardId);
            if(rewardConfig == null || rewardConfig.type != request.getFundTypeValue()){
                logger.debug("配置找不到，或者配置类型错误");
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
                return;
            }
            BonusData bonusData = playerController.getPlayer().getBonusData();
            if(bonusData.isOpenFundRewardReceive(rewardId)){
                logger.debug("开服基金对应等级奖励已经领取过了");
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.GET_REWARD_ED));
                return;
            }

            if(playerController.getPlayer().getLv() < rewardConfig.minlv){
                logger.debug("开服基金领取奖励等级不足, 当前等级" + playerController.getPlayer().getLv() + ",   需要等级：" +    rewardConfig.minlv);
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.LEVEL_NOT_ENOUGH));
                return;
            }

            BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
            bagInfo.addGoods(rewardConfig.reward, null);

            bonusData.addOpenFundRewardRecord(rewardId);

            playerController.sendMsg(code, SGPlayerProto.S2C_ReceiveFundReward.newBuilder().setFundType(request.getFundType()).
            setRewardId(rewardId).build().toByteArray());

        }else {
            logger.debug("暂无此基金类型" + request.getFundType());
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }
    }

    /**
     * 购买基金
     * @param playerController
     * @param code
     * @param data
     */
    private void buyFund(PlayerController playerController, int code, byte[] data) {
        SGPlayerProto.C2S_BuyFund request = null;
        try {
            request = SGPlayerProto.C2S_BuyFund.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if (request == null) {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        if(request.getFundType() == SGCommonProto.E_FUND_TYPE.FUND_TYPE_OPEN){//开服基金
            BonusData bonusData = playerController.getPlayer().getBonusData();
            if(bonusData.isBuyOpenFund()){
                logger.debug("开服基金已经购买过");
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.FUND_HAS_BUY));
                return;
            }

            StableData stableData = DataFactory.getInstance().getGameObject(DataFactory.STABLE_DATA_KEY, E_StableDataType.OPEN_FUND_PRICE.getCode());
            if(playerController.getPlayer().getVipLevel() < stableData.times){
                logger.debug("vip等级不足 需要：" + stableData.times + ", 当前vip等级：" + playerController.getPlayer().getVipLevel());
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.VIP_LEVEL_NOT_ENOUGH));
                return;
            }
            BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
            int count = bagInfo.getGoodsCount(stableData.goods[0].type, stableData.goods[0].id);
            if( count < stableData.goods[0].value){
                logger.debug("钻石资源不足,需要：" + stableData.goods[0].value + "，拥有：" +    count);
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ASSET_ARE_NOT_ENOUGH, stableData.goods[0].id));
                return;
            }

            bagInfo.addGoods(new GoodsData[0], stableData.goods);
            bonusData.buyOpenFund();

            playerController.sendMsg(code, SGPlayerProto.S2C_BuyFund.newBuilder().setFundType(request.getFundType()).build().toByteArray());
        }else {
            logger.debug("暂无此基金类型" + request.getFundType());
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }
    }

    /**
     * 每日签到
     * @param playerController
     * @param code
     * @param data
     */
    private void dailySignReward(PlayerController playerController, int code, byte[] data) {
        BonusData bonusData = playerController.getPlayer().getBonusData();
        int maxDayRecord = bonusData.getMaxSignDay();
        String dayNowString = TimeUtils.getFormatTime(GameServer.currentTime, TimeUtils.DD);
        int dayNow = Integer.valueOf(dayNowString);
        logger.debug("每日签到 当前日期:" + dayNow + ", 已签到的最大日期:" +  maxDayRecord);
        if(maxDayRecord >= dayNow){
            logger.debug("当前已经没有可签到的天数了");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.DAILY_SIGN_FULL));
            return;
        }

        int nextDay = maxDayRecord + 1;
        DailySignConfig dailySignConfig = DataFactory.getInstance().getGameObject(DataFactory.DAILY_SIGN_KEY, nextDay);
        if(dailySignConfig == null){
            logger.debug("本月已经签到完毕");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.DAILY_SIGN_FULL));
            return;
        }

        BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
        if(bonusData.isSignToday()){
            //需要补签
            PurchaseTimesConfig timesConfig = DataFactory.getInstance().getGameObject(DataFactory.PURCHASE_KEY, bonusData.getReissueTimes() + 1);
            int hasCount = bagInfo.getGoodsCount(timesConfig.reissueDailySign[0].type, timesConfig.reissueDailySign[0].id);
            if(hasCount < timesConfig.reissueDailySign[0].value){//自己拥有小于配置的
                logger.debug("每日签到补签资源不足 自己拥有:" + hasCount + "     ,需要：" + timesConfig.reissueDailySign[0].value);
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ASSET_NOT_ENOUGH, timesConfig.reissueDailySign[0].id));
                return;
            }

            bagInfo.addGoods(new GoodsData[0], timesConfig.reissueDailySign);
        }

        bonusData.addSignRecord(dailySignConfig.id, GameServer.currentTime);

        bagInfo.addGoods(dailySignConfig.reward[bonusData.getSignRewardIndex()]);

        playerController.sendMsg(code, SGPlayerProto.S2C_DailySignReward.newBuilder().setDayId(dailySignConfig.id).build().toByteArray());

    }


    /**
     * 充值的初始化
     * @param playerController
     * @param code
     */
    private void rechargeInit(PlayerController playerController, int code) {
        SGPlayerProto.S2C_RechargeInit.Builder response = SGPlayerProto.S2C_RechargeInit.newBuilder();
        response.setVipExp(playerController.getPlayer().getVipExp());
        response.setVipLv(playerController.getPlayer().getVipLevel());
        RechargeRecordData rechargeRecordData = playerController.getPlayer().getRechargeRecordData();
        response.addAllVipLvs(rechargeRecordData.getGiftBagRecordList());
        Iterator<Integer> iterator = rechargeRecordData.getRechargeRecordMap().keySet().iterator();
        int key = 0;
        RechargeConfig rechargeConfig = null;
        while (iterator.hasNext()){
            key = iterator.next();
            rechargeConfig = DataFactory.getInstance().getGameObject(DataFactory.RECHARGE_KEY, key);
            if(rechargeConfig.type == E_RechargeType.MOUTH_CARD.getCode()){
              //获取月卡剩余天数
                long endTime = rechargeRecordData.getRechargeRecordMap().get(key).getEndTime();
                if(GameServer.currentTime <= endTime){
                    //获取剩余天数
                    int day = Utils.getBetweenDay(GameServer.currentTime, endTime);
                    if(day != 0){
                        response.addMouthCards(SGCommonProto.MouthCard.newBuilder().
                                setId(key).
                                setEndDays(day).
                                setHasRewardToday(rechargeRecordData.isMouthCardRewardToday(key)).
                                build());
                    }
                }
            }else if(rechargeConfig.type == E_RechargeType.DIAMOND.getCode()){
                response.addHasBuyRechargeId(key);
            }
        }
        logger.debug("返回给客户端充值初始化信息" + response);
        playerController.sendMsg(code, response.build().toByteArray());
    }


    /**
     * 充值
     * @param playerController
     * @param code
     * @param data
     */
    private void recharge(PlayerController playerController, int code, byte[] data) {
        SGPlayerProto.C2S_Recharge request = null;
        try {
            request = SGPlayerProto.C2S_Recharge.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if (request == null) {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        int rechargeId = request.getRecharId();
        logger.debug("充值id：" + rechargeId);
        RechargeConfig rechargeConfig = DataFactory.getInstance().getGameObject(DataFactory.RECHARGE_KEY, rechargeId);
        if(rechargeConfig == null){
            logger.error("充值id找不到...");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);

        //TODO 接入充值，返回第三方充值的内容
        RechargeRecordData recordData = playerController.getPlayer().getRechargeRecordData();
        if(rechargeConfig.type == E_RechargeType.MOUTH_CARD.getCode()){
            if(recordData.hasRechargeRecord(rechargeId)) {//曾经购买过
                long endTime = recordData.getRechargeRecordMap().get(rechargeId).getEndTime();
                if(GameServer.currentTime - endTime < 0){
//                    logger.error("月卡存在剩余天数，不能继续购买");
//                    playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.MOUTH_CARD_REMAIN_DAY));
//                    return;
                    recordData.addRecord(rechargeId, endTime + ConstantFactory.DAY_TIME * rechargeConfig.days);
                }
            }else {
                recordData.addRecord(rechargeId, GameServer.currentTime + ConstantFactory.DAY_TIME * rechargeConfig.days);
                bagInfo.addAsset(ConfigFactory.ASSET_MONEY_KEY, rechargeConfig.money);
            }
        }else if(rechargeConfig.type == E_RechargeType.DIAMOND.getCode()){
            if(recordData.hasRechargeRecord(rechargeId)){//曾经购买过
                bagInfo.addAsset(ConfigFactory.ASSET_MONEY_KEY, rechargeConfig.money);
            }else {
                bagInfo.addAsset(ConfigFactory.ASSET_MONEY_KEY, rechargeConfig.money + rechargeConfig.extra);
                recordData.addRecord(rechargeId, 0);
            }
        }

        int receiveExp = rechargeConfig.rmb * 10;
        playerController.getPlayer().addVipExp(receiveExp);

        SGPlayerProto.S2C_Recharge.Builder response = SGPlayerProto.S2C_Recharge.newBuilder();
        response.setVipExp(playerController.getPlayer().getVipExp());
        response.setVipLv(playerController.getPlayer().getVipLevel());
        Iterator<Integer> iterator = recordData.getRechargeRecordMap().keySet().iterator();
        int key = 0;
        while (iterator.hasNext()){
            key = iterator.next();
            rechargeConfig = DataFactory.getInstance().getGameObject(DataFactory.RECHARGE_KEY, key);
            if(rechargeConfig.type == E_RechargeType.MOUTH_CARD.getCode()){
                //获取月卡剩余天数
                long endTime = recordData.getRechargeRecordMap().get(key).getEndTime();
                if(GameServer.currentTime <= endTime){
                    //获取剩余天数
                    int day = Utils.getBetweenDay(GameServer.currentTime, endTime);
                    if(day != 0){
                        response.addMouthCards(SGCommonProto.MouthCard.newBuilder().setId(key).setEndDays(day).build());
                    }
                }
            }else if(rechargeConfig.type == E_RechargeType.DIAMOND.getCode()){
                response.addHasBuyRechargeId(key);
            }
        }

        response.setAddVipExp(receiveExp);

        logger.debug("返回给客户端充值结果信息" + response);
        playerController.sendMsg(code, response.build().toByteArray());

    }

    /**
     * 购买vip礼包
     * @param playerController
     * @param code
     * @param data
     */
    private void buyVipGiftBag(PlayerController playerController, int code, byte[] data) {
        SGPlayerProto.C2S_BuyVipGiftBag request = null;
        try {
            request = SGPlayerProto.C2S_BuyVipGiftBag.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if (request == null) {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        int targetVipLv = request.getVipLv();
        logger.debug("vip礼包购买目标vipLv:" + targetVipLv);
        VipConfig vipConfig = DataFactory.getInstance().getGameObject(DataFactory.VIP_KEY, targetVipLv);
        if(vipConfig == null){
            logger.error("找不到对应vip配置");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        if(playerController.getPlayer().getVipLevel() < targetVipLv){
            logger.error("vip等级不足");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.VIP_LEVEL_NOT_ENOUGH));
            return;
        }

        RechargeRecordData rechargeRecordData = playerController.getPlayer().getRechargeRecordData();
        if(rechargeRecordData.hasGiftBagForVipId(targetVipLv)){
            logger.error("礼包已经购买过了");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.VIP_GIFT_BAG_ALREADY_BUY));
            return;
        }

        BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);

        if(playerController.getPlayer().money < vipConfig.discountPrice){
            logger.error("钻石不足，拥有" + playerController.getPlayer().money + ", 需要：" + vipConfig.discountPrice);
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ASSET_NOT_ENOUGH, ConfigFactory.ASSET_MONEY_KEY));
            return;
        }

        rechargeRecordData.addGiftBagVipId(targetVipLv);

        DropGroupConfig dropGroupConfig = DataFactory.getInstance().getGameObject(DataFactory.DROP_GROUP_KEY, vipConfig.gift);
        dropGroupConfig.reward(playerController);

        bagInfo.addAsset(ConfigFactory.ASSET_MONEY_KEY, -vipConfig.discountPrice);

        SGPlayerProto.S2C_BuyVipGiftBag.Builder response = SGPlayerProto.S2C_BuyVipGiftBag.newBuilder();
        response.setVipLv(targetVipLv);
        playerController.sendMsg(code, response.build().toByteArray());
    }


    /**
     * 领取月卡奖励
     * @param playerController
     * @param code
     * @param data
     */
    private void receiveMouthCardReward(PlayerController playerController, int code, byte[] data) {
        SGPlayerProto.C2S_ReceiveMouthCardReward request = null;
        try {
            request = SGPlayerProto.C2S_ReceiveMouthCardReward.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if (request == null) {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        logger.debug("领取月卡奖励参数:" + request.toString());
        int rechargeId = request.getRecharId();
        RechargeConfig rechargeConfig = DataFactory.getInstance().getGameObject(DataFactory.RECHARGE_KEY, rechargeId);
        if(rechargeConfig == null || rechargeConfig.type != E_RechargeType.MOUTH_CARD.getCode()){
            logger.error("月卡找不到...");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        RechargeRecordData recordData = playerController.getPlayer().getRechargeRecordData();
        if(recordData.hasRechargeRecord(rechargeId)) {//曾经购买过
            long endTime = recordData.getRechargeRecordMap().get(rechargeId).getEndTime();
            if(GameServer.currentTime - endTime > 0){ //已过期
                logger.error("月卡未购买");
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.MOUTH_CARD_NOT_BUY));
                return;
            }
        }else {
            logger.error("月卡未购买");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.MOUTH_CARD_NOT_BUY));
            return;
        }


        if(recordData.isMouthCardRewardToday(rechargeId)){
            logger.error("月卡奖励今天已经领取过了");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.MOUTH_CARD_HAS_REWARD_TODAY));
            return;
        }

        recordData.recordMouthCardRewardTime(rechargeId, GameServer.currentTime);

        //发放奖励
        BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
        bagInfo.addAsset(ConfigFactory.ASSET_MONEY_KEY, rechargeConfig.extra);

        playerController.sendMsg(code, SGPlayerProto.S2C_ReceiveMouthCardReward.newBuilder().setRecharId(rechargeId).build().toByteArray());


    }

    /**
     * 挑战次数购买
     * @param playerController
     * @param data
     * @param code
     */
    private void buyTimes(PlayerController playerController, byte[] data, int code) {
        SGPlayerProto.C2S_StoreBuyTimes request = null;
        try {
            request = SGPlayerProto.C2S_StoreBuyTimes.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if (request == null) {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }
        logger.debug("挑战次数购买参数:" + request.toString());
        int times = request.getTimes();
        if(times < 0){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        switch (request.getType().getNumber()){
            case SGCommonProto.E_BUY_TIMES_TYPE.BUY_TIMES_TYPE_ARENA_VALUE:
                buyArenaChallengeTimes(playerController, times, code);
                break;
            case SGCommonProto.E_BUY_TIMES_TYPE.BUY_TIMES_TYPE_OFFICIAL_RANK_VALUE:
                buyOfficialRankChallengeTimes(playerController, times, code);
                break;
            case SGCommonProto.E_BUY_TIMES_TYPE.BUY_TIMES_TYPE_HERO_TEMPLE_VALUE:
//                buyHeroTempleChallengeTimes(playerController, times, code);
                break;
        }


    }

    /**
     * 竞技场次数购买
     * @param playerController
     * @param times
     * @param code
     */
    private void buyArenaChallengeTimes(PlayerController playerController, int times, int code) {
        VipConfig vipConfig = DataFactory.getInstance().getGameObject(DataFactory.VIP_KEY,playerController.getPlayer().getVipLevel());
        ArenaData arenaData = playerController.getPlayer().getArenaData();
        int alreadyBuyTimes = arenaData.getAlreadyBuyTimes();
        if( alreadyBuyTimes + times > vipConfig.buyArenaMax){ //超过vip限制
            logger.debug("竞技场购买次数超过限制最多购买次数:" + vipConfig.buyArenaMax + "  , 实际购买了:" + (arenaData.getAlreadyBuyTimes() + times));
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ARENA_BUY_CHALLENGE_TIME_OVERRUN));
            return;
        }

        PurchaseTimesConfig timesConfig;
        GoodsData goodsData = null;
        //计算购买的总价格
        for(int i = 1; i <= times; i++){
            int targetTimes = i + alreadyBuyTimes;
            timesConfig = DataFactory.getInstance().getGameObject(DataFactory.PURCHASE_KEY, targetTimes);
            if(goodsData == null){
                goodsData = new GoodsData(timesConfig.buyArenaMax[0].type, timesConfig.buyArenaMax[0].id, timesConfig.buyArenaMax[0].value);
            }else {
                goodsData.value += timesConfig.buyArenaMax[0].value;
            }
        }
        if(goodsData != null){
            BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
            int hasCount = bagInfo.getGoodsCount(goodsData.type, goodsData.id);
            if(hasCount < goodsData.value){//自己拥有小于配置的
                logger.debug("竞技场购买次数资源不足 自己拥有:" + hasCount + "     ,需要：" + goodsData.value);
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ASSET_NOT_ENOUGH, goodsData.id));
                return;
            }

            arenaData.addTodayTotalChallengeTime(times);
            bagInfo.addGoods(new GoodsData[0], new GoodsData[]{goodsData});

            SGPlayerProto.S2C_StoreBuyTimes.Builder response = SGPlayerProto.S2C_StoreBuyTimes.newBuilder();
            response.setTimes(times);
            response.setTotalTimes(arenaData.getRemainTodayChallengeTimes());
            response.setType(SGCommonProto.E_BUY_TIMES_TYPE.BUY_TIMES_TYPE_ARENA);
            playerController.sendMsg(code, response.build().toByteArray());

        }else {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

    }


    /**
     * 官阶战次数购买
     * @param playerController
     * @param times
     * @param code
     */
    private void buyOfficialRankChallengeTimes(PlayerController playerController, int times, int code) {
        OfficialRankData officialData = playerController.getPlayer().getOfficialData();
        StableData stableData = DataFactory.getInstance().getGameObject(DataFactory.STABLE_DATA_KEY, E_StableDataType.OFFICIAL_RANK_BUY_PRICE.getCode());


        GoodsData goodsData = new GoodsData(stableData.goods[0].type, stableData.goods[0].id, stableData.goods[0].value *times);

         BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
         int hasCount = bagInfo.getGoodsCount(goodsData.type,goodsData.id);
         if(hasCount < goodsData.value){//自己拥有小于配置的
             logger.debug("官阶战购买次数资源不足 自己拥有:" + hasCount + "     ,需要：" + goodsData.value);
             playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ASSET_NOT_ENOUGH, goodsData.id));
             return;
         }

         officialData.addTodayTotalChallengeTime(times);
         bagInfo.addGoods(new GoodsData[0], new GoodsData[]{goodsData});

         SGPlayerProto.S2C_StoreBuyTimes.Builder response = SGPlayerProto.S2C_StoreBuyTimes.newBuilder();
         response.setTimes(times);
         response.setTotalTimes(officialData.getRemainTodayChallengeTimes());
         response.setType(SGCommonProto.E_BUY_TIMES_TYPE.BUY_TIMES_TYPE_OFFICIAL_RANK);
         playerController.sendMsg(code, response.build().toByteArray());

    }


    /**
     * 购买商品
     *
     * @param playerController
     * @param data
     * @param code
     */
    private void buyGoods(PlayerController playerController, byte[] data, int code) {
        SGPlayerProto.C2S_StoreBuyGoods request = null;
        try {
            request = SGPlayerProto.C2S_StoreBuyGoods.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if (request == null) {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        logger.debug("购买商品请求参数: " + request.toString());
        int goodId = request.getGoodsId();
        int count = request.getCount();
        count = count == 0 ? 1 : count;
        GoblinStoreConfig goods = DataFactory.getInstance().getGameObject(DataFactory.GOBLIN_STORE_KEY, goodId);
        if (goods == null || goods.storeType != request.getType().getNumber()) {
            logger.error("根据商品id获取商品配置错误");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.SERVER_DATA_ERROR));
            return;
        }
        BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
        if (bagInfo.getAsset(goods.price.id) < goods.price.value * count) {//自己拥有的小于配置的
            //TODO 资源不足
            //需要的资源不够
            logger.debug("购买的价格id-->" + goods.price.id + " 超出价格：" + goods.price.value + ", 自己拥有:" + bagInfo.getAsset(goods.price.id));
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.STORE_GOODS_MONEY_NOT_ENOUGH, goods.price.id));
            return;
        }

        if (request.getType() == SGCommonProto.E_STORE_TYPE.STORE_TYPE_GOBLIN) {
            count = 1;
            BoonInfo boonInfo = playerController.getPlayer().getExtInfo(BoonInfo.class);
            StoreCacheData storeData = boonInfo.getGoblinStoreData().getStoreCache(request.getType().getNumber());
            int index = storeData.getRecords().indexOf(new GoblinStoreRecordData(goodId));
            if (index == -1) {
                logger.error("该物品没有刷新在列表");
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.STORE_GOODS_NOT_REFRESH));
                return;
            }
            GoblinStoreRecordData recordData = storeData.getRecords().get(index);
            if (recordData.isBuy) {
                logger.error("该物品已经购买过");
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.STORE_GOODS_HAS_BUY));
                return;
            }
            //添加购买记录
            recordData.isBuy = true;
            //刷新任务
            TaskInfo taskInfo = playerController.getPlayer().getExtInfo(TaskInfo.class);
            List<TaskDB> doingList = taskInfo.getDoingEveryTask(ConstantFactory.TASK_TARAGET_EVERYDAY_GUBLIANSTORE);
            for(TaskDB taskDB : doingList) {
                if(taskDB.status == SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY_VALUE){
                    taskDB.check(ConstantFactory.TASK_TARAGET_EVERYDAY_GUBLIANSTORE, 1);
                }
            }

        } else if(request.getType() == SGCommonProto.E_STORE_TYPE.STORE_TYPE_ARENA){
            count = 1;
            BoonInfo boonInfo = playerController.getPlayer().getExtInfo(BoonInfo.class);
            StoreCacheData storeData = boonInfo.getGoblinStoreData().getStoreCache(request.getType().getNumber());
            int index = storeData.getRecords().indexOf(new GoblinStoreRecordData(goodId));
            if (index == -1) {
                logger.error("该物品没有刷新在列表");
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.STORE_GOODS_NOT_REFRESH));
                return;
            }
            GoblinStoreRecordData recordData = storeData.getRecords().get(index);
            if (recordData.isBuy) {
                logger.error("该物品已经购买过");
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.STORE_GOODS_HAS_BUY));
                return;
            }
            //添加购买记录
            recordData.isBuy = true;
        }else if(request.getType() == SGCommonProto.E_STORE_TYPE.STORE_TYPE_EQUIP){

        }else {
            logger.error("暂无此商店类型：" + request.getType());
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR, goods.price.id));
            return;
        }

        //增加兑换和发送给客户端
        bagInfo.addGoods(new GoodsData[]{new GoodsData(goods.goods.type, goods.goods.id, goods.goods.value * count)}, new GoodsData[]{new GoodsData(
                SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_ASSETS_VALUE, goods.price.id, goods.price.value * count)});

        SGPlayerProto.S2C_StoreBuyGoods.Builder response = SGPlayerProto.S2C_StoreBuyGoods.newBuilder();
        response.setType(request.getType());
        response.setGoodsId(goodId);
        response.setCount(count);
        playerController.sendMsg(code, response.build().toByteArray());
    }

    /**
     * 地精商店刷新
     *
     * @param playerController
     * @param data
     * @param code
     */
    private void storeRefresh(PlayerController playerController, byte[] data, int code) {
        SGPlayerProto.C2S_StoreInitInfo request = null;
        try {
            request = SGPlayerProto.C2S_StoreInitInfo.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if (request == null) {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        logger.debug("地精商店刷新请求参数: " + request.getType() + (request.getIsFresh() ? ", 刷新" : ", 拉取上次记录"));
        boolean isFresh = request.getIsFresh();

        if (request.getType() == SGCommonProto.E_STORE_TYPE.STORE_TYPE_GOBLIN) {

            BoonInfo boonInfo = playerController.getPlayer().getExtInfo(BoonInfo.class);
            StoreCacheData storeData = boonInfo.getGoblinStoreData().getStoreCache(request.getType().getNumber());
            logger.debug("当前已经刷新的次数 :" + storeData.getHasRefreshTimes());
            if (isFresh && !storeData.isFreeTimesEnough()) {
                //免费次数不足，消耗物品
                StableData stableData = DataFactory.getInstance().getGameObject(DataFactory.STABLE_DATA_KEY, E_StableDataType.GOBLIN_FRESH_TIMES.getCode());
                BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
                int goodsCount = bagInfo.getGoodsCount(stableData.goods[0].type, stableData.goods[0].id);
                if(goodsCount < stableData.goods[0].value){
                    logger.debug("商店刷新消耗 自己拥有:" + goodsCount + "     ,需要：" + stableData.goods[0].value);
                    playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ASSET_NOT_ENOUGH, stableData.goods[0].id));
                    return;
                }
                //扣除资源
                bagInfo.addGoods(new GoodsData[0], stableData.goods);
            }

            //获取符合条件的物品
            List<SGCommonProto.StoreGoods> newGoods = getGoods(playerController, isFresh, storeData, request.getType());
            SGPlayerProto.S2C_StoreInitInfo.Builder response = SGPlayerProto.S2C_StoreInitInfo.newBuilder();
            response.setType(request.getType());
            response.addAllGoods(newGoods);
            if (isFresh && storeData.isFreeTimesEnough()) {
                storeData.addRefreshTimes(GameServer.currentTime);
            }
            response.setFreeTimes(storeData.getRemindRefreshTimes());
            logger.debug("返回给客户端竞技场商店初始化信息 ： " + response.toString());
            //返回给客户端
            playerController.sendMsg(code, response.build().toByteArray());

        } else  if(request.getType() == SGCommonProto.E_STORE_TYPE.STORE_TYPE_ARENA) {
            BoonInfo boonInfo = playerController.getPlayer().getExtInfo(BoonInfo.class);
            StoreCacheData storeData = boonInfo.getGoblinStoreData().getStoreCache(request.getType().getNumber());
            if (isFresh) {
                //免费次数不足，消耗物品
                StableData stableData = DataFactory.getInstance().getGameObject(DataFactory.STABLE_DATA_KEY, E_StableDataType.ARENA_BUY_PRICE.getCode());
                BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
                int goodsCount = bagInfo.getGoodsCount(stableData.goods[0].type, stableData.goods[0].id);
                if(goodsCount < stableData.goods[0].value){
                    logger.debug("商店刷新消耗 自己拥有:" + goodsCount + "     ,需要：" + stableData.goods[0].value);
                    playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ASSET_NOT_ENOUGH, stableData.goods[0].id));
                    return;
                }
                //扣除资源
                bagInfo.addGoods(new GoodsData[0], stableData.goods);
            }

            //获取符合条件的物品
            List<SGCommonProto.StoreGoods> newGoods = getGoods(playerController, isFresh, storeData, request.getType());
            SGPlayerProto.S2C_StoreInitInfo.Builder response = SGPlayerProto.S2C_StoreInitInfo.newBuilder();
            response.setType(request.getType());
            response.addAllGoods(newGoods);
            if (isFresh) {
                storeData.addRefreshTimes(GameServer.currentTime);
            }
            logger.debug("返回给客户端竞技场商店初始化信息 ： " + response.toString());
            //返回给客户端
            playerController.sendMsg(code, response.build().toByteArray());
        }else{
            logger.error("暂无此商店类型：" + request.getType());
        }

    }

    /**
     * 获取新刷新的物品
     */
    private List<SGCommonProto.StoreGoods> getGoods(PlayerController playerController, boolean isFresh, StoreCacheData storeData, SGCommonProto.E_STORE_TYPE storeType) {

        List<SGCommonProto.StoreGoods> storeGoodsList = new ArrayList<>();
        BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
        SGCommonProto.StoreGoods.Builder storeGoods;
        if(storeType == SGCommonProto.E_STORE_TYPE.STORE_TYPE_GOBLIN){
            if (isFresh || storeData.getRecords().size() == 0) {//刷新或者没有缓存过
                storeData.getRecords().clear();
                //刷新任务
                TaskInfo taskInfo = playerController.getPlayer().getExtInfo(TaskInfo.class);
                List<TaskDB> doingList = taskInfo.getDoingTask(ConstantFactory.TASK_TARAGET_GUBLIANSTORE);
                TaskConfig taskConfig = null;

                for(TaskDB taskDB : doingList) {
                    taskConfig = DataFactory.getInstance().getGameObject(DataFactory.TASK_KEY, taskDB.id);
                    taskDB.check(ConstantFactory.TASK_TARAGET_GUBLIANSTORE, taskConfig.demand[0]);
                }

                List<GoblinStoreConfig> allGoods = new ArrayList<>();
                allGoods.addAll(StoreManager.getInstance().getStoreGoods(SGCommonProto.E_STORE_TYPE.STORE_TYPE_GOBLIN_VALUE,
                        E_StoreGoodGroup.GOBLIN_STORE_GROUP_ASSETS.getCode(), 2));

                allGoods.addAll(StoreManager.getInstance().getStoreGoods(SGCommonProto.E_STORE_TYPE.STORE_TYPE_GOBLIN_VALUE,
                        E_StoreGoodGroup.GOBLIN_STORE_GROUP_CARD.getCode(), 4));
                for (GoblinStoreConfig goods : allGoods) {
                    storeData.getRecords().add(new GoblinStoreRecordData(goods.getId()));
                    storeGoods = SGCommonProto.StoreGoods.newBuilder();
                    storeGoods.setGoodsId(goods.id);
                    //当为碎片类型需要查询背包获取已有，剩余和宿命上阵信息
                    if (goods.group == E_StoreGoodGroup.GOBLIN_STORE_GROUP_CARD.getCode() &&
                            goods.goods.type == SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_PROPS_VALUE) {
                        getGoodsCompareBagParam(bagInfo, storeGoods, goods);
                    }
                    storeGoodsList.add(storeGoods.build());
                }
            } else {
                List<GoblinStoreRecordData> records = storeData.getRecords();
                for (GoblinStoreRecordData data : records) {
                    storeGoods = SGCommonProto.StoreGoods.newBuilder();
                    storeGoods.setGoodsId(data.goodsId);
                    storeGoods.setIsBuy(data.isBuy);
                    GoblinStoreConfig store = DataFactory.getInstance().getGameObject(DataFactory.GOBLIN_STORE_KEY, data.goodsId);
                    if (store == null) {
                        logger.error("根据商品id获取商品配置错误");
                        return storeGoodsList;
                    }
                    if (store.group == E_StoreGoodGroup.GOBLIN_STORE_GROUP_CARD.getCode() && store.goods.type ==
                            SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_PROPS_VALUE) {
                        //当为碎片类型需要查询背包获取已有，剩余和宿命上阵信息
                        getGoodsCompareBagParam(bagInfo, storeGoods, store);
                    }
                    storeGoodsList.add(storeGoods.build());
                }
            }
        }else if(storeType == SGCommonProto.E_STORE_TYPE.STORE_TYPE_ARENA){
            if (isFresh || storeData.getRecords().size() == 0) {//刷新或者没有缓存过
                storeData.getRecords().clear();

                List<GoblinStoreConfig> allGoods = new ArrayList<>();
                allGoods.addAll(StoreManager.getInstance().getStoreGoods(SGCommonProto.E_STORE_TYPE.STORE_TYPE_ARENA_VALUE,
                        E_StoreGoodGroup.ARENA_STORE_GROUP_PROP.getCode(), 3));

                allGoods.addAll(StoreManager.getInstance().getStoreGoods(SGCommonProto.E_STORE_TYPE.STORE_TYPE_ARENA_VALUE,
                        E_StoreGoodGroup.GROUP_DEFAULT.getCode(), 5));
                for (GoblinStoreConfig goods : allGoods) {
                    storeData.getRecords().add(new GoblinStoreRecordData(goods.getId()));
                    storeGoods = SGCommonProto.StoreGoods.newBuilder();
                    storeGoods.setGoodsId(goods.id);
                    storeGoodsList.add(storeGoods.build());
                }
            } else {
                List<GoblinStoreRecordData> records = storeData.getRecords();
                for (GoblinStoreRecordData data : records) {
                    storeGoods = SGCommonProto.StoreGoods.newBuilder();
                    storeGoods.setGoodsId(data.goodsId);
                    storeGoods.setIsBuy(data.isBuy);
                    GoblinStoreConfig store = DataFactory.getInstance().getGameObject(DataFactory.GOBLIN_STORE_KEY, data.goodsId);
                    if (store == null) {
                        logger.error("根据商品id获取商品配置错误");
                        return storeGoodsList;
                    }
                    storeGoodsList.add(storeGoods.build());
                }
            }
        }

        return storeGoodsList;
    }

    /**
     * 获取商品和背包的比较
     * @param bagInfo
     * @param storeGoods
     * @param goods
     */
    private void getGoodsCompareBagParam(BagInfo bagInfo, SGCommonProto.StoreGoods.Builder storeGoods, GoblinStoreConfig goods) {
        //获取碎片数量
        int num = bagInfo.getPropCount(goods.goods.id);
        storeGoods.setHasCount(num);
        //查询下一级所需的数量
        CardDB card = bagInfo.getCardData().getCard(goods.goods.id);
        if(card != null){ // 自己已经拥有这张卡片
            int nextLevel = card.star + 1;
            CardStarConfig cardStarConfig = DataFactory.getInstance().getGameObject(DataFactory.CARD_STAR_KEY, nextLevel);
            if(cardStarConfig != null){
                for(GoodsData data : cardStarConfig.needDebris){
                    if(data.type == card.getConfig().quality){
                        storeGoods.setNeedCount(data.value);
                        break;
                    }
                }
            }
        }else{ // 没有卡，合成需要的数量
            //TODO
            CardConfig cardConfig =  DataFactory.getInstance().getGameObject(DataFactory.CARD_KEY, goods.goods.id);
            if(cardConfig != null){
                storeGoods.setNeedCount(cardConfig.transformation);
            }else {
                logger.error("BoonMsgChannel error 卡片找不到： " + goods.goods.id);
            }
        }
        // 查询已上阵标签和宿命
        SGCommonProto.E_STORE_LABEL label = null;
        if(bagInfo.isBattleCard(goods.goods.id)){
            label = SGCommonProto.E_STORE_LABEL.LABEL_ALREADY_BATTLE;
        }else if(bagInfo.hasFateLabel(goods.goods.id)){
            label = SGCommonProto.E_STORE_LABEL.LABEL_FATE;
        }
        if(label != null){
            storeGoods.setLabel(label);
        }
//        while (it.hasNext()) {
//            Map.Entry<Integer, List<Integer>> entry = it.next();
//            List<Integer> cardList = entry.getValue();
//            if(cardList.contains(goods.goods.id)){
//                label = SGCommonProto.E_STORE_LABEL.LABEL_ALREADY_BATTLE;
//                break;
//            }
//            ll:
//            for(Integer id : cardList){//id 上阵卡组的id
//                for(CardFate fate : fateList){
//                    if(fate.isCardHasFate(goods.goods.id, id)){
//                        label = SGCommonProto.E_STORE_LABEL.LABEL_FATE;
//                        break ll;
//                    }
//
//                }
//            }
//        }

    }

}

