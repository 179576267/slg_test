package com.douqu.game.core.entity.ext.data.boon;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.config.PurchaseTimesConfig;
import com.douqu.game.core.config.VipConfig;
import com.douqu.game.core.controller.PlayerController;
import com.douqu.game.core.config.AltarConfig;
import com.douqu.game.core.config.drop.DropGroupConfig;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.entity.db.TaskDB;
import com.douqu.game.core.entity.ext.BagInfo;
import com.douqu.game.core.entity.ext.TaskInfo;
import com.douqu.game.core.entity.ext.data.BaseData;
import com.douqu.game.core.factory.ConfigFactory;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.factory.WordFactory;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.protobuf.SGMainProto;
import com.douqu.game.core.protobuf.SGPlayerProto;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-10-30 17:06
 */
public class AltarData extends BaseData {

    /**玩家祭坛数据*/
    public Map<Integer, SingleBoonData> altarMap;

    private Player player;
    public AltarData(Player player) {
        this.player = player;
        altarMap = new ConcurrentHashMap<>();
    }


    @Override
    public void init() {
        List<AltarConfig> list = DataFactory.getInstance().getDataList(DataFactory.ALTAR_KEY);
        for(AltarConfig altarConfig : list){
            altarMap.put(altarConfig.id,new SingleBoonData(player, DataFactory.ALTAR_KEY, altarConfig.id));
        }
    }

    @Override
    public void checkInit() {
        if(altarMap.size() == 0){
            init();
        }
    }

    @Override
    public void reset()
    {
        for(Map.Entry<Integer,SingleBoonData> entry : altarMap.entrySet()){
            entry.getValue().clear();
        }
    }

    @Override
    public void checkReset() {
        boolean isToday = false;
        for(Map.Entry<Integer,SingleBoonData> entry : altarMap.entrySet()){
            if(entry.getValue().isToday())
            {
                isToday = true;
                break;
            }
        }
        if(!isToday)
            reset();
    }

    @Override
    public void writeTo(ByteBuffer buffer)
    {
        buffer.writeByte(altarMap.size());
        for(Integer key : altarMap.keySet())
        {
            altarMap.get(key).writeTo(buffer);
            buffer.writeByte(key);
        }
    }

    @Override
    public void loadFrom(ByteBuffer buffer)
    {
        int size = buffer.readByte();
        SingleBoonData altar = null;
        for (int i = 0;i < size; i++){
            altar = new SingleBoonData(player, DataFactory.ALTAR_KEY);
            altar.loadFrom(buffer);
            altarMap.put(buffer.readByte(),altar);
        }
    }


    /**
     * 是否有免费次数
     * @return
     */
    public boolean hasRedPointRemind(){
        Iterator<Integer> iterator = altarMap.keySet().iterator();
        while (iterator.hasNext()){
            SingleBoonData singleBoonData = altarMap.get(iterator.next());
            if(singleBoonData.freeCount > 0){
                return true;
            }
        }
        return false;
    }


