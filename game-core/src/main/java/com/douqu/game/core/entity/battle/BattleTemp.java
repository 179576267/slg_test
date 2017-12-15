package com.douqu.game.core.entity.battle;

import com.douqu.game.core.entity.EntityObject;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-12-06 14:17
 */
public class BattleTemp extends EntityObject{

    /**
     * 如果是玩家的时候表示是玩家流水号
     * 是关卡或者什么官阶的时候是其它信息组合而成的字符串
     */
    private String indexInfo;

    private int star;

    /**
     * 主将数据
     */
    private SpriteTemp master;

    /**
     * 士兵数据
     */
    private List<SpriteTemp> soldierList;

    public BattleTemp()
    {
        soldierList = new CopyOnWriteArrayList<>();
    }

    public void addSoldierTemp(SpriteTemp spriteTemp)
    {
        soldierList.add(spriteTemp);
    }


    public String getIndexInfo() {
        return indexInfo;
    }

    public void setIndexInfo(String indexInfo) {
        this.indexInfo = indexInfo;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public SpriteTemp getMaster() {
        return master;
    }

    public void setMaster(SpriteTemp master) {
        this.master = master;
    }

    public List<SpriteTemp> getSoldierList() {
        return soldierList;
    }

    public void setSoldierList(List<SpriteTemp> soldierList) {
        this.soldierList = soldierList;
    }

    @Override
    public String toString() {
        return "{" +
                "indexInfo='" + indexInfo + '\'' +
                ", star=" + star +
                ", master=" + master +
                ", soldierList=" + soldierList +
                "} ";
    }
}
