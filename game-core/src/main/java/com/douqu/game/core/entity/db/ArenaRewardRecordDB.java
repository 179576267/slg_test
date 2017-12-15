package com.douqu.game.core.entity.db;

import com.bean.core.buffer.ByteBuffer;
import com.douqu.game.core.entity.EntityObject;

/**
 * @author wangzhenfei
 *         2017-10-10 18:57
 *         竞技场兑换记录(总数记录)
 */
public class ArenaRewardRecordDB extends EntityObject{
    private int type;
    private int id;
    private int num;

    public ArenaRewardRecordDB() {
    }

    public ArenaRewardRecordDB(int type, int id) {
        this.type = type;
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void addNum(int num) {
        this.num += num;
    }

    public void writeTo(ByteBuffer buffer){
        buffer.writeByte(type);
        buffer.writeShort(id);
        buffer.writeInt(num);
    }

    public ArenaRewardRecordDB loadFrom(ByteBuffer buffer){
        type = buffer.readByte();
        id = buffer.readShort();
        num = buffer.readInt();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArenaRewardRecordDB)) return false;

        ArenaRewardRecordDB record = (ArenaRewardRecordDB) o;

        if (id != record.id) return false;
        if (type != record.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type;
        result = 31 * result + id;
        return result;
    }
}
