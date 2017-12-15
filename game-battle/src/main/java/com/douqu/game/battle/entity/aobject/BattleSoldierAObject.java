package com.douqu.game.battle.entity.aobject;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.battle.controller.sprite.PlayerController;
import com.douqu.game.battle.controller.sprite.SpriteController;
import com.douqu.game.battle.entity.BattleSkill;
import com.douqu.game.battle.server.GameServer;
import com.douqu.game.battle.util.BattleUtils;
import com.douqu.game.battle.util.RouteUtils;
import com.douqu.game.core.astar.*;
import com.douqu.game.core.e.E_Attribute;
import com.douqu.game.core.e.E_BattleAObjectStatus;
import com.douqu.game.core.config.common.Position;
import com.douqu.game.core.config.battle.SkillConfig;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.entity.common.TwoTuple;
import com.douqu.game.core.config.card.CardConfig;
import com.douqu.game.core.config.sprite.SoldierConfig;
import com.douqu.game.core.entity.db.CardDB;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.protobuf.SGMainProto;
import com.douqu.game.core.protobuf.SGWarProto;
import com.douqu.game.core.util.SendUtils;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by bean on 2017/7/25.
 */
public class BattleSoldierAObject extends BattleAObject {

    Logger logger = Logger.getLogger(BattleSoldierAObject.class);

    /**
     * 路线列表
     */
    private List<Position> routeList;

    /**
     * 当前走到哪一步
     */
    private int curRouteIndex;

    /**
     * 出生时间
     */
    private long bornTime;


    public BattleSoldierAObject(SpriteController masterController, CardDB cardDB, int uniqueId, int teamNo, Position position)
    {
        super(masterController, cardDB.getConfig().soldier, uniqueId, teamNo, position);

        boolean isInvincible = false;
        if(masterController instanceof PlayerController)
        {
            Player player = ((PlayerController) masterController).getPlayer();
            isInvincible = player.invincible;
        }
        if(isInvincible)
        {
            this.hp = maxHp = 99999999;

            this.atk = new BattleAtt(99999999);
            this.def = new BattleAtt(99999999);
            this.cri = new BattleAtt(9999);
            this.rec = new BattleAtt(9999);
            this.add = new BattleAtt(9999);
            this.exd = new BattleAtt(9999);
            this.hit = new BattleAtt(9999);
            this.eva = new BattleAtt(9999);
            this.power   = new BattleAtt(999999);
            this.agility = new BattleAtt(999999);
            this.wisdom  = new BattleAtt(999999);
        }
        else
        {
            this.hp = maxHp = cardDB.getAttribute(E_Attribute.HP.getCode());

            this.atk = new BattleAtt(cardDB.getAttribute(E_Attribute.ATK.getCode()));
            this.def = new BattleAtt(cardDB.getAttribute(E_Attribute.DEF.getCode()));
            this.cri = new BattleAtt(cardDB.getAttribute(E_Attribute.CRI.getCode()));
            this.rec = new BattleAtt(cardDB.getAttribute(E_Attribute.REC.getCode()));
            this.add = new BattleAtt(cardDB.getAttribute(E_Attribute.ADD.getCode()));
            this.exd = new BattleAtt(cardDB.getAttribute(E_Attribute.EXD.getCode()));
            this.hit = new BattleAtt(cardDB.getAttribute(E_Attribute.HIT.getCode()));
            this.eva = new BattleAtt(cardDB.getAttribute(E_Attribute.EVA.getCode()));
            this.power   = new BattleAtt(cardDB.getAttribute(E_Attribute.POWER.getCode()));
            this.agility = new BattleAtt(cardDB.getAttribute(E_Attribute.AGILITY.getCode()));
            this.wisdom  = new BattleAtt(cardDB.getAttribute(E_Attribute.WISDOM.getCode()));
        }

        CardConfig cardConfig = cardDB.getConfig();
        this.moveSpeed = new BattleAtt(cardConfig.soldier.moveSpeed);
        this.attackSpeed = new BattleAtt(cardConfig.soldier.attackSpeed);
        this.isMove = true;

        List<SkillConfig> skillConfigList = cardConfig.getSkills(cardDB.star);
        this.skills = new BattleSkill[skillConfigList.size()];
        for(int i = 0; i < skills.length; i++)
        {
            skills[i] = new BattleSkill(masterController, this, skillConfigList.get(i));
        }

        bornTime = GameServer.currentTime;

        logger.info("士兵初始化 -> " + detailInfo());
    }


