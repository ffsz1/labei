package com.erban.main.model;

import com.erban.main.param.admin.BaseParam;

import java.util.Date;

public class DailyUserPurse extends BaseParam {
    private Date createDate;

    private Date beginDate;

    private Date endDate;

    private Long goldSum;

    private Long diamondSum;

    private Long giftNormalDiamondSum;

    private Long giftDrawDiamondSum;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getGoldSum() { return goldSum; }

    public void setGoldSum(Long goldSum) { this.goldSum = goldSum; }

    public Long getDiamondSum() {
        return diamondSum;
    }

    public void setDiamondSum(Long diamondSum) {
        this.diamondSum = diamondSum;
    }

    public Long getGiftNomalDiamondSum() {
        return giftNormalDiamondSum;
    }

    public void setGiftNomalDiamondSum(Long giftNomalDiamondSum) {
        this.giftNormalDiamondSum = giftNomalDiamondSum;
    }

    public Long getGiftDrawDiamondSum() {
        return giftDrawDiamondSum;
    }

    public void setGiftDrawDiamondSum(Long giftDrawDiamondSum) {
        this.giftDrawDiamondSum = giftDrawDiamondSum;
    }
}
