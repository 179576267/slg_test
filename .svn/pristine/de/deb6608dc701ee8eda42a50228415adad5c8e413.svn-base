package com.douqu.game.core.config.drop;

import com.douqu.game.core.config.common.CommonData;
import com.douqu.game.core.entity.EntityObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangzhenfei
 *         2017-09-15 17:45
 */
public class DropResult extends EntityObject {


    public List<CommonData> cards = new ArrayList<>();

    public List<CommonData> props = new ArrayList<>();

    public List<CommonData> assets = new ArrayList<>();

    public DropResult(List<CommonData> cards, List<CommonData> props, List<CommonData> assets) {
        this.cards = cards;
        this.props = props;
        this.assets = assets;
    }
    public DropResult(){}

    public void addCard(CommonData data)
    {
        cards.add(data);
    }

    public void addProp(CommonData data)
    {
        props.add(data);
    }

    public void addAsset(CommonData data)
    {
        assets.add(data);
    }


    public List<CommonData> getCards() {
        return cards;
    }

    public void setCards(List<CommonData> cards) {
        this.cards = cards;
    }

    public List<CommonData> getProps() {
        return props;
    }

    public void setProps(List<CommonData> props) {
        this.props = props;
    }

    public List<CommonData> getAssets() {
        return assets;
    }

    public void setAssets(List<CommonData> assets) {
        this.assets = assets;
    }
}
