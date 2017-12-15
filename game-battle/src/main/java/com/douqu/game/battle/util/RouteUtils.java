package com.douqu.game.battle.util;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.battle.controller.BattleController;
import com.douqu.game.battle.entity.aobject.BattleAObject;
import com.douqu.game.battle.server.GameServer;
import com.douqu.game.core.config.common.Position;
import com.douqu.game.core.i.ICallBack;
import com.douqu.game.core.util.SendUtils;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-12-08 18:49
 */
public class RouteUtils {

    public static void getRoutePath(BattleController battleController, BattleAObject source, BattleAObject target)
    {
        ByteBuffer data = new ByteBuffer();
        data.writeUTF(battleController.getId());
        data.writeInt(source.getUniqueId());
        data.writeByte(target.getTeamNo());
        data.writeInt(target.getUniqueId());
//        data.writeByte(source.position.getIntX());
//        data.writeByte(source.position.getIntY());
//        data.writeByte(targetPosition.getIntX());
//        data.writeByte(targetPosition.getIntY());

        SendUtils.sendMsg(GameServer.getInstance().getRouteChannel(), BattleUtils.GET_ROUTE_PATH, data.getBytes());
    }


    /**
     * 获取攻击一个目标时的起点(加上目标和自己的体积计算)
     * @param target
     * @return
     */
    public Position getAtkPosition(BattleAObject source, BattleAObject target)
    {
        float ax = source.position.getX();
        float ay = source.position.getY();
        float bx = target.position.getX();
        float by = target.position.getY();
        float distance = BattleUtils.getDistance(source.position, target.position);
        Position result = null;
        if(distance > 0)
        {
            float aax = ax + (source.getSprite().gridArea - 1) / distance * (bx - ax);
            float aay = ay + (source.getSprite().gridArea - 1) / distance * (by - ay);
            float bbx = bx + (target.getSprite().gridArea - 1) / distance * (ax - bx);
            float bby = by + (target.getSprite().gridArea - 1) / distance * (ay - by);

            result = new Position(bbx - (aax - ax), bby - (aay - ay));

        }
        else
            result = target.position;

        return result;
    }
}
