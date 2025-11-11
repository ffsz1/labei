package com.erban.main.model.redis;

/**
 * Created by liuguofu on 2017/6/4.
 */
public class MicroSeqModel {
    private Long uid;
    private int seqNo;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }
}
