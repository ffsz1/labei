package com.juxiao.xchat.service.api.wish.vo;

import com.juxiao.xchat.service.api.wish.bo.PageBo;

import java.util.List;

public class PageInfo<T> {
    private Long total;//总数
    private Long totalPage;//总页数
    private Long pageNum;//当前第几页
    private Long pageSize;//页面大小
    private List<T> pagedata;//页面数据

    public PageInfo() {
    }

    public PageInfo(Long total, Long totalPage, Long pageNum, Long pageSize, List<T> pagedata) {
        this.total = total;
        this.totalPage = totalPage;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.pagedata = pagedata;
    }

    public PageInfo(PageBo pageBo, Long total,List<T> pagedata) {
        this.total=total;
        Long pageSize1=null;
        Long pageNum1=null;
        if(pageBo!=null) {
            pageSize1 = pageBo.getPageSize();
            pageNum1=pageBo.getPageNum();
            if(pageSize1!=null&&pageNum1!=null) {
                this.totalPage = (total - 1 + pageSize1) / pageSize1;
                this.pageNum = pageNum1;
                this.pageSize = pageSize1;
            }
        }
        this.pagedata=pagedata;
    }
    public PageInfo(PageBo pageBo) {
        this.pageNum=pageBo.getPageNum();
        this.pageSize=pageBo.getPageSize();
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    public Long getPageNum() {
        return pageNum;
    }

    public void setPageNum(Long pageNum) {
        this.pageNum = pageNum;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getPagedata() {
        return pagedata;
    }

    public void setPagedata(List<T> pagedata) {
        this.pagedata = pagedata;
    }
}
