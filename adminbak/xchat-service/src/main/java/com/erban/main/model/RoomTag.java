package com.erban.main.model;

import java.util.Date;

/**
 * 房间标签类
 */
public class RoomTag {
    private Integer id;

    private String name;

    private String pict;

    private Integer seq;

    private Integer type;

    private Boolean status;

    private Boolean istop;

    private Date createTime;

    private String description;

    private Integer tmpint;

    private String tmpstr;

    private String children;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPict() {
        return pict;
    }

    public void setPict(String pict) {
        this.pict = pict == null ? null : pict.trim();
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getIstop() {
        return istop;
    }

    public void setIstop(Boolean istop) {
        this.istop = istop;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) { this.description = description == null ? null : description.trim(); }

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

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children == null ? null : children.trim();
    }

    @Override
    public String toString() {
        return "RoomTag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pict='" + pict + '\'' +
                ", seq=" + seq +
                ", type=" + type +
                ", status=" + status +
                ", istop=" + istop +
                ", createTime=" + createTime +
                ", description='" + description + '\'' +
                ", tmpint=" + tmpint +
                ", tmpstr='" + tmpstr + '\'' +
                ", children='" + children + '\'' +
                '}';
    }
}
