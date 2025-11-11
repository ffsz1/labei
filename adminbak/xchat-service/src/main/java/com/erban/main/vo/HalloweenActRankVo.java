package com.erban.main.vo;

/**
 * Created by liuguofu on 2017/10/28.
 */
public class HalloweenActRankVo {
    private Long erbanNo;
    private String nick;
    private String avatar;
    private long totalCount;
    private int seqNo;

    public Long getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(Long erbanNo) {
        this.erbanNo = erbanNo;
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


    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public int getSeqNo() {
        return seqNo;

    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
