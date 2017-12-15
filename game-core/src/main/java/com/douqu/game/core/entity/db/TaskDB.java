package com.douqu.game.core.entity.db;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.config.goods.EquipConfig;
import com.douqu.game.core.config.task.TaskConfig;
import com.douqu.game.core.factory.ConstantFactory;
import com.douqu.game.core.factory.DataFactory;
import com.douqu.game.core.protobuf.SGCommonProto;

/**
 * @author: Gavin.
 * Description:
 * Date: 2017/10/25 0025 下午 12:32
 * Huan Yu Copyright (c) 2017 All Rights Reserved.
 */
public class TaskDB extends DB {

    public int currentDemand;

    public int status;

    public TaskDB()
    {
        super(DataFactory.TASK_KEY);
    }

    public TaskDB(int id)
    {
        super(DataFactory.TASK_KEY, id);

        this.status = SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNDERWAY_VALUE;
    }

    public TaskDB(int id,int status)
    {
        super(DataFactory.TASK_KEY, id);

        this.status = status;
    }

    public TaskDB(int id, int currentDemand, int status)
    {
        super(DataFactory.TASK_KEY, id);

        this.currentDemand = currentDemand;
        this.status = status;
    }

    @Override
    public void writeTo(ByteBuffer byteBuffer){
        byteBuffer.writeInt(id);
        byteBuffer.writeShort(currentDemand);
        byteBuffer.writeByte(status);
    }


    @Override
    public void loadFrom(ByteBuffer byteBuffer){
        id = byteBuffer.readInt();
        currentDemand =byteBuffer.readShort();
        status=byteBuffer.readByte();
    }

    @Override
    public void reset() {

    }


    public void check(int type, int count){

        if(count == 0)
            return;

        if(status == SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNCLAIMED_VALUE)
            return;

        TaskConfig taskConfig = DataFactory.getInstance().getGameObject(DataFactory.TASK_KEY,id);
        if(type != taskConfig.target)
            return;



        boolean updateStatus = false;
        switch (type)
        {
            case ConstantFactory.TASK_TARAGET_LOTTERY:
                this.currentDemand += count;
                updateStatus = currentDemand >= taskConfig.demand[0];
                break;
            case ConstantFactory.TASK_TARAGET_EQUIPINTENSIFY:
                this.currentDemand = count;
                updateStatus = currentDemand >= taskConfig.demand[0];
                break;
            case ConstantFactory.TASK_TARAGET_MAINUPLV:
                this.currentDemand = count;
                updateStatus = currentDemand >= taskConfig.demand[0];
                break;
            case ConstantFactory.TASK_TARAGET_PASSLEVEL:
                this.currentDemand = count;
                updateStatus = currentDemand >= taskConfig.demand[0];
                break;
            case ConstantFactory.TASK_TARAGET_ARENA:
                this.currentDemand += count;
                updateStatus = currentDemand >= taskConfig.demand[0];
                break;
            case ConstantFactory.TASK_TARAGET_GUBLIANSTORE:
                this.currentDemand = count;
                updateStatus = currentDemand == taskConfig.demand[0];
                break;
            case ConstantFactory.TASK_TARAGET_CARD:
                this.currentDemand += count;
                updateStatus = currentDemand == taskConfig.demand[0];
                break;
            case ConstantFactory.TASK_TARAGET_MAIN_ARENA_BATTLE:
                this.currentDemand += count;
                updateStatus = currentDemand >= taskConfig.demand[0];
                break;
            case ConstantFactory.TASK_TARAGET_EVERYDAY_INSTANCE:
                this.currentDemand += count;
                updateStatus = currentDemand >= taskConfig.demand[0];
                break;
            case ConstantFactory.TASK_TARAGET_EVERYDAY_GUBLIANSTORE:
                this.currentDemand = count;
                updateStatus = currentDemand >= taskConfig.demand[0];
                break;
            case ConstantFactory.TASK_TARAGET_MAIN_EQUIPINTENSIFY:
                this.currentDemand = count;
                updateStatus = currentDemand >= taskConfig.demand[0];
                break;
            case ConstantFactory.TASK_TARAGET_MAIN_CARD_LV:
                this.currentDemand = count;
                updateStatus = currentDemand >= taskConfig.demand[0];
                break;
            case ConstantFactory.TASK_TARAGET_EVERYDAY_CARD_LV:
                this.currentDemand += count;
                updateStatus = currentDemand >= taskConfig.demand[0];
                break;
            case ConstantFactory.TASK_TARAGET_MAIN_CARD_STAR:
                this.currentDemand = count;
                updateStatus = currentDemand >= taskConfig.demand[0];
                break;
            case ConstantFactory.TASK_TARAGET_MAIN_OFFICIALWAR:
                this.currentDemand = count;
                updateStatus = currentDemand >= taskConfig.demand[0];
                break;
            case ConstantFactory.TASK_TARAGET_MAIN_ACCESSORY_INTENSIFY:
                this.currentDemand = count;
                updateStatus = currentDemand >= taskConfig.demand[0];
                break;
            case ConstantFactory.TASK_TARAGET_MAIN_EQUIPSYN:
                this.currentDemand = count;
                updateStatus = currentDemand >= taskConfig.demand[0];
                break;
            case ConstantFactory.TASK_TARAGET_BATTLE_FC:
                this.currentDemand = count;
                updateStatus = currentDemand >= taskConfig.demand[0];
                break;
            case ConstantFactory.TASK_TARAGET_REBORN:
                this.currentDemand = count;
                updateStatus = currentDemand >= taskConfig.demand[0];
                break;
            case ConstantFactory.TASK_TARAGET_EVERY_ALTAR:
                this.currentDemand = count;
                updateStatus = currentDemand >= taskConfig.demand[0];
                break;


        }
        this.currentDemand = this.currentDemand > taskConfig.demand[0] ? this.currentDemand = taskConfig.demand[0] : this.currentDemand;
        if(updateStatus) {
            status = SGCommonProto.E_TASK_STATUS.TASK_STATUS_UNCLAIMED_VALUE;
        }

    }

    @Override
    public TaskConfig getConfig() {
        return (TaskConfig) super.getConfig();
    }

    @Override
    public String toString() {
        return "TaskData{" +
                "id=" + id +
                ", currentDemand=" + currentDemand +
                ", status=" + status +
                '}';
    }
}
