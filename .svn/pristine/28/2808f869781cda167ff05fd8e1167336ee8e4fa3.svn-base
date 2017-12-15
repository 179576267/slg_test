package com.douqu.game.battle.controller;

import com.douqu.game.battle.controller.sprite.PlayerController;
import com.douqu.game.battle.controller.sprite.SpriteController;
import com.douqu.game.battle.entity.*;
import com.douqu.game.battle.entity.aobject.BattleAObject;
import com.douqu.game.battle.entity.aobject.BattleMasterAObject;
import com.douqu.game.battle.entity.aobject.BattleSoldierAObject;
import com.douqu.game.battle.entity.tmp.PlayerBattleTmp;
import com.douqu.game.battle.entity.tmp.SpriteBattleTmp;
import com.douqu.game.battle.server.GameServer;
import com.douqu.game.battle.util.BattleUtils;
import com.douqu.game.core.astar.AStar;
import com.douqu.game.core.astar.AbsCoord;
import com.douqu.game.core.astar.AstarMap;
import com.douqu.game.core.astar.ObjectBound;
import com.douqu.game.core.e.E_BattleAObjectStatus;
import com.douqu.game.core.e.E_PlayerStatus;
import com.douqu.game.core.config.InitDataConfig;
import com.douqu.game.core.entity.battle.BattleDetail;
import com.douqu.game.core.config.common.Position;
import com.douqu.game.core.config.card.CardConfig;
import com.douqu.game.core.config.sprite.MasterConfig;
import com.douqu.game.core.entity.battle.BattleTemp;
import com.douqu.game.core.entity.battle.SpriteTemp;
import com.douqu.game.core.entity.db.CardDB;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.factory.WordFactory;
import com.douqu.game.core.i.IMainServer;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.protobuf.SGCommonProto.*;
import com.douqu.game.core.protobuf.SGMainProto;
import com.douqu.game.core.protobuf.SGMainProto.*;
import com.douqu.game.core.protobuf.SGWarProto;
import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


/**
* Created by bean on 2017/7/18.
*/
public class BattleController {

    private Logger logger = Logger.getLogger(BattleController.class);

    /** 公共CD时间 */
    public final static int PUBLIC_CD_TIME = 4 * ConstantFactory.UPDATE_BATTLE_TIME;

    /** 公共出卡CD时间 */
    public final static int PUBLIC_CARD_CD_TIME = 2 * ConstantFactory.UPDATE_BATTLE_TIME;

    /**
     * ID生成
     */
    public int createSoldierUniqueId = 0;

    /**
     * 主将ID
     */
    public int createMasterUniqueId = createSoldierUniqueId + 1000;

    protected String id;

//    /**
//     * 初始地图
//     */
//    protected AstarMap astarMap;
    /**
     * 寻路类
     */
    protected AStar astar;
//    /**
//     * 左上
//     */
//    protected Position leftTop;
//    /**
//     * 左下
//     */
//    protected Position leftBottom;
//    /**
//     * 右上
//     */
//    protected Position rightTop;
//    /**
//     * 右下
//     */
//    protected Position rightBottom;


    /**
     * 战斗类型
     */
    protected SGCommonProto.E_BATTLE_TYPE battleType;

    /**
     * 观战的人
     */
    protected List<PlayerController> spectators = new CopyOnWriteArrayList<>();
    /**
     * 一局最长的时间
     */
    protected int maxBattleTime;

    /**
     * 战斗创建时间
     */
    protected int createTime;

    /**
     * 战斗开始时间
     */
    protected int startTime;

    protected int endTime;

    /**
     * 战斗持续时间
     */
    protected int battleTime;

    /**
     * 当前战斗状态
     */
    protected int status;

    protected PlayerController player;

    protected SpriteController target;

    /**
     * 用来通知主服务器的数据
     */
    protected BattleDetail battleInfo;

    /**
     * 场上的所有兵
     */
    protected List<BattleAObject> soldiers;

    protected Map<Integer, BattleAObject> soldierMap;

