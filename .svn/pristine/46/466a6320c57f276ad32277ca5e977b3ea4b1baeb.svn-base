package com.douqu.game.battle.controller;

import com.douqu.game.battle.controller.sprite.PlayerController;
import com.douqu.game.battle.controller.sprite.SpriteController;
import com.douqu.game.battle.entity.tmp.SpriteBattleTmp;
import com.douqu.game.core.config.challenge.LevelConfig;
import com.douqu.game.core.config.battle.StarCheckConfig;
import com.douqu.game.core.config.card.CardConfig;
import com.douqu.game.core.entity.db.CardDB;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.protobuf.SGCommonProto;

/**
* Created by bean on 2017/7/25.
*/
public class PVEInstanceController extends PVEController {

    private LevelConfig levelConfig;

    public PVEInstanceController(String battleId, PlayerController player, SpriteController target,
                                 SGCommonProto.E_BATTLE_TYPE battleType,
                                 SGCommonProto.Pos leftBottom, SGCommonProto.Pos rightTop,
                                 String levelId)
    {
        super(battleId, player, target, battleType, leftBottom, rightTop);

        this.levelConfig = DataFactory.getInstance().getGameObject(DataFactory.LEVEL_KEY, Integer.parseInt(levelId));
    }




    @Override
    public int getStar(SpriteBattleTmp spriteBattleTmp, int winTeam)
    {
        if(spriteBattleTmp == null)
            return 0;

        if(spriteBattleTmp.getTeamNo() == ConstantFactory.BATTLE_TEAM_2)
            return 0;

        int result = 0;

        DataFactory dataFactory = DataFactory.getInstance();
        StarCheckConfig starCheckConfig = null;
        for(int sc : levelConfig.starChecks)
        {
            starCheckConfig = dataFactory.getGameObject(DataFactory.STAR_CHECK_KEY, sc);
            if(starCheckConfig.type == ConstantFactory.STAR_TYPE_WIN)
            {
                if(winTeam == spriteBattleTmp.getTeamNo())
                    result += starCheckConfig.star;
            }
            else if(starCheckConfig.type == ConstantFactory.STAR_TYPE_BATTLE_TIME)
            {
                int time = battleTime / 1000;
                if(time >= starCheckConfig.values[0] && time <= starCheckConfig.values[1])
                    result += starCheckConfig.star;
            }
            else if(starCheckConfig.type == ConstantFactory.STAR_TYPE_HP_RATE)
            {
                int hp = spriteBattleTmp.getMasterSoldier().getHp();
                int maxHp = spriteBattleTmp.getMasterSoldier().getMaxHp();
                float temp = (float) hp / maxHp;
                int rate = (int) (temp * 100);
                if(rate >= starCheckConfig.values[0] && rate <= starCheckConfig.values[1])
                    result += starCheckConfig.star;
            }
            else if(starCheckConfig.type == ConstantFactory.STAR_TYPE_CAMP_ID)
            {
                for(CardDB cardDB : spriteBattleTmp.getHistoryCards())
                {
                    if(starCheckConfig.values[0] == cardDB.getConfig().soldier.camp)
                    {
                        result += starCheckConfig.star;
                        break;
                    }
                }
            }
            else if(starCheckConfig.type == ConstantFactory.STAR_TYPE_KILL_COUNT)
            {
                if(spriteBattleTmp.killCount >= starCheckConfig.values[0])
                    result += starCheckConfig.star;
            }
        }
        return result;
    }



}
