package com.douqu.game.core.config.battle;

import com.douqu.game.core.config.card.CardConfig;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-10-27 10:58
 */
public class MonsterConfig extends GameObject {

    public CardConfig[] cards;

    public int type;


    @Override
    public void setVariable(String key, String value)
    {

        if("cards".equals(key))
        {
            String[] strs = value.split(ConstantFactory.SEMICOLON);
            cards = new CardConfig[strs.length];
            for(int i = 0; i < strs.length; i++)
            {
                cards[i] = DataFactory.getInstance().getGameObject(DataFactory.CARD_KEY, Integer.parseInt(strs[i]));
                if(cards[i] == null)
                {
                    System.out.println("Load Monster card is null -> cardId:" + strs[i] + " monsterId:" + id);
                }
            }
        }
        else
            super.setVariable(key, value);
    }
}
