package com.douqu.game.battle.controller;

import com.douqu.game.battle.controller.sprite.MonsterController;
import com.douqu.game.battle.controller.sprite.PlayerController;
import com.douqu.game.battle.controller.sprite.SpriteController;
import com.douqu.game.battle.entity.aobject.BattleMasterAObject;
import com.douqu.game.battle.entity.tmp.MonsterBattleTmp;
import com.douqu.game.battle.entity.tmp.SpriteBattleTmp;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.protobuf.SGCommonProto;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-10-27 17:01
 */
public class PVEController extends BattleController
{

    public PVEController(String battleId, PlayerController player, SpriteController target,
                         SGCommonProto.E_BATTLE_TYPE battleType, SGCommonProto.Pos leftBottom, SGCommonProto.Pos rightTop)
    {
        super(battleId, player, target, battleType, leftBottom, rightTop);
    }

    @Override
    public void init()
    {
        super.init();

        //队伍2的主将
        MonsterController monsterController = (MonsterController) target;
        BattleMasterAObject masterAObject2 = new BattleMasterAObject(target, monsterController.getMasterConfig(),
                createMasterUniqueId + ConstantFactory.BATTLE_TEAM_2, monsterController.getMasterLv(),
                ConstantFactory.BATTLE_TEAM_2, getMasterPos(ConstantFactory.BATTLE_TEAM_2, monsterController.getMasterConfig().gridArea));

        SpriteBattleTmp pbt2 = new MonsterBattleTmp();
        pbt2.setReady(true);
        pbt2.init(this, target, masterAObject2, ConstantFactory.BATTLE_TEAM_2, battleType);
        target.setAttachment(pbt2);

        pbt2.setTargetBattleTmp(player.getAttachment());
        player.getAttachment().setTargetBattleTmp(pbt2);
    }
}
