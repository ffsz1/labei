package com.erban.main.vo;

import java.util.Collections;
import java.util.List;

import com.beust.jcommander.internal.Lists;

public class GiftVo implements Comparable<GiftVo> {
	private Integer giftId;
	private String giftName;
	private Long goldPrice;
	private String giftUrl;
	private Integer nobleId;
	private String nobleName;
	private Boolean isNobleGift;
	private Integer seqNo;
	private Boolean hasGifPic;
	private Byte giftType;
	private String gifUrl;
	private Boolean hasVggPic;//是否有vgg动画
	private String vggUrl;//vgg动画链接
	private Boolean hasLatest;//是否是最新礼物
	private Boolean hasTimeLimit;//是否是限时礼物
	private Boolean hasEffect;//是否有特效
	private Integer userGiftPurseNum;//用户礼物数
	private Boolean isExpressGift;// 是否是表白礼物

	public Integer getUserGiftPurseNum() {
		return userGiftPurseNum;
	}

	public void setUserGiftPurseNum(Integer userGiftPurseNum) {
		this.userGiftPurseNum = userGiftPurseNum;
	}

	public Boolean getNobleGift() {
		return isNobleGift;
	}

	public void setNobleGift(Boolean nobleGift) {
		isNobleGift = nobleGift;
	}

	public Boolean getIsNobleGift() {
		return isNobleGift;
	}

	public void setIsNobleGift(Boolean nobleGift) {
		isNobleGift = nobleGift;
	}

	public Integer getNobleId() {
		return nobleId;
	}

	public void setNobleId(Integer nobleId) {
		this.nobleId = nobleId;
	}

	public String getNobleName() {
		return nobleName;
	}

	public void setNobleName(String nobleName) {
		this.nobleName = nobleName;
	}

	public Boolean getHasLatest() {
		return hasLatest;
	}

	public void setHasLatest(Boolean hasLatest) {
		this.hasLatest = hasLatest;
	}

	public Boolean getHasTimeLimit() {
		return hasTimeLimit;
	}

	public void setHasTimeLimit(Boolean hasTimeLimit) {
		this.hasTimeLimit = hasTimeLimit;
	}

	public Boolean getHasEffect() {
		return hasEffect;
	}

	public void setHasEffect(Boolean hasEffect) {
		this.hasEffect = hasEffect;
	}

	public Byte getGiftType() {
		return giftType;
	}

	public void setGiftType(Byte giftType) {
		this.giftType = giftType;
	}

	public Boolean getHasGifPic() {
		return hasGifPic;
	}

	public void setHasGifPic(Boolean hasGifPic) {
		this.hasGifPic = hasGifPic;
	}

	public String getGifUrl() {
		return gifUrl;
	}

	public void setGifUrl(String gifUrl) {
		this.gifUrl = gifUrl;
	}

	public Boolean getHasVggPic() {
		return hasVggPic;
	}

	public void setHasVggPic(Boolean hasVggPic) {
		this.hasVggPic = hasVggPic;
	}

	public String getVggUrl() {
		return vggUrl;
	}

	public void setVggUrl(String vggUrl) {
		this.vggUrl = vggUrl;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public Integer getGiftId() {
		return giftId;
	}

	public void setGiftId(Integer giftId) {
		this.giftId = giftId;
	}

	public String getGiftName() {
		return giftName;
	}

	public void setGiftName(String giftName) {
		this.giftName = giftName;
	}

	public Long getGoldPrice() {
		return goldPrice;
	}

	public void setGoldPrice(Long goldPrice) {
		this.goldPrice = goldPrice;
	}

	public String getGiftUrl() {
		return giftUrl;
	}

	public void setGiftUrl(String giftUrl) {
		this.giftUrl = giftUrl;
	}

	public Boolean getExpressGift() {
		return isExpressGift;
	}

	public void setExpressGift(Boolean expressGift) {
		isExpressGift = expressGift;
	}

	@Override
	public int compareTo(GiftVo giftVo) {
		Integer seqNoVo = giftVo.seqNo;
		Integer seqNoThis = this.seqNo;
		if (seqNoVo > seqNoThis) {
			return -1;
		} else if (seqNoVo < seqNoThis) {
			return 1;
		} else {
			return 0;
		}
	}

	public static void main(String[] args) {
		GiftVo giftVo = new GiftVo();
		giftVo.setGiftId(1);
		giftVo.setGoldPrice(1000L);

		GiftVo giftVo1 = new GiftVo();
		giftVo1.setGiftId(2);
		giftVo1.setGoldPrice(3000L);

		GiftVo giftVo2 = new GiftVo();
		giftVo2.setGiftId(3);
		giftVo2.setGoldPrice(1500L);

		GiftVo giftVo3 = new GiftVo();
		giftVo3.setGiftId(4);
		giftVo3.setGoldPrice(4000L);

		GiftVo giftVo4 = new GiftVo();
		giftVo4.setGiftId(5);
		giftVo4.setGoldPrice(500L);

		List<GiftVo> list = Lists.newArrayList();

		list.add(giftVo);
		list.add(giftVo1);
		list.add(giftVo2);
		list.add(giftVo3);
		list.add(giftVo4);

		for (int i = 0; i < list.size(); i++) {
			System.out.print(list.get(i).getGiftId());
		}
		Collections.sort(list);
		System.out.println("----------------");
		for (int i = 0; i < list.size(); i++) {
			System.out.print(list.get(i).getGiftId());
			System.out.println(list.get(i).getGoldPrice());
		}
	}
}
