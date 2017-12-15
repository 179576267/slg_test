package com.douqu.game.core.e;



public enum ReturnMessage {

    SUCCESS("0000", "成功"),
    FAILURE("1111", "失败"),
    ILLEGAL("2222", "非法请求"),
    EMPTY_PARAM("3333", "空参数"),

    NICK_NAME_ILLEGAL("1001", "昵称长度不合法,不能注册"),
    ACCOUNT_ILLEGAL("1002", "账号输入错误"),
    PHONE_NUM_ILLEGAL("1003", "手机号码格式错误"),
    DEVICE_ID_NOT_NULL("1004", "设备号不能为空"),
    PASSWORD_NOT_EMPTY("1005", "密码不能为空"),
    PASSWORD_ERROR("1006", "密码错误"),
    UNREGISTER("1007", "未注册"),
    AUTH_FAILURE("1008", "三方认证失败"),
    SERVER_ID_ERROR("1010", "找不到服务器"),
    NICKNAME_EXITS("1011", "角色名称已存在"),
    THIS_SERVER_PLAYER_EXITS("1012", "该服务器下已存在角色"),
    NO_RECORD("1013", "角色记录不存在")

    ;


    private String code;
    private String msg;

    ReturnMessage(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }




    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static String forCode(String code) {

        for (ReturnMessage errorMsg : ReturnMessage.values()) {
            if (errorMsg.code.equals(code)) {
                return errorMsg.msg;
            }
        }
        return null;
    }

    public static ReturnMessage forEnum(String code) {

        for (ReturnMessage errorMsg : ReturnMessage.values()) {
            if (errorMsg.code.equals(code)) {
                return errorMsg;
            }
        }
        return null;
    }

}

