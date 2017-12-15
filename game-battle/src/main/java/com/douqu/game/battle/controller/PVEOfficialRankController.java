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
public class PVEOfficialRankController extends PVEController {

//    /**
//     * 官阶ID
//     */
//    private int officialRankId;
//
//    /**
//     * 官阶中的位置
//     */
//    private int officialRankIndex;

    public PVEOfficialRankController(String battleId, PlayerController player, SpriteController target,
                                     SGCommonProto.E_BATTLE_TYPE battleType, SGCommonProto.Pos leftBottom, SGCommonProto.Pos rightTop)
    {
        super(battleId, player, target, battleType, leftBottom, rightTop);
//
//        String[] strs = officialRankData.split(ConstantFactory.SEMICOLON);
//        this.officialRankId = Integer.parseInt(strs[0]);
//        this.officialRankIndex = Integer.parseInt(strs[1]);
    }



//    public int getOfficialRankId() {
//        return officialRankId;
//    }
//
//
//    public int getOfficialRankIndex() {
//        return officialRankIndex;
//    }

}
