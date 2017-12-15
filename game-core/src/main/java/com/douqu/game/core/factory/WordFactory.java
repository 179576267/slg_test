package com.douqu.game.core.factory;

/**
 * Created by bean on 2017/9/26.
 */
public class WordFactory {

    /** 战斗服务器异常 */
    public final static String BATTLE_SERVER_ERROR = "battle_server_error";
    /** 你的账号已在其它地方登录 */
    public final static String OTHER_LOGIN = "other_login";
    /** 你的账号已被冻结 */
    public final static String LOCK_ACCOUNT = "lock_account";
    /** 非法请求 */
    public final static String ILLEGAL_ERROR = "illegal_error";
    /** 参数错误 */
    public final static String PARAM_ERROR = "param_error";
    /** 名称存在 */
    public final static String NAME_EXIST = "name_exist";
    /** 你已经注册过账户了 */
    public final static String ACCOUNT_EXIST = "account_exist";
    /** 数据异常,请稍后再试 */
    public final static String SERVER_DATA_ERROR = "server_data_error";
    /** 名称过长 **/
    public final static String NAME_TOO_LONG = "name_too_long";
    /** 名称不能为空 **/
    public final static String NAME_NOT_EMPTY = "name_not_empty";
    /** 你的XX(资源)不足够哦 */
    public final static String ASSET_NOT_ENOUGH = "asset_not_enough";
    /** 你的XX(物品)不足够哦 */
    public final static String GOODS_NOT_ENOUGH = "goods_not_enough";
    /** v等级不足 */
    public final static String LEVEL_NOT_ENOUGH = "level_not_enough";
    /** 战斗中不允许进行此操作 */
    public final static String BATTLING_NOT_ALLOW = "battling_not_allow";
    /** 此对象不存在 */
    public final static String OBJECT_NOT_EXIST = "object_not_exist";
    /** CD时间未到 */
    public final static String CD_TIME_ERROR = "cd_time_error";
    /** 你已领取过此奖励 */
    public final static String GET_REWARD_ED = "get_reward_ed";
    /** 地图未解锁 */
    public final static String MAP_NOT_UNLOCK = "map_not_unlock";
    /** 已经升级到最高等级了 */
    public final static String LV_MAX_ERROR = "lv_max_error";
    /** 你还没有获得此卡 */
    public final static String CARD_NOTHING_ERROR = "card_nothing_error";
    /** 此宝箱没有奖励可领取 */
    public final static String BOX_NOTHING_REWARD = "box_nothing_reward";
    /** 领取条件不满足 */
    public final static String CONDITION_ERROR = "condition_error";
    /** 能量不够 */
    public final static String ENERGY_NOT_ENOUGH = "energy_not_enough";
    /** 你没有这个技能 */
    public final static String SKILL_NOT_EXIST = "skill_not_exist";
    /** 当前奖池为空 */
    public final static String CURRENT_LOTTERY = "current_lottery_is_null";
    /** 当前奖池对象为空*/
    public final static String LOTTERY_OBJECT  = "current_lottery_object_is_null";
    /** 当前奖励数组为空 */
    public final static String REWARDS_OBJECT = "rewards_array_is_null";
    /** 背包资源为空*/
    public final static String ASSET_DATA = "asset_data_is_null";
    /**CD时间未到*/
    public final static String CD_TIME_NOT_TIME_YET ="cdTime_not_time_yet";
    /**资源不足*/
    public final static String BAG_ASSET_DEFICIENT = "bag_asset_deficient";
    /** 竞技场挑战次数已经用完 */
    public final static String ARENA_TIMES_NOT_ENOUGH = "arena_times_not_enough";
    /** 竞技场挑战目标排位比自己低 */
    public final static String ARENA_TARGET_LOWER_THAN_ME = "arena_target_lower_than_me";
    /** 竞技场每日奖励已经领取 */
    public final static String ARENA_DAILY_HAS_REWARD = "arena_daily_has_reward";
    /** 竞技场排名不够 */
    public final static String ARENA_RANK_NOT_ENOUGH = "arena_rank_not_enough";
    /** 竞技场兑换奖励组已经兑换过 */
    public final static String ARENA_REWARD_GROUP_ALREADY_REWARD = "arena_reward_group_already_reward";
    /** 竞技场不能扫荡比自己高的玩家 */
    public final static String ARENA_NOT_SWEEP_HEIGHT_PLAYER = "arena_not_sweep_height_player";
    /** 官阶战不能越级挑战 */
    public final static String OFFICIAL_NOT_JUMP_RANK_CHALLENGE = "official_not_jump_rank_challenge";
    /** 官阶战不能扫荡比自己高的玩家 */
    public final static String OFFICIAL_NOT_SWEEP_HEIGHT_PLAYER = "official_not_sweep_height_player";
    /** 官阶战挑战次数已经用完 */
    public final static String OFFICIAL_TIMES_NOT_ENOUGH = "official_times_not_enough";
    /** 玩家暂无官阶 */
    public final static String PLAYER_HAS_NOT_OFFICIAL_RANK = "player_has_not_official_rank";
    /** 官阶战每日奖励已经领取 */
    public final static String OFFICIAL_DAILY_HAS_REWARD = "official_daily_has_reward";
    /** 官阶战兑换奖励组已经兑换过 */
    public final static String OFFICIAL_REWARD_GROUP_ALREADY_REWARD = "official_reward_group_already_reward";
    /** 官阶战积分奖励组已经兑换过 */
    public final static String OFFICIAL_INTEGRAL_REWARD_GROUP_ALREADY_REWARD = "official_integral_reward_group_already_reward";
    /** 官阶战排名不够 */
    public final static String OFFICIAL_RANK_NOT_ENOUGH = "official_rank_not_enough";
    /** 官阶战积分不够 */
    public final static String OFFICIAL_INTAGRAL_NOT_ENOUGH = "official_integral_enough";

