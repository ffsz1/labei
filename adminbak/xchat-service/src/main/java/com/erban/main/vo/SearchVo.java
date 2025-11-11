package com.erban.main.vo;

import com.erban.main.model.NobleUsers;

/**
 * Created by liuguofu on 2017/9/27.
 */
public class SearchVo {
    private Long uid;
    private Long roomId;

    private String title;

    private Byte type;

    private Boolean valid;//是否在线

    private String avatar;//房间头像图

    private Byte gender;//性别

    private String nick;

    private Long erbanNo;

    private boolean hasPrettyErbanNo;

    private Integer fansNum;

    private NobleUsers nobleUsers;

    public boolean isHasPrettyErbanNo() {
        return hasPrettyErbanNo;
    }

    public void setHasPrettyErbanNo(boolean hasPrettyErbanNo) {
        this.hasPrettyErbanNo = hasPrettyErbanNo;
    }

    public NobleUsers getNobleUsers() {
        return nobleUsers;
    }

    public void setNobleUsers(NobleUsers nobleUsers) {
        this.nobleUsers = nobleUsers;
    }

    public Long getUid() {
        return uid;
    }

    public Long getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(Long erbanNo) {
        this.erbanNo = erbanNo;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Integer getFansNum() {
        return fansNum;
    }

    public void setFansNum(Integer fansNum) {
        this.fansNum = fansNum;
    }
}
