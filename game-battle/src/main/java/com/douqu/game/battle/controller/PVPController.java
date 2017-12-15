package com.douqu.game.battle.controller;

import com.douqu.game.battle.controller.sprite.PlayerController;
import com.douqu.game.battle.controller.sprite.SpriteController;
import com.douqu.game.battle.entity.aobject.BattleMasterAObject;
import com.douqu.game.battle.entity.tmp.PlayerBattleTmp;
import com.douqu.game.battle.entity.tmp.SpriteBattleTmp;
import com.douqu.game.core.config.sprite.MasterConfig;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.protobuf.SGCommonProto;
import org.apache.log4j.Logger;

/**
 * Created by bean on 2017/7/25.
 */
public class PVPController extends BattleController {

    Logger logger = Logger.getLogger(PVPController.class);

    public PVPController(String battleId, PlayerController player, SpriteController target, SGCommonProto.E_BATTLE_TYPE battleType,
                         SGCommonProto.Pos leftBottom, SGCommonProto.Pos rightTop)
    {
        super(battleId, player, target, battleType, leftBottom, rightTop);
    }

    @Override
    public void init()
    {
        super.init();

        //队伍2的主将
        PlayerController targetController = (PlayerController) target;
        MasterConfig masterConfig = DataFactory.getInstance().getGameObject(DataFactory.MASTER_KEY, targetController.getPlayer().master);
        if(masterConfig == null)
            masterConfig = (MasterConfig) DataFactory.getInstance().getDataList(DataFactory.MASTER_KEY).get(0);

        BattleMasterAObject masterAObject2 = new BattleMasterAObject(target, masterConfig,
                createMasterUniqueId + ConstantFactory.BATTLE_TEAM_2, player.getPlayer().getLv(),
                ConstantFactory.BATTLE_TEAM_2, getMasterPos(ConstantFactory.BATTLE_TEAM_2, masterConfig.gridArea));

        SpriteBattleTmp pbt2 = new PlayerBattleTmp();
        pbt2.init(this, target, masterAObject2, ConstantFactory.BATTLE_TEAM_2, battleType);
        target.setAttachment(pbt2);

        if(!target.isOnlinePlayer())
        {
            pbt2.setReady(true);
        }

        pbt2.setTargetBattleTmp(player.getAttachment());
        player.getAttachment().setTargetBattleTmp(pbt2);
    }




    @Override
    public int getStar(SpriteBattleTmp spriteBattleTmp, int winTeam)
    {
        return 0;
    }


}
