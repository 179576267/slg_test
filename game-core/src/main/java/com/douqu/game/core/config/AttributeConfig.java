package com.douqu.game.core.config;

import com.douqu.game.core.config.sprite.Sprite;
import com.douqu.game.core.e.E_Attribute;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.util.LoadUtils;

/**
 * Created by bean on 2017/8/9.
 */
public class AttributeConfig extends GameObject {

    @Override
    public void check()
    {
//        System.out.println("Load Attribute id->" + id + " name->" + name + " remark->" + remark);

        if(LoadUtils.getDeclaredField(new Sprite(), name) == null)
        {
            System.out.println("Load Attribute name Error -> " + id + " name -> " + name + " remark -> " + remark);
        }

        switch (name)
        {
            case "atk":
                E_Attribute.ATK.init(this);
                break;
            case "def":
                E_Attribute.DEF.init(this);
                break;
            case "power":
                E_Attribute.POWER.init(this);
                break;
            case "agility":
                E_Attribute.AGILITY.init(this);
                break;
            case "wisdom":
                E_Attribute.WISDOM.init(this);
                break;
            case "cri":
                E_Attribute.CRI.init(this);
                break;
            case "rec":
                E_Attribute.REC.init(this);
                break;
            case "add":
                E_Attribute.ADD.init(this);
                break;
            case "exd":
                E_Attribute.EXD.init(this);
                break;
            case "hit":
                E_Attribute.HIT.init(this);
                break;
            case "eva":
                E_Attribute.EVA.init(this);
                break;
            case "hp":
                E_Attribute.HP.init(this);
                break;
            case "moveSpeed":
                E_Attribute.MOVE_SPEED.init(this);
                break;
            case "attackSpeed":
                E_Attribute.ATTACK_SPEED.init(this);
                break;
        }

        System.out.println(name + " -> " + E_Attribute.forNumber(id));

    }


}
