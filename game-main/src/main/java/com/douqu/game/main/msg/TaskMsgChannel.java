package com.douqu.game.main.msg;

import com.douqu.game.core.controller.PlayerController;
import com.douqu.game.core.entity.ext.TaskInfo;
import com.douqu.game.core.protobuf.SGMainProto;
import com.douqu.game.core.protobuf.SGTaskProto;
import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author: Gavin.
 * Description:
 * Date: 2017/10/25 0025 下午 4:48
 * Huan Yu Copyright (c) 2017 All Rights Reserved.
 */
@Component
public class TaskMsgChannel  implements AMsgChannel {

    Logger logger = Logger.getLogger(this.getClass());

    @Override
    public void messageChannel(int code, PlayerController playerController, byte[] data)throws Exception{
        if(playerController == null)
            return;

        TaskInfo taskInfo = playerController.getPlayer().getExtInfo(TaskInfo.class);

        switch (code) {
            //任务列表显示
            case SGMainProto.E_MSG_ID.MsgID_Task_TaskList_VALUE:
                SGTaskProto.C2S_TaskList c = SGTaskProto.C2S_TaskList.parseFrom(data);
                taskInfo.taskList(playerController, c.getType());
                break;
            //任务领奖
            case SGMainProto.E_MSG_ID.MsgID_Task_TaskReward_VALUE:
                SGTaskProto.C2S_TaskReward reward = SGTaskProto.C2S_TaskReward.parseFrom(data);
                taskInfo.taskReward(playerController,reward.getTaskId());
                break;
            //宝箱领奖
            case SGMainProto.E_MSG_ID.MsgID_Task_TreasureReward_VALUE:
                SGTaskProto.C2S_TreasureReward treasureReward = SGTaskProto.C2S_TreasureReward.parseFrom(data);
                taskInfo.treasureReward(playerController,treasureReward.getType().getNumber(),treasureReward.getTreasureId());
            default:
                break;
        }
    }
}
