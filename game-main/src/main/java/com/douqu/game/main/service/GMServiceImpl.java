package com.douqu.game.main.service;

import com.douqu.game.core.database.model.GMModel;
import com.douqu.game.main.database.mapper.GMMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by bean on 2017/8/12.
 */
@Service
public class GMServiceImpl implements GMService {

    @Autowired
    private GMMapper gmMapper;

    @Override
    public void create(GMModel gmModel) {
        gmMapper.create(gmModel);
    }

    @Override
    public int openOrClose(int id) {
        return gmMapper.openOrClose(id);
    }

    @Override
    public int update(GMModel gmModel) {
        return gmMapper.update(gmModel);
    }

    @Override
    public List<GMModel> findList() {
        return gmMapper.findList();
    }

    @Override
    public GMModel findByName(String username) {
        return gmMapper.findByName(username);
    }

    @Override
    public GMModel findById(Integer id) {
        return gmMapper.findById(id);
    }
}
