package com.erban.main.vo;

import com.erban.main.model.NobleUsers;

/**
 * Created by liuguofu on 2017/11/7.
 */
public class RankMineVo {
    private Long erbanNo;
    private String avatar;
    private String nick;
    private Byte gender;
    private double totalNum;
    private int seqNo;
    private Long reachGoldNum;
    private Integer nobleId;
    private String nobleName;

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
        this.nobleName = nobleName;
    }

    public Long getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(Long erbanNo) {
        this.erbanNo = erbanNo;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public Long getReachGoldNum() {
        return reachGoldNum;
    }

    public void setReachGoldNum(Long reachGoldNum) {
        this.reachGoldNum = reachGoldNum;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public double getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(double totalNum) {
        this.totalNum = totalNum;
    }
}
