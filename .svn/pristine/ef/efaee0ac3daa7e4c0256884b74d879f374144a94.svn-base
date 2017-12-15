package com.douqu.game.core.entity;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.bean.core.buffer.ByteBuffer;
import com.bean.core.util.HttpJsonUtils;
import com.bean.core.util.TimeUtils;
import com.douqu.game.core.config.VipConfig;
import com.douqu.game.core.config.card.CardConfig;
import com.douqu.game.core.config.sprite.MasterConfig;
import com.douqu.game.core.controller.PlayerController;
import com.douqu.game.core.database.model.PlayerModel;
//import com.douqu.game.core.entity.Grade;
import com.douqu.game.core.entity.common.TwoTuple;
import com.douqu.game.core.entity.db.CardDB;
import com.douqu.game.core.entity.db.TaskDB;
import com.douqu.game.core.entity.ext.*;
import com.douqu.game.core.entity.ext.data.boon.*;
import com.douqu.game.core.entity.ext.data.card.CardData;
import com.douqu.game.core.entity.ext.data.challenge.ArenaData;
import com.douqu.game.core.entity.ext.data.challenge.HeroTempleData;
import com.douqu.game.core.entity.ext.data.challenge.InstanceData;
import com.douqu.game.core.entity.ext.data.challenge.OfficialRankData;
import com.douqu.game.core.entity.db.LvDB;
import com.douqu.game.core.factory.ConfigFactory;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.util.SendUtils;

import java.util.*;

/**
 * Created by bean on 2017/7/14.
 */
public class Player extends GameObject {

    public String avatar;

    public int camp;

    public int master;

    public boolean isDel;

    /**
     * 总战力
     */
    public int fc;

    /** RMB冲值的对应币 */
    public int money;

    private int uid;

    private String account;

    public Integer channel;

    private Date lastLoginTime;

    private Date lastLogoutTime;

    private Map<String, ExtInfo> extInfos;


    /**
     * 成长盒子
     */
    private LvDB lvDB;

    private Integer vipLevel;

    private Integer vipExp;


    private ChangeGoods changeGoods;


    private WorldInfo worldInfo;

    /**
     * 无敌状态(只在开发版本有用)
     */
    public boolean invincible;

    /**
     * 锁住
     */
    public boolean lock;

    /**
     * 获取离线玩家数据时用
     * @param playerModel
     */
    public Player(PlayerModel playerModel)
    {
        definition();

        initExtInfo(new WorldInfo());

        load(playerModel);
    }

    /**
     * 注册时用
     * @param worldInfo
     */
    public Player(WorldInfo worldInfo)
    {
        definition();

        initExtInfo(worldInfo);

        init();
    }

    /**
     * 登录时用
     * @param worldInfo
     * @param playerModel
     */
    public Player(WorldInfo worldInfo, PlayerModel playerModel)
    {
        definition();

        initExtInfo(worldInfo);

        load(playerModel);
    }


    private void initExtInfo(WorldInfo worldInfo)
    {
        this.worldInfo = worldInfo;

        addExtInfo(new BagInfo(this, worldInfo));
        addExtInfo(new ChallengeInfo(this, worldInfo));
        addExtInfo(new BoonInfo(this, worldInfo));
        addExtInfo(new TaskInfo(this,worldInfo));
        addExtInfo(new SettingInfo(this,worldInfo));
    }


    private void definition()
    {
        lvDB = new LvDB(this);

        changeGoods = new ChangeGoods();

        extInfos = new HashMap<>();
    }

    public void clearAll()
    {
        lvDB.reset();
        vipLevel = 1;
        vipExp = 0;

        extInfos.clear();

        initExtInfo(worldInfo);

        init();
    }

    public void init()
    {
        for(String key : extInfos.keySet())
        {
            extInfos.get(key).init();
        }

        updateFC();
    }

    public void checkInit()
    {
        for(String key : extInfos.keySet())
        {
            extInfos.get(key).checkInit();
        }
    }

