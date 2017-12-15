package com.douqu.game.core.config.common;

import com.douqu.game.core.entity.EntityObject;
import com.douqu.game.core.protobuf.SGCommonProto;

/**
* Created by bean on 2017/8/8.
*/
public class Position extends EntityObject {

    public float x;

    public float y;

    public Position(float x, float y) {
        this.x = (int) x;
        this.y = (int) y;
        x = x < 0 ? 0 : x;
        y = y < 0 ? 0 : y;
    }

//    public Position(int x, int y) {
//        this.x = x;
//        this.y = y;
//    }

    public Position(SGCommonProto.Pos pos) {
        x = (int) pos.getX();
        y = (int) pos.getY();
        x = x < 0 ? 0 : x;
        y = y < 0 ? 0 : y;
    }

    

    public SGCommonProto.Pos.Builder parsePos()
    {
        SGCommonProto.Pos.Builder result = SGCommonProto.Pos.newBuilder();
        result.setX(this.x);
        result.setY(this.y);

        return result;
    }

    public boolean isSame(Position target)
    {
        int ax = (int) x;
        int ay = (int) y;
        int bx = (int) target.x;
        int by = (int) target.y;
        return ax == bx && ay == by;
    }


    public int getIntX()
    {
        return Math.round(x);
    }

    public int getIntY()
    {
        return Math.round(y);
    }


    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                "} ";
    }
}
