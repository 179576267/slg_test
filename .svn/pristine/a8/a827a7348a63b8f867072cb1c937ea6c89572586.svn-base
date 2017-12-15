package com.douqu.game.battle.entity.tmp;

import com.douqu.game.battle.controller.BattleController;
import com.douqu.game.battle.controller.sprite.MonsterController;
import com.douqu.game.battle.controller.sprite.PlayerController;
import com.douqu.game.battle.controller.sprite.SpriteController;
import com.douqu.game.battle.entity.BattleSkill;
import com.douqu.game.battle.entity.aobject.BattleAObject;
import com.douqu.game.battle.entity.aobject.BattleSoldierAObject;
import com.douqu.game.core.e.E_BattleAObjectStatus;
import com.douqu.game.core.config.InitDataConfig;
import com.douqu.game.core.config.battle.MonsterConfig;
import com.douqu.game.core.config.common.Position;
import com.douqu.game.core.config.common.CommonData;
import com.douqu.game.core.entity.db.CardDB;
import com.douqu.game.core.entity.ext.BagInfo;
import com.douqu.game.core.config.card.CardConfig;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.protobuf.SGMainProto;
import com.douqu.game.core.protobuf.SGWarProto;
import com.douqu.game.core.util.Utils;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 精灵战斗数据类
 * Created by bean on 2017/7/25.
 */
public class SpriteBattleTmp implements Serializable {

    protected static final long serialVersionUID = 1L;

    Logger logger = Logger.getLogger(this.getClass());

    protected BattleController battleController;

    protected SpriteController spriteController;

    /**
     * 对手
     */
    protected SpriteBattleTmp targetBattleTmp;


    /**
     * 卡牌列表
     */
    protected List<CardDB> cardList;

    protected Map<Integer, CardDB> cardMap;

    /**
     * 历史卡牌
     */
    protected List<CardDB> historyCards;

    /**
     * 队伍编号
     */
    protected int teamNo;

    /**
     * 主将
     */
    public BattleAObject masterSoldier;

    /**
     * 召唤出来的兵
     */
    protected List<BattleAObject> soldierList;

    protected List<BattleAObject> historySoldiers;

    /**
     * 准备
     */
    protected boolean ready;


    /**
     * 当前水晶值
     */
    protected int curCrystal;

    /**
     * 最大水晶值
     */
    protected int maxCrystal;

    /**
     * 水晶成长
     * key: 战斗运行时间
     */
    protected Map<Integer, CrystalGrow> crystalGrowMap;

    /**
     * 是否是自动
     */
    protected boolean isAuto;

    /**
     * 公共CD
     */
    protected int publicCDTime;

    /**
     * 出卡的公共CD
     */
    protected int publicCardCDTime;

    /**
     * 杀死敌人数量
     */
    public int killCount;


    public void init(BattleController battleController, SpriteController spriteController, BattleAObject masterSoldier, int teamNo, SGCommonProto.E_BATTLE_TYPE battleType)
    {
        InitDataConfig initDataConfig = DataFactory.getInstance().getInitDataConfig();
        this.curCrystal = initDataConfig.curCrystal;
        this.maxCrystal = initDataConfig.maxCrystal;
        int start = 0;
        crystalGrowMap = new ConcurrentHashMap<>();
        for(CommonData data : DataFactory.getInstance().getInitDataConfig().crystalGrow)
        {
            for(int i = start + data.value; i <= data.id; i+=data.value)
            {
                crystalGrowMap.put(i, new CrystalGrow());

//                logger.info("i:" + i);
            }

            start = data.id;
        }

        this.battleController = battleController;
        this.spriteController = spriteController;
        this.masterSoldier = masterSoldier;
        this.teamNo = teamNo;
        this.soldierList = new CopyOnWriteArrayList<>();
        this.historySoldiers = new CopyOnWriteArrayList<>();

        if(spriteController instanceof PlayerController)
        {
            PlayerController playerController = (PlayerController) spriteController;
            BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);

            cardList = historyCards = bagInfo.getCardData().getBattleCardList();
            cardMap = new ConcurrentHashMap<>();
            for(CardDB cardDB : cardList)
            {
                cardMap.put(cardDB.id, cardDB);
            }

            //打乱顺序
            Collections.shuffle(cardList);

            isAuto = !playerController.isOnlinePlayer();
        }
        else if(spriteController instanceof MonsterController)
        {
            MonsterController monsterController = (MonsterController) spriteController;

            cardList = new CopyOnWriteArrayList<>();
            MonsterConfig monsterConfig = monsterController.getMonsterConfig();
            CardDB cardDB = null;
            for(int i = 0; i < 20; i++)
            {
                for(CardConfig cardConfig : monsterConfig.cards)
                {
                    cardDB = new CardDB(cardConfig.id);
                    cardDB.setMonsterData(monsterController.getMonsterLv());
                    cardList.add(cardDB);
                }
            }

            historyCards = cardList;

            cardMap = new ConcurrentHashMap<>();
            for(CardDB card : cardList)
            {
                cardMap.put(card.id, card);
            }

            //打乱顺序
            Collections.shuffle(cardList);
            isAuto = true;
        }

