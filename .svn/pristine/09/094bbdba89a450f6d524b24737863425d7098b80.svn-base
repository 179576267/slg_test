package com.douqu.game.core.config.drop;

import com.douqu.game.core.entity.EntityObject;
import com.douqu.game.core.factory.ConstantFactory;

/**
 * @author: Gavin
 * Description: 掉落对象
 * Date: 2017-08-03 16:34-34
 * Huan Yu Copyright (c) 2017 All Rights Reserved.
 */

public class DropObject extends EntityObject {

    public int id;//掉落id

    public int count;//数量

    public int odds; //几率


//    public DropObject(String[] values)
//    {
//        id = Integer.parseInt(values[0]);
//        count = Integer.parseInt(values[1]);
//        odds = Integer.parseInt(values[2]);
//    }

    public DropObject(String value)
    {
        String[] values = value.split(ConstantFactory.SEMICOLON);
        id = Integer.parseInt(values[0]);
        count = Integer.parseInt(values[1]);
        odds = Integer.parseInt(values[2]);

        if(count <= 0 || odds <= 0)
        {
            System.out.println("DropObject init error -> count or odds error:" + value);
        }
    }

    public DropObject(){}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOdds() {
        return odds;
    }

    public void setOdds(int odds) {
        this.odds = odds;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", count=" + count +
                ", odds=" + odds +
                "} " + super.toString();
    }
}
