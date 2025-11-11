package com.erban.main.model;

import java.util.Date;

public class NobleRes {
    private Integer id;

    private Integer nobleId;

    private String nobleName;

    private String name;

    private String value;

    private String preview;

    private Byte status;

    private Byte resType;

    private Byte isDyn;

    private Byte isDef;

    private Integer seq;

    private Date createTime;

    private String tmpstr;

    private Integer tmpint;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNobleId() {
        return nobleId;
    }

    public void setNobleId(Integer nobleId) {
        this.nobleId = nobleId;
    }

    public String getNobleName() {
        return nobleName;
    }

    public void setNobleName(String nobleName) {
        this.nobleName = nobleName == null ? null : nobleName.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview == null ? null : preview.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getResType() {
        return resType;
    }

    public void setResType(Byte resType) {
        this.resType = resType;
    }

    public Byte getIsDyn() {
        return isDyn;
    }

    public void setIsDyn(Byte isDyn) {
        this.isDyn = isDyn;
    }

    public Byte getIsDef() {
        return isDef;
    }

    public void setIsDef(Byte isDef) {
        this.isDef = isDef;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTmpstr() {
        return tmpstr;
    }

    public void setTmpstr(String tmpstr) {
        this.tmpstr = tmpstr == null ? null : tmpstr.trim();
    }

    public Integer getTmpint() {
        return tmpint;
    }

    public void setTmpint(Integer tmpint) {
        this.tmpint = tmpint;
    }
}
