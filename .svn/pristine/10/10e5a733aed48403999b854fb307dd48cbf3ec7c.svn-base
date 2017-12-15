package com.douqu.game.battle.controller;

import com.douqu.game.battle.controller.sprite.PlayerController;
import com.douqu.game.battle.controller.sprite.SpriteController;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.protobuf.SGCommonProto;

/**
 * 官阶PVE
 * @Author: Bean
 * @Description:
 * @Date: 2017-10-27 14:20
 */
public class PVPOfficialRankController extends PVPController {

    /**
     * 官阶数据
     */
    private String officialRankData;

    public PVPOfficialRankController(String battleId, PlayerController player, SpriteController target,
                                     SGCommonProto.E_BATTLE_TYPE battleType, SGCommonProto.Pos leftBottom, SGCommonProto.Pos rightTop,
                                     String officialRankData)
    {
        super(battleId, player, target, battleType, leftBottom, rightTop);

        this.officialRankData = officialRankData;
    }

    @Override
    public void init()
    {
        super.init();

        //这里设置官阶战数据
        battleInfo.getTeam2Info().setIndexInfo(officialRankData);
    }


}
