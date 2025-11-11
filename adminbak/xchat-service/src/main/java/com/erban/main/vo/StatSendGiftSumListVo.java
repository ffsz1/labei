package com.erban.main.vo;

public class StatSendGiftSumListVo implements Comparable<StatSendGiftSumListVo> {
    private Long roomUid;

    private Long uid;

    private Long goldNum;

    private String nick;

    private String avatar;

    private Byte gender;

    public void setRoomUid(Long roomUid) {
        this.roomUid = roomUid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public void setGoldNum(Long goldNum) {
        this.goldNum = goldNum;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof StatSendGiftSumListVo)) {
            return false;
        }
        StatSendGiftSumListVo that = (StatSendGiftSumListVo) o;
        if (this.getUid().equals(that.getUid())) {
            return true;
        }
        return false;

    }

    public Long getRoomUid() {


        return roomUid;
    }

    public Long getUid() {
        return uid;
    }

    public Long getGoldNum() {
        return goldNum;
    }

    public String getNick() {
        return nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public Byte getGender() {
        return gender;
    }

    @Override
    public int compareTo(StatSendGiftSumListVo statSendGiftWeekListVo) {
        Long goldNumVo = statSendGiftWeekListVo.goldNum;
        Long goldNumThis = this.goldNum;
        if (goldNumVo > goldNumThis) {
            return 1;
        } else if (goldNumThis > goldNumVo) {
            return -1;
        } else {
            return 0;
        }
    }
}
