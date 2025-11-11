package com.erban.main.vo;

/**
 * Created by liuguofu on 2017/7/27.
 */
public class MicroSeqV2Vo implements Comparable<MicroSeqV2Vo> {
    private Long uid;
    private Integer seqNo;
    private int status;
    private String nick;
    private String avatar;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getUid() {
        return uid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    @Override
    public int compareTo(MicroSeqV2Vo microSeqV2Vo) {
        int seqNoVo = microSeqV2Vo.seqNo;
        int seqNoThis = this.seqNo;
        if (seqNoVo > seqNoThis) {
            return -1;
        } else if (seqNoVo < seqNoThis) {
            return 1;
        } else {
            return 0;
        }
    }
}
