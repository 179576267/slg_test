package com.douqu.game.core.config;

import com.douqu.game.core.entity.EntityObject;
import com.douqu.game.core.factory.ConstantFactory;

/**
 * @author: Gavin.
 * Description:
 * Date: 2017/10/17 0017 下午 4:09
 * Huan Yu Copyright (c) 2017 All Rights Reserved.
 */
public class AltarDrop extends EntityObject {

    public int minGrade; // 最小等级
    public int maxGrade; // 最大等级
    public int dropId;   // 掉落id


    public AltarDrop(String value)
    {
        String[] values = value.split(ConstantFactory.SEMICOLON);
        minGrade = Integer.parseInt(values[0]);
        maxGrade = Integer.parseInt(values[1]);
        dropId = Integer.parseInt(values[2]);

        if(minGrade < 0 || maxGrade <= 0)
        {
            System.out.println("AltarDrop init error -> count or minGrade error:" + value);
        }
    }

}
