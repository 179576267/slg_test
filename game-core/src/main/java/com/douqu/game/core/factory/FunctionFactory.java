package com.douqu.game.core.factory;

import com.douqu.game.core.config.FunctionConfig;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 功能开放的一些属性
 * @Author: Bean
 * @Description:
 * @Date: 2017-11-24 14:26
 */
public class FunctionFactory implements Serializable{

    private static final long serialVersionUID = 1L;


    /**
     * 功能开放检测方法
     * 1.把所有功能开放对象的类型定义成常量
     * 2.定义一个Map,key值是接口指令号,value是功能开放对象的类型
     * 3.在玩家登录时遍历所有功能开放对象,把能开放的功能放进玩家身上的开放功能列表里
     * 4.接收消息入口的地方,去Map里面获取功能开放对象,然后去玩家身上的开放功能列表里查找,
     *   若不在里面则表示不能操作,返回错误信息功能未开放
     */

    /**-----------------------------------------功能开放------------------------------------------**/

    /**大地图*/
    public final static int UNLOCK_BAG_MAP  = 100;
    /**酒馆**/
    public final static int UNLOCK_LOTTERY  = 200;
    /**卡牌养成**/
    public final static int UNLOCK_CARD_CULTIVATE  = 300;
    /**卡牌升级**/
    public final static int UNLOCK_CARD_UP_LV  = 301;
    /**卡牌升星**/
    public final static int UNLOCK_CARD_UP_STAR  = 302;
    /**装备强化**/
    public final static int UNLOCK_EQUIP_INTENSIFY  = 310;
    /**装备合成**/
    public final static int UNLOCK_EQUIP_SYN  = 311;
    /**饰品激活升级**/
    public final static int UNLOCK_ACCESSORY_ACTIVATION_UP_LV = 320;
    /**饰品升阶**/
    public final static int UNLOCK_ACCESSORY_UP_LV = 321;
    /**宿命激活**/
    public final static int UNLOCK_CARD_FATE = 330;
    /**副本系统**/
    public final static int UNLOCK_REPLICA_SYSTEM = 400;
    /**军衔系统**/
    public final static int UNLOCK_Military_SYSTEM = 500;
    /**竞技系统**/
    public final static int UNLOCK_COMPETITIVE_SYSTEM = 600;
    /**英雄圣殿试炼（装备冢）**/
    public final static int UNLOCK_HEROIC_SANCTUARY = 700;
    /**献祭系统**/
    public final static int UNLOCK_ALTAR_SYSTEM = 800;
    /**重生系统**/
    public final static int UNLOCK_RENASCENCE_SANCTUARY = 900;
    /**商店系统**/
    public final static int UNLOCK_SHOP_SYSTEM = 1000;
    /**任务系统**/
    public final static int UNLOCK_TASK_SYSTEM = 1100;






}