    /**
     * 是否可以开始行走了(暂时处理成出生后500毫秒之后)
     * @return
     */
    public boolean isCanStartMove()
    {
        return GameServer.currentTime - bornTime >= 500;
    }



    public void initRoute(List<Position> list)
    {
        battleController.leaveAstarMap(this);

        this.routeList = list;
        curRouteIndex = 0;
        setPosition(routeList.get(0));
        this.status = E_BattleAObjectStatus.GOING;
    }

//    @Override
//    public boolean isRestrict(BattleAObject target)
//    {
//        if(target instanceof BattleSoldierAObject)
//            return getSoldier().isRestrict(((BattleSoldierAObject) target).getSoldier());
//
//        return false;
//    }


    @Override
    public int addHP(int value)
    {
        int result = super.addHP(value);

        if(isDie())
        {
            checkDieSkill();

            battleController.removeSoldier(this);
        }

        return result;
    }

    /**
     * 前进
     */
    @Override
    public void go()
    {
        if(!isCanGo())
        {
            //冰冻或者眩晕了，不能行走
            logger.debug("被冰冻或者眩晕了，不能行走 -> " + this);
            return;
        }

        if(status == E_BattleAObjectStatus.BATTLING)
            return;

        if(routeList == null)
        {
            clearGoTarget();
            setStatus(E_BattleAObjectStatus.FREE);
            return;
        }

        if(curRouteIndex >= routeList.size()-1)
        {
            clearGoTarget();
            setStatus(E_BattleAObjectStatus.FREE);
            return;
        }

        battleController.leaveAstarMap(this);

        if(goTarget != null && goTargetOriginalPosMap.get(goTarget.getUniqueId()) != null)
        {
            if(!goTarget.position.isSame(goTargetOriginalPosMap.get(goTarget.getUniqueId())))
            {
                logger.debug("前进的目标已经不在原来的位置上:goTarget:" + goTarget + " rote:" + routeList.get(routeList.size() - 1));
                clearGoTarget();
                curRouteIndex = 0;
                routeList = null;
                status = E_BattleAObjectStatus.FREE;
                return;
            }
        }

        Position goPosition = routeList.get(curRouteIndex+1);

        if(battleController.getAstar().getAstarMap().isObstacleToApplication(goPosition.getIntX(), goPosition.getIntY()))
        {
//            logger.debug("前进的路线是障碍物,重新寻路");
//            if(!goTarget.isDie())
//                checkRoute(goTarget);

            clearGoTarget();
            curRouteIndex = 0;
            routeList = null;
            status = E_BattleAObjectStatus.FREE;

            return;
        }

        curRouteIndex++;

        setPosition(routeList.get(curRouteIndex));

        logger.debug("BattleSoldierAObject go teamNo:" + teamNo + " uniqueId:" + getUniqueId() + " soldierId:" + sprite.getId() + " name:" + sprite.getName() + " position:" + position + "  总的路线:" + routeList.size() + "  最后的点:" + routeList.get(routeList.size() - 1));
    }

    @Override
    public boolean checkCanAtk(BattleAObject target)
    {
        if(target == null)
             return false;

        if(!defaultSkill.checkUnitType(target))
            return false;

        return defaultSkill.isInAtkRange(this, target);
    }


    @Override
    public TwoTuple<BattleAObject, BattleAObject> checkDefaultSkillTarget(List<BattleAObject> targetList)
    {
        BattleAObject atkTarget = null, visionTarget = null;
        List<BattleAObject> tempList = new CopyOnWriteArrayList<>();
        tempList.addAll(targetList);
        //按照离我的距离排序,最近的排在前面
        Collections.sort(tempList, new Comparator<BattleAObject>() {
            @Override
            public int compare(BattleAObject o1, BattleAObject o2) {
                return new Float(BattleUtils.getDistance(position, o1.position)).compareTo(BattleUtils.getDistance(position, o2.position));
            }
        });

        for(BattleAObject battleAObject : tempList)
        {
            if(!defaultSkill.checkUnitType(battleAObject))
                continue;

            if(atkTarget == null)
            {
                atkTarget = defaultSkill.isInAtkRange(this, battleAObject) ? battleAObject : null;
            }
            if(visionTarget == null)
            {
                visionTarget = BattleUtils.isInCircleRange(position, battleAObject.getPosition(), getSoldier().vision) ? battleAObject : null;
            }

            if(atkTarget != null && visionTarget != null)
                break;
        }

        return new TwoTuple<>(atkTarget, visionTarget);
    }