        publicCardCDTime = BattleController.PUBLIC_CARD_CD_TIME;
    }

    /**
     *
     * @param currentTime 当前时间
     * @param battleTime 当前战斗持续时间
     */
    public void update(long currentTime, int battleTime)
    {
        if(publicCDTime > 0)
        {
            publicCDTime -= ConstantFactory.UPDATE_BATTLE_TIME;
            publicCDTime = publicCDTime < 0 ? 0 : publicCDTime;
        }

        if(publicCardCDTime > 0)
        {
            publicCardCDTime -= ConstantFactory.UPDATE_BATTLE_TIME;
            publicCardCDTime = publicCardCDTime < 0 ? 0 : publicCardCDTime;
        }

        if(!isMaxCrystal())
        {
//            logger.debug(spriteController.getName() + " 计算水晶成长，当前战斗运行时间:" + battleTime);
            if(crystalGrowMap.get(battleTime) != null)
            {
                if(!crystalGrowMap.get(battleTime).added)
                {
                    curCrystal++;
                }
                crystalGrowMap.remove(battleTime);
            }
//            logger.debug("当前水晶："+curCrystal);
        }

        if(isAuto)
        {
            if(battleController.isEnd())
                return;
            //出牌AI
            if(checkAI(currentTime))
                return;
        }

        //检测主将攻击
        masterSoldier.checkDefaultSkillTarget(currentTime);

        if(battleController.isEnd())
            return;

        masterSoldier.update(currentTime);

        if(battleController.isEnd())
            return;

        //小兵前进
        for(BattleAObject aObject : soldierList)
        {
            if(aObject.isDie())
                continue;

//                logger.debug("soldier:" + soldier);
            if(aObject.getStatus() == E_BattleAObjectStatus.GOING)
            {
//                    logger.debug(soldier.getTeamNo() + "  " + soldier.getId()+"  向前走");
                aObject.go();
            }
        }

        if(battleController.isEnd())
            return;

        //检测小兵攻击
        for(BattleAObject soldier : soldierList)
        {
            if(soldier.isDie())
                continue;

            soldier.checkDefaultSkillTarget(currentTime);
            if(battleController.isEnd())
                return;
            soldier.update(currentTime);
            if(battleController.isEnd())
                return;
        }



    }


    public void addCrystal(int value)
    {
        curCrystal += value;
        curCrystal = curCrystal < 0 ? 0 : curCrystal;
        curCrystal = curCrystal > maxCrystal ? maxCrystal : curCrystal;
    }


    public boolean checkAI(long currentTime)
    {
        if(!isAuto)
        {
            logger.debug("你是玩家你不是AI!");
            return false;
        }

//        if(soldierList.size() == 0)
//        {
//           // 测试时只出一个兵
//            return false;
//        }

        BattleSkill skill = masterSoldier.getReleaseSkill(currentTime);
        //技能是否可以释放
        if(skill != null && skill.checkCrystal(curCrystal))
        {
            skill.release();
        }

        if(curCrystal > 0 && publicCardCDTime == 0)
        {
            //对手场上的兵
            List<BattleAObject> list = targetBattleTmp.getSoldierList();
            CardDB result = null;
            int route = 0;
            if(list.size() > 0)
            {
                //两路路线上,对方的兵数-我方的兵数,取小的那和边放
                //我方最高品质里检测对对方的兵的克制关系,若有则出该卡,有多个情况则随机
                List<CardDB> tempList = new ArrayList<>();
                tempList.addAll(cardList);
                Collections.sort(tempList, new Comparator<CardDB>() {
                    @Override
                    public int compare(CardDB o1, CardDB o2) {
                        return o2.getConfig().quality - o1.getConfig().quality;//这样表示从大到小排列
                    }
                });

                choose:for(CardDB cardDB : tempList)
                {
                    for(BattleAObject soldier : targetBattleTmp.soldierList)
                    {
                        if(cardDB.getConfig().soldier.isRestrict(soldier.getSprite()))
                        {
                            result = cardDB;
                            break choose;
                        }
                    }
                }
                if(result != null)
                {
                    int topCount = targetBattleTmp.getRouteSoldierCount(ConstantFactory.ROUTE_TOP) - getRouteSoldierCount(ConstantFactory.ROUTE_TOP);
                    int botCount = targetBattleTmp.getRouteSoldierCount(ConstantFactory.ROUTE_BOTTOM) - getRouteSoldierCount(ConstantFactory.ROUTE_TOP);
                    if(topCount < botCount)
                    {
                        route = ConstantFactory.ROUTE_BOTTOM;
                    }
                }
            }
            else
            {
                //我方出最高品质高最等级的卡,相同则随机,路线随机(上下两条路)
                if(cardList.size() > 0)
                {
                    CardDB maxQualityCard = Utils.getMaxGameObject(cardList, new Comparator<CardDB>() {
                        @Override
                        public int compare(CardDB o1, CardDB o2) {
                            return new Integer(o1.getConfig().quality).compareTo(o2.getConfig().quality);
                        }
                    });
                    List<CardDB> tempList = new CopyOnWriteArrayList<>();
                    for(CardDB cardDB : cardList)
                    {
                        if(cardDB.getLv() == maxQualityCard.getLv())
                        {
                            tempList.add(cardDB);
                        }
                    }
                    result = tempList.get((int) (Math.random() * tempList.size()));
                    route = (int) (Math.random() * 1) == 1 ? ConstantFactory.ROUTE_TOP : ConstantFactory.ROUTE_BOTTOM;
                }
            }

            if(result != null)
            {
                logger.info("AI出兵结果:" + result);
                soldierBorn(result, route);
                publicCardCDTime = BattleController.PUBLIC_CARD_CD_TIME;
                return true;
            }
        }

        return false;
    }


    public void soldierBorn(CardDB cardDB, int route)
    {
        List<Position> posList = new ArrayList<>();
        CardConfig cardConfig = cardDB.getConfig();
        for(int i = 0; i < cardConfig.soldierCount; i++)
        {
            posList.add(battleController.getSoldierPos(teamNo, cardConfig.soldier.initRange, route, cardConfig.soldier.gridArea));
        }

        soldierBorn(cardDB, posList);
    }
    /**
     * 小兵出生
     */
    public void soldierBorn(CardDB cardDB, List<Position> posList)
    {
        CardConfig cardConfig = cardDB.getConfig();
        if(posList != null && posList.size() < cardConfig.soldierCount)
            return;

        addCrystal(-cardConfig.crystal);
        toBattlefield(cardDB);

        BattleAObject battleSoldierAObject = null;
        SGCommonProto.BattleUnit.Builder soldierData = null;

        //转发消息
        SGWarProto.S2C_ToBattle.Builder response = SGWarProto.S2C_ToBattle.newBuilder();
        String objectIndex = getSpriteObjectIndex();
        if(objectIndex != null)
            response.setPlayerIndex(objectIndex);
        response.setCardId(cardConfig.getId());

        List<BattleAObject> sendList = new CopyOnWriteArrayList<>();

        logger.info("出战 -> 队伍:" + teamNo);

        for(int i = 0; i < cardConfig.soldierCount; i++)
        {
            battleSoldierAObject = new BattleSoldierAObject(spriteController, cardDB,
                    battleController.getSoldierUniqueId(), teamNo, posList==null?battleController.getSoldierPos(teamNo, cardConfig.soldier.initRange, 0, cardConfig.soldier.gridArea):posList.get(i));

            addBattleSoldier(battleSoldierAObject);

            soldierData = SGCommonProto.BattleUnit.newBuilder();
            soldierData.setBattleUnit(SGCommonProto.E_BATTLE_UNIT.BATTLE_UNIT_SOLDIER);
            soldierData.setUniqueId(battleSoldierAObject.getUniqueId());
            soldierData.setObjectId(cardConfig.soldier.getId());
            soldierData.setMaxHP(battleSoldierAObject.getMaxHp());
            soldierData.setPos(battleSoldierAObject.getPosition().parsePos());
            soldierData.setTeamNo(battleSoldierAObject.getTeamNo());
            response.addSoldier(soldierData);

            sendList.add(battleSoldierAObject);

//            logger.info("unique:" + battleSoldierAObject.getId() + " id:" + battleSoldierAObject.getSprite().id + " name:" + battleSoldierAObject.getName() + " position:" + battleSoldierAObject.getPosition());
        }

        battleController.dispatchMsg(SGMainProto.E_MSG_ID.MsgID_War_ToBattle_VALUE, response.build().toByteArray());

        for(BattleAObject battleAObject : sendList)
        {
            battleAObject.checkBornSkill();
        }

        int targetSoldierSize = targetBattleTmp.getSoldierList().size();
        if(targetSoldierSize == 0)
        {
            BattleAObject targetMaster = targetBattleTmp.getMasterSoldier();
            for(BattleAObject battleAObject : sendList)
            {
                battleAObject.checkRoute(targetMaster);
            }
        }
    }



    public String getSpriteObjectIndex()
    {
        return null;
    }

    public boolean isMaxCrystal()
    {
        return curCrystal >= maxCrystal;
    }




    public void sendMsg(int code, byte[] data)
    {
        if(spriteController != null)
            spriteController.sendMsg(code, data);
    }


    /**
     * 获取战斗卡牌
     * @return
     */
    public CardDB getCardById(int id)
    {
        return cardMap.get(id);
    }

    /**
     * 上战场
     */
    public void toBattlefield(CardDB cardDB)
    {
        cardList.remove(cardDB);
        cardMap.remove(cardDB.id);
    }


    /**
     * 是否死亡
     * @return
     */
    public boolean isDie()
    {
        return masterSoldier.isDie();
    }

    public int getRouteSoldierCount(int route)
    {
        int count = 0;
        for(BattleAObject battleAObject : soldierList)
        {
            if(battleAObject.bornRoute == route)
                count++;
        }
        return count;
    }


    /**
     * 由于客户端和服务器是各自计算的,所以要检测一下是否在临界点附近
     * 只有在出战的时候水晶不够才检测
     * @return
     */
    public boolean isAllowToBattleByCrystal(int need)
    {
        if(need > 1)
            return false;

//        logger.info("crystalGrowMap:" + crystalGrowMap);
        int battleTime = battleController.getBattleTime();
        CrystalGrow crystalGrow = crystalGrowMap.get(battleTime+500);
//        logger.info("1battleTime:" + battleTime + "   crystalGrow:" + crystalGrow);
        if(crystalGrow != null)
        {
            logger.info("还差500毫秒才增长水晶,先加上");
            curCrystal++;
            crystalGrow.added = true;
            return true;
        }

        crystalGrow = crystalGrowMap.get(battleTime+1000);
//        logger.info("2battleTime:" + battleTime + "   crystalGrow:" + crystalGrow);
        if(crystalGrow != null)
        {
            logger.info("还差1000毫秒才增长水晶,先加上");
            curCrystal++;
            crystalGrow.added = true;
            return true;
        }

        return false;
    }


    public int getPublicCDTime()
    {
        return publicCDTime;
    }

    public void setPublicCDTime(int time)
    {
        this.publicCDTime = time;
    }


    public void addBattleSoldier(BattleAObject battleAObject)
    {
        soldierList.add(battleAObject);
        historySoldiers.add(battleAObject);

        battleController.addSoldier(battleAObject);
    }



    public void removeSoldier(BattleAObject soldier)
    {
        soldierList.remove(soldier);
    }

    public BattleController getBattleController() {
        return battleController;
    }

    public void setBattleController(BattleController battleController) {
        this.battleController = battleController;
    }

    public int getTeamNo() {
        return teamNo;
    }

    public SpriteController getSpriteController() {
        return spriteController;
    }

    public List<BattleAObject> getSoldierList() {
        return soldierList;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public int getCurCrystal() {
        return curCrystal;
    }

    public int getMaxCrystal() {
        return maxCrystal;
    }

    public SpriteBattleTmp getTargetBattleTmp() {
        return targetBattleTmp;
    }

    public void setTargetBattleTmp(SpriteBattleTmp targetBattleTmp) {
        this.targetBattleTmp = targetBattleTmp;
    }

    public BattleAObject getMasterSoldier() {
        return masterSoldier;
    }

    public List<CardDB> getCardList() {
        return cardList;
    }

    public boolean isAuto() {
        return isAuto;
    }

    public void setAuto(boolean isAuto) {
        this.isAuto = isAuto;
    }

    public List<CardDB> getHistoryCards() {
        return historyCards;
    }

    public Map<Integer, CrystalGrow> getCrystalGrowMap() {
        return crystalGrowMap;
    }

    public List<BattleAObject> getHistorySoldiers() {
        return historySoldiers;
    }

    public void setHistorySoldiers(List<BattleAObject> historySoldiers) {
        this.historySoldiers = historySoldiers;
    }

    class CrystalGrow
    {
        public int value;

        public boolean added;

        public CrystalGrow()
        {
            this.value = 1;
            this.added = false;
        }

    }


    @Override
    public String toString() {
        return "{" +
                ", teamNo=" + teamNo +
                ", masterSoldier=" + masterSoldier +
                ", soldierList=" + soldierList +
                ", curCrystal=" + curCrystal +
                ", maxCrystal=" + maxCrystal +
                ", isAuto=" + isAuto +
                '}';
    }


}
