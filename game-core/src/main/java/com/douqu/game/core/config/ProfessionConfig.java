package com.douqu.game.core.config;

import com.douqu.game.core.config.common.CommonData;
import com.douqu.game.core.entity.GameObject;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.util.Utils;

import java.util.Arrays;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-10-25 12:08
 */
public class ProfessionConfig extends GameObject {

    /**
     * 克制类型(其它职业)
     */
    public int restrictType;

    /**
     * 对各种职业的伤害加成(万分比)
     *
     */
    public CommonData[] damagePlus;

    /**
     * 对各种职业的免伤加成(万分比)
     */
    public CommonData[] exdamagePlus;

    @Override
    public void check() {

        if(damagePlus == null)
        {
            System.out.println("Load Profession Error -> damagePlus is null,id = " + id);
        }
        if(exdamagePlus == null)
        {
            System.out.println("Load Profession Error -> exdamagePlus is null,id = " + id);
        }


    }


    /**
     * 获取伤害加成
     * @param profession
     * @return
     */
    public int getDamagePlus(int profession)
    {
        for(CommonData commonData : damagePlus)
        {
            if(commonData.id == profession)
                return commonData.value;
        }

        return 0;
    }

    /**
     * 获取免伤加成
     * @param profession
     * @return
     */
    public int getExdamagePlus(int profession)
    {
        for(CommonData commonData : exdamagePlus)
        {
            if(commonData.id == profession)
                return commonData.value;
        }

        return 0;
    }




    @Override
    public void setVariable(String key, String value)
    {
       if("damagePlus".equals(key))
       {
           if(Utils.isErrorValue(value))
           {
               damagePlus = new CommonData[0];
           }
           else
           {
               String[] strs = value.split(ConstantFactory.DIVISION);
               damagePlus = new CommonData[strs.length];
               for(int i = 0; i < strs.length; i++)
               {
                   damagePlus[i] = new CommonData(strs[i]);
               }
           }
       }
       else if("exdamagePlus".equals(key))
       {
           if(Utils.isErrorValue(value))
           {
               exdamagePlus = new CommonData[0];
           }
           else
           {
               String[] strs = value.split(ConstantFactory.DIVISION);
               exdamagePlus = new CommonData[strs.length];
               for(int i = 0; i < strs.length; i++)
               {
                   exdamagePlus[i] = new CommonData(strs[i]);
               }
           }
       }
       else
           super.setVariable(key, value);
    }

    @Override
    public String toString() {
        return "Profession{" +
                "damagePlus=" + Arrays.toString(damagePlus) +
                ", exdamagePlus=" + Arrays.toString(exdamagePlus) +
                "} " + super.toString();
    }
}
