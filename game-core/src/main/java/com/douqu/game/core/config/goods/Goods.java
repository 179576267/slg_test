package com.douqu.game.core.config.goods;

import com.douqu.game.core.entity.GameObject;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-11-06 18:45
 */
public class Goods extends GameObject {

    public String icon;

    public int quality;

    public String source;

    @Override
    public void copyTo(GameObject obj) {
        super.copyTo(obj);
        Goods goods = (Goods)obj;
        goods.icon = this.icon;
        goods.quality = this.quality;
        goods.source = this.source;
    }
}
