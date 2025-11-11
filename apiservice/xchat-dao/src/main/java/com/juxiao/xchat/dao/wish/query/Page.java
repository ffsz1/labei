package com.juxiao.xchat.dao.wish.query;

public class Page {
    private long bottom;//分页限制 下限
    private long pageSize;//分页大小


    public long getBottom() {
        return bottom;
    }

    public void setBottom(long bottom) {
        this.bottom = bottom;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }
}
