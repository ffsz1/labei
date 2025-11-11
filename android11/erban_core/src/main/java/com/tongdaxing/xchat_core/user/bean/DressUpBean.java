package com.tongdaxing.xchat_core.user.bean;

public class DressUpBean {
    private int headwearId;//头饰id
    private String headwearName;//头饰名称
    private int carId;//座驾id
    private String carName;//座驾名称
    private String picUrl;//装扮地址
    private String markPic;//左边角标图片（理解为限制条件）
    private int goldPrice;//装扮价值开心
    private String vggUrl;//装扮特效动画
    private int isPurse;//0未购买，1购买未选中，2选中
    private String effectiveTime;//装扮天数
    private String daysRemaining;//装扮剩余生效天数
    private boolean allowPurse = true;//是否允许购买 默认允许

    private int isSelect = isPurse;

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

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getIsPurse() {
        return isPurse;
    }

    public void setIsPurse(int isPurse) {
        this.isPurse = isPurse;
    }

    public String getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(String effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public String getDaysRemaining() {
        return daysRemaining;
    }

    public void setDaysRemaining(String daysRemaining) {
        this.daysRemaining = daysRemaining;
    }

    public int getGoldPrice() {
        return goldPrice;
    }

    public void setGoldPrice(int goldPrice) {
        this.goldPrice = goldPrice;
    }

    public String getVggUrl() {
        return vggUrl;
    }

    public void setVggUrl(String vggUrl) {
        this.vggUrl = vggUrl;
    }


    public boolean isAllowPurse() {
        return allowPurse;
    }

    public void setAllowPurse(boolean allowPurse) {
        this.allowPurse = allowPurse;
    }

    public int getHeadwearId() {
        return headwearId;
    }

    public void setHeadwearId(int headwearId) {
        this.headwearId = headwearId;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getMarkPic() {
        return markPic;
    }

    public void setMarkPic(String markPic) {
        this.markPic = markPic;
    }

    public int getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }


    @Override
    public String toString() {
        return "DressUpBean{" +
                "headwearId=" + headwearId +
                ", headwearName='" + headwearName + '\'' +
                ", carId=" + carId +
                ", carName='" + carName + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", markPic='" + markPic + '\'' +
                ", goldPrice=" + goldPrice +
                ", vggUrl='" + vggUrl + '\'' +
                ", isPurse=" + isPurse +
                ", effectiveTime='" + effectiveTime + '\'' +
                ", daysRemaining='" + daysRemaining + '\'' +
                ", allowPurse=" + allowPurse +
                ", isSelect=" + isSelect +
                '}';
    }
}
