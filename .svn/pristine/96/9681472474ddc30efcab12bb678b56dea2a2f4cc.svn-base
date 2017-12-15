package com.douqu.game.main.msg;

import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.controller.PlayerController;
import com.douqu.game.core.database.model.PlayerModel;
import com.douqu.game.core.e.E_MailSenderType;
import com.douqu.game.core.entity.db.MailDB;
import com.douqu.game.core.entity.db.PrivacyDB;
import com.douqu.game.core.entity.ext.BagInfo;
import com.douqu.game.core.entity.ext.MailInfo;
import com.douqu.game.core.entity.ext.data.mail.DialogueData;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.WordFactory;
import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.protobuf.SGMainProto;
import com.douqu.game.core.protobuf.SGPlayerProto;
import com.douqu.game.main.GameServer;
import com.douqu.game.main.service.PlayerService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Gavin.
 * Description:
 * Date: 2017/12/11 0011 下午 2:41
 * Huan Yu Copyright (c) 2017 All Rights Reserved.
 */
public class MailMsgChannel implements AMsgChannel  {

    Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private PlayerService playerService;


    @Override
    public void messageChannel(int code, PlayerController playerController, byte[] data) throws Exception {
        if(playerController == null)
            return;
        MailInfo mailInfo = playerController.getPlayer().getExtInfo(MailInfo.class);

        switch (code) {
            case SGMainProto.E_MSG_ID.MsgID_Mail_List_VALUE:
                //邮件列表显示
                SGPlayerProto.C2S_MailList c = SGPlayerProto.C2S_MailList.parseFrom(data);
                getMailList(playerController, c.getMailTypeValue(), mailInfo);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Mail_Detail_VALUE:
                //读取邮件
                SGPlayerProto.C2S_MailDetail detail = SGPlayerProto.C2S_MailDetail.parseFrom(data);
                mailDetail(playerController, detail.getMailTypeValue(), detail.getMailId(), mailInfo);
                break;
            case SGMainProto.E_MSG_ID.MsgID_Mail_GetAuxiliary_VALUE:
                //领取附件
                SGPlayerProto.C2S_MailGetAuxiliary auxiliary = SGPlayerProto.C2S_MailGetAuxiliary.parseFrom(data);
                getAuxiliaryList(playerController,auxiliary.getMailId(),mailInfo);
            case SGMainProto.E_MSG_ID.MsgID_Mail_PrivacyList_VALUE:
                //私信列表
                SGPlayerProto.C2S_MailPrivacyList privacyList = SGPlayerProto.C2S_MailPrivacyList.parseFrom(data);
                getPrivacyList(privacyList.getMailTypeValue(), mailInfo, playerController);
            case SGMainProto.E_MSG_ID.MsgID_Mail_PrivacyDetail_VALUE:
                //私信详情
                SGPlayerProto.C2S_MailPrivacyDetail privacyDetail = SGPlayerProto.C2S_MailPrivacyDetail.parseFrom(data);
                getPrivacyDetail(privacyDetail.getPrivacyId(),mailInfo,playerController);
            case SGMainProto.E_MSG_ID.MsgID_Mail_SendPrivacy_VALUE:
                //发送回复私信
                SGPlayerProto.C2S_MailSendPrivacy sendPrivacy = SGPlayerProto.C2S_MailSendPrivacy.parseFrom(data);
                sendPrivacy(playerController,sendPrivacy,mailInfo);
            default:
                break;
        }
    }