    @Override
    public void checkDefaultSkillTarget(long currentTime)
    {
        if(isDie())
            return;

        if(!isCanAtk())
        {
            //被眩晕了，不能发动攻击
            logger.debug("被眩晕了，不能发动攻击 -> " + uniqueId + " name:" + sprite.name);
            return;
        }

        if(checkCDSkill(currentTime))
        {
            logger.debug("有其它技能要释放:" + uniqueId + " name:" + sprite.name);
            return;
        }

//        if(defaultSkill.isEffectFlying())
//        {
//            logger.debug("延时技能还没完成:" + id + " name:" + sprite.name);
//            return;
//        }

        //普通攻击速度有可能会受BUFF影响变化
        if(!defaultSkill.isCanRelease(attackSpeed.getAtt()))
        {
            logger.debug("CD时间没到:" + uniqueId + " name:" + sprite.name + " attackSpeed:" + attackSpeed);
            return;
        }

        if(defaultSkill.getSkillConfig().crystal > masterController.getAttachment().getCurCrystal())
        {
            logger.debug("水晶不够:" + uniqueId + " name:" + sprite.name);
            return;
        }

        checkDefaultAtk();

        if(defaultSkill.getDefaultAtkTarget() == null)
        {
            //检测对方主将是否可打
            BattleAObject masterAObject = masterController.getAttachment().getTargetBattleTmp().getMasterSoldier();
            logger.debug("检测对方主将是否可打:" + masterAObject);
            if(checkCanAtk(masterAObject))
            {
                logger.debug(this + " 攻打对方主将:" + masterAObject);
                defaultAtk(masterAObject);
            }
            else
            {
                TwoTuple<BattleAObject,BattleAObject> targets = checkDefaultSkillTarget(masterController.getAttachment().getTargetBattleTmp().getSoldierList());
//                logger.debug("检测到周围的攻击目标:" + targets.getFirst() + "   " + targets.getSecond() + "  " + masterController.getAttachment().getTargetBattleTmp().getSoldierList());
                if(targets.isNull())
                {
                    defaultSkill.clearDefaultAtkTarget();

                    logger.debug("AI向敌方主将寻路 -> this:" + this + " target:" + masterController.getAttachment().getTargetBattleTmp().getMasterSoldier());
                    checkRoute(masterController.getAttachment().getTargetBattleTmp().getMasterSoldier());
                }
                else if(targets.getFirst() != null)
                {
                    logger.debug(uniqueId + " name:" + sprite.name + " 检测到攻击目标:" + targets.getFirst() + "  " + targets.getSecond());
                    //找到可以直接攻击的目标，告诉客户端开始攻击
                    defaultAtk(targets.getFirst());
                }
                else if(targets.getSecond() != null)
                {
                    //检测是否有在视野范围内有
//                logger.debug(soldier.getName() + " 的视野范围内有目标:" + targets.getSecond().getName());
                    //向目标走去，跟路线服务器请求路线
                    checkRoute(targets.getSecond());
                }
            }
        }
        else
        {
            //是否还在攻击范围内
            BattleAObject defaultAtkTarget = defaultSkill.getDefaultAtkTarget();
            if(!defaultSkill.isInAtkRange(this, defaultAtkTarget))
            {
                //不在攻击范围内，要追击，要告诉路线服务器重新获取路线(现在是客户端来当路线服务器的)
                this.status = E_BattleAObjectStatus.CHASING;
                //发送获取追击路线指令
                checkRoute(defaultAtkTarget);
            }
            else
            {
                //告诉客户端攻击了
                defaultSkill.release(defaultAtkTarget);

                checkDefaultAtk();
            }

        }
    }

    private void defaultAtk(BattleAObject target)
    {
        setDefaultAtk(target);

        battleController.sendSoldierBattleStart(this, target);

        defaultSkill.release(target);

        checkDefaultAtk();
    }


