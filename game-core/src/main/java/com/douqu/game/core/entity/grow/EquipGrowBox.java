//package com.douqu.game.core.entity.grow;
//
//import com.bean.core.buffer.ByteBuffer;
//import com.douqu.game.core.config.goods.EquipConfig;
//import com.douqu.game.core.entity.GameObject;
//import com.douqu.game.core.entity.db.LvDB;
//
//
///**
//* @author: Gavin.
//* Description:
//* Date: 2017/11/2 0002 下午 2:53
//* Huan Yu Copyright (c) 2017 All Rights Reserved.
//*/
//public class EquipGrowBox extends LvDB {
//
//
//    public EquipGrowBox(GameObject master)
//    {
//        super(master);
//
//        this.lv = 0;
//    }
//
//    /**装备合成次数*/
//    public int upCount;
//
//    public void loadFrom(ByteBuffer buffer)
//    {
//        super.loadFrom(buffer);
//
//        upCount = buffer.readInt();
//    }
//
//
//
//
//    public void writeTo(ByteBuffer buffer)
//    {
//        super.writeTo(buffer);
//
//        buffer.writeInt(upCount);
//    }
//
//    @Override
//    public void reset() {
//        super.reset();
//        upCount = 0;
//    }
//
//    /**
//     * 获取属性加成
//     * @param attrId
//     * @return
//     */
//    public int getAttribute(int attrId)
//    {
//        EquipConfig equip = (EquipConfig) getMaster();
//
//        return equip.getAttribute(attrId, lv);
//    }
//
//
//
//}