package com.erban.main.vo;

import java.util.List;

/**
 * Created by liuguofu on 2017/5/28.
 */
public class MicroSeqListVo {
    private Long uid;
    private List<Long> applyUids;

    private List<SeqVo> seqUids;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public List<Long> getApplyUids() {
        return applyUids;
    }

    public void setApplyUids(List<Long> applyUids) {
        this.applyUids = applyUids;
    }

    public List<SeqVo> getSeqUids() {
        return seqUids;
    }

    public void setSeqUids(List<SeqVo> seqUids) {
        this.seqUids = seqUids;
    }
}
