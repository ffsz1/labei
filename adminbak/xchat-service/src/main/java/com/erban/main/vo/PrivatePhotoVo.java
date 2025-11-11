package com.erban.main.vo;

import java.util.Date;

public class PrivatePhotoVo implements Comparable<PrivatePhotoVo> {

    private Long pid;
    private String photoUrl;
    private Integer seqNo;
    private Date createTime;

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {

        return createTime;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public int compareTo(PrivatePhotoVo privatePhotoVo) {
        Date createTimeVo = privatePhotoVo.createTime;
        Date createTimeThis = this.createTime;
        if (createTimeVo.getTime() > createTimeThis.getTime()) {
            return 1;
        } else if (createTimeVo.getTime() < createTimeThis.getTime()) {
            return -1;
        } else {
            return 0;
        }
    }
}