    /** 当前祭祀为空 */
    public final static String CURRENT_ALTAR = "current_altar_is_null";

    /** 未达到开启等级 */
    public final static String CURRENT_LEVEL = "current_level_is_not_reached";

    /** 当前次数受限 */
    public final static String CURRENT_NUM_LIMITATION = "current_num_limitation";

    /** 掉落资源为空 */
    public final static String DROP_ASSETS_IS_NULL = "drop_assets_is_null";

    /** 未达到领取奖励的条件 */
    public final static String FAILED_TO_MEET_THE_AWARD_CONDITIONS = "failed_to_meet_the_award_conditions";

    /** 当前的任务不存在 */
    public final static String CURRENT_TASK_IS_NULL = "current_task_is_null";

    /** 当前的任务不存在 */
    public final static String TREASURE_CHEST = "treasure_chest";

    /** 地精商店今天的免费刷新次数已经用完 */
    public final static String FREE_REFRESH_TIMES_NOT_ENOUGH = "free_refresh_times_not_enough";

    /** 商店物品未刷新 */
    public final static String STORE_GOODS_NOT_REFRESH = "store_goods_not_refresh";
    /** 商店物品已经购买过 */
    public final static String STORE_GOODS_HAS_BUY = "store_goods_has_buy";
    /** 商店购买资金不足 */
    public final static String STORE_GOODS_MONEY_NOT_ENOUGH = "store_goods_money_not_enough";
    /**技能未解锁 */
    public final static String SKILL_UNLOCK = "skill_unlock";

    /**需要升级的等级不能小于0*/
    public final static String CARD_LV_NEEDLV_IS_ZERO = "card_lv_needlv_is_zero";

