package com.erban.main.vo.admin;

import java.math.BigDecimal;

public class JoinpayTransferCallback {
    // 交易状态
    private Integer status;
    // 错误码
    private String errorCode;
    // 错误描述
    private String errorCodeDesc;
    // 错误描述
    private String errorDesc;
    // 商户编号
    private String userNo;
    // 商户订单号
    private String merchantOrderNo;
    // 平台流水号
    private String platformSerialNo;
    // 收款账户号
    private String receiverAccountNoEnc;
    // 收款人
    private String receiverNameEnc;
    // 交易金额
    private BigDecimal paidAmount;
    // 是否复核
    private BigDecimal fee;
    // 签名数据
    private String hmac;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCodeDesc() {
        return errorCodeDesc;
    }

    public void setErrorCodeDesc(String errorCodeDesc) {
        this.errorCodeDesc = errorCodeDesc;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
    }

    public String getPlatformSerialNo() {
        return platformSerialNo;
    }

    public void setPlatformSerialNo(String platformSerialNo) {
        this.platformSerialNo = platformSerialNo;
    }

    public String getReceiverAccountNoEnc() {
        return receiverAccountNoEnc;
    }

    public void setReceiverAccountNoEnc(String receiverAccountNoEnc) {
        this.receiverAccountNoEnc = receiverAccountNoEnc;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public String getReceiverNameEnc() {
        return receiverNameEnc;
    }

    public void setReceiverNameEnc(String receiverNameEnc) {
        this.receiverNameEnc = receiverNameEnc;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getHmac() {
        return hmac;
    }

    public void setHmac(String hmac) {
        this.hmac = hmac;
    }
}
