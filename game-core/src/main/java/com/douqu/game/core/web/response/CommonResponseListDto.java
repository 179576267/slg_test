package com.douqu.game.core.web.response;


import java.util.List;

/**
 * @author wangzhenfei
 *  2017-03-21 17:20
 *  通用封装list的数据类型
 */
public class CommonResponseListDto<T> {
    private List<T> list;

    public CommonResponseListDto() {
    }

    public CommonResponseListDto(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "T{" +
                "list=" + list +
                '}';
    }
}
