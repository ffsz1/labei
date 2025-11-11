package com.erban.main.vo.admin;

/**
 * 后台管理--钻石提现VO
 */
public class StatRoomFlowVo {
    //========statroomflowtotal============
    private Long totalGoldNum;
    private Double totalDiamondNum;
    private String createTime;
    //========users==================
    private Long uid;
    private Long erbanNo;
    private String phone;
    private String nick;
    private String alipayAccount;
    private String alipayAccountName;
    //================================
    private String roomFlowLink;
    private Double totalGold;
    private Double totalBouns;
    private Integer bounsPersent;
    private Double bouns;
    private Byte isPermitRoom;
    private Double occupation;
    private String bankCard;
    private String cardholder;
    // =================================
    private Long countUsers;
    private Long countNewUsers;
    private String proportion;

    private Long trickTreatTotal;

    private String total;
    private Long roomId;
    private String roomTag;
    
    private String guildName;

    public Double getOccupation() {
        return occupation;
    }

    public void setOccupation(Double occupation) {
        this.occupation = occupation;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getCardholder() {
        return cardholder;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(Long erbanNo) {
        this.erbanNo = erbanNo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getTotalGoldNum() {
        return totalGoldNum;
    }

    public void setTotalGoldNum(Long totalGoldNum) {
        this.totalGoldNum = totalGoldNum;
    }

    public Double getTotalDiamondNum() {
        return totalDiamondNum;
    }

    public void setTotalDiamondNum(Double totalDiamondNum) {
        this.totalDiamondNum = totalDiamondNum;
    }

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }

    public String getAlipayAccountName() {
        return alipayAccountName;
    }

    public void setAlipayAccountName(String alipayAccountName) {
        this.alipayAccountName = alipayAccountName;
    }

    public String getRoomFlowLink() {
        return roomFlowLink;
    }

    public void setRoomFlowLink(String roomFlowLink) {
        this.roomFlowLink = roomFlowLink;
    }

    public Double getTotalGold() {
        return totalGold;
    }

    public void setTotalGold(Double totalGold) {
        this.totalGold = totalGold;
    }

    public Integer getBounsPersent() {
        return bounsPersent;
    }

    public void setBounsPersent(Integer bounsPersent) {
        this.bounsPersent = bounsPersent;
    }

    public Double getBouns() {
        return bouns;
    }

    public void setBouns(Double bouns) {
        this.bouns = bouns;
    }

    public Byte getIsPermitRoom() {
        return isPermitRoom;
    }

    public void setIsPermitRoom(Byte isPermitRoom) {
        this.isPermitRoom = isPermitRoom;
    }

    public Double getTotalBouns() {
        return totalBouns;
    }

    public void setTotalBouns(Double totalBouns) {
        this.totalBouns = totalBouns;
    }

    public Long getCountUsers() {
        return countUsers;
    }

    public void setCountUsers(Long countUsers) {
        this.countUsers = countUsers;
    }

    public Long getCountNewUsers() {
        return countNewUsers;
    }

    public void setCountNewUsers(Long countNewUsers) {
        this.countNewUsers = countNewUsers;
    }

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }

    public Long getTrickTreatTotal() {
        return trickTreatTotal;
    }

    public void setTrickTreatTotal(Long trickTreatTotal) {
        this.trickTreatTotal = trickTreatTotal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getRoomTag() {
        return roomTag;
    }

    public void setRoomTag(String roomTag) {
        this.roomTag = roomTag;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    
    public String getGuildName() {
		return guildName;
	}

	public void setGuildName(String guildName) {
		this.guildName = guildName;
	}

	@Override
    public String toString() {
        return "StatRoomFlowVo{" +
                "totalGoldNum=" + totalGoldNum +
                ", totalDiamondNum=" + totalDiamondNum +
                ", createTime='" + createTime + '\'' +
                ", uid=" + uid +
                ", erbanNo=" + erbanNo +
                ", phone='" + phone + '\'' +
                ", nick='" + nick + '\'' +
                ", alipayAccount='" + alipayAccount + '\'' +
                ", alipayAccountName='" + alipayAccountName + '\'' +
                ", roomFlowLink='" + roomFlowLink + '\'' +
                ", totalGold=" + totalGold +
                ", totalBouns=" + totalBouns +
                ", bounsPersent=" + bounsPersent +
                ", bouns=" + bouns +
                ", isPermitRoom=" + isPermitRoom +
                ", occupation=" + occupation +
                ", bankCard='" + bankCard + '\'' +
                ", cardholder='" + cardholder + '\'' +
                ", countUsers=" + countUsers +
                ", countNewUsers=" + countNewUsers +
                ", proportion='" + proportion + '\'' +
                ", trickTreatTotal=" + trickTreatTotal +
                ", total='" + total + '\'' +
                ", roomTag='" + roomTag + '\'' +
                ", guildName='" + guildName + '\'' +
                '}';
    }
}
