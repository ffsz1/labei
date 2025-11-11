package com.erban.main.vo;

import com.erban.main.model.NobleUsers;

import java.util.Date;
import java.util.List;

public class UserVo {
	private Long uid;
	private Long erbanNo;
	private String phone;
	private Date birth;
	private Byte star;
	private String nick;
	private String signture;
	private String userVoice;
	private Integer voiceDura;
	private Byte defUser;
	private Long fortune;
	private Byte gender;
	private String avatar;
	private String region;
	private String userDesc;
	private Integer followNum;
	private Integer fansNum;
	private Long goldNum;
	private Boolean hasPrettyErbanNo;
	private Byte operType;//用户新增或更新操作时，返回操作类型，1是更新用户资料，2是第一次注册补全用户资料
	private boolean hasRegPacket;
	private NobleUsers nobleUsers;
	private Integer experLevel; //等级值
	private Integer charmLevel; //魅力值
	private String carUrl; //座驾
	private String carName; //座驾
	private String headwearUrl; //头饰
	private String headwearName; //头饰
	private Date createTime;
	private Byte findNewUsers;

	public Byte getFindNewUsers() {
		return findNewUsers;
	}

	public void setFindNewUsers(Byte findNewUsers) {
		this.findNewUsers = findNewUsers;
	}

	public String getHeadwearUrl() {
		return headwearUrl;
	}

	public void setHeadwearUrl(String headwearUrl) {
		this.headwearUrl = headwearUrl;
	}

	public String getHeadwearName() {
		return headwearName;
	}

	public void setHeadwearName(String headwearName) {
		this.headwearName = headwearName;
	}

	public String getCarName() {
		return carName;
	}

	public void setCarName(String carName) {
		this.carName = carName;
	}

	public String getCarUrl() {
		return carUrl;
	}

	public void setCarUrl(String carUrl) {
		this.carUrl = carUrl;
	}

	public NobleUsers getNobleUsers() {
		return nobleUsers;
	}

	public void setNobleUsers(NobleUsers nobleUsers) {
		this.nobleUsers = nobleUsers;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isHasRegPacket() {
		return hasRegPacket;
	}

	public void setHasRegPacket(boolean hasRegPacket) {
		this.hasRegPacket = hasRegPacket;
	}

	private List<PrivatePhotoVo> privatePhoto;

	public List<PrivatePhotoVo> getPrivatePhoto() {
		return privatePhoto;
	}

	public void setPrivatePhoto(List<PrivatePhotoVo> privatePhoto) {
		this.privatePhoto = privatePhoto;
	}

	public Long getGoldNum() {
		return goldNum;
	}

	public Byte getOperType() {
		return operType;
	}

	public void setOperType(Byte operType) {
		this.operType = operType;
	}

	public void setGoldNum(Long goldNum) {
		this.goldNum = goldNum;
	}

	private String alipayAccount;

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

	private String alipayAccountName;

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

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public Byte getStar() {
		return star;
	}

	public void setStar(Byte star) {
		this.star = star;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getSignture() {
		return signture;
	}

	public void setSignture(String signture) {
		this.signture = signture;
	}

	public String getUserVoice() {
		return userVoice;
	}

	public void setUserVoice(String userVoice) {
		this.userVoice = userVoice;
	}

	public Integer getFollowNum() {
		return followNum;
	}

	public void setFollowNum(Integer followNum) {
		this.followNum = followNum;
	}

	public Integer getFansNum() {
		return fansNum;
	}

	public void setFansNum(Integer fansNum) {
		this.fansNum = fansNum;
	}

	public Byte getGender() {
		return gender;
	}

	public void setGender(Byte gender) {
		this.gender = gender;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getUserDesc() {
		return userDesc;
	}

	public Integer getVoiceDura() {
		return voiceDura;
	}

	public void setVoiceDura(Integer voiceDura) {
		this.voiceDura = voiceDura;
	}

	public Byte getDefUser() {
		return defUser;
	}

	public void setDefUser(Byte defUser) {
		this.defUser = defUser;
	}

	public Long getFortune() {
		return fortune;
	}

	public void setFortune(Long fortune) {
		this.fortune = fortune;
	}

	public void setUserDesc(String userDesc) {
		this.userDesc = userDesc;
	}

	public Boolean getHasPrettyErbanNo() {
		return hasPrettyErbanNo;
	}

	public void setHasPrettyErbanNo(Boolean hasPrettyErbanNo) {
		this.hasPrettyErbanNo = hasPrettyErbanNo;
	}

	public Integer getExperLevel() {
		return experLevel;
	}

	public void setExperLevel(Integer experLevel) {
		this.experLevel = experLevel;
	}

	public Integer getCharmLevel() {
		return charmLevel;
	}

	public void setCharmLevel(Integer charmLevel) {
		this.charmLevel = charmLevel;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
