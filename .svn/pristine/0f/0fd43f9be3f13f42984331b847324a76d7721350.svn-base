package com.douqu.game.core.entity.db;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.entity.ext.data.mail.DialogueData;
import com.douqu.game.core.factory.DataFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author: Gavin.
 * Description:
 * Date: 2017/12/2 0002 下午 6:16
 * Huan Yu Copyright (c) 2017 All Rights Reserved.
 */
public class PrivacyDB extends DB {

    /***
     * 内容
     */
    public String content;

    /***
     * 对方流水号
     */
    public String targetIndex;

    /***
     * 发送时间
     */
    public long sendTime;

    /**
     * 邮件状态是否已读
     */
    public boolean isRead;

    /**
     * 私信
     */
    public List<DialogueData> replyList;

    public PrivacyDB(){
        super(0);
        replyList = new CopyOnWriteArrayList<>();
    }


    @Override
    public void loadFrom(ByteBuffer buffer) {
        super.loadFrom(buffer);
        content = buffer.readUTF();
        targetIndex =buffer.readUTF();
        sendTime = buffer.readLong();
        isRead = buffer.readBoolean();
        int size = buffer.readShort();

        DialogueData dialogueData = null;
        for(int i = 0; i < size; i++)
        {
            dialogueData = new DialogueData();
            dialogueData.loadFrom(buffer);
            replyList.add(dialogueData);
        }

    }

    @Override
    public void writeTo(ByteBuffer buffer) {
        super.writeTo(buffer);
        buffer.writeUTF(content);
        buffer.writeUTF(targetIndex);
        buffer.writeLong(sendTime);
        buffer.writeBoolean(isRead);
        buffer.writeShort(replyList.size());
        for(DialogueData dialogueData : replyList)
        {
            dialogueData.writeTo(buffer);
        }
    }

    @Override
    public void reset() {
        replyList = new CopyOnWriteArrayList<>();
    }

    @Override
    public String toString() {
        return "PrivacyDB{" +
                "content='" + content + '\'' +
                ", targetIndex='" + targetIndex + '\'' +
                ", sendTime=" + sendTime +
                ", isRead=" + isRead +
                ", replyList=" + replyList +
                '}';
    }
}
