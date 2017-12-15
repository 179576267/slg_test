package com.douqu.game.core.entity;

import com.douqu.game.core.protobuf.SGCommonProto;
import com.douqu.game.core.util.SendUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author: Bean
 * @Description:
 * @Date: 2017-11-20 10:28
 */
public class ChangeGoods extends EntityObject {

    private List<SGCommonProto.FlushData> showList;

    private List<SGCommonProto.FlushData> otherList;

    /**
     * 红点的缓存，不需要写入数据库
     */
    private List<SGCommonProto.E_RED_POINT_TYPE> redPointTypes;

    public ChangeGoods() {

        showList = new CopyOnWriteArrayList<>();
        otherList = new CopyOnWriteArrayList<>();
        redPointTypes = new CopyOnWriteArrayList<>();
    }


    public void addGoods(SGCommonProto.E_GOODS_TYPE goodsType, int id, int change, int curValue, Object... hide)
    {
        if(change == 0)
            return;

        if((hide != null && hide.length > 0) || change < 0)
            otherList.add(SendUtils.createFlushData(goodsType, id, change, curValue));
        else
            showList.add(SendUtils.createFlushData(goodsType, id, change, curValue));
    }

//    public void addShowGoods(SGCommonProto.E_GOODS_TYPE goodsType, int id, int change, int curValue)
//    {
//        if(change <= 0)
//            return;
//
//        showGoodsGetList.add(SendUtils.createGoodsGet(goodsType, id, change, curValue));
//    }



//    public List<SGCommonProto.FlushData> getFlushDataList()
//    {
//        return flushDataList;
//    }

    public List<SGCommonProto.FlushData> getShowList() {
        return showList;
    }

    public List<SGCommonProto.FlushData> getOtherList() {
        return otherList;
    }

    public List<SGCommonProto.E_RED_POINT_TYPE> getRedPointTypes() {
        return redPointTypes;
    }

    public void clear()
    {
        showList.clear();
        otherList.clear();
    }
}

