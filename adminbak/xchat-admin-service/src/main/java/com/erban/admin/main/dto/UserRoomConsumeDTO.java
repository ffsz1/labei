package com.erban.admin.main.dto;

/**
 * @Author: alwyn
 * @Description: 用户在房间的消费情况
 * @Date: 2018/11/10 001019:49
 */
public class UserRoomConsumeDTO {

    private Integer userNum;
    private Long roomUid;
    private String title;
    private Long erbanNo;
    private String nick;
    private String firstDate;
    private String uids;

    @Override
    public String toString() {
        return "UserRoomConsumeDTO{" +
                "userNum=" + userNum +
                ", roomUid=" + roomUid +
                ", title='" + title + '\'' +
                ", erbanNo=" + erbanNo +
                ", nick='" + nick + '\'' +
                ", firstDate='" + firstDate + '\'' +
                ", uids='" + uids + '\'' +
                '}';
    }

    public Integer getUserNum() {
        return userNum;
    }

    public void setUserNum(Integer userNum) {
        this.userNum = userNum;
    }

    public Long getRoomUid() {
        return roomUid;
    }

    public void setRoomUid(Long roomUid) {
        this.roomUid = roomUid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public String getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(String firstDate) {
        this.firstDate = firstDate;
    }

    public String getUids() {
        return uids;
    }

    public void setUids(String uids) {
        this.uids = uids;
    }
}
