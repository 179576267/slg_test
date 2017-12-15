package com.douqu.game.auth.database.mapper;


import com.douqu.game.auth.database.model.RoleRecordModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleRecordMapper {
    int insert(RoleRecordModel record);

    RoleRecordModel selectByPrimaryKey(Integer id);

    RoleRecordModel getByAccount(String account);

    RoleRecordModel getByDeviceId(@Param("deviceId")String deviceId);

    RoleRecordModel getByPlayName(@Param("playerName")String playerName);

    RoleRecordModel gerRecordInServer(@Param("key")String key,
                                      @Param("serverId")Integer serverId);

    Integer gerRecentLoginServerId(String key);

    List<RoleRecordModel> gerRecordsByAccountOrDeviceId(String key);

    void update(@Param("key")String key,
                @Param("serverId")Integer serverId,
                @Param("avatar")String avatar,
                @Param("playerName")String playerName, @Param("level")Integer level);

    void updateLoginTime(@Param("key")String key,
                @Param("serverId")Integer serverId);
}