    /***
     * 获取邮件列表
     * @param type
     */
    public void getMailList(PlayerController playerController,int type,MailInfo mailInfo){
        //获取某种类型的邮件集合
        List<MailDB> list = mailInfo.mailMap.get(type);
        SGPlayerProto.S2C_MailList.Builder b =  SGPlayerProto.S2C_MailList.newBuilder();
        SGCommonProto.Mail.Builder mail = SGCommonProto.Mail.newBuilder();
        for (MailDB mailDB : list) {
            //1.标题,2.内容，3.发送者，4是否已读，附件
            mail.setId(mailDB.id);
            mail.setTitle(mailDB.title);
            mail.setIsGet(mailDB.isGet);
            mail.setSender(mailDB.target);
            mail.setSendTime(mailDB.sendTime);
            mail.setIsRead(mailDB.isRead);
            //附件
            getAuxiliary(type,mail,mailDB);
            b.addMail(mail);
        }

        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Mail_List_VALUE, b.build().toByteArray());
    }


    /***
     * 邮件详情 设置已读邮件
     * @param playerController
     * @param type
     * @param mailId
     */
    public void mailDetail(PlayerController playerController, int type, int mailId ,MailInfo mailInfo){
        MailDB mailDB = getMail(mailInfo.getMailList(type), mailId);
        if(mailDB == null){
            //邮件不存在
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.MAIL_NOT_EXIST,mailDB.id));
            return;
        }
        SGPlayerProto.S2C_MailDetail.Builder b = SGPlayerProto.S2C_MailDetail.newBuilder();
        SGCommonProto.Mail.Builder mail  = SGCommonProto.Mail.newBuilder();
        if(!mailDB.isRead)
            mailDB.isRead = true;
        mail.setId(mailDB.id);
        mail.setTitle(mailDB.title);
        mail.setIsGet(mailDB.isGet);
        mail.setSender(mailDB.target);
        mail.setSendTime(mailDB.sendTime);
        mail.setIsRead(mailDB.isRead);
        mail.setContent(mailDB.content);
        getAuxiliary(type,mail,mailDB);
        b.setMail(mail);
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Mail_List_VALUE, b.build().toByteArray());
    }


    //附件
    public void getAuxiliary(int type,SGCommonProto.Mail.Builder mail,MailDB mailDB){
        SGCommonProto.FlushData.Builder goodsGet = null;
        //系统邮件
        if(type == SGCommonProto.E_MAIL_TYPE.MAIL_TYPE_SYSTEM_VALUE){
            for (GoodsData goodsData : mailDB.auxiliaryList) {
                goodsGet = SGCommonProto.FlushData.newBuilder();
                goodsGet.setId(goodsData.id);
                goodsGet.setType(SGCommonProto.E_GOODS_TYPE.forNumber(goodsData.type));
                goodsGet.setChange(goodsData.value);
                mail.addExtGoods(goodsGet);
            }
        }
    }

    /***
     * 私信列表
     * @param type
     * @param mailInfo
     * @param playerController
     */
    public void getPrivacyList(int type,MailInfo mailInfo,PlayerController playerController){
        SGPlayerProto.S2C_MailPrivacyList.Builder b = SGPlayerProto.S2C_MailPrivacyList.newBuilder();
        SGCommonProto.Privacy.Builder privacy = null;
        if (type != SGCommonProto.E_MAIL_TYPE.MAIL_TYPE_PRIVACY_VALUE) {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }
        for (PrivacyDB privacyDB : mailInfo.privacyList) {
            //当私信最后一条不是自己时候显示
            if(privacyDB.targetIndex != playerController.getPlayer().getAccount()){
                //私信
                privacy = SGCommonProto.Privacy.newBuilder();
                privacy.setSendTime(privacyDB.sendTime);
                privacy.setTargetIndex(privacyDB.targetIndex);
                //头像
                privacy.setTargetAvatar(getAvatar(privacyDB.targetIndex));
                b.addPrivacy(privacy);
            }
        }
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Mail_PrivacyList_VALUE, b.build().toByteArray());
    }

    /***
     * 私信详情
     * @param mailInfo
     * @param playerController
     */
    public void getPrivacyDetail(int privacyId,MailInfo mailInfo,PlayerController playerController){
        SGPlayerProto.S2C_MailPrivacyDetail.Builder b = SGPlayerProto.S2C_MailPrivacyDetail.newBuilder();
        SGCommonProto.Privacy.Builder  privacy = SGCommonProto.Privacy.newBuilder();
        SGCommonProto.Dialogue.Builder dialogue = null;
        for (PrivacyDB privacyDB : mailInfo.privacyList) {
            if(!privacyDB.isRead)
                privacyDB.isRead = true;
            if(privacyId == privacyDB.id){
                for (DialogueData dialogueData : privacyDB.replyList) {
                    dialogue = SGCommonProto.Dialogue.newBuilder();
                    dialogue.setSendTime(dialogueData.sendTime);
                    dialogue.setTargetIndex(privacyDB.targetIndex);
                    dialogue.setAvatar(getAvatar(privacyDB.targetIndex));
                    dialogue.setContent(dialogueData.content);
                    dialogue.setMailSendType(SGCommonProto.E_MAIL_SENDER_TYPE.forNumber(dialogueData.senderType.getCode()));
                    privacy.addDialogue(dialogue);
                    break;
                }
            }
            //私信
            b.setPrivacy(privacy);
            break;
        }
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Mail_PrivacyDetail_VALUE, b.build().toByteArray());
    }


    /***
     * 领取附件
     * @param playerController
     * @param mailId
     */
    public void getAuxiliaryList(PlayerController playerController,int mailId,MailInfo mailInfo) {

        SGPlayerProto.S2C_MailGetAuxiliary.Builder b = SGPlayerProto.S2C_MailGetAuxiliary.newBuilder();

        MailDB mailDB = null;
        List<MailDB> mailList = null;
        BagInfo bagInfo = playerController.getPlayer().getExtInfo(BagInfo.class);
        //单个领取
        if(mailId > ConstantFactory.ZERO){
            mailDB = getMail(getMailInfo(playerController).getMailList(SGCommonProto.E_MAIL_TYPE.MAIL_TYPE_SYSTEM_VALUE),mailId);
            if(mailDB == null){
                //邮件不存在
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.MAIL_NOT_EXIST,mailDB.id));
                return;
            }
            if(mailDB.isGet){
                //此邮件的奖励已领取不能再次领取
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.ANNEX_HAS_BEEN_RECEIVED,mailDB.id));
                return;
            }
            setReadMail(mailDB, bagInfo);
            b.setMailId(mailId);
        }else{
            //一键领取
            mailList = mailInfo.mailMap.get(SGCommonProto.E_MAIL_TYPE.MAIL_TYPE_SYSTEM_VALUE);
            for (MailDB mailDB1 : mailList){
                if (mailDB1 != null && !mailDB1.isGet){
                    setReadMail(mailDB1, bagInfo);
                }
            }
        }
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Mail_GetAuxiliary_VALUE, b.build().toByteArray());
    }


  /*  public void sendPrivacy(PlayerController playerController,String nickName,MailInfo mailInfo, String content){
        if(nickName == null || nickName.equals("")){
            return;
        }

        PlayerModel playerModel =  playerService.getPlayerByName(nickName);
        if(playerModel == null){
            //用户不存在
            return;
        }
        long sendTime = System.currentTimeMillis();
        PrivacyDB privacyDB = new PrivacyDB();
        privacyDB.id =  ++mailInfo.mailNumber;
        privacyDB.content = content;
        privacyDB.targetIndex = playerModel.getAccount();
        privacyDB.sendTime = sendTime;

        List<DialogueData> list = new ArrayList<>();
        DialogueData dialogueData = new DialogueData();
        dialogueData.content = content;
        dialogueData.sendTime = sendTime;
        dialogueData.senderType = E_MailSenderType.MAIL_TYPE_SENDER;

        list.add(dialogueData);
        privacyDB.replyList = list;
        if(mailInfo.privacyList.size() > 20)
            mailInfo.privacyList.remove(0);
        mailInfo.privacyList.add(privacyDB);
    }*/

    /***
     * 发送回复私信
     * @param playerController
     * @param sendPrivacy
     * @param mailInfo
     */
    public void sendPrivacy(PlayerController playerController ,SGPlayerProto.C2S_MailSendPrivacy sendPrivacy,MailInfo mailInfo){

        PlayerModel playerModel = null;
        String content = "";
        if(sendPrivacy == null){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }

        if(sendPrivacy.getContent() == null || sendPrivacy.getContent().equals("")){
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
            return;
        }else{
            content = sendPrivacy.getContent();
        }

        if(sendPrivacy.getNickName() != null && !sendPrivacy.getNickName().equals("") && sendPrivacy.getPrivacyId() == 0){
            playerModel =  playerService.getPlayerByName(sendPrivacy.getNickName());
            if(playerModel == null){
                //用户不存在
                playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.USER_DOES_NOT_EXIST));
                return;
            }
        }else {
            playerController.sendAlert(SGCommonProto.E_NOTIFY_TYPE.NOTIFY_TYPE_WARN, WordFactory.getWord(WordFactory.PARAM_ERROR));
        }

        SGPlayerProto.S2C_MailSendPrivacy.Builder b =  SGPlayerProto.S2C_MailSendPrivacy.newBuilder();
        long sendTime = System.currentTimeMillis();
        //回复
        if(checkPrivacyIsExists(mailInfo, sendPrivacy.getPrivacyId())){
            PrivacyDB replay = new PrivacyDB();
            replay.id = sendPrivacy.getPrivacyId();
            replay.sendTime = sendTime;
            replay.content = content;
            DialogueData dialogue = null;
            for (PrivacyDB privacyDB : mailInfo.privacyList){
                if(privacyDB.id == sendPrivacy.getPrivacyId()){
                    replay.targetIndex = playerController.getPlayer().getAccount();
                    if(!replay.isRead)
                        replay.isRead = true;
                    dialogue = new DialogueData();
                    dialogue.content = content;
                    dialogue.sendTime = sendTime;
                    dialogue.senderType = E_MailSenderType.MAIL_TYPE_RECEIVER;
                    privacyDB.replyList.add(dialogue);
                    replay.replyList = privacyDB.replyList;
                    break;
                }
            }
            //发送
        }else{
            PrivacyDB privacyDB = new PrivacyDB();
            privacyDB.id =  ++mailInfo.mailNumber;
            privacyDB.content = content;
            privacyDB.targetIndex = playerModel.getAccount();
            privacyDB.sendTime = sendTime;
            privacyDB.isRead = false;
            List<DialogueData> list = new ArrayList<>();
            DialogueData dialogueData = new DialogueData();
            dialogueData.content = content;
            dialogueData.sendTime = sendTime;
            dialogueData.senderType = E_MailSenderType.MAIL_TYPE_SENDER;

            list.add(dialogueData);
            privacyDB.replyList = list;
            if(mailInfo.privacyList.size() > 20)
                mailInfo.privacyList.remove(0);
            mailInfo.privacyList.add(privacyDB);
        }
        b.setContent(content);
        b.setPrivacyId(sendPrivacy.getPrivacyId());
        b.setNickName(sendPrivacy.getNickName());
        playerController.sendMsg(SGMainProto.E_MSG_ID.MsgID_Mail_SendPrivacy_VALUE, b.build().toByteArray());
    }

    public MailInfo getMailInfo(PlayerController playerController){
        return playerController.getPlayer().getExtInfo(MailInfo.class);
    }

    //在邮件列表中获取单个邮件对象
    public MailDB getMail(List<MailDB> list,int mailId){
        if (list == null || list.size() <= 0)
            return null;
        else
            for (MailDB mailDB:list){
                if(mailId == mailDB.id)
                    return mailDB;
            }
        return null;
    }


    //检查私信列表是否存在此私信
    public boolean checkPrivacyIsExists(MailInfo mailInfo, int privacyId){
        for(PrivacyDB privacyDB : mailInfo.privacyList){
            if(privacyDB.id == privacyId)
                return true;
        }
        return false;
    }


    //设置邮件已读，并把附件物品加入背包
    public void setReadMail(MailDB mailDB, BagInfo bagInfo){
        if(!mailDB.isRead)
            mailDB.isRead = true;
        //奖励加入背包
        for(GoodsData goodsData : mailDB.auxiliaryList){
            bagInfo.addGoods(goodsData);
        }
    }



    /***
     * 判断用户是否在线，如果在线列表中存在那么在在线列表中去，否则离线用户中获取头像
     * @param targetIndex
     * @return
     */
    public String getAvatar(String targetIndex){
        String avatar = null;
        List<PlayerController> onlinePlayers = GameServer.getInstance().getWorldManager().getPlayerList();
        for (PlayerController onlinePlayer : onlinePlayers){
            if(onlinePlayer.getPlayer().getAccount().equals(targetIndex)){
                avatar = onlinePlayer.getPlayer().avatar;
                break;
            }
        }
        if (avatar == null || avatar.equals("")){
            PlayerModel player = playerService.getPlayerByIndex(targetIndex);
            avatar = player.getAvatar();
        }
        return avatar;
    }
}
