package com.douqu.game.main.service;

import com.douqu.game.core.database.model.GMModel;

import java.util.List;

/**
 * Created by bean on 2017/8/12.
 */
public interface GMService {

    void create(GMModel gmModel);

    int openOrClose(int id);

    int update(GMModel gmModel);

    List<GMModel> findList();

    GMModel findByName(String username);

    GMModel findById(Integer id);

}