    @Override
    public void checkRoute(BattleAObject target)
    {
        if(!isCanStartMove())
            return;

        if(!isCanGo())
        {
            //被冰冻或者眩晕了，不能行走
            logger.debug("被冰冻或者眩晕了，不能行走 -> this:" + this + " target:" + target);
            return;
        }

//        if(isEffectActioning())
//        {
//            logger.debug("isEffectActioning -> this:" + this + " target:" + target);
//            return;
//        }

        if(!masterController.isBattling())
        {
            logger.debug("!masterController.isBattling() -> this:" + this + " target:" + target);
            return;
        }

        if(position.isSame(target.getPosition()))
        {
            logger.debug("isSame -> this:" + this + " target:" + target);
            return;
        }

        logger.info(getName() + " 向 " + target.getName() +  " 寻路 -> sourcePosition:" + position + "  target:" + target);

        RouteUtils.getRoutePath(battleController, this, target);

//        if(isSameGoTarget(target))
//        {
//            logger.debug("isSameGoTarget -> this:" + this + " target:" + target);
//            return;
//        }
//
//        Position targetPosition = getAtkPosition(target);
//        if(targetPosition == null)
//            return;
//
//        if(goTargetPosition != null && targetPosition.isSame(goTargetPosition))
//            return;
//
//        logger.info(getName() + " 向 " + target.getName() +  " 寻路 -> sourcePosition:" + position + "  target:" + target);
//
//        RouteUtils.getRoutePath(battleController, this, target);
//
//        goTarget = target;
//        goTargetPosition = targetPosition;
//        goTargetOriginalPosMap.put(goTarget.getUniqueId(), goTarget.position);
//

//        if(!GameServer.ROUTE_TO_CLIENT)
//        {
//            GameServer.getInstance().startThread(() -> {
//
//                battleController.getAstar().getPath(new Node(position.getIntX(), position.getIntY()), new Node(targetPosition.getIntX(), targetPosition.getIntY()), pathNodes -> {
//
//                    logger.info(uniqueId + " -> " + getName() + " 从服务器获取到路线:" + pathNodes);
//                    if(isDie())
//                        return;
//
//                    SGWarProto.S2C_SoldierRoute.Builder sendToClient = SGWarProto.S2C_SoldierRoute.newBuilder();
//                    sendToClient.setUniqueId(uniqueId);
//                    SGCommonProto.Pos.Builder pos = null;
//                    for(Node node : pathNodes)
//                    {
//                        pos = SGCommonProto.Pos.newBuilder();
//                        pos.setX(node.grid.x);
//                        pos.setY(node.grid.y);
//                        sendToClient.addRoute(pos);
//                    }
//                    battleController.dispatchMsg(SGMainProto.E_MSG_ID.MsgID_War_SoldierRoute_VALUE, sendToClient.build().toByteArray());
//
//                    List<Position> changeRouteList = BattleUtils.changeRoute(pathNodes, moveSpeed.getAtt());
//                    initRoute(changeRouteList);
//
//                    setStatus(E_BattleAObjectStatus.GOING);
//
//                    logger.info(uniqueId + " -> " + getName() + " 从服务器获取转换后的路线:" + changeRouteList);
//                });
//            });
//        }


//        SGWarProto.S2C_SoldierRoute.Builder request = SGWarProto.S2C_SoldierRoute.newBuilder();
//        request.setUniqueId(id);
//        request.setStart(position.parsePos());
//        request.setEnd(targetPosition.parsePos());
//        battleController.dispatchMsg(SGMainProto.E_MSG_ID.MsgID_War_SoldierRoute_VALUE, request.build().toByteArray());


    }

    public boolean goTargetSame(Position targetPosition)
    {
        return goTargetPosition != null && targetPosition.isSame(goTargetPosition);
    }


    public void setRouteTarget(BattleAObject target, Position targetPosition)
    {
        goTarget = target;
        goTargetPosition = targetPosition;
        goTargetOriginalPosMap.put(goTarget.getUniqueId(), goTarget.position);
    }


