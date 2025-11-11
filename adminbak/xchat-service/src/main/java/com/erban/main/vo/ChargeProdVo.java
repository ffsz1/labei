package com.erban.main.vo;

/**
 * Created by liuguofu on 2017/7/1.
 */
public class ChargeProdVo implements Comparable<ChargeProdVo> {
	private String chargeProdId;
	private String prodName;
	private String prodDesc;
	private Long money;
	private Integer giftGoldNum;
	private Byte channel;
	private Byte seqNo;

	public String getProdDesc() {
		return prodDesc;
	}

	public void setProdDesc(String prodDesc) {
		this.prodDesc = prodDesc;
	}

	public Byte getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Byte seqNo) {
		this.seqNo = seqNo;
	}

	public String getChargeProdId() {
		return chargeProdId;
	}

	public void setChargeProdId(String chargeProdId) {
		this.chargeProdId = chargeProdId;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public Long getMoney() {
		return money;
	}

	public void setMoney(Long money) {
		this.money = money;
	}

	public Integer getGiftGoldNum() {
		return giftGoldNum;
	}

	public void setGiftGoldNum(Integer giftGoldNum) {
		this.giftGoldNum = giftGoldNum;
	}

	public Byte getChannel() {
		return channel;
	}

	public void setChannel(Byte channel) {
		this.channel = channel;
	}

	@Override
	public int compareTo(ChargeProdVo chargeProdVo) {
		Byte seqNoVo = chargeProdVo.seqNo;
		Byte seqNoThis = this.seqNo;
		if (seqNoVo > seqNoThis) {
			return -1;
		} else if (seqNoVo < seqNoThis) {
			return 1;
		} else {
			return 0;
		}
	}
}
