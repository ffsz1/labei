package com.tongdaxing.xchat_core.gift;

import java.io.Serializable;

/**
 * Created by chenran on 2017/7/27.
 */

public class GiftInfo implements Serializable {
    private int giftId;
    private int giftType;//2是普通礼物，3是背包礼物，5是点点币礼物,1
    private String giftName;
    private int goldPrice;
    private String giftUrl;
    private int seqNo;
    private boolean hasGifPic;
    private String gifUrl;
    private String gifFile;
    private boolean hasVggPic;
    private String vggUrl;
    private boolean hasLatest;//是否是最新礼物
    private boolean hasTimeLimit;//是否是限时礼物
    private boolean hasEffect;//是否有特效
    private int userGiftPurseNum;//抽奖礼物数量
    private int giftNum;//捡海螺砸中礼物数
    private boolean selected = false;//选中状态，本地字段
    private int conchNum;//捡海螺次数

    public int getConchNum() {
        return conchNum;
    }

    public void setConchNum(int conchNum) {
        this.conchNum = conchNum;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getUserGiftPurseNum() {
        return userGiftPurseNum;
    }

    public void setUserGiftPurseNum(int userGiftPurseNum) {
        this.userGiftPurseNum = userGiftPurseNum;
    }

    /**
     * 是否是贵族礼物
     */
    private boolean isNobleGift;

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    public int getGiftType() {
        return giftType;
    }

    public void setGiftType(int giftType) {
        this.giftType = giftType;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public int getGoldPrice() {
        return goldPrice;
    }

    public void setGoldPrice(int goldPrice) {
        this.goldPrice = goldPrice;
    }

    public String getGiftUrl() {
        return giftUrl;
    }

    public void setGiftUrl(String giftUrl) {
        this.giftUrl = giftUrl;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public boolean isHasGifPic() {
        return hasGifPic;
    }

    public void setHasGifPic(boolean hasGifPic) {
        this.hasGifPic = hasGifPic;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

    public String getGifFile() {
        return gifFile;
    }

    public void setGifFile(String gifFile) {
        this.gifFile = gifFile;
    }

    public boolean isHasVggPic() {
        return hasVggPic;
    }

    public void setHasVggPic(boolean hasVggPic) {
        this.hasVggPic = hasVggPic;
    }

    public String getVggUrl() {
        return vggUrl;
    }

    public void setVggUrl(String vggUrl) {
        this.vggUrl = vggUrl;
    }

    public boolean isHasLatest() {
        return hasLatest;
    }

    public void setHasLatest(boolean hasLatest) {
        this.hasLatest = hasLatest;
    }

    public boolean isHasTimeLimit() {
        return hasTimeLimit;
    }

    public void setHasTimeLimit(boolean hasTimeLimit) {
        this.hasTimeLimit = hasTimeLimit;
    }

    public boolean isHasEffect() {
        return hasEffect;
    }

    public void setHasEffect(boolean hasEffect) {
        this.hasEffect = hasEffect;
    }

    public boolean isNobleGift() {
        return isNobleGift;
    }

    public void setNobleGift(boolean nobleGift) {
        isNobleGift = nobleGift;
    }

    public int getGiftNum() {
        return giftNum;
    }

    public void setGiftNum(int giftNum) {
        this.giftNum = giftNum;
    }

    @Override
    public String toString() {
        return "GiftInfo{" +
                "giftId=" + giftId +
                ", giftType=" + giftType +
                ", giftName='" + giftName + '\'' +
                ", goldPrice=" + goldPrice +
                ", giftUrl='" + giftUrl + '\'' +
                ", seqNo=" + seqNo +
                ", hasGifPic=" + hasGifPic +
                ", gifUrl='" + gifUrl + '\'' +
                ", gifFile='" + gifFile + '\'' +
                ", hasVggPic=" + hasVggPic +
                ", vggUrl='" + vggUrl + '\'' +
                ", hasLatest=" + hasLatest +
                ", hasTimeLimit=" + hasTimeLimit +
                ", hasEffect=" + hasEffect +
                ", userGiftPurseNum=" + userGiftPurseNum +
                ", giftNum=" + giftNum +
                ", selected=" + selected +
                ", isNobleGift=" + isNobleGift +
                '}';
    }
}
