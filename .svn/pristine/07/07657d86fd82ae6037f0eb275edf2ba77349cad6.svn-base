package com.douqu.game.core.config.sprite;


import com.douqu.game.core.config.common.CommonData;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.util.LoadUtils;
import com.douqu.game.core.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-10-16 16:55
 */
public class MasterConfig extends Sprite {

    /** 主将技能 id需要的主将等级， value 对应的skill id */
    public CommonData[] skills;


    public MasterConfig()
    {
        this.unitType = ConstantFactory.UNIT_TYPE_LAND;
    }

    @Override
    public void check()
    {
        super.check();

        if(skills == null)
        {
            System.out.println("Load Sprite skills is null! id:" + id);
        }
    }

    public List<Integer> getSkills(int lv)
    {
        List<Integer> result = new ArrayList<>();
        for(CommonData data : skills)
        {
            if(data.id <= lv)
                result.add(data.value);
        }

        return result;
    }

    @Override
    public void setVariable(String key, String value)
    {
        if("skills".equals(key))
        {
            if(Utils.isErrorValue(value))
            {
                System.out.println("Load Master skills is null,Please check!");
                skills = new CommonData[0];
            }
            else
            {
                skills = LoadUtils.loadDataToArray(key, value);
                for(CommonData skill : skills)
                {
                    if(DataFactory.getInstance().getGameObject(DataFactory.SKILL_KEY, skill.value) == null)
                    {
                        System.out.println("Master setVariable Error -> skill : " + value + " id:" + id);
                    }
                    if(skill.id <= 0)
                    {
                        System.out.println("Master setVariable Error -> need level  error : " + value + " id:" + value + " id:" + id);
                    }
                }
            }
        }
        else
            super.setVariable(key, value);
    }



}