    public void reset()
    {
        for(String key : extInfos.keySet())
        {
            extInfos.get(key).reset();
        }

        updateFC();
    }

    public void checkReset()
    {
//        for(String key : extInfos.keySet())
//        {
//            extInfos.get(key).checkReset();
//        }

        if(isNewDay())
        {
            reset();
        }
    }


    private void load(PlayerModel playerModel)
    {
        if(playerModel == null)
            return;

        this.id = playerModel.getId();
        this.objectIndex = playerModel.getObjectIndex();
        this.avatar = playerModel.getAvatar();
        this.uid = playerModel.getUid();
        this.name = playerModel.getName();
        this.account = playerModel.getAccount();
        this.channel = playerModel.getChannel();
        this.camp = playerModel.getCamp();
        this.master = playerModel.getMaster();
        this.fc = playerModel.getFc();
        this.money = playerModel.getMoney();
        this.lastLoginTime = playerModel.getLastLoginTime();
        this.lastLogoutTime = playerModel.getLastLogoutTime();
        this.vipExp = playerModel.getVipExp();
        this.vipLevel = playerModel.getVipLevel();

        if(lvDB == null) lvDB = new LvDB(this);
        lvDB.lv = playerModel.getLevel();
        lvDB.lv = lvDB.lv == 0 ? 1 : lvDB.lv;
        lvDB.exp = playerModel.getExp();

        if(DataFactory.getInstance().getGameObject(DataFactory.MASTER_KEY, this.master) == null){
            List<MasterConfig> masterConfigs = DataFactory.getInstance().getDataList(DataFactory.MASTER_KEY);
            for(MasterConfig masterConfig : masterConfigs)
            {
                if(masterConfig.camp == this.camp)
                {
                    this.master = masterConfig.id;
                    break;
                }
            }
        }

        //这里加载数据库的数据
        byte[] byteData = playerModel.getBagInfo();
        if(byteData != null)
        {
            getExtInfo(BagInfo.class).loadFrom(new ByteBuffer(byteData));
        }

        byteData = playerModel.getChallengeInfo();
        if(byteData != null)
        {
            getExtInfo(ChallengeInfo.class).loadFrom(new ByteBuffer(byteData));
        }

        byteData = playerModel.getBoonInfo();
        if(byteData != null)
        {
            getExtInfo(BoonInfo.class).loadFrom(new ByteBuffer(byteData));
        }

        byteData = playerModel.getTaskInfo();
        if(byteData != null)
        {
            getExtInfo(TaskInfo.class).loadFrom(new ByteBuffer(byteData));
        }

        byteData = playerModel.getSettingInfo();
        if(byteData != null)
        {
            getExtInfo(SettingInfo.class).loadFrom(new ByteBuffer(byteData));
        }

        checkInit();

        checkReset();

        updateFC();
    }

    public PlayerModel save()
    {
        PlayerModel playerModel = new PlayerModel();
        playerModel.setId(id);
        playerModel.setName(name);
        playerModel.setObjectIndex(objectIndex);
        playerModel.setAvatar(avatar);
        playerModel.setCamp(camp);
        playerModel.setLevel(lvDB.lv);
        playerModel.setExp(lvDB.exp);
        playerModel.setVipLevel(vipLevel);
        playerModel.setVipExp(vipExp);
        playerModel.setMaster(master);
        playerModel.setMoney(money);
        playerModel.setUid(uid);
        playerModel.setFc(fc);
        playerModel.setIsDel(isDel);
        playerModel.setLastLoginTime(lastLoginTime);
        playerModel.setLastLogoutTime(lastLogoutTime);

        //复制给PlayerModel
        ByteBuffer buffer = new ByteBuffer();
        getExtInfo(BagInfo.class).writeTo(buffer);
        playerModel.setBagInfo(buffer.getBytes());


        buffer = new ByteBuffer();
        getExtInfo(ChallengeInfo.class).writeTo(buffer);
        playerModel.setChallengeInfo(buffer.getBytes());

        buffer = new ByteBuffer();
        getExtInfo(TaskInfo.class).writeTo(buffer);
        playerModel.setTaskInfo(buffer.getBytes());

        buffer = new ByteBuffer();
        getExtInfo(BoonInfo.class).writeTo(buffer);
        playerModel.setBoonInfo(buffer.getBytes());

        buffer = new ByteBuffer();
        getExtInfo(SettingInfo.class).writeTo(buffer);
        playerModel.setSettingInfo(buffer.getBytes());

        return playerModel;
    }


