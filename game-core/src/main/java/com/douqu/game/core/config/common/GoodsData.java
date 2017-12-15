package com.douqu.game.core.config.common;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.entity.EntityObject;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.protobuf.SGCommonProto;

/**
 * Created by zhenfei on 2017/7/14.
 *
 */
public class GoodsData extends EntityObject {

    public int type;

    public int id;

    public int value;

    public GoodsData(String value)
    {
        try {
            String[] strs = value.split(ConstantFactory.SEMICOLON);
            this.type = Integer.parseInt(strs[0]);
            this.id = Integer.parseInt(strs[1]);
            this.value = Integer.parseInt(strs[2]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public GoodsData(int type, int id, int value)
    {
        this.type = type;
        this.id = id;
        this.value = value;
    }

    public GoodsData loadFrom(ByteBuffer buffer){
        type = buffer.readShort();
        id = buffer.readInt();
        value = buffer.readInt();
        return this;
    }

    public void writeTo(ByteBuffer buffer){
        buffer.writeShort(type);
        buffer.writeInt(id);
        buffer.writeInt(value);
    }

    public GoodsData(int type, int value)
    {
        this(type,0,value);
    }

    public GoodsData(){}


    public void checkAsset(Class cls, String key)
    {
        if(DataFactory.getInstance().getGameObject(DataFactory.ASSET_KEY, id) == null)
        {
            System.out.println(cls.getSimpleName() + " setVariable " + key + " Error -> type : " + type + " id:" + id + " num:" + value);
        }
    }

    public void checkProp(Class cls, String key)
    {
        if(DataFactory.getInstance().getGameObject(DataFactory.PROP_KEY, id) == null)
        {
            System.out.println(cls.getSimpleName() + " setVariable " + key + " Error -> type : " + type + " id:" + id + " num:" + value);
        }
    }

    public void check(Class cls, String key)
    {
        int typeKey = 0;
        if(type == SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_PROPS_VALUE){
            typeKey = DataFactory.PROP_KEY;
        }else if(type == SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_ASSETS_VALUE){
            typeKey = DataFactory.ASSET_KEY;
        }else if(type == SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_CARDS_VALUE){
            typeKey = DataFactory.CARD_KEY;
        }

        if(typeKey == 0)
            return;

        if(DataFactory.getInstance().getGameObject(typeKey, id) == null)
        {
            System.out.println(cls.getSimpleName() + " setVariable " + key + " Error -> type : " + type + " id:" + id + " num:" + value);
        }
    }


    public SGCommonProto.RewardInfo.Builder parseCommonObject()
    {
        SGCommonProto.RewardInfo.Builder data = SGCommonProto.RewardInfo.newBuilder();
        data.setType(SGCommonProto.E_GOODS_TYPE.forNumber(type));
        data.setId(id);
        data.setNum(value);
        return data;
    }


//    public SGCommonProto.FlushData parseFlushData(int curValue)
//    {
//        SGCommonProto.FlushData.Builder data = SGCommonProto.FlushData.newBuilder();
//        SGCommonProto.E_DATA_TYPE dataType = SGCommonProto.E_DATA_TYPE.forNumber(type);
//        data.setType(dataType==null ? SGCommonProto.E_DATA_TYPE.DATA_TYPE_UNKNOWN : dataType);
//        data.setId(id);
//        data.setChange(value);
//        data.setCurValue(curValue);
//        return data.build();
//    }


    @Override
    public String toString() {
        return "GoodsData{" +
                "type=" + type +
                ", id=" + id +
                ", value=" + value +
                "} " + super.toString();
    }
}
