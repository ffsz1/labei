package com.erban.admin.main.vo;

public class HomeHotRecommendVO {
    // 人数
    private Integer peopleCount;
    // 礼物流水
    private Integer giftFlow;
    // 背包礼物流水
    private Integer backGiftFlow;

    public Integer getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(Integer peopleCount) {
        this.peopleCount = peopleCount;
    }

    public Integer getGiftFlow() {
        return giftFlow;
    }

    public void setGiftFlow(Integer giftFlow) {
        this.giftFlow = giftFlow;
    }

    public Integer getBackGiftFlow() {
        return backGiftFlow;
    }

    public void setBackGiftFlow(Integer backGiftFlow) {
        this.backGiftFlow = backGiftFlow;
    }

    @Override
    public String toString() {
        return "HomeHotRecommendVO{" +
                "peopleCount=" + peopleCount +
                ", giftFlow=" + giftFlow +
                ", backGiftFlow=" + backGiftFlow +
                '}';
    }
}