    /**
     * 获取攻击一个目标时的起点(加上目标和自己的体积计算)
     * @param target
     * @return
     */
    public Position getAtkPosition(BattleAObject target)
    {
        int startX = 0, endX = 0, startY = 0, endY = 0;
        ObjectBound targetBound = target.getObjectBound();
        if(position.x <= target.position.x)
        {
            Grid leftUpGrid = targetBound.getVertex(DirectionHelper.Direction.LeftUp);
            Grid leftDownGrid = targetBound.getVertex(DirectionHelper.Direction.LeftDown);
            startX = leftDownGrid.x - 1;
            endX = leftDownGrid.x - sprite.gridArea;
            if(position.y <= target.position.y)
            {
                //x--,y++
                startY = leftDownGrid.y - sprite.gridArea;
                endY = leftUpGrid.y + sprite.gridArea;
            }
            else
            {
                //x--,y--
                startY = leftUpGrid.y + sprite.gridArea;
                endY = leftDownGrid.y - sprite.gridArea;
            }
        }
        else
        {
            Grid rightUpGrid = targetBound.getVertex(DirectionHelper.Direction.RightUp);
            Grid rightDownGrid = targetBound.getVertex(DirectionHelper.Direction.RightDown);
            startX = rightDownGrid.x + 1;
            endX = rightDownGrid.x + sprite.gridArea;
            if(position.y <= target.position.y)
            {
                //x++,y++
                startY = rightDownGrid.y - sprite.gridArea;
                endY = rightUpGrid.y + sprite.gridArea;
            }
            else
            {
                //x++,y--
                startY = rightUpGrid.y + sprite.gridArea;
                endY = rightDownGrid.y - sprite.gridArea;
            }
        }

        AstarMap astarMap = battleController.getAstar().getAstarMap();
        Position result = null;
        startX = startX < astarMap.minX ? astarMap.minX : startX;
        startX = startX > astarMap.maxX ? astarMap.maxX : startX;
        startY = startY < astarMap.minY ? astarMap.minY : startY;
        startY = startY > astarMap.maxY ? astarMap.maxY : startY;
        endX = endX < astarMap.minX ? astarMap.minX : endX;
        endX = endX > astarMap.maxX ? astarMap.maxX : endX;
        endY = endY < astarMap.minY ? astarMap.minY : endY;
        endY = endY > astarMap.maxY ? astarMap.maxY : endY;
        if(startX > endX)
        {
            choose:
            for (int i = startX; i >= endX; i--)
            {
                if(startY > endY)
                {
                    for (int j = startY; j >= endY; j--)
                    {
                        if(!astarMap.isObstacleToApplication(i, j))
                        {
//                            System.out.println("选定目标点 -> {x=" + i + ",y=" + j + "}");
                            result = new Position(i, j);
                            break choose;
                        }
                    }
                }
                else
                {
                    for (int j = startY; j <= endY; j++)
                    {
                        if(!astarMap.isObstacleToApplication(i, j))
                        {
//                            System.out.println("选定目标点 -> {x=" + i + ",y=" + j + "}");
                            result = new Position(i, j);
                            break choose;
                        }
                    }
                }
            }
        }
        else
        {
            choose:
            for (int i = startX; i <= endX; i++)
            {
                if(startY > endY)
                {
                    for (int j = startY; j >= endY; j--)
                    {
                        if(!astarMap.isObstacleToApplication(i, j))
                        {
//                            System.out.println("选定目标点 -> {x=" + i + ",y=" + j + "}");
                            result = new Position(i, j);
                            break choose;
                        }
                    }
                }
                else
                {
                    for (int j = startY; j <= endY; j++)
                    {
                        if(!astarMap.isObstacleToApplication(i, j))
                        {
//                            System.out.println("选定目标点 -> {x=" + i + ",y=" + j + "}");
                            result = new Position(i, j);
                            break choose;
                        }
                    }
                }
            }
        }

//        float ax = position.getX();
//        float ay = position.getY();
//        float bx = target.position.getX();
//        float by = target.position.getY();
//        float distance = BattleUtils.getDistance(position, target.position);
//        Position result = null;
//        if(distance > 0)
//        {
//            float aax = ax + (sprite.gridArea - 1) / distance * (bx - ax);
//            float aay = ay + (sprite.gridArea - 1) / distance * (by - ay);
//            float bbx = bx + (target.getSprite().gridArea - 1) / distance * (ax - bx);
//            float bby = by + (target.getSprite().gridArea - 1) / distance * (ay - by);
//
//            result = new Position(bbx - (aax - ax), bby - (aay - ay));
//        }

        return result;
    }






    public SoldierConfig getSoldier() {
        return (SoldierConfig) sprite;
    }


    public List<Position> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<Position> routeList) {
        this.routeList = routeList;
    }

    public int getCurRouteIndex() {
        return curRouteIndex;
    }

    public void setCurRouteIndex(int curRouteIndex) {
        this.curRouteIndex = curRouteIndex;
    }




}
