package com.erban.main.vo;

public class BannerVo implements Comparable<BannerVo> {

	private Integer bannerId;
	private String bannerName;
	private String bannerPic;
	private Byte skipType;
	private String skipUri;
	private Integer seqNo;
	private Byte isNewUser;

	public Byte getIsNewUser() {
		return isNewUser;
	}

	public void setIsNewUser(Byte isNewUser) {
		this.isNewUser = isNewUser;
	}

	public Integer getBannerId() {
		return bannerId;
	}

	public void setBannerId(Integer bannerId) {
		this.bannerId = bannerId;
	}

	public String getBannerName() {
		return bannerName;
	}

	public void setBannerName(String bannerName) {
		this.bannerName = bannerName;
	}

	public String getBannerPic() {
		return bannerPic;
	}

	public void setBannerPic(String bannerPic) {
		this.bannerPic = bannerPic;
	}

	public Byte getSkipType() {
		return skipType;
	}

	public void setSkipType(Byte skipType) {
		this.skipType = skipType;
	}

	public String getSkipUri() {
		return skipUri;
	}

	public void setSkipUri(String skipUri) {
		this.skipUri = skipUri;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	@Override
	public int compareTo(BannerVo bannerVo) {
		Integer bannerVoSeq = bannerVo.seqNo;
		Integer bannerThisSeq = this.seqNo;
		if (bannerVoSeq > bannerThisSeq) {
			return -1;
		} else if (bannerVoSeq < bannerThisSeq) {
			return 1;
		} else {
			return 0;
		}
	}
}
