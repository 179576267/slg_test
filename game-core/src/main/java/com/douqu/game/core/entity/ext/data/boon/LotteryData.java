package com.douqu.game.core.entity.ext.data.boon;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.controller.PlayerController;
import com.douqu.game.core.e.E_LotteryType;
import com.douqu.game.core.config.LotteryConfig;
import com.douqu.game.core.config.task.TaskConfig;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.entity.db.TaskDB;
import com.douqu.game.core.entity.ext.BagInfo;
import com.douqu.game.core.entity.ext.TaskInfo;
import com.douqu.game.core.entity.ext.data.BaseData;
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
 * @Date: 2017-10-30 17:05
 */
public class LotteryData extends BaseData {

    /**
     * 抽奖次数
     * key: 类型
     * value: 抽奖数据
     */
    public Map<Integer,SingleBoonData> lotteryMap;

    /**
     * 定时器是否推送过了
     */
    public boolean hasPushRedPointRemind = false;

    private Player player;

    public LotteryData(Player player) {
        this.player = player;
        lotteryMap = new ConcurrentHashMap<>();
        hasPushRedPointRemind = false;
    }

    @Override
    public void init() {
        List<LotteryConfig> list = DataFactory.getInstance().getDataList(DataFactory.LOTTERY_KEY);
        for(LotteryConfig object : list) {
            if(object.level == 1) {
                lotteryMap.put(object.type, new SingleBoonData(player,DataFactory.LOTTERY_KEY, object.id));
            }
        }
    }

    @Override
    public void checkInit() {
        if(lotteryMap.size() == 0)
        {
            init();
        }
    }

    @Override
    public void reset()
    {
        for(Map.Entry<Integer,SingleBoonData> entry : lotteryMap.entrySet()){
            entry.getValue().clear();
        }
    }

    @Override
    public void checkReset() {
        boolean isToday = false;
        for(Map.Entry<Integer,SingleBoonData> entry : lotteryMap.entrySet()){
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
        buffer.writeByte(lotteryMap.size());
        for(Integer key : lotteryMap.keySet())
        {
            lotteryMap.get(key).writeTo(buffer);
            buffer.writeByte(key);
        }

    }

    @Override
    public void loadFrom(ByteBuffer buffer)
    {
        int size = buffer.readByte();
        SingleBoonData lotteryData = null;
        for (int i = 0;i < size; i++){
            lotteryData = new SingleBoonData(player, DataFactory.LOTTERY_KEY);
            lotteryData.loadFrom(buffer);
            lotteryMap.put(buffer.readByte(),lotteryData);
        }
    }


    /**
     * 是否有红点
     * @return
     */
    public boolean hasRedPointRemind(boolean isScheduleTask, long currentTime){
        if(hasPushRedPointRemind && isScheduleTask){
            return false;
        }
        boolean has = false;
        Iterator<Integer> iterator = lotteryMap.keySet().iterator();
        LotteryConfig lotteryConfig = null;
        while (iterator.hasNext()){
            SingleBoonData singleBoonData = lotteryMap.get(iterator.next());
            if(singleBoonData.freeCount > 0){
                lotteryConfig = lotteryConfig == null ? DataFactory.getInstance().getGameObject(DataFactory.LOTTERY_KEY, singleBoonData.id) : lotteryConfig;
                long offsetTime = currentTime - singleBoonData.lastUpdateTime;
                has = (offsetTime - lotteryConfig.cdTime >= 0);
                break;
            }
        }
        if(isScheduleTask){
            hasPushRedPointRemind = true;
        }
        return has;
    }

    /**
     * 抽奖
     * @param playerController
     * @param type
     * @param buyType
     */
    public void getCommonLottery(PlayerController playerController, int type, int buyType) {

        System.out.println("抽奖参数 -> type:" + type + " buyType:" + buyType);
        //获取抽奖数据
        SingleBoonData lotteryData = lotteryMap.get(type);
        if(lotteryData == null) {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CURRENT_LOTTERY));
            return;
        }

