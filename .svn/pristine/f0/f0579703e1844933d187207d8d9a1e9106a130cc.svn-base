package com.douqu.game.core.e;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-12-01 12:05
 */
public enum E_ProfileVersion {

    DEV("dev", "开发版"),
    TEST("test", "测试版"),
    PRODUCT("product", "线上版");

    String key;

    String msg;

    E_ProfileVersion(String key, String msg)
    {
        this.key = key;
        this.msg = msg;
    }


    public String getKey() {
        return key;
    }

    public String getMsg() {
        return msg;
    }

    public static E_ProfileVersion forKey(String key)
    {
        for(E_ProfileVersion pv : values())
        {
            if(pv.getKey().equals(key))
                return pv;
        }

        return null;
    }

    @Override
    public String toString() {
        return "{" +
                "key='" + key + '\'' +
                ", msg='" + msg + '\'' +
                "} ";
    }
}
