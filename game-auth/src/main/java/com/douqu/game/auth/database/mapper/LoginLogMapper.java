package com.douqu.game.auth.database.mapper;


import com.douqu.game.auth.database.model.LoginLogModel;

public interface LoginLogMapper {
    int insert(LoginLogModel record);
    LoginLogModel selectByPrimaryKey(Integer id);
}