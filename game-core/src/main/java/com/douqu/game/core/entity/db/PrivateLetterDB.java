package com.douqu.game.core.entity.db;

/**
 * @author: Gavin.
 * Description:
 * Date: 2017/12/2 0002 下午 2:42
 * Huan Yu Copyright (c) 2017 All Rights Reserved.
 */
public class PrivateLetterDB extends DB{

    public PrivateLetterDB(){
        super(0);
    }

    /***
     * 对方玩家的流水号
     */
    public String targetIndex;

    /***
     * 发送内容
     */
//    public List<>


    @Override
    public void reset() {

    }
}
