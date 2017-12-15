package com.douqu.game.core.config.common;

import com.douqu.game.core.entity.EntityObject;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.protobuf.SGCommonProto;

/**
 * Created by bean on 2017/7/14.
 */
public class CommonData extends EntityObject {

    public int id;

    public int value;

    public CommonData(String value)
    {
        String[] strs = value.split(ConstantFactory.SEMICOLON);
        this.id = Integer.parseInt(strs[0]);
        this.value = Integer.parseInt(strs[1]);

    }


    public CommonData(int id, int value)
    {
        this.id = id;
        this.value = value;
    }

    public CommonData(){}


    public SGCommonProto.CommonObject.Builder parseCommonObject()
    {
        SGCommonProto.CommonObject.Builder data = SGCommonProto.CommonObject.newBuilder();
        data.setId(id);
        data.setValue(value);
        return data;
    }



    @Override
    public String toString() {
        return "Config{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}
