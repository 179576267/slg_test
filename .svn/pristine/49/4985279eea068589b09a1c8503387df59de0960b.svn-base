package com.douqu.game.core.entity.ext.data.mail;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.e.E_MailSenderType;
import com.douqu.game.core.entity.ext.data.BaseData;

/**
 * @author: Gavin.
 * Description:
 * Date: 2017/12/2 0002 下午 5:42
 * Huan Yu Copyright (c) 2017 All Rights Reserved.
 */
public class DialogueData extends BaseData {

    /**
     * 内容
     */
    public String content;

    /***
     * 时间
     */
    public long  sendTime;

    /***
     * 类型（发送或者接收）
     */
    public E_MailSenderType senderType;

    @Override
    public void init() {

    }

    @Override
    public void checkInit() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void checkReset() {

    }

    @Override
    public void writeTo(ByteBuffer buffer) {
        buffer.writeUTF(content);
        buffer.writeLong(sendTime);
        buffer.writeInt(senderType.getCode());
    }

    @Override
    public void loadFrom(ByteBuffer buffer) {
        content = buffer.readUTF();
        sendTime = buffer.readLong();
        senderType =  E_MailSenderType.forNumber(buffer.readInt());
    }

    @Override
    public String toString() {
        return "PrivacyData{" +
                "content='" + content + '\'' +
                ", sendTime=" + sendTime +
                ", senderType=" + senderType +
                '}';
    }
}
