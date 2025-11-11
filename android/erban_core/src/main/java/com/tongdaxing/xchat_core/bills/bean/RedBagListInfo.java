package com.tongdaxing.xchat_core.bills.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by ${Seven} on 2017/9/25.
 */

public class RedBagListInfo implements Serializable {
    private int pageCount;
    private List<Map<String,List<RedBagInfo>>> billList;

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<Map<String, List<RedBagInfo>>> getBillList() {
        return billList;
    }

    public void setBillList(List<Map<String, List<RedBagInfo>>> billList) {
        this.billList = billList;
    }
}
