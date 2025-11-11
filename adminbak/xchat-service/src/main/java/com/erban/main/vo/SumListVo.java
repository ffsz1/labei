package com.erban.main.vo;

public class SumListVo {
    private Long id;

    private Long roomUid;

    private Long price;

    private Long uid;

    private Long prodId;

    private String nick;

    private String avatar;

    private Byte gender;

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Byte getGender() {

        return gender;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNick() {

        return nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRoomUid(Long roomUid) {
        this.roomUid = roomUid;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public Long getRoomUid() {

        return roomUid;
    }

    public Long getPrice() {
        return price;
    }

    public Long getUid() {
        return uid;
    }

    public Long getProdId() {
        return prodId;
    }

    public Long getId() {

        return id;
    }
}
