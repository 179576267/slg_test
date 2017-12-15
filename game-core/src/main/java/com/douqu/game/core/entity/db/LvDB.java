package com.douqu.game.core.entity.db;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.config.common.CommonLvUp;
import com.douqu.game.core.config.goods.AccessoryConfig;
import com.douqu.game.core.config.card.CardConfig;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.entity.EntityObject;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.DataFactory;

import java.util.List;

/**
* @Author: Bean
* @Description:
* @Date: 2017-10-31 18:33
*/
public class LvDB extends EntityObject {

    /**
     * 等级
     */
    public int lv;

    /**
     * 当前总经验
     */
    public int exp;

    /**
     * 升级
     */
    private Object master;

    public LvDB(Object master)
    {
        this.master = master;

        reset();
    }

    public void reset(){
        if(master instanceof EquipDB)
            this.lv = 0;
        else if(master instanceof Player)
            this.lv = 1;
        else if(master instanceof AccessoryDB)
            this.lv = 0;
        else if(master instanceof CardDB)
            this.lv = 1;
        exp = 0;
    }




    public void writeTo(ByteBuffer buffer)
    {
        buffer.writeShort(lv);
        buffer.writeInt(exp);
    }


    public void loadFrom(ByteBuffer buffer)
    {
        lv = buffer.readShort();
        exp = buffer.readInt();
        updateLv();
    }


    public void lvUp(int value)
    {
        lv += value;
    }

    /**
     * 加经验
     * @param value
     */
    public void addExp(int value)
    {
        if(value <= 0){
            return;
        }
        int beforeExp = this.exp;

        this.exp += value;
        exp = exp < 0 ? 0 : exp;

        if(exp != beforeExp)
            updateLv();
    }

    public void updateLv()
    {
        List<CommonLvUp> list = null;
        if(master instanceof CardDB)
        {
            list = DataFactory.getInstance().getDataList(DataFactory.CARD_LV_KEY);
        }
        else if(master instanceof Player)
        {
            list = DataFactory.getInstance().getDataList(DataFactory.PLAYER_LV_KEY);
        }
        else if(master instanceof AccessoryDB)
        {
            list = DataFactory.getInstance().getDataList(DataFactory.ACCESSORY_INTENSIFY_KEY);
        }

        if(list != null)
        {
            int start = lv, end = list.size();
            for(int i = start; i < end; i++)
            {
                if(exp < list.get(i).totalExp)
                    break;

                this.lv = list.get(i).id;
            }
        }
    }

}
