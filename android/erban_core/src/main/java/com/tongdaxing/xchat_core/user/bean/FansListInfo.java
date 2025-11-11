package com.tongdaxing.xchat_core.user.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by chenran on 2017/10/2.
 */

public class FansListInfo implements Serializable {
    private int pageCount;
    private List<FansInfo> fansList;

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<FansInfo> getFansList() {
        return fansList;
    }

    public void setFansList(List<FansInfo> fansList) {
        this.fansList = fansList;
    }
}
