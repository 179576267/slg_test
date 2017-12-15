package com.douqu.game.core.e;

/**
 * @author: Gavin.
 * Description:
 * Date: 2017/12/2 0002 下午 3:04
 * Huan Yu Copyright (c) 2017 All Rights Reserved.
 */
public enum E_MailSenderType implements BaseEnum{
    MAIL_TYPE_SENDER(1,"发送者"),
    MAIL_TYPE_RECEIVER(2,"接收者")
    ;

    int code;
    String msg;

    E_MailSenderType(int code, String msg)
    {
        this.code = code;
        this.msg = msg;
    }

    public static E_MailSenderType forNumber(int id)
    {
        for(E_MailSenderType senderType : values())
        {
            if(senderType.code == id)
                return senderType;
        }
        return null;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
