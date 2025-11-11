package com.juxiao.xchat.service.api.wish.bo;

import com.juxiao.xchat.dao.wish.query.Page;

public class PageBo {
    private Long pageSize;
    private Long pageNum;

    public static Page getPage(PageBo pageBo){
        Page page=null;
        if(pageBo!=null) {

            if (pageBo.getPageSize() != null && pageBo.getPageNum() != null) {
                page = new Page();
                page.setBottom((pageBo.getPageNum() - 1) * pageBo.getPageSize());
                page.setPageSize(pageBo.getPageSize());
            }
        }
        return page;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPageNum() {
        return pageNum;
    }

    public void setPageNum(Long pageNum) {
        this.pageNum = pageNum;
    }

    public String checkParam(){
        //不传参数不分页，传了参数必须检验，null值放过
        if(this.pageSize==null||this.pageNum==null){
            return null;
        }
        if(this.pageSize>0&&this.pageNum>0){
            return null;
        }else{
            return "分页参数不合法";
        }
    }
}
