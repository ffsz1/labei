package com.juxiao.xchat.manager.external.joinpay.vo;

import lombok.Data;

@Data
public class JoinpayReciver {
    /** 商户编号 */
    private String r1_MerchantNo;
    /** 商户订单号 */
    private String r2_OrderNo;
    /**支付金额*/
    private String r3_Amount;
    /**交易币种*/
    private String r4_Cur;
    /**公用回传参数 */
    private String r5_Mp;
    /** 支付状态 */
    private String r6_Status;
    /** 交易流水号 */
    private String r7_TrxNo;
    /** 银行订单号*/
    private String r8_BankOrderNo;
    /** 银行流水号*/
    private String r9_BankTrxNo;
    /** 支付时间*/
    private String ra_PayTime;
    /** 交易结果通知时间*/
    private String rb_DealTime;
    /** 银行编码*/
    private String rc_BankCode;
    /** 签名数据*/
    private String hmac;
}
