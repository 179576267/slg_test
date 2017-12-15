package com.douqu.game.core.entity.ext;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.config.task.ActiveBoxConfig;
import com.douqu.game.core.config.task.CaptureCityBoxConfig;
import com.douqu.game.core.config.task.GrowUpBoxConfig;
import com.douqu.game.core.config.task.TaskConfig;
import com.douqu.game.core.controller.PlayerController;
import com.douqu.game.core.entity.*;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.entity.db.CardDB;
import com.douqu.game.core.entity.db.EquipDB;
import com.douqu.game.core.entity.db.TaskDB;
import com.douqu.game.core.entity.ext.data.boon.SingleBoonData;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.factory.WordFactory;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.protobuf.SGMainProto;
import com.douqu.game.core.protobuf.SGTaskProto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author: Gavin.
 * Description:
 * Date: 2017/10/24 0024 下午 7:20
 * Huan Yu Copyright (c) 2017 All Rights Reserved.
 */
public class TaskInfo extends ExtInfo {

    /**当前成长值*/
    public int currentGrowthValue;

    /**每日活跃值*/
    public  int everyActiveValue;

    /**每周活跃值*/
    public  int weekActiveValue;

    /**玩家进行中的主线任务
     * */
    public Map<Integer,TaskDB> underwayMainTaskMap;

    /**玩家已完成的主线任务*/
    public List<Integer> doneMainTasks;

    /**玩家进行中的支线任务
     * */
    public Map<Integer,TaskDB> underwayBranchTaskMap;

    /**玩家已完成的支线任务*/
    public List<Integer> doneBranchTasks;

    /**日常任务*/
    public Map<Integer,TaskDB> everyDayTaskMap;

    /**攻城任务*/
    public Map<Integer,TaskDB> siegeMissionTaskMap;

    /**主线已领取宝箱*/
    public List<Integer> mainBox;

    /**日常已领取宝箱*/
    public List<Integer> everyDayBox;

    /**攻城任务已领取宝箱*/
    public List<Integer> siegeMissionBox;




    public TaskInfo(Player player, WorldInfo worldInfo) {
        super(player, worldInfo);

        underwayMainTaskMap = new ConcurrentHashMap<>();
        doneMainTasks = new CopyOnWriteArrayList<>();
        underwayBranchTaskMap = new ConcurrentHashMap<>();
        doneBranchTasks = new CopyOnWriteArrayList<>();
        everyDayTaskMap = new ConcurrentHashMap<>();
        siegeMissionTaskMap = new ConcurrentHashMap<>();
        mainBox = new CopyOnWriteArrayList<>();
        everyDayBox = new CopyOnWriteArrayList<>();
        siegeMissionBox = new CopyOnWriteArrayList<>();
    }


    @Override
    public void init() {
        taskCheck(0);
    }

    @Override
    public void reset(){
        everyDayBox = new CopyOnWriteArrayList<>();
//        siegeMissionBox= new CopyOnWriteArrayList<>();
        everyActiveValue = 0;
        everyDayTaskMap = new ConcurrentHashMap<>();
    }

    @Override
    public void checkReset(){
        // 每日的已领奖列表要重置，每日活跃要重置，每日任务要重置
//        everyDayBox = new CopyOnWriteArrayList<>();
//        everyActiveValue = 0;
//       everyDayTaskMap = new ConcurrentHashMap<>();
        this.reset();
        everyRefresh(0);

    }

    @Override
    public void checkInit(){
        if(underwayMainTaskMap.size() == 0){
            taskCheck(0);
        }
    }

    @Override
    public void writeTo(ByteBuffer buffer){
        buffer.writeByte(underwayMainTaskMap.size());
        for(Integer key : underwayMainTaskMap.keySet()) {
            underwayMainTaskMap.get(key).writeTo(buffer);
        }

        buffer.writeShort(doneMainTasks.size());
        for (int i = 0; i < doneMainTasks.size(); i++){
            buffer.writeInt(doneMainTasks.get(i).intValue());
        }

        buffer.writeByte(underwayBranchTaskMap.size());
        for(Integer key : underwayBranchTaskMap.keySet()) {
            underwayBranchTaskMap.get(key).writeTo(buffer);
        }

        buffer.writeShort(doneBranchTasks.size());
        for (int i = 0; i < doneBranchTasks.size(); i++){
            buffer.writeInt(doneBranchTasks.get(i).intValue());
        }

        buffer.writeByte(everyDayTaskMap.size());
        for(Integer key : everyDayTaskMap.keySet()){
            everyDayTaskMap.get(key).writeTo(buffer);
        }

        buffer.writeByte(siegeMissionTaskMap.size());
        for (Integer key: siegeMissionTaskMap.keySet()){
            siegeMissionTaskMap.get(key).writeTo(buffer);
        }

        buffer.writeByte(mainBox.size());
        for (int i = 0; i < mainBox.size(); i++){
            buffer.writeInt(mainBox.get(i).intValue());
        }

        buffer.writeByte(everyDayBox.size());
        for (int i = 0; i < everyDayBox.size(); i++){
            buffer.writeInt(everyDayBox.get(i).intValue());
        }

        buffer.writeByte(siegeMissionBox.size());
        for (int i = 0; i < siegeMissionBox.size(); i++){
            buffer.writeInt(siegeMissionBox.get(i).intValue());
        }



        buffer.writeShort(currentGrowthValue);
        buffer.writeShort(everyActiveValue);
        buffer.writeShort(weekActiveValue);


    }

