package com.erban.admin.main.model;

import java.util.Date;

public class AdminDict extends AdminDictKey {
    private String dictval;

    private Boolean status;

    private Integer showorder;

    private Date createtime;

    private String description;

    public String getDictval() {
        return dictval;
    }

    public void setDictval(String dictval) {
        this.dictval = dictval == null ? null : dictval.trim();
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getShoworder() {
        return showorder;
    }

    public void setShoworder(Integer showorder) {
        this.showorder = showorder;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}
