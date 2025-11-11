package com.erban.main.model.redis;

/**
 * Created by liuguofu on 2017/6/4.
 */
public class MicroSeqModelV2 {
    private Long uid;
    private String nick;
    private String avatar;
    private int seqNo;
    private int status;//当前麦序状态，1房主发起邀请中，2在麦序上

    public MicroSeqModelV2(Long uid,String nick,String avatar){
        this.uid=uid;
        this.nick=nick;
        this.avatar=avatar;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