        LotteryConfig  lotteryConfig = DataFactory.getInstance().getGameObject(DataFactory.LOTTERY_KEY, lotteryData.id);
        long currentTime = System.currentTimeMillis();


        if (type == E_LotteryType.ONCE.getCode() ){
            if(buyType == E_LotteryType.ONCE.getCode()){
                if((lotteryData.currentNum + 1) % lotteryConfig.nextLevelCount == 0){
                    lotteryConfig = DataFactory.getInstance().getNextLevelLottery(type, lotteryConfig.level);
                }
            }else{
                lotteryConfig = DataFactory.getInstance().getNextLevelLottery(type, lotteryConfig.level);
            }
        } else {
            if(buyType == E_LotteryType.ONCE.getCode()){
                if((lotteryData.currentNum + 1) % lotteryConfig.nextLevelCount == 0){
                    lotteryConfig = DataFactory.getInstance().getNextLevelLottery(type, lotteryConfig.level);
                }
            }else{
                LotteryConfig tenObject = DataFactory.getInstance().getNextLevelLottery(type, lotteryConfig.level);
                if(lotteryData.currentNum % (lotteryConfig.nextLevelCount * tenObject.nextLevelCount) == 0) {
                    lotteryConfig = DataFactory.getInstance().getNextLevelLottery(type, tenObject.level);
                } else {
                    lotteryConfig = tenObject;
                }
            }
        }

        if(buyType == E_LotteryType.REPEATEDLY.getCode()){



            //获取这一次应该从哪个奖池里操作
//            if((lotteryData.currentNum + 1) % lotteryConfig.nextLevelCount == 0) {
//                LotteryConfig tenObject = DataFactory.getInstance().getNextLevelLottery(type, lotteryConfig.level);
//                if(tenObject.nextLevelCount > 1 && (lotteryData.currentNum + 1) % (lotteryConfig.nextLevelCount * tenObject.nextLevelCount) == 0) {
//                    lotteryConfig = DataFactory.getInstance().getNextLevelLottery(type, tenObject.level);
//                } else {
//                    lotteryConfig = tenObject;
//                }
//            }
        }



        BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
        boolean cdTimeFlag = buyType == E_LotteryType.ONCE.getCode() && currentTime - lotteryData.lastUpdateTime >= lotteryConfig.cdTime;
        //没有免费次数检测cd时间是否未到
        if(lotteryData.freeCount <= 0 || !cdTimeFlag) {
            //检测资源是否足够
            if(bagInfo.getAsset(lotteryConfig.needAsset.id) < lotteryConfig.needAsset.value) {
                //资源不够
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ASSET_NOT_ENOUGH, lotteryConfig.needAsset.id));
                return;
            }
        }

        if(lotteryData.freeCount > 0) {
            if(!cdTimeFlag){
                bagInfo.addAsset(lotteryConfig.needAsset.id, -lotteryConfig.needAsset.value);
            }
            else
            {
                lotteryData.freeCount--;
            }
        } else {
            //扣除资源
            bagInfo.addAsset(lotteryConfig.needAsset.id, -lotteryConfig.needAsset.value);
        }

        bagInfo.addGoods(lotteryConfig.reward, true);

        SGPlayerProto.S2C_LotteryClick.Builder b = SGPlayerProto.S2C_LotteryClick.newBuilder();
        //抽奖
        if(buyType  == E_LotteryType.ONCE.getCode()){
            bagInfo.addGoods(lotteryConfig.getReward());
            //刷新次数
            lotteryData.currentNum++;
            lotteryData.lastUpdateTime = currentTime;
            initLotteryResponse(playerController, false);

        }else{
            LotteryConfig onceLotteryConfig = DataFactory.getInstance().getInitLevelLottery(lotteryConfig.type);
            //刷新次数
            lotteryData.currentNum+= 10;
            initLotteryResponse(playerController,false);
//            //循环9次获取奖励
            for (int i = 0; i < 9; i++) {
                bagInfo.addGoods(onceLotteryConfig.getReward());
            }
            bagInfo.addGoods(lotteryConfig.getReward());
        }
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Pub_LotteryClick_VALUE, b.build().toByteArray());

        //抽奖之后检测是否完成任务