    /**卡牌已升级到最高等级*/
    public final static String CARD_UPGRADED_HIGHEST_LEVEL = "card_upgraded_highest_level";
    /**经验道具不足够*/
    public final static String EXPERIENCE_PROPS_ARE_NOT_ENOUGH ="experience_props_are_not_enough";
    /**经验道具不存在*/
    public final static String EXPERIENCE_PROPS_IS_NULL ="experience_props_is_null";
    /**道具不足够*/
    public final static String PROPS_ARE_NOT_ENOUGH ="props_are_not_enough";
    /**资源不足够*/
    public final static String ASSET_ARE_NOT_ENOUGH ="asset_are_not_enough";
    /**此宿命已激活*/
    public final static String CARD_FATE_HAS_BEEN_ACTIVATED ="card_fate_has_been_activated";
    /**此宿命不存在*/
    public final static String CARD_FATE_NOT_EXIST ="card_fate_not_exist";
    /**此卡牌不在宿命列表中*/
    public final static String CARD_NOT_FATE_LIST ="card_not_fate_list";
    /**背包中卡牌碎片不足够*/
    public final static String BAG_LVUPPROP_ARE_NOT_ENOUGH ="bag_lvupprop_are_not_enough";
    /**强化超过限制*/
    public final static String  EQUIPMENT_STRENGTHENING_OVERRUN = "equipment_strengthening_overrun";
    /**卡牌碎片不够*/
    public final static String  CARD_FRAGMENT_ARE_NOT_ENOUGH = "card_fragment_are_not_enough";
    /**饰品升级到最高等级不能在升级*/
    public final static String ACCESSORY_UPGRADED_HIGHEST_LEVEL = "accessory_upgraded_highest_level";
    /**装备合成到最高等级不能在升级*/
    public final static String EQUIP_UPGRADED_HIGHEST_LEVEL = "equip_upgraded_highest_level";
    /**装备强化到最高等级不能在强化*/
    public final static String EQUIP_INTENSIFY_HIGHEST_LEVEL = "equip_intensify_highest_level";
    /** 卡牌等级不能大于主将等级 */
    public final static String CARD_NOT_GREATER_THAN_PLAYER_LV = "card_not_greater_than_player_lv";
    /** 装备等级不能大于主将等级得两倍 */
    public final static String EQUIP_NOT_GREATER_THAN_PLAYER_LV = "equip_not_greater_than_player_lv";
    /** 上阵卡组的卡片不能重生 */
    public final static String BATTLE_CARD_NOT_REBIRTH = "battle_card_not_rebirth";
    /** 卡片还未培养 */
    public final static String CARD_NOT_TRAIN = "card_not_train";
    /** 卡片已经培养 */
    public final static String CARD_HAS_TRAIN = "card_has_train";
    /** 此卡牌已存在 */
    public final static String CARDS_ALREADY_EXIST = "cards_already_exist";
    /**不能大于携带数量*/
    public final static String CANNOT_BE_GREATER_THAN_THE_NUMBER_OF_CARRY = "cannot_be_greater_than_the_number_of_carry";

    /** 此卡牌已在战斗卡组 */
    public final static String CARD_IN_CARD_BATTLE_GROUP = "card_in_card_battle_group";

    /** 此卡牌不在战斗卡组 */
    public final static String CARD_NOT_IN_CARD_BATTLE_GROUP = "card_not_in_card_battle_group";
    /** 英雄圣殿挑战次数不足 */
    public final static String HERO_TEMPLE_CHALLENGE_TIMES_NOT_ENOUGH = "hero_temple_challenge_times_not_enough";

    /** 当前解锁配置不存在 */
    public final static String UNLOCKED_CONFIGURATION_FILE_DOES_NOT_EXIST = "unlocked_configuration_file_does_not_exist";

    /** 竞技场购买次数超限 */
    public final static String ARENA_BUY_CHALLENGE_TIME_OVERRUN = "arena_buy_challenge_time_overrun";

    /** 月卡存在剩余天数 */
    public final static String MOUTH_CARD_REMAIN_DAY = "mouth_card_remain_day";

    /** vip礼包已经购买过了 */
    public final static String VIP_GIFT_BAG_ALREADY_BUY = "vip_gift_bag_already_buy";

    /** vip等级不足 */
    public final static String VIP_LEVEL_NOT_ENOUGH = "vip_level_not_enough";

    /** 月卡奖励已经领取过了 */
    public final static String MOUTH_CARD_HAS_REWARD_TODAY = "mouth_card_has_reward_today";

    /** 月卡未购买 */
    public final static String MOUTH_CARD_NOT_BUY = "mouth_card_not_buy";

    /** 签到天数已满 */
    public final static String DAILY_SIGN_FULL = "daily_sign_full";

    /** 邮件不存在*/
    public final static String MAIL_NOT_EXIST = "mail_not_exist";

    /** 附件已领取*/
    public final static String ANNEX_HAS_BEEN_RECEIVED = "annex_has_been_received";

    /** 基金已经购买过 **/
    public final static String FUND_HAS_BUY = "fund_has_buy";

    /** 用户不存在*/
    public final static String USER_DOES_NOT_EXIST = "user_does_not_exist";

    /** 首冲未完成*/
    public final static String FIRST_RECHARGE_NOT_COMPLETE = "first_recharge_not_complete";


    public static String getWord(String key, Object... params)
    {
        return DataFactory.getInstance().getWord(key, params);
    }


}