    public String getChannel()
    {
        if(StringUtils.isEmpty(account))
            return "";

        return account.indexOf("_") == -1 ? account : account.substring(0, account.indexOf("_"));
    }





    public void cacheRedPoint(SGCommonProto.E_RED_POINT_TYPE e_red_point_type){
        if(!changeGoods.getRedPointTypes().contains(e_red_point_type)){
            changeGoods.getRedPointTypes().add(e_red_point_type);
        }
    }

    public void sendReadPoint(PlayerController playerController){
        if(changeGoods.getRedPointTypes().size() == 0)
            return;

        SendUtils.sendRedPointRemind(playerController, changeGoods.getRedPointTypes());
        changeGoods.getRedPointTypes().clear();
    }

    /**
     *  检测添加物品条件的红点
     * @param type
     * @param id
     */
    public void checkRedPointRemind(SGCommonProto.E_GOODS_TYPE type, int id){
        for(String key : extInfos.keySet())
        {
            extInfos.get(key).checkRedPointRemindAddGoods(type, id);
        }
    }

    /**
     * 检测所有条件的红点,一般在登录304返回
     * @return
     */
    public List<SGCommonProto.E_RED_POINT_TYPE> getAllRedPointTypes(long currentTime){
        List<SGCommonProto.E_RED_POINT_TYPE> list = new ArrayList<>();
        List<SGCommonProto.E_RED_POINT_TYPE> temp = null;
        list.addAll(changeGoods.getRedPointTypes());
        for(String key : extInfos.keySet())
        {
            temp = extInfos.get(key).checkRedPointRemindAllCondition(currentTime);
            if(temp != null){
                list.addAll(temp);
            }
        }

        changeGoods.getRedPointTypes().clear();
        return list;
    }


    public boolean isNewDay()
    {
        if(lastLogoutTime != null)
        {
            return !TimeUtils.isToday(lastLogoutTime);
        }

        return true;
    }

    public RechargeRecordData getRechargeRecordData()
    {
        BoonInfo boonInfo =  getExtInfo(BoonInfo.class);
        return boonInfo.getRechargeRecordData();
    }

    public ArenaData getArenaData()
    {
        ChallengeInfo challengeInfo =  getExtInfo(ChallengeInfo.class);
        return challengeInfo.getArenaData();
    }

    public OfficialRankData getOfficialData()
    {
        ChallengeInfo challengeInfo = getExtInfo(ChallengeInfo.class);
        return challengeInfo.getOfficialData();
    }

    public InstanceData getInstanceData()
    {
        ChallengeInfo challengeInfo = getExtInfo(ChallengeInfo.class);
        return challengeInfo.getInstanceData();
    }

    public HeroTempleData getHeroTempleData()
    {
        ChallengeInfo challengeInfo = getExtInfo(ChallengeInfo.class);
        return challengeInfo.getHeroTempleData();
    }

    public AltarData getAltarData()
    {
        BoonInfo boonInfo = getExtInfo(BoonInfo.class);
        return boonInfo.getAltarData();
    }

    public LotteryData getLotteryData()
    {
        BoonInfo boonInfo = getExtInfo(BoonInfo.class);
        return boonInfo.getLotteryData();
    }

    public RechargeRecordData getRechargeData()
    {
        BoonInfo boonInfo = getExtInfo(BoonInfo.class);
        return boonInfo.getRechargeRecordData();
    }

    public BonusData getBonusData()
    {
        BoonInfo boonInfo = getExtInfo(BoonInfo.class);
        return boonInfo.getBonusData();
    }

