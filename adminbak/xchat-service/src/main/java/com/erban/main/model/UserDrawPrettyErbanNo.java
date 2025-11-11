package com.erban.main.model;

import java.util.Date;

public class UserDrawPrettyErbanNo {
    private Long prettyErbanNo;

    private Byte type;

    private Byte useStatus;

    private Long useErbanNo;

    private Byte seq;

    private Date createTime;

    public Long getPrettyErbanNo() {
        return prettyErbanNo;
    }

    public void setPrettyErbanNo(Long prettyErbanNo) {
        this.prettyErbanNo = prettyErbanNo;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(Byte useStatus) {
        this.useStatus = useStatus;
    }

    public Long getUseErbanNo() {
        return useErbanNo;
    }

    public void setUseErbanNo(Long useErbanNo) {
        this.useErbanNo = useErbanNo;
    }

    public Byte getSeq() {
        return seq;
    }

    public void setSeq(Byte seq) {
        this.seq = seq;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
