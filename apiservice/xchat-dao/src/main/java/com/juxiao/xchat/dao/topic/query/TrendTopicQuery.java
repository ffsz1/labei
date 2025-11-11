package com.juxiao.xchat.dao.topic.query;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class TrendTopicQuery {
    private Long id;

    private Long uid;//当前手机客户端用户uid

    private String name;

    private Long themeid;

/*    private Integer commentnum;

    private Integer visitnum;*/

    private String state;

    private String remarks;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getThemeid() {
        return themeid;
    }

    public void setThemeid(Long themeid) {
        this.themeid = themeid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
