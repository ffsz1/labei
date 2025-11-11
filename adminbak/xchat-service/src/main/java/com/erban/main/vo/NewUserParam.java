package com.erban.main.vo;

public class NewUserParam {

    private Long uid;
    private Byte gender;
    private Integer pageNum;
    private Integer pageSize;

    public NewUserParam(Long uid, Byte gender, Integer pageNum, Integer pageSize){
        this.uid = uid;
        this.gender = gender;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