    @Override
    public void loadFrom(ByteBuffer buffer) {

        //[1, 17, 39, 0, 0, 0, 0, 1, 17, 39, 0, 0]
        int size = buffer.readByte();
        TaskDB taskDB = null;
        for (int i = 0;i < size; i++){
            taskDB = new TaskDB();
            taskDB.loadFrom(buffer);
            if(taskDB.getConfig() == null)
            {
                try {
                    throw new Exception("Load TaskInfo underwayMainTaskMap Task is Null -> " + taskDB.id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
                underwayMainTaskMap.put(taskDB.id, taskDB);
        }

        size = buffer.readShort();
        int taskId = 0;
        for(int i = 0; i < size; i++){
            taskId = buffer.readInt();
            if(DataFactory.getInstance().getGameObject(DataFactory.TASK_KEY, taskId) == null)
            {
                try {
                    throw new Exception("Load TaskInfo doneMainTasks Task is Null -> " + taskId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
                doneMainTasks.add(taskId);
        }

        size = buffer.readByte();
        for (int i = 0;i < size; i++){
            taskDB = new TaskDB();
            taskDB.loadFrom(buffer);
            if(taskDB.getConfig() == null)
            {
                try {
                    throw new Exception("Load TaskInfo underwayBranchTaskMap Task is Null -> " + taskDB.id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
                underwayBranchTaskMap.put(taskDB.id, taskDB);
        }

        size = buffer.readShort();
        for(int i = 0; i < size; i++){
            taskId = buffer.readInt();
            if(DataFactory.getInstance().getGameObject(DataFactory.TASK_KEY, taskId) == null)
            {
                try {
                    throw new Exception("Load TaskInfo doneBranchTasks Task is Null -> " + taskId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
                doneBranchTasks.add(taskId);
        }

        size = buffer.readByte();
        for (int i = 0;i < size; i++){
            taskDB = new TaskDB();
            taskDB.loadFrom(buffer);
            if(taskDB.getConfig() == null)
            {
                try {
                    throw new Exception("Load TaskInfo everyDayTaskMap Task is Null -> " + taskDB.id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
                everyDayTaskMap.put(taskDB.id, taskDB);
        }

        size = buffer.readByte();
        for (int i = 0;i < size; i++){
            taskDB = new TaskDB();
            taskDB.loadFrom(buffer);
            if(DataFactory.getInstance().getGameObject(DataFactory.TASK_KEY, taskDB.id) == null)
            {
                try {
                    throw new Exception("Load TaskInfo siegeMissionTaskMap Task is Null -> " + taskDB.id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
                siegeMissionTaskMap.put(taskDB.id, taskDB);
        }

        size = buffer.readByte();
        for(int i = 0; i < size; i++){
            mainBox.add(buffer.readInt());
        }

        size = buffer.readByte();
        for(int i = 0; i < size; i++){
            everyDayBox.add(buffer.readInt());
        }

        size = buffer.readByte();
        for(int i = 0; i < size; i++){
            siegeMissionBox.add(buffer.readInt());
        }

        currentGrowthValue = buffer.readShort();
        everyActiveValue = buffer.readShort();
        weekActiveValue = buffer.readShort();
    }

    public void checkFinish(int type, int... extValue) {

        int count = extValue != null && extValue.length > 0 ? extValue[0] : 0;
        //主线任务
        for(Map.Entry<Integer,TaskDB> entry : underwayMainTaskMap.entrySet()){
            entry.getValue().check(type, count);
        }
        //支线任务
        for(Map.Entry<Integer,TaskDB> entry : underwayBranchTaskMap.entrySet()){
            entry.getValue().check(type, count);
        }
        //日常任务
        for(Map.Entry<Integer,TaskDB> entry : everyDayTaskMap.entrySet()){
            entry.getValue().check(type, count);
        }
        //攻城任务
        for(Map.Entry<Integer,TaskDB> entry : siegeMissionTaskMap.entrySet()){
            entry.getValue().check(type, count);
        }
    }


    public void taskCheck(int taskId){
        List<TaskConfig> list = DataFactory.getInstance().getDataList(DataFactory.TASK_KEY);
        if(list != null){
            for(TaskConfig taskConfig : list){
                //主线任务
                if(taskConfig.type == SGCommonProto.E_TASK_TYPE.TASK_TYPE_MAIN_LINE_VALUE){
                    if(taskConfig.takeGrade <= player.getLv() && taskConfig.beforeTask == taskId){
                        if(!doneMainTasks.contains(ConstantFactory.MAIN_TASK_INIT) && underwayMainTaskMap.get(ConstantFactory.MAIN_TASK_INIT) == null){
                            underwayMainTaskMap.put(taskConfig.id,new TaskDB(taskConfig.id));
                        }
                    }
                }
                //支线任务
                if(taskConfig.type == SGCommonProto.E_TASK_TYPE.TASK_TYPE_BRANCH_LINE_VALUE){
                    //支线任务判断条件 任务配置等级小于等于当前玩家等级 并且前置任务为0 直接下发
                    if(taskConfig.takeGrade <= player.getLv() && taskConfig.beforeTask == taskId){
                        if((!doneBranchTasks.contains(ConstantFactory.BRANCH_TASK_INIT_HERO_UP_LV)  && underwayMainTaskMap.get(ConstantFactory.BRANCH_TASK_INIT_HERO_UP_LV) == null) &&
                                (!doneBranchTasks.contains(ConstantFactory.BRANCH_TASK_INIT_BATTLE)  && underwayMainTaskMap.get(ConstantFactory.BRANCH_TASK_INIT_BATTLE) == null ) &&
                                (!doneBranchTasks.contains(ConstantFactory.BRANCH_TASK_INIT_BATTLE_UP)  && underwayMainTaskMap.get(ConstantFactory.BRANCH_TASK_INIT_BATTLE_UP) == null ) &&
                                (!doneBranchTasks.contains(ConstantFactory.BRANCH_TASK_INIT_CARD_COUNT)  && underwayMainTaskMap.get(ConstantFactory.BRANCH_TASK_INIT_CARD_COUNT) == null ) &&
                                (!doneBranchTasks.contains(ConstantFactory.BRANCH_TASK_INIT_GUBLIAN_STORE)  && underwayMainTaskMap.get(ConstantFactory.BRANCH_TASK_INIT_GUBLIAN_STORE) == null ) &&
                                (!doneBranchTasks.contains(ConstantFactory.BRANCH_TASK_INIT_REBORN)  && underwayMainTaskMap.get(ConstantFactory.BRANCH_TASK_INIT_REBORN) == null ) &&
                                (!doneBranchTasks.contains(ConstantFactory.BRANCH_TASK_INIT_EQUIP_INTENSIFY)  && underwayMainTaskMap.get(ConstantFactory.BRANCH_TASK_INIT_EQUIP_INTENSIFY) == null )){
                            underwayBranchTaskMap.put(taskConfig.id,new TaskDB(taskConfig.id));
                        }
                    }
                }
                //日常任务
                if(taskConfig.type == SGCommonProto.E_TASK_TYPE.TASK_TYPE_EVERYDAY_VALUE){
                    if(taskConfig.takeGrade <= player.getLv()  && taskConfig.beforeTask == taskId){
                        if(everyDayTaskMap.get(taskConfig.id) == null){
                            everyDayTaskMap.put(taskConfig.id,new TaskDB(taskConfig.id));
                        }
                    }
                }
                //攻城任务
//                if(taskConfig.type == SGCommonProto.E_TASK_TYPE.TASK_TYPE_SIEGEMISSION_VALUE){
//                    if(taskConfig.takeGrade <= player.getLv() && taskConfig.beforeTask == taskId){
//                        siegeMissionTaskMap.put(taskConfig.id,new TaskDB(taskConfig.id));
//                    }
//                }
            }
        }

    }

    public void everyRefresh(int taskId){
        List<TaskConfig> list = DataFactory.getInstance().getDataList(DataFactory.TASK_KEY);
        if(list != null){
            for(TaskConfig taskConfig : list){
                //日常任务
                if(taskConfig.type == SGCommonProto.E_TASK_TYPE.TASK_TYPE_EVERYDAY_VALUE){
                    if(taskConfig.takeGrade <= player.getLv()  && taskConfig.beforeTask == taskId){
                        if(everyDayTaskMap.get(taskConfig.id) == null){
                            everyDayTaskMap.put(taskConfig.id,new TaskDB(taskConfig.id));
                        }
                    }
                }
            }
        }
    }

    /***
     * 进行中的任务封装在集合中
     * @param type
     * @return
     */
    public List<TaskDB> getDoingTask(int type) {
        List<TaskDB> list = new CopyOnWriteArrayList<>();
        TaskConfig taskConfig = null;
        //主线
        for(Map.Entry<Integer,TaskDB> entry : underwayMainTaskMap.entrySet()){
            taskConfig = DataFactory.getInstance().getGameObject(DataFactory.TASK_KEY,entry.getValue().id);
            if(taskConfig.target == type){
                list.add(entry.getValue());
            }
        }
        //支线
        for(Map.Entry<Integer,TaskDB> entry : underwayBranchTaskMap.entrySet()){
            taskConfig = DataFactory.getInstance().getGameObject(DataFactory.TASK_KEY,entry.getValue().id);
            if(taskConfig.target == type){
                list.add(entry.getValue());
            }
        }
        //日常
//        for(Map.Entry<Integer,TaskData> entry : everyDayTaskMap.entrySet()){
//            task = DataFactory.getInstance().getGameObject(DataFactory.TASK_KEY,entry.getValue().id);
//            if(task.target == type){
//                list.add(entry.getValue());
//            }
//        }

        return list;
    }

    public List<TaskDB> getDoingEveryTask(int type){
        TaskConfig taskConfig = null;
        List<TaskDB> list = new CopyOnWriteArrayList<>();
        for(Map.Entry<Integer,TaskDB> entry : everyDayTaskMap.entrySet()){
            taskConfig = DataFactory.getInstance().getGameObject(DataFactory.TASK_KEY,entry.getValue().id);
            if(taskConfig.target == type){
                list.add(entry.getValue());
            }
        }
        return list;
    }

    public List<TaskDB> getDoingSiegeMissionTask(int type){
        TaskConfig taskConfig = null;
        List<TaskDB> list = new CopyOnWriteArrayList<>();
        for(Map.Entry<Integer,TaskDB> entry : siegeMissionTaskMap.entrySet()){
            taskConfig = DataFactory.getInstance().getGameObject(DataFactory.TASK_KEY,entry.getValue().id);
            if(taskConfig.target == type){
                list.add(entry.getValue());
            }
        }
        return list;
    }


//    public void checkEquipIntensify(int changeValue){
//        List<TaskData> list = getDoingTask(ConstantFactory.TASK_TARAGET_EQUIPINTENSIFY);
//        for(TaskData taskData : list) {
//            taskData.check(ConstantFactory.TASK_TARAGET_EQUIPINTENSIFY, changeValue);
//        }
//    }

    /**
     *
     * 任务列表显示
     *
     *
     * */
    public void taskList(PlayerController playerController,SGCommonProto.E_TASK_TYPE type ){

        SGTaskProto.S2C_TaskList.Builder b = SGTaskProto.S2C_TaskList.newBuilder();
        b.setType(type);

        TaskConfig taskConfig = null;
        switch (type.getNumber()){
            case  SGCommonProto.E_TASK_TYPE.TASK_TYPE_MAIN_LINE_VALUE: //主线任务
                b.setGrowUpValue(currentGrowthValue);
                for(Map.Entry<Integer,TaskDB> entry : underwayMainTaskMap.entrySet()){
                    TaskDB taskDB = entry.getValue();
                    b.addTaskDetail(buildTaskDetail(taskDB));
                }
                for(Map.Entry<Integer,TaskDB> entry : underwayBranchTaskMap.entrySet()){
                    TaskDB taskDB = entry.getValue();
                    b.addTaskDetail(buildTaskDetail(taskDB));
                }
                for(int i = 0 ; i < mainBox.size(); i++){
                    b.addRewardId(mainBox.get(i).intValue());
                }
                break;
            case SGCommonProto.E_TASK_TYPE.TASK_TYPE_EVERYDAY_VALUE://日常任务是活跃值
                b.setGrowUpValue(everyActiveValue);

                for(Map.Entry<Integer,TaskDB> entry : everyDayTaskMap.entrySet()){
                    TaskDB taskDB = entry.getValue();
                    b.addTaskDetail(buildTaskDetail(taskDB));
                }
                for(int i = 0 ; i < everyDayBox.size(); i++){
                    b.addRewardId(everyDayBox.get(i).intValue());
                }
                break;
//            case SGCommonProto.E_TASK_TYPE.TASK_TYPE_SIEGEMISSION_VALUE://攻城任务
//                b.setGrowUpValue(weekActiveValue);
//
//                for(Map.Entry<Integer,TaskDB> entry : siegeMissionTaskMap.entrySet()){
//                    TaskDB taskDB = entry.getValue();
//                    b.addTaskDetail(buildTaskDetail(taskDB));
//                }
//                for(int i = 0 ; i < siegeMissionBox.size(); i++){
//                    b.addRewardId(siegeMissionBox.get(i).intValue());
//                }
//                break;
            default:
                break;
        }
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Task_TaskList_VALUE, b.build().toByteArray());

    }


    public SGCommonProto.TaskDetail.Builder buildTaskDetail(TaskDB taskDB){
        SGCommonProto.TaskDetail.Builder td = SGCommonProto.TaskDetail.newBuilder();
        td.setTaskId(taskDB.id);
        td.setCurrentDemand(taskDB.currentDemand > taskDB.getConfig().showNum ? taskDB.getConfig().showNum:taskDB.currentDemand);
        td.setStatus(SGCommonProto.E_TASK_STATUS.forNumber(taskDB.status));
       return td;
    }

    /**
     * 任务领取奖励
     * */
    public void  taskReward(PlayerController playerController, int taskId){

        SGTaskProto.S2C_TaskReward.Builder b = SGTaskProto.S2C_TaskReward.newBuilder();
        TaskConfig taskConfig = DataFactory.getInstance().getGameObject(DataFactory.TASK_KEY,taskId);
        TaskDB taskDB = null;

        switch (taskConfig.type){
            case SGCommonProto.E_TASK_TYPE.TASK_TYPE_EVERYDAY_VALUE:
                taskDB = everyDayTaskMap.get(taskConfig.getId());
                if(taskDB == null){
                    //当前任务不在进行中
                    playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CURRENT_TASK_IS_NULL,taskId));
                    return;
                }
                if(taskDB.status == SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNCLAIMED_VALUE){
                    b.setTaskId(taskId);
                    everyActiveValue += taskConfig.activeValue;
                    //检测活跃值是否有可领取活跃宝箱，发送红底
                    checkEveryActiveValueSendRedPoint();
                    b.setCurrentActiveValue(everyActiveValue);
                    SGCommonProto.TaskDetail.Builder td = SGCommonProto.TaskDetail.newBuilder();
                    td.setTaskId(taskId);
                    td.setCurrentDemand(taskDB.currentDemand);
                    td.setStatus(SGCommonProto.E_TASK_STATUS.TASK_STATUS_ACQUIRE);
                    b.addTaskDetail(td);
                    //日常任务中标记已完成任务
                    everyDayTaskMap.put(taskConfig.id,new TaskDB(taskConfig.id, taskDB.currentDemand, SGCommonProto.E_TASK_STATUS.TASK_STATUS_ACQUIRE_VALUE));
                }else{
                    //未达到领取奖励的条件
                    playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.FAILED_TO_MEET_THE_AWARD_CONDITIONS,taskId));
                    return;
                }
                break;
            case SGCommonProto.E_TASK_TYPE.TASK_TYPE_BRANCH_LINE_VALUE:
                taskDB = underwayBranchTaskMap.get(taskConfig.getId());
                if(taskDB == null){
                    //当前任务不在进行中
                    playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CURRENT_TASK_IS_NULL,taskId));
                    return;
                }
                if(taskDB.status == SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNCLAIMED_VALUE){
                    b.setTaskId(taskId);
                    currentGrowthValue += taskConfig.growthValue;
                    checkGrowValueSendRedPoint();
                    b.setCurrentGrowthValue(currentGrowthValue);
                }else{
                    //未达到领取奖励的条件
                    playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.FAILED_TO_MEET_THE_AWARD_CONDITIONS,taskId));
                    return;
                }
                //根据类型在对应的map移除
                underwayBranchTaskMap.remove(taskConfig.getId());
                doneBranchTasks.add(taskConfig.getId());
                break;
            case  SGCommonProto.E_TASK_TYPE.TASK_TYPE_MAIN_LINE_VALUE:
                taskDB = underwayMainTaskMap.get(taskConfig.getId());
                if(taskDB == null){
                    //当前任务不在进行中
                    playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CURRENT_TASK_IS_NULL,taskId));
                    return;
                }
                if(taskDB.status == SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNCLAIMED_VALUE){
                    b.setTaskId(taskId);
                    currentGrowthValue += taskConfig.growthValue;
                    checkGrowValueSendRedPoint();
                    b.setCurrentGrowthValue(currentGrowthValue);
                }else{
                    //未达到领取奖励的条件
                    playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.FAILED_TO_MEET_THE_AWARD_CONDITIONS,taskId));
                    return;
                }
                underwayMainTaskMap.remove(taskConfig.getId());
                doneMainTasks.add(taskConfig.getId());
                break;
        /*    case SGCommonProto.E_TASK_TYPE.TASK_TYPE_SIEGEMISSION_VALUE:
                taskDB = siegeMissionTaskMap.get(taskConfig.getId());
                if(taskDB == null){
                    //当前任务不在进行中
                    playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CURRENT_TASK_IS_NULL,taskId));
                    return;
                }

                if(taskDB.status == SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNCLAIMED_VALUE){
                    b.setTaskId(taskId);
                    weekActiveValue += taskConfig.activeValue;
                    //检测活跃值是否有可领取活跃宝箱，发送红底
                    checkEveryActiveValueSendRedPoint();
                    b.setCurrentActiveValue(weekActiveValue);
                    //日常任务中标记已完成任务
                    siegeMissionTaskMap.put(taskConfig.id,new TaskDB(taskConfig.id, taskDB.currentDemand, SGCommonProto.E_TASK_STATUS.TASK_STATUS_ACQUIRE_VALUE));
                }else{
                    //未达到领取奖励的条件
                    playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.FAILED_TO_MEET_THE_AWARD_CONDITIONS,taskId));
                    return;
                }
                break;*/
            default:
                break;
        }
        if(taskConfig.type != SGCommonProto.E_TASK_TYPE.TASK_TYPE_EVERYDAY_VALUE){
            //任务完成后下发任务
            checkSendTasks(taskConfig, b);
        }

        BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
        for (int i = 0 ; i < taskConfig.assets.length; i++){
            bagInfo.addAsset(taskConfig.assets[i].id, taskConfig.assets[i].value);
        }
        for (int i = 0 ; i < taskConfig.props.length; i++){
            bagInfo.addProp(taskConfig.props[i].id, taskConfig.props[i].value);
        }
        for (int i = 0 ; i < taskConfig.cards.length; i++){
            bagInfo.addCard(taskConfig.cards[i].id, taskConfig.cards[i].value);
        }

        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Task_TaskReward_VALUE, b.build().toByteArray());

    }

    /**
     * 检测支线任务
     */
    public void checkSendTasks(TaskConfig taskConfig,SGTaskProto.S2C_TaskReward.Builder b){
        BagInfo bagInfo = player.getExtInfo(BagInfo.class);

        SGCommonProto.TaskDetail.Builder td = SGCommonProto.TaskDetail.newBuilder();


        //主线任务和支线任务列表 添加
        if(taskConfig.type == SGCommonProto.E_TASK_TYPE.TASK_TYPE_MAIN_LINE_VALUE){


            if(taskConfig.afterTask > 0 && underwayMainTaskMap.get(taskConfig.afterTask) == null && !doneMainTasks.contains(taskConfig.afterTask)){

                taskConfig = DataFactory.getInstance().getGameObject(DataFactory.TASK_KEY,taskConfig.afterTask);
                if(taskConfig == null ){
                    return;
                }
                td.setTaskId(taskConfig.id);
                //下发单个任务的时候检测 进行中和已完成的任务列表中是否已存在
                if(taskConfig.target == ConstantFactory.TASK_TARAGET_PASSLEVEL){
                    if(player.getInstanceData().levelHasPass(taskConfig.demand[0])){
                        underwayMainTaskMap.put(taskConfig.id,new TaskDB(taskConfig.id,taskConfig.demand[0],SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNCLAIMED_VALUE));
                        td.setCurrentDemand(taskConfig.showNum);
                        td.setStatus(SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNCLAIMED);
                    }else{
                        underwayMainTaskMap.put(taskConfig.id,new TaskDB(taskConfig.id,SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY_VALUE));
                        td.setCurrentDemand(0);
                        td.setStatus(SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY);
                    }
                }else{
                    underwayMainTaskMap.put(taskConfig.id,new TaskDB(taskConfig.id,SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY_VALUE));
                    td.setCurrentDemand(0);
                    td.setStatus(SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY);
                }
            }
        }
        //支线任务
        if(taskConfig.type == SGCommonProto.E_TASK_TYPE.TASK_TYPE_BRANCH_LINE_VALUE){


            if(taskConfig.afterTask > 0 && underwayBranchTaskMap.get(taskConfig.afterTask) == null || !doneBranchTasks.contains(taskConfig.afterTask)){


                taskConfig = DataFactory.getInstance().getGameObject(DataFactory.TASK_KEY,taskConfig.afterTask);
                if(taskConfig == null || taskConfig.afterTask == 0){
                    return;
                }

                td.setTaskId(taskConfig.id);
                //支线任务检测主将等级
                if(taskConfig.target == ConstantFactory.TASK_TARAGET_MAINUPLV){
                    if(player.getLv() >= taskConfig.demand[0]){
                        underwayBranchTaskMap.put(taskConfig.id,new TaskDB(taskConfig.id,player.getLv(),SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNCLAIMED_VALUE));
                        td.setCurrentDemand(taskConfig.showNum);
                        td.setStatus(SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNCLAIMED);
                    }else{
                        underwayBranchTaskMap.put(taskConfig.id,new TaskDB(taskConfig.id,SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY_VALUE));
                        td.setCurrentDemand(0);
                        td.setStatus(SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY);
                    }
                }//竞技场获胜次数，需要调用此人打的总竞技场次数
                else if(taskConfig.target == ConstantFactory.TASK_TARAGET_ARENA){
                    if(player.getArenaData().getTotalWinCount() > taskConfig.demand[0]){
                        underwayBranchTaskMap.put(taskConfig.id,new TaskDB(taskConfig.id,player.getArenaData().getTotalWinCount(),SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNCLAIMED_VALUE));
                        td.setCurrentDemand(taskConfig.showNum);
                        td.setStatus(SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNCLAIMED);
                    }else{
                        underwayBranchTaskMap.put(taskConfig.id,new TaskDB(taskConfig.id,SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY_VALUE));
                        td.setCurrentDemand(0);
                        td.setStatus(SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY);
                    }
                }//检查战斗力
                else if(taskConfig.target == ConstantFactory.TASK_TARAGET_BATTLE_FC){
                    if(player.fc > taskConfig.demand[0]){
                        underwayBranchTaskMap.put(taskConfig.id,new TaskDB(taskConfig.id,player.fc,SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNCLAIMED_VALUE));
                        td.setCurrentDemand(taskConfig.showNum);
                        td.setStatus(SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNCLAIMED);
                    }else{
                        underwayBranchTaskMap.put(taskConfig.id,new TaskDB(taskConfig.id,SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY_VALUE));
                        td.setCurrentDemand(0);
                        td.setStatus(SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY);
                    }
                }//检查拥有的卡牌 数量
                else if(taskConfig.target == ConstantFactory.TASK_TARAGET_CARD){

                    if((bagInfo.getCardData().getCardMap() ==  null ?  0 :   bagInfo.getCardData().getCardMap().size()) > taskConfig.demand[0]){
                        underwayBranchTaskMap.put(taskConfig.id,new TaskDB(taskConfig.id, bagInfo.getCardData().getCardMap().size(),SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNCLAIMED_VALUE));
                        td.setCurrentDemand(taskConfig.showNum);
                        td.setStatus(SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNCLAIMED);
                    }else{
                        underwayBranchTaskMap.put(taskConfig.id,new TaskDB(taskConfig.id,SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY_VALUE));
                        td.setCurrentDemand(0);
                        td.setStatus(SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY);
                    }
                }//检查xxx件装备提升到xxx级
                else if(taskConfig.target == ConstantFactory.TASK_TARAGET_EQUIPINTENSIFY){
                    if(bagInfo.getCardData().getEquipLvCount(taskConfig.demand[1]) > taskConfig.demand[0] ){
                        underwayBranchTaskMap.put(taskConfig.id,new TaskDB(taskConfig.id,bagInfo.getCardData().getEquipLvCount(taskConfig.demand[1]),SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNCLAIMED_VALUE));
                        td.setCurrentDemand(taskConfig.showNum);
                        td.setStatus(SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNCLAIMED);
                    }else{
                        underwayBranchTaskMap.put(taskConfig.id,new TaskDB(taskConfig.id,SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY_VALUE));
                        td.setCurrentDemand(0);
                        td.setStatus(SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY);
                    }
                }
            }
        }
        b.addTaskDetail(td);

    }


    /**
     * 检测活跃值是否触发红点
     */
    private boolean checkEveryActiveValueSendRedPoint() {
        boolean has = false;
        List<ActiveBoxConfig> growUpBoxs = DataFactory.getInstance().getDataList(DataFactory.ACTIVE_BOX_KEY);
        for(ActiveBoxConfig box : growUpBoxs){
            if(box.activeValue <= everyActiveValue && !everyDayBox.contains(box.getId())){
                has = true;
                player.cacheRedPoint(SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_TASK);
                break;
            }
        }
        return has;
    }

    /**
     * 检测成长值是否触发红点
     */
    private boolean checkGrowValueSendRedPoint() {
        boolean has = false;
        List<GrowUpBoxConfig> growUpBoxConfigs = DataFactory.getInstance().getDataList(DataFactory.GROW_UP_BOX_KEY);
        for(GrowUpBoxConfig box : growUpBoxConfigs){
            if(box.growthValue <= currentGrowthValue && !mainBox.contains(box.getId())){
                has = true;
                player.cacheRedPoint(SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_TASK);
                break;
            }
        }
        return has;
    }

    @Override
    public List<SGCommonProto.E_RED_POINT_TYPE> checkRedPointRemindAllCondition(long currentTime) {
        if(checkEveryActiveValueSendRedPoint() || checkGrowValueSendRedPoint()){
            List<SGCommonProto.E_RED_POINT_TYPE> list = new ArrayList<>();
            list.add(SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_TASK);
            return list;
        }
        return super.checkRedPointRemindAllCondition(currentTime);
    }


    /***
     * 宝箱领取奖励
     * @param playerController
     * @param type
     * @param treasureId
     */
    public void treasureReward(PlayerController playerController, int type , int treasureId){

        GrowUpBoxConfig growUpBoxConfig = null;
        ActiveBoxConfig activeBoxConfig = null;
        CaptureCityBoxConfig captureCityBoxConfig = null;
        SGTaskProto.S2C_TreasureReward.Builder b = SGTaskProto.S2C_TreasureReward.newBuilder();

        switch (type){
            //日常任务
            case SGCommonProto.E_TASK_TYPE.TASK_TYPE_EVERYDAY_VALUE:
                activeBoxConfig = DataFactory.getInstance().getGameObject(DataFactory.ACTIVE_BOX_KEY,treasureId);
                if(boxExist(everyDayBox, treasureId)){
                    //此宝箱已经领取过
                    playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.TREASURE_CHEST,treasureId));
                    return;
                }
                if(everyActiveValue >= activeBoxConfig.activeValue){
                    everyDayBox.add(treasureId);
                }
                break;
            //主线任务
            case SGCommonProto.E_TASK_TYPE.TASK_TYPE_MAIN_LINE_VALUE:
                growUpBoxConfig = DataFactory.getInstance().getGameObject(DataFactory.GROW_UP_BOX_KEY,treasureId);
                if (boxExist(mainBox, treasureId)){
                    //此宝箱已经领取过
                    playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.TREASURE_CHEST, treasureId));
                    return;
                }
                if(currentGrowthValue >= growUpBoxConfig.growthValue ){
                    mainBox.add(treasureId);
                }
                break;
            //攻城任务
            case SGCommonProto.E_TASK_TYPE.TASK_TYPE_SIEGEMISSION_VALUE:
                captureCityBoxConfig = DataFactory.getInstance().getGameObject(DataFactory.CAPTURECITY_DATA_KEY,treasureId);
                if (boxExist(siegeMissionBox, treasureId)){
                    //此宝箱已经领取过
                    playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.TREASURE_CHEST, treasureId));
                    return;
                }
                if(weekActiveValue >= captureCityBoxConfig.num ){
                    siegeMissionBox.add(treasureId);
                }
                break;
            default:
                break;

        }
        getReward(type, growUpBoxConfig, activeBoxConfig, playerController);
        b.setTypeValue(type);
        b.setTreasureId(treasureId);
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Task_TreasureReward_VALUE, b.build().toByteArray());
    }

    public void getReward(int type ,GrowUpBoxConfig growUpBoxConfig, ActiveBoxConfig activeBoxConfig ,PlayerController playerController){
        BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);

        switch (type){
            case SGCommonProto.E_TASK_TYPE.TASK_TYPE_EVERYDAY_VALUE:
                for (int i = 0 ; i < activeBoxConfig.assets.length; i++){
                    bagInfo.addAsset(activeBoxConfig.assets[i].id, activeBoxConfig.assets[i].value);
                }
                for (int i = 0 ; i < activeBoxConfig.props.length; i++){
                    bagInfo.addProp(activeBoxConfig.props[i].id, activeBoxConfig.props[i].value);
                }
                for (int i = 0 ; i < activeBoxConfig.cards.length; i++){
                    bagInfo.addCard(activeBoxConfig.cards[i].id, activeBoxConfig.cards[i].value);
                }
                break;
            case SGCommonProto.E_TASK_TYPE.TASK_TYPE_MAIN_LINE_VALUE:
                for (int i = 0 ; i < growUpBoxConfig.assets.length; i++){
                    bagInfo.addAsset(growUpBoxConfig.assets[i].id, growUpBoxConfig.assets[i].value);
                }
                for (int i = 0 ; i < growUpBoxConfig.props.length; i++){
                    bagInfo.addProp(growUpBoxConfig.props[i].id, growUpBoxConfig.props[i].value);
                }
                for (int i = 0 ; i < growUpBoxConfig.cards.length; i++){
                    bagInfo.addCard(growUpBoxConfig.cards[i].id, growUpBoxConfig.cards[i].value);
                }
                break;
            default:
                break;
        }
    }

    /**检测此宝箱id是否已领奖*/
    public boolean boxExist(List<Integer> box ,int treasureId){
        boolean fl = false;
        for(int i = 0 ; i < box.size() ; i ++)
            if (treasureId == box.get(i)) {
                fl = true;
                break;
            }
        return fl;
    }



}