    /**
     * 祭祀
     * */
    public void getAltarReward(PlayerController playerController, int id) {
        SingleBoonData altarData = altarMap.get(id);
        if(altarData == null) {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CURRENT_ALTAR));
            return;
        }
        //根据id 获取配置文件数据
        AltarConfig altarConfig = DataFactory.getInstance().getGameObject(DataFactory.ALTAR_KEY, altarData.id);
        BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
        if(playerController.getPlayer().getLv() < altarConfig.openLevel){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CURRENT_LEVEL));
            return;
        }


        //获取购买次数 当前的总次数 - 配置的免费次数
        int hasBuyTimes = altarData.getBuyTimes();
        int assetsCount = 0;
        //查询当前下次的购买价格
        if(hasBuyTimes >= 0){
            PurchaseTimesConfig timesConfig = DataFactory.getInstance().getGameObject(DataFactory.PURCHASE_KEY, hasBuyTimes + 1);
            assetsCount = timesConfig.altarBuyPriceMap.get(altarData.id);
//            CommonData commonData = altarConfig.getDropCommonData(altarData.currentNum);
            if(bagInfo.getAsset(ConfigFactory.ASSET_MONEY_KEY) < assetsCount) {
                //资源不够
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.BAG_ASSET_DEFICIENT, ConfigFactory.ASSET_MONEY_KEY));
                return;
            }
        }



        long currentTime = System.currentTimeMillis();
        VipConfig vipConfig = DataFactory.getInstance().getGameObject(DataFactory.VIP_KEY, player.getVipLevel());
        //每日抽奖次数大于最大次数
        if(altarData.currentNum >= vipConfig.altarFreeTimesMap.get(altarData.id) + vipConfig.altarPurchaseMaxMap.get(altarData.id)){
            //不能参与祭祀 每日次数上限
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CURRENT_NUM_LIMITATION,altarData.currentNum));
            return;
        }else{
            altarData.currentNum++;
            altarData.lastUpdateTime = currentTime;

        }
        int everydayResidueCount = altarData.getRemainTimes();
        //免费次数大于0 减去免费次数
        if(altarData.freeCount > 0){
            altarData.freeCount--;
        }else{
            if(assetsCount > 0){
                bagInfo.addAsset(ConfigFactory.ASSET_MONEY_KEY, -assetsCount);
            }

        }

        //获取奖励id
        int dropId = altarConfig.getAltarDrop(playerController);
        DropGroupConfig dropGroupConfig = DataFactory.getInstance().getGameObject(DataFactory.DROP_GROUP_KEY, dropId);
        if(dropGroupConfig == null){
            //掉落资源为空
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CURRENT_ALTAR));
            return;
        }

        //掉落
        SGPlayerProto.S2C_Sacrifice.Builder b = SGPlayerProto.S2C_Sacrifice.newBuilder();
        dropGroupConfig.dropRewardResult(bagInfo);
//        if(dropResult == null){
//            //掉落数据为空
//            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.DROP_ASSETS_IS_NULL));
//            return;
//        }
//        SGCommonProto.CommonDrop.Builder comDrop = null;
//        for(CommonData com : dropResult.assets) {
//            comDrop = SGCommonProto.CommonDrop.newBuilder();
//            comDrop.setType(SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_ASSETS);
//            comDrop.setId(com.id);
//            comDrop.setCount(com.value);
//            b.addDropData(comDrop);
//        }
//        for(CommonData com : dropResult.cards) {
//            comDrop = SGCommonProto.CommonDrop.newBuilder();
//            comDrop.setType(SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_CARDS);
//            comDrop.setId(com.id);
//            comDrop.setCount(com.value);
//            b.addDropData(comDrop);
//        }
//        for(CommonData com : dropResult.props) {
//            comDrop = SGCommonProto.CommonDrop.newBuilder();
//            comDrop.setType(SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_PROPS);
//            comDrop.setId(com.id);
//            comDrop.setCount(com.value);
//            b.addDropData(comDrop);
//        }



        b.setId(id);
        b.setFreeCount(altarData.freeCount);
        b.setEverydayResidueCount(everydayResidueCount);

        //任务检测
        TaskInfo taskInfo = playerController.getPlayer().getExtInfo(TaskInfo.class);
        List<TaskDB> everyList = taskInfo.getDoingEveryTask(ConstantFactory.TASK_TARAGET_EVERY_ALTAR);
        for(TaskDB taskDB : everyList) {
            if(taskDB.status == SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY_VALUE){
                taskDB.check(ConstantFactory.TASK_TARAGET_EVERY_ALTAR, 1);
            }
        }

        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Altar_Sacrifice_VALUE, b.build().toByteArray());

//        SendUtils.sendFlushData(playerController, dropResult);
    }


    public void altarInit(PlayerController playerController){
        SGPlayerProto.S2C_AltarInit.Builder b = SGPlayerProto.S2C_AltarInit.newBuilder();
        List<AltarConfig> list = DataFactory.getInstance().getDataList(DataFactory.ALTAR_KEY);
        SGPlayerProto.AltarInit.Builder c = SGPlayerProto.AltarInit.newBuilder();
        SingleBoonData altarData = null;
        for (AltarConfig altarConfig : list) {
            altarData = altarMap.get(altarConfig.id);
            c.setId(altarConfig.id);
//            c.setEverydayResidueCount(altarConfig.everydayMaxCount - altarData.currentNum);
            c.setEverydayResidueCount(altarData.getRemainTimes());
            c.setFreeCount(altarData.freeCount);
            b.addAltarInit(c);
        }
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Altar_Init_VALUE, b.build().toByteArray());
    }




}
