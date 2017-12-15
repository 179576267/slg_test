package com.douqu.game.core.config.task;

import com.douqu.game.core.config.AssetConfig;
import com.douqu.game.core.config.common.CommonData;
import com.douqu.game.core.config.goods.PropConfig;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.util.Utils;

/**
 * @author: Gavin.
 * Description:
 * Date: 2017/11/25 0025 上午 9:51
 * Huan Yu Copyright (c) 2017 All Rights Reserved.
 */
public class CaptureCityBoxConfig extends GameObject {

    public int num;

    /**道具掉落配置*/
    public CommonData[] props;
    /**资源掉落配置*/
    public CommonData[] assets;

    @Override
    public void check() {
        if(props == null)
        {
            System.out.println("CaptureCityBox check props is null -> id:" + id + " name:" + name);
        }

        if(assets == null)
        {
            System.out.println("CaptureCityBox check assets is null -> id:" + id + " name:" + name);
        }

    }

    @Override
    public void setVariable(String key, String value)
    {
        if("props".equals(key))
        {
            if(Utils.isErrorValue(value)){
//                System.out.println("Load ActiveBox props is null,Please check!");
                props = new CommonData[0];
            }else{
                String[] strs = value.split(ConstantFactory.DIVISION);
                props = new CommonData[strs.length];
                for(int i = 0; i < strs.length; i++)
                {
                    props[i] = new CommonData(strs[i]);
                    PropConfig prop = DataFactory.getInstance().getGameObject(DataFactory.PROP_KEY, props[i].id);
                    if(prop == null)
                    {
                        System.out.println("Load CaptureCityBox Error: props is null! id=" + props[i].id);
                    }
                }
            }

        }else if("assets".equals(key)){
            if(Utils.isErrorValue(value)){
//                System.out.println("Load ActiveBox assets is null,Please check!");
                assets = new CommonData[0];
            }else{
                String[] strs = value.split(ConstantFactory.DIVISION);
                assets = new CommonData[strs.length];
                for(int i = 0; i < strs.length; i++)
                {
                    assets[i] = new CommonData(strs[i]);
                    AssetConfig assetConfig = DataFactory.getInstance().getGameObject(DataFactory.ASSET_KEY, assets[i].id);
                    if(assetConfig == null)
                    {
                        System.out.println("Load CaptureCityBox Error: assets is null! id=" + assets[i].id);
                    }
                }
            }
        }
        else
            super.setVariable(key, value);

    }
}
