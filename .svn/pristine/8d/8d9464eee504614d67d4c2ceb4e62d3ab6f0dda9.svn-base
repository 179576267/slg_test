package com.douqu.game.main.database.mapper;

import com.douqu.game.core.database.model.PlayerModel;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by bean on 2017/7/17.
 */
public interface PlayerMapper {

    int insert(PlayerModel playerModel);

    int update(PlayerModel playerModel);

    int login(@Param("lastLoginTime") Date lastLoginTime,
              @Param("playerId") Integer playerId);

    PlayerModel getPlayerByUid(@Param("uid") Integer uid);

    PlayerModel getPlayerByName(@Param("name") String name);

    PlayerModel selectByPrimaryKey(@Param("id") Integer id);

    PlayerModel getPlayerByIndex(@Param("objectIndex") String objectIndex);

    List<PlayerModel> findAll();

    int findAllCount();

    List<PlayerModel> findList(@Param("start") Integer start,
                               @Param("count") Integer count);

    PlayerModel offlineMatch(@Param("grade") int grade,
                             @Param("indexs") String... indexs);
}
