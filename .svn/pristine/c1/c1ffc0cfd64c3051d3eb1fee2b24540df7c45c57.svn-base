package com.douqu.game.core.entity.ext.data.boon;

import com.bean.core.buffer.ByteBuffer;
import com.bean.core.util.TimeUtils;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.entity.ext.data.BaseData;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author wangzhenfei
 *         2017-10-28 16:06
 *         福利
 */
public class BonusData extends BaseData {

    //固定福利

    /**首冲礼包是否领取过**/
    private boolean isFistRechargeReceive;

    /**签到福利  签到的id**/
    private int signRewardIndex; //对应每日签到配置的下标
    private List<Integer> signRecordList;
    private long lastSignTime;
    private int   reissueTimes;//补签次数

    /**开服基金  是否购买过**/
    private boolean isBuyOpenFund;
    /**记录领取奖励的id**/
    private List<Integer> openfundReciverRecord;

    /**登录福利   登录天数**/
    private int loginTimesByDay;
    private List<Integer> loginRewardRecord;//登录奖励的领取记录



    private Player player;
    public BonusData(Player player) {
        this.player = player;
        signRecordList = new CopyOnWriteArrayList<>();
        openfundReciverRecord = new CopyOnWriteArrayList<>();
        loginRewardRecord = new CopyOnWriteArrayList<>();
    }

    @Override
    public void init() {

    }

    @Override
    public void checkInit() {
        if(!TimeUtils.isToday(player.getLastLogoutTime())){
            loginTimesByDay ++;
        }
    }

    @Override
    public void reset() {

    }

    @Override
    public void checkReset() {

    }

    public boolean hasRedPointRemind(){

        return false;
    }

    @Override
    public void writeTo(ByteBuffer buffer) {
        buffer.writeBoolean(isFistRechargeReceive);
        buffer.writeByte(signRewardIndex);

        buffer.writeShort(signRecordList.size());
        for(Integer id : signRecordList){
            buffer.writeInt(id);
        }

        buffer.writeLong(lastSignTime);
        buffer.writeShort(reissueTimes);

        buffer.writeBoolean(isBuyOpenFund);
        buffer.writeShort(loginTimesByDay);

        buffer.writeShort(loginRewardRecord.size());
        for(Integer id : loginRewardRecord){
            buffer.writeInt(id);
        }

        buffer.writeInt(0);
    }

    @Override
    public void loadFrom(ByteBuffer buffer) {
        isFistRechargeReceive = buffer.readBoolean();
        signRewardIndex = buffer.readByte();

        int size = buffer.readShort();
        for(int i = 0; i < size ; i++){
            signRecordList.add(buffer.readInt());
        }
        lastSignTime = buffer.readLong();
        reissueTimes = buffer.readShort();
        isBuyOpenFund = buffer.readBoolean();
        loginTimesByDay = buffer.readShort();

         size = buffer.readShort();
        for(int i = 0; i < size ; i++){
            loginRewardRecord.add(buffer.readInt());
        }
        buffer.readInt();
    }


    /**
     * 获取最大的签到天数
     * @return
     */
    public int getMaxSignDay(){
        return signRecordList.size() == 0 ? 0 : signRecordList.get(signRecordList.size() - 1);
    }

    /**
     * 增加签到记录
     * @param day
     * @param timeNow
     */
    public void addSignRecord(int day, long timeNow){
        if(!signRecordList.contains(day)){
            signRecordList.add(day);
            this.lastSignTime = timeNow;
        }
    }

    /**
     * 今天是否签到过了
     * @return
     */
    public boolean isSignToday(){
        return  TimeUtils.isToday(lastSignTime);
    }

    /**
     * 首冲奖励是否领取过
     * @return
     */
    public boolean isFistRechargeReceive() {
        return isFistRechargeReceive;
    }

    /**
     * 设置首冲奖励已经领取
     * @param isFistRechargeReceive
     */
    public void setFistRechargeReceive(boolean isFistRechargeReceive) {
        this.isFistRechargeReceive = isFistRechargeReceive;
    }

    /**
     * 是否购买了开服基金
     * @return
     */
    public boolean isBuyOpenFund() {
        return isBuyOpenFund;
    }

    /**
     *  设置开服基金已购买
     */
    public void buyOpenFund(){
        isBuyOpenFund = true;
        openfundReciverRecord.clear();
    }

    /**
     * 增加开服基金领取记录
     * @param rewardId
     */
    public void addOpenFundRewardRecord(int rewardId){
        if(!openfundReciverRecord.contains(rewardId)){
            openfundReciverRecord.add(rewardId);
        }
    }

    /**
     * 开服基金奖励是否领取
     * @param rewardId
     * @return
     */
    public boolean isOpenFundRewardReceive(int rewardId){
        return openfundReciverRecord.contains(rewardId);
    }

    /**
     * 累计登陆奖励是否领取
     * @param rewardId
     * @return
     */
    public boolean isLoginTimesRewardReceive(int rewardId){
        return loginRewardRecord.contains(rewardId);
    }

    /**
     * 累计登陆奖励是否领取
     * @param rewardId
     * @return
     */
    public void addLoginTimesRewardReceive(int rewardId){
        if(!loginRewardRecord.contains(rewardId)){
            loginRewardRecord.add(rewardId);
        }
    }


    public int getLoginTimesByDay() {
        return loginTimesByDay;
    }

    public List<Integer> getLoginRewardRecord() {
        return loginRewardRecord;
    }

    /**
     *  获取已经补签的次数
     * @return
     */
    public int getReissueTimes() {
        return reissueTimes;
    }

    /**
     *  增加补签的次数
     * @return
     */
    public int addReissueTimes() {
        return ++reissueTimes;
    }

    /**
     *  获取每日签到奖励对应配置表的下标
     * @return
     */
    public int getSignRewardIndex() {
        return signRewardIndex;
    }


    public List<Integer> getSignRecordList() {
        return signRecordList;
    }

    public List<Integer> getOpenfundReciverRecord() {
        return openfundReciverRecord;
    }
}