    /**
     * 世界大BUFF(暴风雪)
     */
    private List<ExtendEffect> extendEffects;


    public BattleController(String battleId, PlayerController player, SpriteController target, E_BATTLE_TYPE battleType,
                            SGCommonProto.Pos leftBottom, SGCommonProto.Pos rightTop)
    {
        this.id = battleId;
        this.player = player;
        this.target = target;
        this.battleType = battleType;

        this.astar = new AStar(new AstarMap(AstarMap.AnchorPos.BottomLeft, BattleUtils.BATTLE_MAP_1, BattleUtils.BATTLE_MAP_1[0].length, BattleUtils.BATTLE_MAP_1.length));

//        this.leftBottom = new Position(leftBottom);
//        this.rightTop = new Position(rightTop);s
//        this.leftTop = new Position(leftBottom.getX(), rightTop.getY());
//        this.rightBottom = new Position(rightTop.getX(), leftBottom.getY());
//

        player.setBattleController(this);
        player.setStatus(E_PlayerStatus.BATTLE_WAIT_START);
        target.setBattleController(this);
        target.setStatus(E_PlayerStatus.BATTLE_WAIT_START);

        soldiers = new CopyOnWriteArrayList<>();
        soldierMap = new ConcurrentHashMap<>();
        extendEffects = new CopyOnWriteArrayList<>();

        battleInfo = new BattleDetail();
    }

    public void init()
    {
        InitDataConfig initDataConfig = DataFactory.getInstance().getInitDataConfig();
        this.maxBattleTime = initDataConfig.crystalGrow[initDataConfig.crystalGrow.length-1].id;

        this.createTime = (int) (GameServer.currentTime/1000);

        //队伍1的主将
        MasterConfig masterConfig = DataFactory.getInstance().getGameObject(DataFactory.MASTER_KEY, player.getPlayer().master);
        if(masterConfig == null)
            masterConfig = (MasterConfig) DataFactory.getInstance().getDataList(DataFactory.MASTER_KEY).get(0);

        BattleMasterAObject masterAObject1 = new BattleMasterAObject(player, masterConfig,
                createMasterUniqueId + ConstantFactory.BATTLE_TEAM_1, player.getPlayer().getLv(),
                ConstantFactory.BATTLE_TEAM_1, getMasterPos(ConstantFactory.BATTLE_TEAM_1, masterConfig.gridArea));

        SpriteBattleTmp pbt1 = new PlayerBattleTmp();
        pbt1.init(this, player, masterAObject1, ConstantFactory.BATTLE_TEAM_1, battleType);
        player.setAttachment(pbt1);

        battleInfo.setId(id);
        battleInfo.setCreateTime(createTime);
        battleInfo.setBattleType(battleType.getNumber());
        battleInfo.getTeam1Info().setIndexInfo(player.getObjectIndex());
        battleInfo.getTeam2Info().setIndexInfo(target.getObjectIndex());
    }

    /**
     *
     * @param currentTime 当前时间
     */
    public void update(long currentTime)
    {
        if(startTime <= 0)
        {
//            logger.info("还没开始战斗呢!" + id);
            return;
        }

        if(endTime > 0)
        {
            return;
        }

        battleTime += ConstantFactory.UPDATE_BATTLE_TIME;

        if(battleTime >= maxBattleTime)//战斗时间到了
        {
            if(isEnd())
                return;

            //战斗时间到了,检测胜负
            int winTeam = getWinTeam();
            logger.info("战斗时间到了，胜利方:" + winTeam);
            end(winTeam);
        }
        else//时间没到
        {
            if(checkEnd())
                return;

            for(ExtendEffect extendEffect : extendEffects)
            {
                extendEffect.update(currentTime);

                if(extendEffect.isEnd())
                    extendEffects.remove(extendEffect);
            }

            if(checkEnd())
                return;

            player.getAttachment().update(currentTime, battleTime);

            if(checkEnd())
                return;
            target.getAttachment().update(currentTime, battleTime);

            if(checkEnd())
                return;
        }
    }