    public CardData getCardData(){
        BagInfo bagInfo = getExtInfo(BagInfo.class);
        return bagInfo.getCardData();
    }

//    /**
//     * 添加星星
//     * @param value
//     */
//    public void addStar(int value)
//    {
//        if(value == 0)
//            return;
//
//        star += value;
//        star = star < 0 ? 0 : star;
//
//        Grade gradeObj = null;
//        if(value > 0)
//        {
//            DataFactory dataFactory = DataFactory.getInstance();
//            for(int i = grade; i <= dataFactory.MAX_GRADE; i++)
//            {
//                gradeObj = dataFactory.getGameObject(DataFactory.GRADE_KEY, i);
//                if(gradeObj != null)
//                {
//                    if(star > gradeObj.star)
//                    {
//                        //升级
//                        grade = i;
//                    }
//                }
//            }
//        }
//        else
//        {
//            if(grade == 1)
//                return;
//
//            for(int i = grade; i > 0; i--)
//            {
//                gradeObj = DataFactory.getInstance().getGameObject(DataFactory.GRADE_KEY, i);
//                if(gradeObj != null)
//                {
//                    if(star < gradeObj.star)
//                    {
//                        //降级
//                        grade = i;
//                    }
//                }
//            }
//        }
//    }





    /**
     * 更新总战力
     * 玩家当前使用的组卡内所有单位的战斗力 + 主将战斗力
     * @return
     */
    public int updateFC()
    {
        BagInfo bagInfo = getExtInfo(BagInfo.class);

        int result = 0;
        List<CardDB> cards = bagInfo.getCardData().getBattleCardList();
        for(CardDB card : cards)
        {
            result += card.updateFC();
        }

        MasterConfig masterConfigObj = DataFactory.getInstance().getGameObject(DataFactory.MASTER_KEY, master);
        if(masterConfigObj == null)
        {
            masterConfigObj = (MasterConfig) DataFactory.getInstance().getDataList(DataFactory.MASTER_KEY).get(0);
            master = masterConfigObj.id;
        }
        result += masterConfigObj.getFC(getLv());

        this.fc = result;

        TaskInfo taskInfo = getExtInfo(TaskInfo.class);
        List<TaskDB> doingList = taskInfo.getDoingTask(ConstantFactory.TASK_TARAGET_BATTLE_FC);
        for(TaskDB taskDB : doingList) {
            if(fc > taskDB.currentDemand) {
                taskDB.check(ConstantFactory.TASK_TARAGET_BATTLE_FC, fc);
            }
        }

        return result;
    }



    /**
     * 添加钻石(元宝)
     * @param value
     */
    public void addMoney(int value)
    {
        if(value == 0)
            return;

        BagInfo bagInfo = getExtInfo(BagInfo.class);
        bagInfo.addAsset(ConfigFactory.ASSET_MONEY_KEY, value);
    }


    /**
     * 增加vip经验
     * @return 增加经验后的vip等级
     */
    public int addVipExp(int vipExp){
        this.vipExp += vipExp;
        int vipLvBefore = vipLevel;
        List<VipConfig> vipConfigs = DataFactory.getInstance().getDataList(DataFactory.VIP_KEY);
        int size = vipConfigs.size();
        for(int i = 1; i< size; i++){
            if(this.vipExp < vipConfigs.get(i).needRecharge){
                this.vipLevel = vipConfigs.get(i - 1).id;
                break;
            }
        }
        if(vipLvBefore != vipLevel){//vip level up
            vipLvUp(vipLvBefore);
        }
        return vipLevel;
    }

    /**
     * vip升级了
     */
    private void vipLvUp(int vipLvBefore) {
        //祭坛补上免费差x
        for(Map.Entry<Integer,SingleBoonData> entry : getAltarData().altarMap.entrySet()){
            entry.getValue().vipLevelUp(vipLvBefore);
        }
    }


