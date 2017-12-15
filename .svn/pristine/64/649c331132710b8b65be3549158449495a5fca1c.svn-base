package com.douqu.game.main.database.mapper;

import com.douqu.game.core.database.model.GMModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-11-13 15:08
 */
public interface GMMapper {

    void create(GMModel gmModel);

    int openOrClose(@Param("id") Integer id);

    int update(GMModel gmModel);

    List<GMModel> findList();

    GMModel findByName(@Param("username") String username);

    GMModel findById(@Param("id") Integer id);
}
