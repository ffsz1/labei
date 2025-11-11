package com.erban.admin.main.dto;

/**
 * @Author: alwyn
 * @Description:
 * @Date: 2018/11/10 001016:31
 */
public class UserRoomDTO {

    private Long uid;
    private Long roomId;
    private Integer tagId;
    private String roomTag;
    private Integer type;
    private String title;
    private Long erbanNo;
    private String nick;

    public Long getUid() {
        return uid;
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

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public String getRoomTag() {
        return roomTag;
    }

    public void setRoomTag(String roomTag) {
        this.roomTag = roomTag;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    @Override
    public String toString() {
        return "UserRoomDTO{" +
                "uid=" + uid +
                ", roomId=" + roomId +
                ", tagId=" + tagId +
                ", roomTag='" + roomTag + '\'' +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", erbanNo=" + erbanNo +
                ", nick='" + nick + '\'' +
                '}';
    }
}