    public boolean isStarted()
    {
        return startTime > 0;
    }

    /**
     * 是否过期
     * @return
     */
    public boolean isOverdue()
    {
        return startTime == 0 && GameServer.currentTime/1000 - createTime >= 60;
    }

    public void setToAstarMap(BattleAObject battleAObject)
    {
//        logger.warn("设置到地图中去:" + battleAObject);
//        logger.warn("设置前地图:\n" + astarMap);

        astar.getAstarMap().setMap(battleAObject.getObjectBound(), AstarMap.MapSignal.Player);
//        astar.getAstarMap().setMap(battleAObject.position.getIntX(), battleAObject.position.getIntY(), AstarMap.MapSignal.Player);
//        logger.warn("设置后地图:\n" + astar.getAstarMap());
    }

    public void leaveAstarMap(BattleAObject battleAObject)
    {
        if(battleAObject.getObjectBound().center == null)
            return;

        astar.getAstarMap().setMap(battleAObject.getObjectBound(), AstarMap.MapSignal.Free);

//        astar.getAstarMap().setMap(battleAObject.position.getIntX(), battleAObject.position.getIntY(), AstarMap.MapSignal.Free);
    }


    public void addSoldier(BattleAObject battleAObject)
    {
        soldiers.add(battleAObject);
        soldierMap.put(battleAObject.getUniqueId(), battleAObject);
    }

    public void removeSoldier(BattleAObject soldier)
    {
        soldiers.remove(soldier);
        soldierMap.remove(soldier.getUniqueId());

        if(soldier.getTeamNo() == ConstantFactory.BATTLE_TEAM_1)
        {
            player.getAttachment().removeSoldier(soldier);
        }
        else
        {
            target.getAttachment().removeSoldier(soldier);
        }

        leaveAstarMap(soldier);
    }

    public List<BattleAObject> getSoldiers()
    {
        return soldiers;
    }

    public BattleAObject getSoldier(int uniqueId)
    {
        return soldierMap.get(uniqueId);
    }

    public BattleAObject getBattleAObject(int teamNo, int uniqueId)
    {
        if(teamNo == player.getAttachment().getTeamNo())
        {
            if(player.getAttachment().getMasterSoldier().getUniqueId() == uniqueId)
                return player.getAttachment().getMasterSoldier();
        }
        else
        {
            if(target.getAttachment().getMasterSoldier().getUniqueId() == uniqueId)
                return target.getAttachment().getMasterSoldier();
        }

        return getSoldier(uniqueId);
    }


    public void addExtendEffect(SpriteBattleTmp releaser, BattleSkill skill, Position sourcePoint, Position directionPoint)
    {
        ExtendEffect extendEffect = new ExtendEffect(releaser, skill, sourcePoint, directionPoint);
        extendEffects.add(extendEffect);
    }



    private boolean checkEnd()
    {
        if(isEnd())
        {
            end(getWinTeam());
            return true;
        }

        return false;
    }



