package com.erban.admin.main.vo;

public class GiveGiftcarVo {
    private Long erbanNo;

    private Long uid;

    private String carId;

    private String carName;

    private Integer goldPrice;

    private Integer carDate;

    public Long getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(Long erbanNo) {
        this.erbanNo = erbanNo;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public Integer getGoldPrice() {
        return goldPrice;
    }

    public void setGoldPrice(Integer goldPrice) {
        this.goldPrice = goldPrice;
    }

    public Integer getCarDate() {
        return carDate;
    }

    public void setCarDate(Integer carDate) {
        this.carDate = carDate;
    }
}