    public TwoTuple<Boolean, Boolean> addExp(int value)
    {
        int beforeLevel = getLv();

        lvDB.addExp(value);

        int afterLevel = getLv();
        if(beforeLevel != afterLevel){//升级了
            boolean has = levelUp(beforeLevel);

            TaskInfo taskInfo = getExtInfo(TaskInfo.class);
            List<TaskDB> doingList = taskInfo.getDoingTask(ConstantFactory.TASK_TARAGET_MAINUPLV);
            for(TaskDB taskDB : doingList) {
                if(afterLevel > taskDB.currentDemand) {
                    taskDB.check(ConstantFactory.TASK_TARAGET_MAINUPLV, afterLevel);
                }
            }
            return new TwoTuple<>(true, has);
        } else {
          return new TwoTuple<>(false, false);
        }
    }

    /**
     * 主将升级
     * @param beforeLevel
     * @return
     */
    private boolean levelUp(int beforeLevel) {
        SettingInfo settingInfo = getExtInfo(SettingInfo.class);
        boolean has = settingInfo.levelUpToNewUnlockSkill(beforeLevel);
        if(settingInfo.levelUpToNewUnlockSkill(beforeLevel)){
            changeGoods.getRedPointTypes().add(SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_MASTER);
        }

        BagInfo bagInfo = getExtInfo(BagInfo.class);
        if(bagInfo.getCardData().checkCorps()){
            changeGoods.getRedPointTypes().add(SGCommonProto.E_RED_POINT_TYPE.RED_POINT_TYPE_LEGION);
        }
        
        //通知认证服务器等级改变
        toUpdateAuthServerLevelChange();

        return has;
    }

    private void toUpdateAuthServerLevelChange() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("channel", channel);
        try {
            jsonObject.put("account", getAccount().split("_")[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        jsonObject.put("level", getLv());

        jsonObject.put("serverId", worldInfo.getServerId());


        JSONObject result = HttpJsonUtils.httpPost(ConfigFactory.AUTH_SERVER_HTTP_BASE + "player/updateRecord", jsonObject);
        if(result == null){
            System.err.println("请求认证服务器验证名称合法性出错：");
            return;
        }
        System.out.println("Play.class 请求认证服务器名字升级结果：" + result.toString());
    }


    /**
     * 获取资源数量
     * @param key
     * @return
     */
    public int getAsset(int key)
    {
        BagInfo bagInfo = getExtInfo(BagInfo.class);
        return bagInfo.getAsset(key);
    }


    public int getLv()
    {
        return lvDB.lv;
    }

    public int getExp()
    {
        return lvDB.exp;
    }



    public void addExtInfo(ExtInfo extInfo)
    {
        if(extInfos == null)
            extInfos = new HashMap<String, ExtInfo>();

        extInfos.put(extInfo.getName(), extInfo);
    }


    public ChangeGoods getChangeGoods() {
        return changeGoods;
    }

    public void addChangeGoods(SGCommonProto.E_GOODS_TYPE goodsType, int id, int change, int curValue, Object... hide)
    {
        changeGoods.addGoods(goodsType, id, change, curValue, hide);
    }



    public <T extends ExtInfo> T getExtInfo(Class<T> cls)
    {
        return (T) extInfos.get(cls.getSimpleName());
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getLastLogoutTime() {
        return lastLogoutTime;
    }

    public void setLastLogoutTime(Date lastLogoutTime) {
        this.lastLogoutTime = lastLogoutTime;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getVipLevel() {
        return vipLevel == null ? 0 : vipLevel.intValue();
    }

    public void setVipLevel(Integer vipLevel) {
        this.vipLevel = vipLevel;
    }

    public Integer getVipExp() {
        return vipExp == null ? 0 : vipExp.intValue();
    }

    public void setVipExp(Integer vipExp) {
        this.vipExp = vipExp;
    }



    @Override
    public String toString() {
        return "Player{" +
                "camp=" + camp +
                ", money=" + money +
                ", lv=" + getLv() +
                "} " + super.toString();
    }
}