    public void ready(PlayerController playerController)
    {
        if(playerController.getStatus() != E_PlayerStatus.BATTLE_WAIT_START)
            return;

        SpriteBattleTmp spriteBattleTmp = playerController.getAttachment();
        if(spriteBattleTmp == null)
            return;

        spriteBattleTmp.setReady(true);

        if(isReady())
        {
            player.setStatus(E_PlayerStatus.BATTLING);
            target.setStatus(E_PlayerStatus.BATTLING);

            startTime = (int) (GameServer.currentTime/1000);
            battleInfo.setStartTime(startTime);
            battleTime = 0;

            IMainServer server = GameServer.getInstance().getiMainServer();
            if(server != null)
            {
                try {
                    server.battleStart(GameServer.getInstance().getId(), battleInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            dispatchMsg(E_MSG_ID.MsgID_War_ReadyStart_VALUE, null);
        }
    }


    public void end(int winTeam)
    {
        if(winTeam < 0)
            return;

        if(endTime > 0)
            return;

        logger.info("战斗结束，胜利方:" + winTeam + " id:" + id);

        endTime = (int) (GameServer.currentTime/1000);

        battleInfo.setEndTime(endTime);
        battleInfo.setWinTeam(winTeam);

        /*******************队伍1*************************************************************/
        BattleTemp battleTemp = battleInfo.getTeam1Info();
        int star1 = getStar(player.getAttachment(), winTeam);
        battleTemp.setStar(star1);

        BattleAObject master = player.getAttachment().getMasterSoldier();
        battleTemp.setMaster(new SpriteTemp(master.getObjectId(), master.getHp(), master.getMaxHp()));

        List<BattleAObject> soldiers = player.getAttachment().getHistorySoldiers();
        for(BattleAObject soldier : soldiers)
        {
            battleTemp.addSoldierTemp(new SpriteTemp(soldier.getObjectId(), soldier.getHp(), soldier.getMaxHp()));
        }

        /*******************队伍2*************************************************************/
        battleTemp = battleInfo.getTeam2Info();

        int star2 = getStar(target.getAttachment(), winTeam);
        battleTemp.setStar(star1);

        master = target.getAttachment().getMasterSoldier();
        battleTemp.setMaster(new SpriteTemp(master.getObjectId(), master.getHp(), master.getMaxHp()));

        soldiers = target.getAttachment().getHistorySoldiers();
        for(BattleAObject soldier : soldiers)
        {
            battleTemp.addSoldierTemp(new SpriteTemp(soldier.getObjectId(), soldier.getHp(), soldier.getMaxHp()));
        }

//        GameServer.getInstance().startThread(() -> {
            try{
                GameServer.getInstance().getiMainServer().battleEnd(GameServer.getInstance().getId(), battleInfo);
            }catch (Exception e){
                e.printStackTrace();
            }
//        });

        SGWarProto.S2C_SynResult.Builder data = SGWarProto.S2C_SynResult.newBuilder();
        data.setWinTeam(winTeam);
        data.setStar(star1);
        player.sendMsg(E_MSG_ID.MsgID_War_SynResult_VALUE, data.build().toByteArray());

        data = SGWarProto.S2C_SynResult.newBuilder();
        data.setWinTeam(winTeam);
        data.setStar(star2);
        target.sendMsg(E_MSG_ID.MsgID_War_SynResult_VALUE, data.build().toByteArray());

        //通知双方战斗结束，计算战斗奖励
        player.clearBattle();
        target.clearBattle();

        GameServer.getInstance().getWorldManager().removeBattleController(this);
    }


    public int getStar(SpriteBattleTmp spriteBattleTmp, int winTeam)
    {
        return 0;
    }


    public int getWinTeam()
    {
        if(endTime > 0 || player == null || player.getAttachment() == null || target == null || target.getAttachment() == null)
            return -1;

        if(!isHaveSoldier())
            return target.getAttachment().getTeamNo();

        if(player.getAttachment().isDie())
            return target.getAttachment().getTeamNo();

        if(target.getAttachment().isDie())
            return player.getAttachment().getTeamNo();

        return 0;
    }






    private boolean isHaveSoldier()
    {
        return soldiers.size() > 0 || player.getAttachment().getCardList().size() > 0 || target.getAttachment().getCardList().size() > 0;
    }


    public boolean isEnd()
    {
        if(endTime > 0)
            return true;

        if(player == null || target == null || player.getAttachment() == null || target.getAttachment() == null)
            return true;

        if(!isHaveSoldier())
            return true;

        if(player.getAttachment().isDie() || target.getAttachment().isDie())
            return true;

        return false;
    }


    /**
     * 是否是真人对战
     * @return
     */
    public boolean isRealPlayerBattle()
    {
        return battleType == SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVP_ARENA;
    }

    /**
     * 是否双方都是AI
     * @return
     */
    public boolean isAllAI()
    {
        return battleType == SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVP_ARENA_AI;
    }


    public boolean isReady()
    {
        if(isRealPlayerBattle())
            return player.getAttachment().isReady() && target.getAttachment().isReady();
        else
            return player.getAttachment().isReady();
    }


    public Position getMasterPos(int teamNo, int gridArea)
    {
        if(teamNo == ConstantFactory.BATTLE_TEAM_1)
        {
            return new Position(gridArea, astar.getAstarMap().height>>1);
        }
        else
        {
            return new Position(astar.getAstarMap().width-gridArea-1, astar.getAstarMap().height>>1);
        }
    }

    public Position getSoldierPos(int teamNo, int initRange, int route, int gridArea)
    {
        //最大找5次
        int x = 0, y = 0;
        for(int i = 0; i < 5; i++)
        {
            int r1 = (int) (Math.random() * initRange);
            r1 = r1 < 1 ? 1: r1;
            int temp = (astar.getAstarMap().height - 1)>>1;
            int r2 = (int) (Math.random() * temp);

            if(teamNo == ConstantFactory.BATTLE_TEAM_1)
            {
                x = r1;
            }
            else
            {
                x = astar.getAstarMap().width - 1 - r1;
            }

            if(route == 0)
                route = (int) (Math.random()*2) == 0 ? ConstantFactory.ROUTE_BOTTOM : ConstantFactory.ROUTE_TOP;
            if(route == ConstantFactory.ROUTE_TOP)
            {
                y = temp + r2;
            }
            else
            {
                y = r2;
            }

            if(!astar.getAstarMap().isObstacle(x, y))
                break;
        }

        if(x == 0 || y == 0)
        {
            return astar.getAstarMap().getSoldierPos(teamNo, route, gridArea);
        }

        return new Position(x, y);
    }


    public void destory()
    {
        player.destroy();

        if(target instanceof PlayerController)
        {
           ((PlayerController) target).destroy();
        }
    }

    /**
     * 通知兵相遇要开战了
     * @param attacker
     * @param target
     */
    public void sendSoldierBattleStart(BattleAObject attacker, BattleAObject target)
    {
        SGWarProto.S2C_SoldierBattleStart.Builder data = SGWarProto.S2C_SoldierBattleStart.newBuilder();
        data.setAttackId(attacker.getUniqueId());
        data.setAttackPos(attacker.getPosition().parsePos());
        data.setTargetId(target.getUniqueId());
        data.setTargetPos(target.getPosition().parsePos());
        dispatchMsg(SGMainProto.E_MSG_ID.MsgID_War_SoldierBattleStart_VALUE, data.build().toByteArray());

        logger.info("两个单位开战(普通攻击) -> attacker:" + attacker + "  target:" + target);
    }


    /**
     * 通知使用技能结果
     * @param skillId
     * @param releaser
     * @param targets
     */
    public void sendUseSkill(int skillId, int releaser, List<BattleAObject> targets, int effectTime, Position position)
    {
        SGWarProto.S2C_UseSkill.Builder data = SGWarProto.S2C_UseSkill.newBuilder();
        data.setSkillId(skillId);
        data.setReleaserId(releaser);

        if(targets != null)
        {
            for(BattleAObject battleAObject : targets)
            {
                data.addTargetId(battleAObject.getUniqueId());
            }
        }

        data.setEffectTime(effectTime);
        if(position != null)
            data.setTargetPos(position.parsePos());

        dispatchMsg(SGMainProto.E_MSG_ID.MsgID_War_UseSkill_VALUE, data.build().toByteArray());

        logger.debug("sendUseSkill skillId:" + skillId + " target:" + targets + " 生效时间:" + effectTime + " 目标点:" + position);
    }

    /**
     * 通知伤害
     * @param target
     * @param damage 伤害值 负数为减血 正数为加血
     * @param hp 伤害后的血
     */
    public void sendDamage(int source, int skillId, int target, int damage, int hp)
    {
        if(damage == 0)
            return;

        SGWarProto.S2C_DoDamage.Builder data = SGWarProto.S2C_DoDamage.newBuilder();
        data.addSpriteDamage(createDamageInfo(source, skillId, target, damage, hp));

        dispatchMsg(SGMainProto.E_MSG_ID.MsgID_War_DoDamage_VALUE, data.build().toByteArray());
    }

//    public void checkStar(SpriteBattleTmp spriteBattleTmp, int race){}


    /**
     * 获取目标组
     * @param targetGroup
     * @param spriteBattleTmp
     * @return
     */
    public List<BattleAObject> getTargetsByGroup(int targetGroup, SpriteBattleTmp spriteBattleTmp)
    {
        List<BattleAObject> result = new CopyOnWriteArrayList<>();
        if(targetGroup == ConstantFactory.EFFECT_TARGET_GROUP_ALL)
        {
            result.addAll(soldiers);
            result.add(spriteBattleTmp.getMasterSoldier());
            result.add(spriteBattleTmp.getTargetBattleTmp().getMasterSoldier());
        }
        else if(targetGroup == ConstantFactory.EFFECT_TARGET_GROUP_PARTNER)
        {
            result.addAll(spriteBattleTmp.getSoldierList());
            result.add(spriteBattleTmp.getMasterSoldier());
        }
        else if(targetGroup == ConstantFactory.EFFECT_TARGET_GROUP_ENEMY)
        {
            result.addAll(spriteBattleTmp.getTargetBattleTmp().getSoldierList());
            result.add(spriteBattleTmp.getTargetBattleTmp().getMasterSoldier());
        }
        else if(targetGroup == ConstantFactory.EFFECT_TARGET_GROUP_SELF)
        {
            result.addAll(result);
        }

        return result;
    }



    public String getId() {
        return id;
    }

    public SGCommonProto.E_BATTLE_TYPE getBattleType() {
        return battleType;
    }

    public int getBattleTime() {
        return battleTime;
    }

    public PlayerController getPlayerByIndex(String playerIndex)
    {
        if(playerIndex.equals(player.getObjectIndex()))
            return player;

        if(target instanceof PlayerController && playerIndex.equals(target.getObjectIndex()))
            return (PlayerController) target;

        return null;
    }

    public PlayerController getPlayer() {
        return player;
    }

    public SpriteController getTarget() {
        return target;
    }

    public AStar getAstar()
    {
        return astar;
    }



    public SGCommonProto.SpriteDamage.Builder createDamageInfo(int source, int skillId, int target, int damage, int hp)
    {
        SGCommonProto.SpriteDamage.Builder spriteDamage = SGCommonProto.SpriteDamage.newBuilder();
        spriteDamage.setSrcId(source);
        spriteDamage.setSrcSkillId(skillId);
        spriteDamage.setTargetId(target);
        spriteDamage.setDamage(damage);
        spriteDamage.setHp(hp);

        return spriteDamage;
    }



    public int getSoldierUniqueId()
    {
        return ++createSoldierUniqueId;
    }


    /**
     * 转发容器内所有的消息
     * @param
     */
    public void dispatchMsg(int code, byte[] data)
    {
        player.sendMsg(code, data);

        target.sendMsg(code, data);

        for(PlayerController playerController : spectators)
        {
            playerController.sendMsg(code ,data);
        }
    }


    //---------------------------------------------消息通道-----------------------------------------------------------------------
    /**
     * 上战场
     * @param playerController
     * @param data
     */
    public void toBattlefield(PlayerController playerController, byte[] data) throws InvalidProtocolBufferException
    {
        if(isEnd())
        {
            logger.debug("战斗已经结束");
            playerController.sendAlert(E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ILLEGAL_ERROR));
            return;
        }

        PlayerBattleTmp playerBattleTmp = (PlayerBattleTmp) playerController.getAttachment();
        if(playerBattleTmp == null)
        {
            //错误
            logger.debug("非法数据");
            playerController.sendAlert(E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ILLEGAL_ERROR));
            return;
        }
        SGWarProto.C2S_ToBattle request = SGWarProto.C2S_ToBattle.parseFrom(data);
        CardDB cardDB = playerBattleTmp.getCardById(request.getCardId());
        if(cardDB == null)
        {
            //没有此卡牌
            logger.debug("卡牌不存在");
            playerController.sendAlert(E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.OBJECT_NOT_EXIST));
            return;
        }
        List<Pos> posList = request.getPosList();
        CardConfig cardConfig = cardDB.getConfig();
        if(posList.size() != cardConfig.soldierCount)
        {
            //参数错误
            logger.debug("位置数量不对");
            playerController.sendAlert(E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ILLEGAL_ERROR));
            return;
        }
        if(playerBattleTmp.getCurCrystal() < cardConfig.crystal)
        {
            if(!playerBattleTmp.isAllowToBattleByCrystal(cardConfig.crystal))
            {
                //水晶不够
                logger.debug("水晶不够");
                playerController.sendAlert(E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ENERGY_NOT_ENOUGH));
                return;
            }
        }

        //位置范围检测
        List<Position> positionList = new ArrayList<>();
        for(Pos pos : posList)
        {
            if(ConstantFactory.BATTLE_TEAM_1 == playerBattleTmp.getTeamNo())
            {
                if(pos.getX() > cardConfig.soldier.initRange)
                {
                    logger.debug("释放位置超出限制 : " + pos.getX() + " initRange:" + cardConfig.soldier.initRange);
                    playerController.sendAlert(E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ENERGY_NOT_ENOUGH));
                    return;
                }
            }
            else if(ConstantFactory.BATTLE_TEAM_2 == playerBattleTmp.getTeamNo())
            {
                if(pos.getX() < 50 - cardConfig.soldier.initRange)
                {
                    logger.debug("释放位置超出限制 : " + pos.getX() + " initRange:" + cardConfig.soldier.initRange);
                    playerController.sendAlert(E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ENERGY_NOT_ENOUGH));
                    return;
                }
            }
            positionList.add(new Position(pos));
        }

        playerBattleTmp.soldierBorn(cardDB, positionList);

        if(!isRealPlayerBattle())
        {
            //检测AI
            logger.debug("对手开始检测AI出牌：" + target);
            target.getAttachment().checkAI(GameServer.currentTime);
        }
    }





    /**
     * 同步位置
     * @param playerController
     * @param data
     */
    private void synPos(PlayerController playerController, byte[] data) throws InvalidProtocolBufferException
    {
        SGWarProto.C2S_SynPos request = SGWarProto.C2S_SynPos.parseFrom(data);

//        request.get
    }

