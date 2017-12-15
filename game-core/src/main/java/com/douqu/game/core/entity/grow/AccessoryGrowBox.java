//package com.douqu.game.core.entity.grow;
//
//import com.bean.core.buffer.ByteBuffer;
//import com.douqu.game.core.config.goods.AccessoryConfig;
//import com.douqu.game.core.entity.GameObject;
//import com.douqu.game.core.entity.db.LvDB;
//
///**
//* @author: Gavin.
//* Description:
//* Date: 2017/11/6 0006 下午 5:29
//* Huan Yu Copyright (c) 2017 All Rights Reserved.
//*/
//public class AccessoryGrowBox extends LvDB {
//
//    /**
//     * 进阶等级
//     */
//    public int upLv;
//
//    public AccessoryGrowBox(GameObject master)
//    {
//        super(master);
//
//        this.lv = 0;
//    }
//
//
//
//    //读取存入数据库
//    public void loadFrom(ByteBuffer buffer) {
//        super.loadFrom(buffer);
//        upLv = buffer.readShort();
//    }
//
//    public void writeTo(ByteBuffer byteBuffer){
//        super.writeTo(byteBuffer);
//        byteBuffer.writeShort(upLv);
//    }
//
//
//    /**
//     * 获取属性加成
//     * @param attrId
//     * @return
//     */
//    public int getAttribute(int attrId)
//    {
//        AccessoryConfig accessory = (AccessoryConfig) getMaster();
//
//        return accessory.getAttribute(attrId, lv) + accessory.getUpAttribute(attrId, upLv);
//    }
//}
