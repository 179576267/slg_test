package com.douqu.game.main.service;

import com.douqu.game.core.database.model.ServerModel;
import com.douqu.game.core.entity.WorldInfo;
import com.douqu.game.main.database.mapper.ServerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by bean on 2017/8/12.
 */
@Service
public class WorldServiceImpl implements WorldService {

    @Autowired
    private ServerMapper serverMapper;

    @Override
    public void create(ServerModel serverModel) {
        serverMapper.create(serverModel);
    }

    @Override
    public ServerModel find() {
        return serverMapper.find();
    }

    @Override
    public void update(WorldInfo worldInfo)
    {
        serverMapper.update(worldInfo.createModel());
    }
}
