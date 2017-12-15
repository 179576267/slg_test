package com.douqu.game.battle.controller.sprite;


import com.douqu.game.core.config.challenge.HeroTempleConfig;
import com.douqu.game.core.config.challenge.LevelConfig;
import com.douqu.game.core.config.battle.MonsterConfig;
import com.douqu.game.core.config.challenge.OfficialRankConfig;
import com.douqu.game.core.config.sprite.MasterConfig;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.protobuf.SGCommonProto;

/**
 * Created by bean on 2017/9/13.
 */
public class MonsterController extends SpriteController
{
    private MonsterConfig monsterConfig;

    private int[] monsterLv;

    /**
     * 主将
     */
    private MasterConfig masterConfig;

    private int masterLv;

    /**
     * 关卡的时候是 关卡ID
     * 官阶的时候是 官阶;位置
     */
    private String indexInfo;

    public MonsterController(SGCommonProto.E_BATTLE_TYPE battleType, String data)
    {
        this.indexInfo = data;

        DataFactory dataFactory = DataFactory.getInstance();
        if(SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVE_INSTANCE == battleType)
        {
            LevelConfig levelConfig = dataFactory.getGameObject(DataFactory.LEVEL_KEY, Integer.parseInt(data));
            this.masterConfig = dataFactory.getGameObject(DataFactory.MASTER_KEY, levelConfig.master);
            this.masterLv = levelConfig.masterLv;
            this.monsterConfig = dataFactory.getGameObject(DataFactory.MONSTER_KEY, levelConfig.monsterId);
            this.monsterLv = levelConfig.monsterLv;
        }
        else if(SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVE_OFFICIAL_RANK == battleType)
        {
            String[] strs = data.split(ConstantFactory.SEMICOLON);
            int officialRankId = Integer.parseInt(strs[0]);

            OfficialRankConfig officialRankConfig = dataFactory.getGameObject(DataFactory.OFFICIAL_RANK_KEY, officialRankId);
            this.masterConfig = dataFactory.getGameObject(DataFactory.MASTER_KEY, officialRankConfig.master);
            this.masterLv = officialRankConfig.masterLv;
            this.monsterConfig = dataFactory.getGameObject(DataFactory.MONSTER_KEY, officialRankConfig.monsterId);
            this.monsterLv = officialRankConfig.monsterLv;
        }
        else if(SGCommonProto.E_BATTLE_TYPE.BATTLE_TYPE_PVE_HERO_TEMPLE == battleType)
        {
            HeroTempleConfig heroTempleConfig = DataFactory.getInstance().getGameObject(DataFactory.HERO_TEMPLE_KEY, Integer.parseInt(data));
            this.masterConfig = dataFactory.getGameObject(DataFactory.MASTER_KEY, heroTempleConfig.master);
            this.masterLv = heroTempleConfig.masterLv;
            this.monsterConfig = dataFactory.getGameObject(DataFactory.MONSTER_KEY, heroTempleConfig.monsterId);
            this.monsterLv = heroTempleConfig.monsterLv;
        }
    }


    public MonsterConfig getMonsterConfig() {
        return monsterConfig;
    }

    public int[] getMonsterLv() {
        return monsterLv;
    }

    public MasterConfig getMasterConfig() {
        return masterConfig;
    }

    public int getMasterLv() {
        return masterLv;
    }

    @Override
    public String getObjectIndex()
    {
        return indexInfo;
    }
}
