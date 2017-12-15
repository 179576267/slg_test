package com.douqu.game.core.entity.db;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.config.common.GoodsData;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author: Gavin.
 * Description:
 * Date: 2017/12/2 0002 下午 12:26
 * Huan Yu Copyright (c) 2017 All Rights Reserved.
 */
public class MailDB extends DB {



    /***
     * 标题
     */
    public String title;

    /***
     * 内容
     */
    public String content;
    /***
     * 发送时间
     */
    public long sendTime;

    /***
     * 参与者
     *
     */
    public String  target;

    /**
     * 邮件状态是否已读
     */
    public boolean isRead;

    /***
     * 附件
      */
    public List<GoodsData> auxiliaryList;


    public MailDB()
    {
        super(0);
        auxiliaryList = new CopyOnWriteArrayList<>();
    }
    /***
     * 附件是否领取
     */
    public boolean isGet;

    @Override
    public void reset() {
        auxiliaryList = new CopyOnWriteArrayList<>();
    }

    @Override
    public void loadFrom(ByteBuffer buffer) {

        super.loadFrom(buffer);
        title = buffer.readUTF();
        content = buffer.readUTF();
        sendTime = buffer.readLong();
        target = buffer.readUTF();
        isRead = buffer.readBoolean();
        isGet = buffer.readBoolean();
        int size = buffer.readShort();
        GoodsData goodsData = null;
        for(int i = 0; i < size ; i ++){
            goodsData = new GoodsData();
            goodsData.loadFrom(buffer);
            auxiliaryList.add(goodsData);
        }
    }

    @Override
    public void writeTo(ByteBuffer buffer) {
        super.writeTo(buffer);
        buffer.writeUTF(title);
        buffer.writeUTF(content);
        buffer.writeLong(sendTime);
        buffer.writeUTF(target);
        buffer.writeBoolean(isRead);
        buffer.writeBoolean(isGet);
        buffer.writeShort(auxiliaryList.size());
        for(int i = 0; i < auxiliaryList.size(); i++){
            auxiliaryList.get(i).writeTo(buffer);
        }
    }

    @Override
    public String toString() {
        return "MailDB{" +
                "id='" + super.id + '\'' +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", sendTime=" + sendTime +
                ", target='" + target + '\'' +
                ", isRead=" + isRead +
                ", auxiliaryList=" + auxiliaryList +
                ", isGet=" + isGet +
                '}';
    }
}
