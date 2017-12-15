package com.douqu.game.auth.database.mapper;


import com.douqu.game.auth.database.model.ServerModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ServerMapper {
    int insert(ServerModel record);

    ServerModel selectByPrimaryKey(Integer id);

    ServerModel getNewServer();

    List<ServerModel> getList();

    List<ServerModel> getRecommendList();

    void changeStatus(@Param("id") Integer id, @Param("status")Integer status);
}