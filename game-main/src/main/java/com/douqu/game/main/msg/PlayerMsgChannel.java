package com.douqu.game.main.msg;


import com.alibaba.fastjson.JSONObject;
import com.bean.core.util.HttpJsonUtils;
import com.douqu.game.core.config.card.CardConfig;
import com.douqu.game.core.config.sprite.MasterConfig;
import com.douqu.game.core.config.goods.PropConfig;
import com.douqu.game.core.controller.PlayerController;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.entity.db.*;
import com.douqu.game.core.e.ReturnMessage;
import com.douqu.game.core.config.common.CommonData;
import com.douqu.game.core.entity.ext.BagInfo;
import com.douqu.game.core.entity.ext.TaskInfo;
import com.douqu.game.core.entity.ext.SettingInfo;
import com.douqu.game.core.entity.ext.data.card.CardData;
import com.douqu.game.core.entity.ext.data.world.WorldOfficialRankBean;
import com.douqu.game.core.factory.*;
import com.douqu.game.core.protobuf.*;
import com.douqu.game.main.database.mapper.UserMapper;
import com.douqu.game.main.GameServer;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mysql.jdbc.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by bean on 2017/7/21.
 */
@Component
public class PlayerMsgChannel implements AMsgChannel {

    Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private UserMapper userMapper;

    @Override
    public void messageChannel(int code, PlayerController playerController, byte[] data)throws Exception{

        switch (code) {

            case SGMainProto.E_MSG_ID.MsgID_Player_SynBaseData_VALUE:
                playerBaseInfo(playerController);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Player_EditPlayerName_VALUE:
                editPlayerName(playerController, data, code);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Player_EditPlayerAvatar_VALUE:
                editPlayerAvatar(playerController, data, code);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Player_ChangeConsumeRemindStatus_VALUE:
                changeConsumeRemindStatus(playerController, data, code);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Player_ChangeEquippedSkill_VALUE:
                changeEquippedSkill(playerController, data, code);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Player_SettingBoardInit_VALUE:
                settingBoardInit(playerController, data, code);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Player_RedPointRemind_VALUE:
                redPoint(playerController, data, code);
                break;



        }
    }

    private void redPoint(PlayerController playerController, byte[] data, int code) {
        SGPlayerProto.S2C_RedPointRemind.Builder response = SGPlayerProto.S2C_RedPointRemind.newBuilder();
        List<SGCommonProto.E_RED_POINT_TYPE> allRedPointTypes = playerController.getPlayer().getAllRedPointTypes(GameServer.currentTime);
        logger.debug("目前存在的红点提示:\n" + allRedPointTypes.toString());
        response.addAllTypes(allRedPointTypes);
        response.setIsRequest(true);
        playerController.sendMsg(code, response.build().toByteArray());
    }

    /**
     * 初始化设置面板
     * @param playerController
     * @param data
     * @param code
     */
    private void settingBoardInit(PlayerController playerController, byte[] data, int code) {

        WorldOfficialRankBean rankBean = GameServer.getInstance().getWorldManager().getWorldInfo().getOfficialRankInfo().
                getRankInfoByObjectIndex(playerController.getPlayer());
        SGPlayerProto.S2C_SettingBoardInit.Builder response = SGPlayerProto.S2C_SettingBoardInit.newBuilder();
        SettingInfo settingInfo =  playerController.getPlayer().getExtInfo(SettingInfo.class);
        response.setOfficialRankId(rankBean == null ? 15 : rankBean.getRankId());
        response.setChangeNameTimes(settingInfo.getPlayerNameChangeTimes());
        response.setNewUnlockSkillId(settingInfo.getNewUnlockSkillId());
        logger.debug(response);
        settingInfo.removeNewUnlockSkill();
        //返回给客户端
        playerController.sendMsg(code, response.build().toByteArray());
    }

