package com.erban.admin.main.model;

import java.util.Date;

public class AdminLog {
    private Long id;

    private Integer optUid;

    private String optClass;

    private String optMethod;

    private String optMess;

    private Date createTime;

    private Integer tmpint;

    private String tmpstr;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOptUid() {
        return optUid;
    }

    public void setOptUid(Integer optUid) {
        this.optUid = optUid;
    }

    public String getOptClass() {
        return optClass;
    }

    public void setOptClass(String optClass) {
        this.optClass = optClass == null ? null : optClass.trim();
    }

    public String getOptMethod() {
        return optMethod;
    }

    public void setOptMethod(String optMethod) {
        this.optMethod = optMethod == null ? null : optMethod.trim();
    }

    public String getOptMess() {
        return optMess;
    }

    public void setOptMess(String optMess) {
        this.optMess = optMess == null ? null : optMess.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getTmpint() {
        return tmpint;
    }

    public void setTmpint(Integer tmpint) {
        this.tmpint = tmpint;
    }

    public String getTmpstr() {
        return tmpstr;
    }

    public void setTmpstr(String tmpstr) {
        this.tmpstr = tmpstr == null ? null : tmpstr.trim();
    }
}
