package com.douqu.game.core.config;


import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.ConfigFactory;

/**
 * 资源(金币，钻石。。。。。。等)
 * Created by bean on 2017/7/14.
 */
public class AssetConfig extends GameObject {

    public boolean vip;

    public String icon;

    /**
     * 不知道干嘛用的
     */
    public String uiIcon;


    @Override
    public void check()
    {
        switch (id)
        {
            case ConfigFactory.ASSET_MONEY_KEY:
                System.out.println("Load Asset ConfigFactory.ASSET_MONEY_KEY -> " + id + " name:" + name);
                break;
            case ConfigFactory.ASSET_EXP_KEY:
                System.out.println("Load Asset ConfigFactory.ASSET_EXP_KEY -> " + id + " name:" + name);
                break;
            case ConfigFactory.ASSET_INTEGRAL_KEY:
                System.out.println("Load Asset ConfigFactory.ASSET_INTEGRAL_KEY -> " + id + " name:" + name);
                break;
        }
    }


    public boolean isMoney() {
        return ConfigFactory.ASSET_MONEY_KEY == id;
    }

    @Override
    public String toString() {
        return "Asset{" +
                "vip=" + vip +
                ", icon='" + icon + '\'' +
                "} " + super.toString();
    }
}
