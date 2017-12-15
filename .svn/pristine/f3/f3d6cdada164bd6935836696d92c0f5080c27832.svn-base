package com.douqu.game.battle.server;

import com.douqu.game.battle.controller.BattleController;
import com.douqu.game.battle.controller.sprite.PlayerController;
import com.douqu.game.core.protobuf.SGCommonProto;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by bean on 2017/7/27.
 */
public class WorldManager {

    Logger logger = Logger.getLogger(this.getClass());

    private List<BattleInitInfo> battleInitInfoList = new CopyOnWriteArrayList<>();

    /**
     * key :战斗ID
     */
    private Map<String, BattleInitInfo> battleInitInfoMap = new ConcurrentHashMap<>();

    private List<PlayerController> playerList = new CopyOnWriteArrayList<>();

    private Map<String, PlayerController> playerMap = new ConcurrentHashMap<>();

    private List<BattleController> battleControllerList = new CopyOnWriteArrayList<>();

    private Map<String, BattleController> battleControllerMap = new ConcurrentHashMap<>();



    public void update(long currentTime)
    {
        for(BattleController battleController : battleControllerList)
        {
            if(battleController.isOverdue())
            {
                logger.info("WorldManager update 战斗创建后一直没有开始，删除:" + battleController);
                battleController.destory();
                battleControllerList.remove(battleController);
            }
            else
                battleController.update(currentTime);
        }

        for(BattleInitInfo battleInitInfo : battleInitInfoList)
        {
            if(battleInitInfo.isOverdue())
            {
                System.out.println("WorldManager update 玩家一直没有来创建战斗，战斗过期删除:" + battleInitInfo);
                battleInitInfoList.remove(battleInitInfo);
                battleInitInfoMap.remove(battleInitInfo.getBattleId());
            }
        }
    }



    public void addBattleController(BattleController battleController)
    {
        battleControllerList.add(battleController);
        battleControllerMap.put(battleController.getId(), battleController);
    }

    public void removeBattleController(BattleController battleController)
    {
        battleControllerList.remove(battleController);
        battleControllerMap.remove(battleController.getId());
    }

    public BattleController getBattleController(String battleId)
    {
        return battleControllerMap.get(battleId);
    }



    public void addBattleInitInfo(String battleId, SGCommonProto.E_BATTLE_TYPE battleType, String playerIndex, String targetIndex)
    {
        BattleInitInfo battleInitInfo = new BattleInitInfo(battleId, battleType, playerIndex, targetIndex);
        battleInitInfoList.add(battleInitInfo);
        battleInitInfoMap.put(battleId, battleInitInfo);
    }


    public void removeBattleInitInfo(BattleInitInfo battleInitInfo)
    {
        battleInitInfoList.remove(battleInitInfo);
        battleInitInfoMap.remove(battleInitInfo.getBattleId());
    }

    public BattleInitInfo getBattleInitInfo(String battleId)
    {
       return battleInitInfoMap.get(battleId);
    }


    public PlayerController getPlayerController(String playerIndex)
    {
        return playerMap.get(playerIndex);
    }

    public void playerEnterGame(PlayerController playerController)
    {
        if(getPlayerController(playerController.getObjectIndex()) == null)
        {
            playerList.add(playerController);
            playerMap.put(playerController.getObjectIndex(), playerController);
        }
    }

    public void playerExitGame(PlayerController playerController)
    {
        logger.debug("playerExitGame:" + playerController);

        playerController.destroy();

        playerList.remove(playerController);
        playerMap.remove(playerController.getObjectIndex());
    }

}
