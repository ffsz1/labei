package com.erban.main.vo.admin;

import java.math.BigDecimal;

public class JoinpayTransfer {
    // 商户编号
    private String userNo;
    // 产品类型
    private String productCode;
    // 交易请求时间
    private String requestTime;
    // 商户订单号
    private String merchantOrderNo;
    // 收款账户号
    private String receiverAccountNoEnc;
    // 收款人
    private String receiverNameEnc;
    // 账户类型
    private Integer receiverAccountType;
    // 收款账户联行号
    private String receiverBankChannelNo;
    // 交易金额
    private BigDecimal paidAmount;
    // 币种
    private String currency;
    // 是否复核
    private String isChecked;
    // 代付说明
    private String paidDesc;
    // 代付用途
    private String paidUse;
    // 商户通知地址
    private String callbackUrl;
    // 优先使用产品
    private String firstProductCode;
    // 签名数据
    private String hmac;

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
    }

    public String getReceiverAccountNoEnc() {
        return receiverAccountNoEnc;
    }

    public void setReceiverAccountNoEnc(String receiverAccountNoEnc) {
        this.receiverAccountNoEnc = receiverAccountNoEnc;
    }

    public String getReceiverNameEnc() {
        return receiverNameEnc;
    }

    public void setReceiverNameEnc(String receiverNameEnc) {
        this.receiverNameEnc = receiverNameEnc;
    }

    public Integer getReceiverAccountType() {
        return receiverAccountType;
    }

    public void setReceiverAccountType(Integer receiverAccountType) {
        this.receiverAccountType = receiverAccountType;
    }

    public String getReceiverBankChannelNo() {
        return receiverBankChannelNo;
    }

    public void setReceiverBankChannelNo(String receiverBankChannelNo) {
        this.receiverBankChannelNo = receiverBankChannelNo;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getPaidDesc() {
        return paidDesc;
    }

    public void setPaidDesc(String paidDesc) {
        this.paidDesc = paidDesc;
    }

    public String getPaidUse() {
        return paidUse;
    }

    public void setPaidUse(String paidUse) {
        this.paidUse = paidUse;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getFirstProductCode() {
        return firstProductCode;
    }

    public void setFirstProductCode(String firstProductCode) {
        this.firstProductCode = firstProductCode;
    }

    public String getHmac() {
        return hmac;
    }

    public void setHmac(String hmac) {
        this.hmac = hmac;
    }
}
