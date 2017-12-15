package com.douqu.game.core.entity.ext;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.config.common.GoodsData;
import com.douqu.game.core.controller.PlayerController;
import com.douqu.game.core.entity.Player;
import com.douqu.game.core.entity.WorldInfo;
import com.douqu.game.core.entity.db.MailDB;
import com.douqu.game.core.entity.db.PrivacyDB;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author: Gavin.
 * Description:
 * Date: 2017/12/2 0002 下午 12:21
 * Huan Yu Copyright (c) 2017 All Rights Reserved.
 */
public class MailInfo extends ExtInfo{

    /***
     * 玩家邮件集合，key 类型 value 邮件列表
     */
    public Map<Integer,List<MailDB>> mailMap;

    public List<PrivacyDB> privacyList;

    public MailInfo(Player player, WorldInfo worldInfo){
        super(player, worldInfo);
        mailMap = new ConcurrentHashMap<>();
        privacyList = new ArrayList<>();
    }

    public int mailNumber =1;

    @Override
    public void init() {

    }

    @Override
    public void checkInit() {

    }

    @Override
    public void reset() {
        mailNumber = 1;
        mailMap.clear();
        privacyList.clear();
    }

    @Override
    public void checkReset() {

    }

    @Override
    public void writeTo(ByteBuffer buffer) {
        buffer.writeInt(mailNumber);
        buffer.writeByte(privacyList.size());
        for (PrivacyDB privacyDB : privacyList){
            privacyDB.writeTo(buffer);
        }
        buffer.writeByte(mailMap.size());
        for(Integer key : mailMap.keySet()) {
            buffer.writeByte(mailMap.get(key).size());
            for(MailDB mailDB : mailMap.get(key)){
                mailDB.writeTo(buffer);
            }
            buffer.writeByte(key);
        }
    }

    @Override
    public void loadFrom(ByteBuffer buffer) {
        mailNumber = buffer.readInt();
        PrivacyDB privacyDB = null;
        MailDB mailDB = null;
        int i,j,size,keySize;
        for (i = 0 ; i < buffer.readByte(); i++){
            privacyDB = new PrivacyDB();
            privacyDB.loadFrom(buffer);
            privacyList.add(privacyDB);
        }
        size = buffer.readByte();
        List<MailDB>  list = null;
        for(i = 0; i < size; i++){
            keySize = buffer.readByte();
            list = new CopyOnWriteArrayList<>();
            for( j = 0 ; j < keySize ; j++){
                mailDB = new MailDB();
                mailDB.loadFrom(buffer);
                list.add(mailDB);
            }
            buffer.readByte();
            mailMap.put(j,list);
        }

    }


    public List<MailDB> getMailList(int key){
        if (mailMap != null && mailMap.size() > 0)
            return mailMap.get(key);
        else
            return null;
    }

    /**
     * 发送邮件
     * @param playerController
     * @param type
     * @param title
     * @param content
     * @param auxiliaryList
     */
    public void sendMail (PlayerController playerController , int type,String title ,String content,List<GoodsData> auxiliaryList){
        List<MailDB> mailList = mailMap.get(type);
        if(mailList == null){
            mailList = new ArrayList<>();
            mailMap.put(type,mailList);
        }
        MailDB mailDB = new MailDB();
        mailDB.id = ++mailNumber;
        if(mailDB.auxiliaryList != null && mailDB.auxiliaryList.size() > 0)
            mailDB.auxiliaryList = auxiliaryList;
        mailDB.title = title;
        mailDB.content = content;
        mailDB.target = playerController.getPlayer().getAccount();
        mailDB.isGet = false;
        mailDB.isRead = false;
        mailDB.sendTime = System.currentTimeMillis();
        if(mailList.size() > 50){
            mailList.remove(0);
        }
        mailList.add(mailDB);
    }




}
