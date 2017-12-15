package com.douqu.game.core.entity.ext;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.entity.WorldInfo;
import com.douqu.game.core.entity.ext.data.boon.*;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.protobuf.SGCommonProto;

import java.util.ArrayList;
import java.util.List;

/**
 * 福利信息
 * 祭坛,抽奖这种的信息全部保存在这里面
 * @Author: Bean
 * @Description:
 * @Date: 2017-10-30 10:29
 */
public class BoonInfo extends ExtInfo{

    private AltarData altarData;

    private LotteryData lotteryData;

    private StoreData goblinStoreData;

    private RechargeRecordData rechargeRecordData;


    private BonusData bonusData;


    public BoonInfo(Player player, WorldInfo worldInfo) {
        super(player, worldInfo);

        altarData = new AltarData(player);
        lotteryData = new LotteryData(player);
        goblinStoreData = new StoreData();
        rechargeRecordData = new RechargeRecordData(player);
        bonusData = new BonusData(player);
    }

    @Override
    public List<SGCommonProto.E_RED_POINT_TYPE> checkRedPointRemindAllCondition(long currentTime) {
        List<SGCommonProto.E_RED_POINT_TYPE> list = null;
        if(altarData.hasRedPointRemind()){
            if(list == null){
                list = new ArrayList<>();
            }
            list.add(SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_ALTAR);
        }

        if(lotteryData.hasRedPointRemind(false, currentTime)){
            if(list == null){
                list = new ArrayList<>();
            }
            list.add(SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_LOTTERY);
        }

        if(goblinStoreData.hasRedPointRemind()){
            if(list == null){
                list = new ArrayList<>();
            }
            list.add(SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_GOBLIN_STORE);
        }

        if(rechargeRecordData.hasRedPointRemind()){
            if(list == null){
                list = new ArrayList<>();
            }
            list.add(SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_RECHARGE);
        }

        if(bonusData.hasRedPointRemind()){
            if(list == null){
                list = new ArrayList<>();
            }
            list.add(SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_RECHARGE);
        }

        return list;

    }


    @Override
    public void init() {
        altarData.init();
        lotteryData.init();
        goblinStoreData.init();
        rechargeRecordData.init();
        bonusData.init();
    }

    @Override
    public void checkInit() {
        altarData.checkInit();
        lotteryData.checkInit();
        goblinStoreData.checkInit();
        rechargeRecordData.checkInit();
        bonusData.checkInit();
    }

    @Override
    public void reset() {
        //检测重置
        altarData.reset();
        lotteryData.reset();
        goblinStoreData.reset();
        rechargeRecordData.reset();
        bonusData.reset();
    }

    @Override
    public void checkReset() {
        //检测重置
        altarData.checkReset();
        lotteryData.checkReset();
        goblinStoreData.checkReset();
        rechargeRecordData.checkReset();
        bonusData.checkReset();
    }

    @Override
    public void writeTo(ByteBuffer buffer)
    {
        altarData.writeTo(buffer);

        lotteryData.writeTo(buffer);

        goblinStoreData.writeTo(buffer);

        rechargeRecordData.writeTo(buffer);

        bonusData.writeTo(buffer);
    }

    @Override
    public void loadFrom(ByteBuffer buffer)
    {
        altarData.loadFrom(buffer);

        lotteryData.loadFrom(buffer);

        goblinStoreData.loadFrom(buffer);

        rechargeRecordData.loadFrom(buffer);

        bonusData.loadFrom(buffer);
    }


    public AltarData getAltarData() {
        return altarData;
    }

    public LotteryData getLotteryData() {
        return lotteryData;
    }

    public StoreData getGoblinStoreData() {
        return goblinStoreData;
    }

    public RechargeRecordData getRechargeRecordData() {
        return rechargeRecordData;
    }

    public BonusData getBonusData() {
        return bonusData;
    }
}
