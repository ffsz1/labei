package com.juxiao.xchat.service.api.praise.vo;

public class PraiseVo {
    private Integer isPraise;//是否已经点赞
    private Integer praisenum;//点赞数量

    public Integer getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(Integer isPraise) {
        this.isPraise = isPraise;
    }

    public Integer getPraisenum() {
        return praisenum;
    }

    public void setPraisenum(Integer praisenum) {
        this.praisenum = praisenum;
    }
}
