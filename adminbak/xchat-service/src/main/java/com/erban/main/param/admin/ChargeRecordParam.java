package com.erban.main.param.admin;

import com.erban.main.util.StringUtils;

/**
 * 订单管理
 */
public class ChargeRecordParam extends BaseParam {
    private Long erbanNo;//拉贝号
    private String chargeProdId;//商品ID
    private String minValue;//金币最小值
    private String maxValue;//金币最大值
    private String beginDate;//开始时间
    private String endDate;//结束时间
    private String channel;//支付渠道
    private Byte status;//支付状态
    private Byte type;//类型:1.金币充值 2.金币兑换，3.公款充值
    private String os;//平台
    private Byte gender;//性别

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(Long erbanNo) {
        this.erbanNo = erbanNo;
    }

    public String getChargeProdId() {
        return chargeProdId;
    }

    public void setChargeProdId(String chargeProdId) {
        this.chargeProdId = StringUtils.isBlank(chargeProdId)?null:StringUtils.trim(chargeProdId);
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = StringUtils.isBlank(beginDate) ? null : StringUtils.trim(beginDate);
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = StringUtils.isBlank(endDate) ? null : StringUtils.trim(endDate);
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = StringUtils.isBlank(channel)?null:StringUtils.trim(channel);
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = StringUtils.isBlank(os)?null:StringUtils.trim(os);
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }
}