    /**
     * 改变上阵技能
     * @param playerController
     * @param data
     * @param code
     */
    private void changeEquippedSkill(PlayerController playerController, byte[] data, int code) {
        SGPlayerProto.C2S_ChangeEquippedSkill request = null;
        try {
            request = SGPlayerProto.C2S_ChangeEquippedSkill.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if (request == null) {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        int addSkillId = request.getAddSkillId();
        int removeSkillId = request.getRemoveSkillId();
        logger.debug("changeEquippedSkill 请求参数->addSkillId:" + addSkillId + ", removeSkillId:" + removeSkillId);
        if(DataFactory.getInstance().getGameObject(DataFactory.SKILL_KEY, addSkillId) == null){
            logger.debug("技能不存在：" + addSkillId);
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }
        if(DataFactory.getInstance().getGameObject(DataFactory.SKILL_KEY, removeSkillId) == null){
            logger.debug("技能不存在：" + removeSkillId);
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        MasterConfig masterConfig = DataFactory.getInstance().getGameObject(DataFactory.MASTER_KEY, playerController.getPlayer().master);
        if(masterConfig == null){
            logger.debug("主将不存在->masterId:" + playerController.getPlayer().master);
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.SERVER_DATA_ERROR));
            return;
        }

        List<Integer> lockSkillIds = new ArrayList<>();
        for(CommonData commonData : masterConfig.skills){
            if(playerController.getPlayer().getLv() >= commonData.id){
                lockSkillIds.add(commonData.value);
            }
        }

        if(!lockSkillIds.contains(addSkillId)){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.SKILL_UNLOCK));
            return;
        }
        if(!lockSkillIds.contains(removeSkillId)){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.SKILL_UNLOCK));
            return;
        }

        SettingInfo settingInfo = playerController.getPlayer().getExtInfo(SettingInfo.class);
        logger.debug("已上阵的技能：" + settingInfo.getEquipSkillIds());
        if(!settingInfo.getEquipSkillIds().contains(removeSkillId) || settingInfo.getEquipSkillIds().contains(addSkillId)){
            logger.debug("removeSkillId技能未装备或者addSkillId技能已添加");
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }
        settingInfo.changeEquipSkill(addSkillId, removeSkillId);
        logger.debug("已上阵的技能：" + settingInfo.getEquipSkillIds());
        //返回给客户端
        playerController.sendMsg(code, SGPlayerProto.S2C_ChangeEquippedSkill.newBuilder().setAddSkillId(addSkillId)
                .setRemoveSkillId(removeSkillId)
                .build().toByteArray());

    }

    /**
     * 改变消费提醒状态
     * @param playerController
     * @param data
     * @param code
     */
    private void changeConsumeRemindStatus(PlayerController playerController, byte[] data, int code) {
        SGPlayerProto.C2S_ChangeConsumeRemindStatus request = null;
        try {
            request = SGPlayerProto.C2S_ChangeConsumeRemindStatus.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if (request == null) {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        int key = request.getKey();
        boolean isShowRemind = request.getIsShowRemind();
        SettingInfo settingInfo = playerController.getPlayer().getExtInfo(SettingInfo.class);
        logger.debug("改变消费提醒前的数据: " + settingInfo.getConsumeNotRemindList());
        if(isShowRemind){
            settingInfo.getConsumeNotRemindList().remove(Integer.valueOf(key));
        }else {
            if(!settingInfo.getConsumeNotRemindList().contains(Integer.valueOf(key))){
                settingInfo.getConsumeNotRemindList().add(key);
            }
        }
        logger.debug("改变消费提醒请求参数 key: " + key + ", isShowRemind:" + isShowRemind);
        //改变消费提醒后的数据
        logger.debug("改变消费提醒后的数据: " + settingInfo.getConsumeNotRemindList());
        //返回给客户端
        playerController.sendMsg(code, SGPlayerProto.S2C_ChangeConsumeRemindStatus.newBuilder().setKey(key).setIsShowRemind(isShowRemind).build().toByteArray());

    }


    /**
     * 修改名称
     * @param playerController
     * @param data
     * @param code
     */
    private void editPlayerName(PlayerController playerController, byte[] data, int code) {
        SGPlayerProto.C2S_EditPlayerName request = null;
        try {
            request = SGPlayerProto.C2S_EditPlayerName.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if (request == null) {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }
        String name = request.getPlayerName();
        logger.debug("修改的名字请求参数:" + name);
        if(StringUtils.isNullOrEmpty(name)){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.NAME_NOT_EMPTY));
            return;
        }
        name = name.replaceAll(" ", "");
        if(name.length() == 0)
        {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.NAME_NOT_EMPTY));
            return;
        }
        if(name.length() > ConstantFactory.NAME_MAX_LENGTH){
            logger.debug("修改的名字过长:" + name);
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.NAME_TOO_LONG));
            return;
        }
        SettingInfo settingInfo = playerController.getPlayer().getExtInfo(SettingInfo.class);
        if(!settingInfo.isFirstChangeName()){
            //判断钻石是否够
            if(playerController.getPlayer().money < 10){
                logger.debug("钻石不足，拥有：" + playerController.getPlayer().money + "， 需要：" + 10);
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN,  WordFactory.getWord(WordFactory.ASSET_NOT_ENOUGH, 1));
                return;
            }
        }


        JSONObject jsonObject = getJsonObject(playerController, name, null);

        JSONObject result = HttpJsonUtils.httpPost(ConfigFactory.AUTH_SERVER_HTTP_BASE + "player/updateRecord", jsonObject);
        if(result == null){
            logger.error("请求认证服务器验证名称合法性出错：");
            return;
        }
        logger.info("请求认证服务器名字是否重复参数：" + result.toString());
        String resultCode = result.getString("code");
        if(Integer.parseInt(resultCode) == 0){//成功
            //扣除资源
            if(!settingInfo.isFirstChangeName()){
                playerController.getPlayer().addMoney(-10);
            }
            logger.debug("修改的名字合法,返回成功");
            settingInfo.addPlayerNameChangeTimes();
            String beforeName = playerController.getName();
            playerController.getPlayer().setName(name);
            GameServer.getInstance().getWorldManager().playerModifyName(beforeName, name, playerController);
            //返回给客户端
            playerController.sendMsg(code, SGPlayerProto.S2C_EditPlayerName.newBuilder().setPlayerName(name).build().toByteArray());
        }else if(resultCode.equals(ReturnMessage.NICKNAME_EXITS.getCode())){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.NAME_EXIST));
            return;
        }else {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.SERVER_DATA_ERROR));
            return;
        }
    }


    /**
     * 修改头像
     * @param playerController
     * @param data
     * @param code
     */
    private void editPlayerAvatar(PlayerController playerController, byte[] data, int code) {
        SGPlayerProto.C2S_EditPlayerAvatar request = null;
        try {
            request = SGPlayerProto.C2S_EditPlayerAvatar.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        if (request == null) {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        String avatar = request.getPlayerAvatar();
        logger.debug("修改的头像请求参数:" + avatar);
        if(StringUtils.isNullOrEmpty(avatar)){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        JSONObject jsonObject = getJsonObject(playerController, null, avatar);
        if(jsonObject == null){
            return;
        }
        logger.info("请求认证服务器修改头像参数：" + jsonObject.toString());
        JSONObject result = HttpJsonUtils.httpPost(ConfigFactory.AUTH_SERVER_HTTP_BASE + "player/updateRecord", jsonObject);
        if(result == null){
            logger.error("返回出错");
            return;
        }
        logger.info("请求认证服务器修改头像返回：" + result.toString());
        String resultCode = result.getString("code");
        if(Integer.parseInt(resultCode) == 0){//成功
            playerController.getPlayer().avatar = avatar;
            //返回给客户端
            playerController.sendMsg(code, SGPlayerProto.S2C_EditPlayerAvatar.newBuilder().setPlayerAvatar(avatar).build().toByteArray());
        }else {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.SERVER_DATA_ERROR));
            return;
        }



    }
    /**
     *  获取修改认证服务器参数的json对象
     * @param playerController
     * @param name
     * @param avatar
     * @return
     */
    private JSONObject getJsonObject(PlayerController playerController, String name, String avatar) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("channel", playerController.getPlayer().channel);
        try {
            jsonObject.put("account",  playerController.getPlayer().getAccount().split("_")[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        jsonObject.put("playerName", name);
        jsonObject.put("avatar", avatar);
        jsonObject.put("serverId", GameServer.getInstance().getServerId());
        return jsonObject;
    }





    public void playerBaseInfo(PlayerController playerController)
    {
        SGPlayerProto.S2C_SynBaseData.Builder response = SGPlayerProto.S2C_SynBaseData.newBuilder();

        SGCommonProto.BaseData.Builder data = SGCommonProto.BaseData.newBuilder();

        Player player = playerController.getPlayer();

        SGCommonProto.PlayerBaseInfo.Builder baseInfo = SGCommonProto.PlayerBaseInfo.newBuilder();
        baseInfo.setPlayerIndex(player.getObjectIndex());
        baseInfo.setNickName(player.getName());
        baseInfo.setAvatar(player.avatar);
        baseInfo.setLv(player.getLv());
        baseInfo.setVipLv(player.getVipLevel());
        baseInfo.setExp(player.getExp());
        baseInfo.setFc(player.fc);
        baseInfo.setMasterId(player.master);
        baseInfo.setCamptypeValue(player.camp);

        data.setBaseInfo(baseInfo);

        BagInfo bagInfo = player.getExtInfo(BagInfo.class);
        CardData cardData = bagInfo.getCardData();

        SGCommonProto.CardGroup.Builder cardGroup = null;
        SGCommonProto.Card.Builder cardInfo = null;
        SGCommonProto.Accessory.Builder accessory = null;
        SGCommonProto.Equip.Builder equip = null;
        int groupId = 0;
        cardGroup = SGCommonProto.CardGroup.newBuilder();
        cardGroup.setGroupId(++groupId);
        cardGroup.setCardGroupType(SGCommonProto.E_CARDGROUP_TYPE.CARDGROUP_TYPE_BATTLE);
        for(CardDB cardDB : cardData.getBattleCardList())
        {
            cardInfo = SGCommonProto.Card.newBuilder();
            cardInfo.setId(cardDB.id);
            cardInfo.setLv(cardDB.getLv());
            cardInfo.setStar(cardDB.star);
            cardInfo.setCurExp(cardDB.getExp());
            cardInfo.setFc(cardDB.fc);
            //饰品
            for(Map.Entry<Integer,AccessoryDB> entry : cardDB.getAccessoryMap().entrySet()){
                accessory = SGCommonProto.Accessory.newBuilder();
                accessory.setId(entry.getValue().id);
                accessory.setIntensifyLv(entry.getValue().getLv());
                accessory.setCurrentExp(entry.getValue().getExp());
                accessory.setUpLv(entry.getValue().upLv);
                cardInfo.addAccessory(accessory);
            }
            //装备
            for(Map.Entry<Integer,EquipDB> entry : cardDB.getEquipMap().entrySet()){
                equip = SGCommonProto.Equip.newBuilder();
                equip.setId(entry.getValue().id);
                equip.setLv(entry.getValue().getLv());
                cardInfo.addEquip(equip);
            }
            //宿命列表
            cardInfo.addAllActiveFateList(cardDB.getActiveFateList());
            cardGroup.addCards(cardInfo);
        }

        data.addCardGroups(cardGroup);

        cardGroup = SGCommonProto.CardGroup.newBuilder();
        cardGroup.setGroupId(++groupId);
        cardGroup.setCardGroupType(SGCommonProto.E_CARDGROUP_TYPE.CARDGROUP_TYPE_SLEEP);
        for(CardDB cardDB : cardData.getSleepCardList())
        {
            cardInfo = SGCommonProto.Card.newBuilder();
            cardInfo.setId(cardDB.id);
            cardInfo.setLv(cardDB.getLv());
            cardInfo.setStar(cardDB.star);
            cardInfo.setCurExp(cardDB.getExp());
            cardInfo.setFc(cardDB.fc);

            //饰品
            for(Map.Entry<Integer,AccessoryDB> entry : cardDB.getAccessoryMap().entrySet()){
                accessory = SGCommonProto.Accessory.newBuilder();
                accessory.setId(entry.getValue().id);
                accessory.setIntensifyLv(entry.getValue().getLv());
                accessory.setCurrentExp(entry.getValue().getExp());
                accessory.setUpLv(entry.getValue().upLv);
                cardInfo.addAccessory(accessory);
            }
            //装备
            for(Map.Entry<Integer,EquipDB> entry : cardDB.getEquipMap().entrySet()){
                equip = SGCommonProto.Equip.newBuilder();
                equip.setId(entry.getValue().id);
                equip.setLv(entry.getValue().getLv());
                cardInfo.addEquip(equip);
            }
            //宿命列表
            cardInfo.addAllActiveFateList(cardDB.getActiveFateList());
            cardGroup.addCards(cardInfo);
        }
        data.addCardGroups(cardGroup);

        List<PropDB> propList = bagInfo.getPropList();
        SGCommonProto.BagProp.Builder propInfo = null;
        for (PropDB prop : propList) {
            propInfo = SGCommonProto.BagProp.newBuilder();
            propInfo.setId(prop.id);
            propInfo.setCount(prop.count);
            data.addProps(propInfo);
        }

        Map<Integer, Integer> assetMap = bagInfo.getAssetData();
        SGCommonProto.FlushData.Builder assetData = null;
        for (Integer key : assetMap.keySet()) {
            assetData = SGCommonProto.FlushData.newBuilder();
            assetData.setType(SGCommonProto.E_GOODS_TYPE.GOODS_TYPE_ASSETS);
            assetData.setId(key);
            assetData.setChange(0);
            assetData.setCurValue(assetMap.get(key));

            data.addData(assetData);
        }

        TaskInfo taskInfo = player.getExtInfo(TaskInfo.class);
        Map<Integer,TaskDB> underwayMainTaskMap = taskInfo.underwayMainTaskMap;
        SGCommonProto.TaskDetail.Builder taskDetail = null;
        for(Map.Entry<Integer,TaskDB> entry : underwayMainTaskMap.entrySet())
        {
            taskDetail = SGCommonProto.TaskDetail.newBuilder();
            TaskDB taskDB = entry.getValue();
            taskDetail.setTaskId(taskDB.id);
            taskDetail.setCurrentDemand(taskDB.currentDemand);
            taskDetail.setStatus(SGCommonProto.E_TASK_STATUS.forNumber(taskDB.status));
            data.setTaskDetail(taskDetail);
            break;
        }

        SettingInfo settingInfo = player.getExtInfo(SettingInfo.class);
        //设置装备的技能和消费钻石不提醒的key
        data.addAllEquippedSkillIds(settingInfo.getEquipSkillIds());
        data.addAllConsumeNotRemindKeys(settingInfo.getConsumeNotRemindList());
        response.setUserBaseData(data);
//        logger.debug("playerBaseInfo:" + response.toString());
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Player_SynBaseData_VALUE, response.build().toByteArray());
    }


}
