package com.douqu.game.core.config.challenge;

import com.douqu.game.core.entity.EntityObject;
import com.douqu.game.core.factory.ConstantFactory;

/**
 * Created by bean on 2017/9/13.
 */
public class InstanceStarBox extends EntityObject {

    public int id;

    public int star;

    public int dropId;

    public InstanceStarBox(String value)
    {
        String[] strs = value.split(ConstantFactory.SEMICOLON);
        this.id = Integer.parseInt(strs[0]);
        this.star = Integer.parseInt(strs[1]);
        this.dropId = Integer.parseInt(strs[2]);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getDropId() {
        return dropId;
    }

    public void setDropId(int dropId) {
        this.dropId = dropId;
    }
}
