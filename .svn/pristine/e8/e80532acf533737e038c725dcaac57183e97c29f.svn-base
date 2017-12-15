package com.douqu.game.core.factory;

/**
 * Author : Bean
 * 2017-06-19 16:34
 */
public class CodeFactory {

    public final static boolean TEST = true;

    /** 获取战斗服务器启动端口 */
    public final static int GET_BATTLE_TCP_PORT = 11;

    /** 告诉主服务器战斗服务器启动了 */
    public final static int START_BATTLE_TCP_SERVER = 12;

    /** 战斗初始化 */
    public final static int BATTLE_INIT = 13;

    /** 关闭战斗服务器 */
    public final static int STOP_SERVER = 14;

    /** 加载配置文件 */
    public final static int LOAD_DATA = 15;

    /** 关闭主服务器 */
    public final static int CLOSE_MAIN_SERVER = 99;

    /** 这个值以下的都是服务器之间的通信接口 */
    public final static int SERVER_MSG_MAX = 100;

    public final static int PING = 103;



    /************************************GM接口********************************************************************/
    public final static int GM_LOGIN                = 0x00101;

    public final static int GM_LOGOUT               = 0x00102;


    public final static int GM_PLAYER_QUERY         = 0x00201;

    public final static int GM_PLAYER_OPTION        = 0x00202;

    public final static int GM_PLAYER_ONLINE        = 0x00203;

    public final static int GM_PLAYER_ALL           = 0x00204;

    public final static int GM_PLAYER_MAIL          = 0x00205;

    public final static int GM_PLAYER_RECHARGE      = 0x00206;

    public final static int GM_PLAYER_CLEAR         = 0x00207;


    public final static int GM_SYSTEM_SEND_NOTICE   = 0x00301;

    public final static int GM_SYSTEM_RECHARGE_LIST = 0x00302;


    public final static int GM_ERROR                = 0x00901;


    public final static String GM_ADD_PROP     = "addProp";
    public final static String GM_ADD_ASSET    = "addAsset";
    public final static String GM_ADD_CARD     = "addCard";
    public final static String GM_ADD_MONEY    = "addMoney";
    public final static String GM_ADD_EXP      = "addExp";
    public final static String GM_ADD_CARD_EXP = "addCardExp";
    public final static String GM_INVINCIBLE   = "invincible";


    public static final String QUERY_TYPE_NICK_NAME = "queryNickName";

    public static final String QUERY_TYPE_UID = "queryUid";

    public static final String QUERY_TYPE_OBJECT_INDEX = "queryObjectIndex";



    /**********************************路线服务器**********************************************************************/

    public final static int ROUTE_GET_PATH = 0x00101;

    public static boolean isServer(int code)
    {
        return code <= SERVER_MSG_MAX;
    }

}
