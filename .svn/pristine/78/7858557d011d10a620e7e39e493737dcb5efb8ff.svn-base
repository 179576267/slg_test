package com.douqu.game.core.entity;

import com.alibaba.druid.util.StringUtils;
import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.util.LoadUtils;

import java.lang.reflect.Field;

/**
 * Author : Bean
 * 2017-07-06 15:34
 */
public class GameObject extends EntityObject {

    public int id;

    public String objectIndex;

    public String name;

    public String remark;


    public String getVariable(String key) {
        try {
            Field field = LoadUtils.getDeclaredField(this, key);
            if(field == null)
                return "0";

            field.setAccessible(true);//如果是私有的则需要设置这个
            Class valueClass = field.getType();
            if (valueClass == Integer.TYPE)
                return String.valueOf(field.getInt(this));
            if (valueClass == Boolean.TYPE)
                return String.valueOf(field.getBoolean(this));
            if (valueClass == Long.TYPE)
                return String.valueOf(field.getLong(this));
            if (valueClass == Double.TYPE)
                return String.valueOf(field.getDouble(this));
            if (valueClass == Boolean.TYPE)
                return String.valueOf(field.getBoolean(this));
            return (String) field.get(this);
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }


    public static GameObject cloneObject(GameObject eo)
    {
        try
        {
            String clsName = eo.getClass().getName();
            GameObject obj = (GameObject)Class.forName(clsName).newInstance();
            eo.copyTo(obj);
            return obj;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void copyTo(GameObject obj)
    {
        obj.id = this.id;
        obj.name = this.name;
        obj.objectIndex = this.objectIndex;
        obj.remark = this.remark;
    }


    public void writeTo(ByteBuffer buffer)
    {
        buffer.writeInt(id);
        buffer.writeUTF(name);
        buffer.writeUTF(objectIndex);
    }

    public void loadFrom(ByteBuffer buffer)
    {
        id = buffer.readInt();
        name = buffer.readUTF();
        objectIndex = buffer.readUTF();
    }


    public void check(){}

    public void setVariable(String key, String value) {
        try {
            if (value.length() == 0)
                return;

            if(ConstantFactory.IGNORE.equals(key))
                return;

            Field field = LoadUtils.getDeclaredField(this, key);
            field.setAccessible(true);//如果是私有的则需要设置这个

            Class valueClass = field.getType();
            if (valueClass == Integer.TYPE)
                field.setInt(this, StringUtils.isEmpty(value) ? 0 : Integer.parseInt(value));
            else if (valueClass == Short.TYPE)
                field.setShort(this, StringUtils.isEmpty(value) ? 0 : Short.parseShort(value));
            else if (valueClass == Float.TYPE)
                field.setFloat(this, StringUtils.isEmpty(value) ? 0 : Float.parseFloat(value));
            else if (valueClass == Long.TYPE)
                field.set(this, StringUtils.isEmpty(value) ? 0 : Long.parseLong(value));
            else if (valueClass == Double.TYPE)
                field.set(this, StringUtils.isEmpty(value) ? 0 : Double.parseDouble(value));
            else if(valueClass == Boolean.TYPE)
                field.set(this, StringUtils.isEmpty(value) ? false : value.equals("1"));
            else
                field.set(this, StringUtils.isEmpty(value) ? "" : value);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObjectIndex() {
        return objectIndex;
    }

    public void setObjectIndex(String objectIndex) {
        this.objectIndex = objectIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                '}';
    }
}