//        taskInfo.checkFinish(ConstantFactory.TASK_TARAGET_LOTTERY, type == E_LotteryType.REPEATEDLY.getCode() ? 10 :E_LotteryType.ONCE.getCode());
        if(type == E_LotteryType.ONCE.getCode()){//初级抽奖
            TaskInfo taskInfo = playerController.getPlayer().getExtInfo(TaskInfo.class);
            //酒馆邂逅
            List<TaskDB> taskDBList = taskInfo.getDoingEveryTask(ConstantFactory.TASK_TARAGET_LOTTERY);
            for(TaskDB taskDB : taskDBList){
                if(taskDB.status == SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY_VALUE){
                    taskDB.check(ConstantFactory.TASK_TARAGET_LOTTERY, 1);
                }
            }
        }

    }

    /***
     * 酒馆初始化
     * @param playerController
     */
    public void initLotteryResponse(PlayerController playerController,boolean bl) {
        hasPushRedPointRemind = false;
        System.out.println("---------------------进入酒馆实例化方法------------------");

        SGPlayerProto.S2C_LotteryInit.Builder b = SGPlayerProto.S2C_LotteryInit.newBuilder();
        SingleBoonData lotteryData = null;
        LotteryConfig initLotteryConfig = null;
        //获取次数和剩余CD时间
        long currentTime = System.currentTimeMillis();
        lotteryData = lotteryMap.get(1);
        int leftFreeCount = lotteryData.freeCount;
        initLotteryConfig = DataFactory.getInstance().getGameObject(DataFactory.LOTTERY_KEY, lotteryData.id);
        int temp = lotteryData.lastUpdateTime  == 0 ? 0 : (int) (currentTime - lotteryData.lastUpdateTime);
        int leftCDTime;
        if(lotteryData.lastUpdateTime == 0){
            leftCDTime = 0;
        }else{
            leftCDTime = temp > initLotteryConfig.cdTime ? 0 : initLotteryConfig.cdTime - temp;
        }
        int leftResidueCount = initLotteryConfig.nextLevelCount - lotteryData.currentNum % initLotteryConfig.nextLevelCount;
        lotteryData = lotteryMap.get(2);
        int rightFreeCount = lotteryData.freeCount;
        initLotteryConfig = DataFactory.getInstance().getGameObject(DataFactory.LOTTERY_KEY, lotteryData.id);
        temp = lotteryData.lastUpdateTime  == 0 ? 0 : (int) (currentTime - lotteryData.lastUpdateTime);
        int rightCDTime;
        if(lotteryData.lastUpdateTime == 0){
            rightCDTime = 0;
        }else{
            rightCDTime  = temp > initLotteryConfig.cdTime ? 0 : initLotteryConfig.cdTime - temp;
        }
        int rightResidueCount = initLotteryConfig.nextLevelCount - lotteryData.currentNum % initLotteryConfig.nextLevelCount;
        LotteryConfig highLotteryConfig = DataFactory.getInstance().getNextLevelLottery(initLotteryConfig.type, initLotteryConfig.level);
        int rightResidueCount2 =  highLotteryConfig.nextLevelCount - ((lotteryData.currentNum / initLotteryConfig.nextLevelCount) % highLotteryConfig.nextLevelCount);

        b.setLeftFreeCount(leftFreeCount);
        b.setLeftCDTime(leftCDTime);
        b.setLeftResidueCount(leftResidueCount);
        b.setRightFreeCount(rightFreeCount);
        b.setRightCDTime(rightCDTime);
        b.setRightResidueCount(rightResidueCount);
        b.setRightResidueCount2(rightResidueCount2);

        System.out.println("---------------------酒馆实例化结束------------------");
        if(bl){
            playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Pub_LotteryInit_VALUE, b.build().toByteArray());
        }
    }
}