//    /**
//     * 获取到兵的路线
//     * @param playerController
//     * @param data
//     */
//    private void soldierRoute(PlayerController playerController, byte[] data) throws InvalidProtocolBufferException
//    {
//        SGWarProto.C2S_SoldierRoute routeData = SGWarProto.C2S_SoldierRoute.parseFrom(data);
//
//        if(routeData.getRouteList() == null || routeData.getRouteList().size() <= 1)
//        {
//            logger.debug("参数为空");
//            return;
//        }
//
//        BattleAObject battleAObject = soldierMap.get(routeData.getUniqueId());
//        if(battleAObject == null)
//        {
//            //错误
//            return;
//        }
//
//        if(!(battleAObject instanceof BattleSoldierAObject))
//        {
//            return;
//        }
//
//        BattleSoldierAObject soldier = (BattleSoldierAObject) battleAObject;
//
//        synchronized (soldier)
//        {
//            logger.info(soldier.getId() + " name:" + soldier.getName() + " 从客户获取路线:" + routeData.getRouteList());
//            List<Position> changeRouteList = BattleUtils.changeRoute(routeData.getRouteList(), soldier.moveSpeed.getAtt());
//            soldier.initRoute(changeRouteList);
//
//            soldier.setStatus(E_BattleAObjectStatus.GOING);
//
//            logger.info(soldier.getId() + " name:" + soldier.getName() + " 从客户获取转换后的路线:" + changeRouteList);
//        }
//
//
//    }


    /**
     * 主将使用技能
     * @param playerController
     * @param data
     */
    public void useSkill(PlayerController playerController, byte[] data) throws InvalidProtocolBufferException
    {
        SGWarProto.C2S_UseSkill request = SGWarProto.C2S_UseSkill.parseFrom(data);

        PlayerBattleTmp playerBattleTmp = (PlayerBattleTmp) playerController.getAttachment();
        if(playerBattleTmp.getPublicCDTime() > 0)
        {
            //公共CD时间没到
            playerController.sendAlert(E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CD_TIME_ERROR));
            return;
        }

        BattleSkill skill = playerBattleTmp.getMasterSoldier().getSkill(request.getSkillId());
        if(skill == null)
        {
            //没有这个技能
            playerController.sendAlert(E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.SKILL_NOT_EXIST));
            return;
        }

        if(playerBattleTmp.getCurCrystal() < skill.getSkillConfig().crystal)
        {
            //水晶不够
            playerController.sendAlert(E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ENERGY_NOT_ENOUGH));
            return;
        }

        if(skill.getSkillConfig().type != ConstantFactory.SKILL_TYPE_MASTER)
        {
            //非主将技能不能手动释放
            playerController.sendAlert(E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ILLEGAL_ERROR));
            return;
        }

        if(!skill.isCanRelease())
        {
            //cd时间没到
            playerController.sendAlert(E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.CD_TIME_ERROR));
            return;
        }

        Pos pos = request.getPos();
        if(skill.getSkillConfig().effectTargetType == ConstantFactory.EFFECT_TARGET_TYPE_POINT && pos == null)
        {
            //参数错误
            playerController.sendAlert(E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        if(skill.getSkillConfig().effectTargetType == ConstantFactory.EFFECT_TARGET_TYPE_POINT)
            skill.releaseByPoint(new Position(pos));
        else
            skill.release();

        playerBattleTmp.setPublicCDTime(PUBLIC_CD_TIME);
        playerBattleTmp.addCrystal(-skill.getSkillConfig().crystal);

    }




    /** 消息通道
     */
    public void messageChannel(int code, PlayerController playerController, byte[] data)
    {
        SpriteBattleTmp battleTmp = playerController.getAttachment();
        if(battleTmp == null)
            return;

        if(battleTmp.getTeamNo() == ConstantFactory.BATTLE_TEAM_SPECTATOR)
        {
            //观战者
            return;
        }

//        logger.info(playerController.getName() + "  当前玩家状态："+playerController.getStatus() + "  " + playerController.getBattleController());
        if(!playerController.isBattling())
            return;

        try{
            switch (code)
            {
                case E_MSG_ID.MsgID_War_ReadyStart_VALUE:
                    if(playerController.getStatus() != E_PlayerStatus.BATTLE_WAIT_START)
                        return;
                    ready(playerController);
                    break;
                case E_MSG_ID.MsgID_War_ToBattle_VALUE://出牌
                    if(playerController.getStatus() != E_PlayerStatus.BATTLING)
                        return;
                    toBattlefield(playerController, data);
                    break;
//                case E_MSG_ID.MsgID_War_SoldierRoute_VALUE://获取路线
//                    if(playerController.getStatus() != E_PlayerStatus.BATTLING)
//                        return;
//                    if(GameServer.ROUTE_TO_CLIENT)
//                        soldierRoute(playerController, data);
//                    break;
                case E_MSG_ID.MsgID_War_SynPos_VALUE://同步位置
                    if(playerController.getStatus() != E_PlayerStatus.BATTLING)
                        return;
                    synPos(playerController, data);
                    break;
                case E_MSG_ID.MsgID_War_UseSkill_VALUE://使用技能
                    if(playerController.getStatus() != E_PlayerStatus.BATTLING)
                        return;
                    useSkill(playerController, data);
                    break;
            }
        }catch (Exception e){
            logger.info("Error: code:" + code +" " + e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return "[" + player.getObjectIndex() + " ---VS--- " + target.getObjectIndex() + "] type:" + battleType;
    }
}
