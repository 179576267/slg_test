package com.douqu.game.main.database.mapper;

import com.douqu.game.core.database.model.ServerModel;

/**
 * Created by bean on 2017/8/12.
 */
public interface ServerMapper {

    void create(ServerModel serverModel);

    ServerModel find();

    void update(ServerModel serverModel);
}
