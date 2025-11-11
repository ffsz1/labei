package com.erban.main.model;

public class UserConfigure {
    private Long uid;

    private Byte superiorBouns;

    private Byte occupationRatio;

    private String bankCard;

    private String cardholder;

    private Byte greenRecom;

    private Byte newUsers;

    private Byte gameRoom;

    private Integer greenSortNo;

    private Integer suibao;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Byte getSuperiorBouns() {
        return superiorBouns;
    }

    public void setSuperiorBouns(Byte superiorBouns) {
        this.superiorBouns = superiorBouns;
    }

    public Byte getOccupationRatio() {
        return occupationRatio;
    }

    public void setOccupationRatio(Byte occupationRatio) {
        this.occupationRatio = occupationRatio;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard == null ? null : bankCard.trim();
    }

    public String getCardholder() {
        return cardholder;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder == null ? null : cardholder.trim();
    }

    public Byte getGreenRecom() {
        return greenRecom;
    }

    public void setGreenRecom(Byte greenRecom) {
        this.greenRecom = greenRecom;
    }

    public Byte getNewUsers() {
        return newUsers;
    }

    public void setNewUsers(Byte newUsers) {
        this.newUsers = newUsers;
    }

    public Byte getGameRoom() {
        return gameRoom;
    }

    public void setGameRoom(Byte gameRoom) {
        this.gameRoom = gameRoom;
    }

    public Integer getGreenSortNo() {
        return greenSortNo;
    }

    public void setGreenSortNo(Integer greenSortNo) {
        this.greenSortNo = greenSortNo;
    }

    public Integer getSuibao() {
        return suibao;
    }

    public void setSuibao(Integer suibao) {
        this.suibao = suibao;
    }
}
